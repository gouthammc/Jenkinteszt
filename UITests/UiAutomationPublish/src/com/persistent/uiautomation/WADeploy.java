package com.persistent.uiautomation;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class WADeploy {
	private static SWTWorkbenchBot wabot;
	private static String lastServiceUsed;

	@BeforeClass
	public static void beforeClass() throws Exception {
		wabot = new SWTWorkbenchBot();
		try {
			wabot.viewByTitle(Messages.welCm).close();
			// import publish-settings file
			Utility.importPubFile();
			Utility.createProjectSrvJDKCloud(Messages.projWithCmpnt,
					Messages.jetSrvPath);
		} catch (Exception e) {
		}
	}

	@AfterClass
	public static void AfterClass() throws Exception {
		wabot.closeAllShells();
		if (Utility.isProjExist(Messages.projWithCmpnt)) {
			// delete existing project
			Utility.selProjFromExplorer(Messages.projWithCmpnt).select();
			Utility.deleteSelectedProject();
		}
		if (Utility.isProjExist(Messages.test)) {
			Utility.selProjFromExplorer(Messages.test).select();
			Utility.deleteSelectedProject();
		}
	}


	@Before
	public void setUp() throws Exception {
		wabot.closeAllShells();
	}

	@After
	public void cleanUp() throws Exception {
		wabot.closeAllShells();
	}

	@Test
	public void testPublishNewAccService() throws Exception {
		Utility.projectRefresh(Messages.projWithCmpnt);
		unConfigure();
		Utility.selProjFromExplorer(Messages.projWithCmpnt).select();
		SWTBotToolbarButton pubBtn = wabot.
				toolbarButtonWithTooltip(Messages.pubBtn);
		pubBtn.click();
		// publish setting file may or may not be present - wait for around 1 min
		wabot.sleep(30000);
		int sleepTime = 600000;
		if (Utility.checkForNewCreation()) {
			sleepTime = 900000;
			lastServiceUsed = Messages.serviceName;
		} else {
			wabot.comboBoxWithLabel(Messages.serNmLbl).setSelection(decideIndex());
		}
		wabot.button("Publish").click();
		wabot.sleep(sleepTime);
		SWTBot botView = wabot.viewByTitle(Messages.azLog).bot();
		/*
		 * Cell text is empty that means it has published link.
		 * Else it has error message.
		 */
		assertTrue("testPublishNewAccService", botView.table().cell(0, 1).isEmpty());
	}

	@Test
	public void testPublishRemoteAccessOverwriteDply() throws Exception {
		Utility.projectRefresh(Messages.projWithCmpnt);
		unConfigure();
		Utility.selProjFromExplorer(Messages.projWithCmpnt).select();
		SWTBotToolbarButton pubBtn = wabot.
				toolbarButtonWithTooltip(Messages.pubBtn);
		pubBtn.click();
		// publish setting file may or may not be present - wait for around 1 min
		wabot.sleep(30000);
		int sleepTime = 600000;
		if (Utility.checkForNewCreation()) {
			sleepTime = 900000;
			lastServiceUsed = Messages.serviceName;
		} else {
			wabot.comboBoxWithLabel(Messages.serNmLbl).setSelection(decideIndex());
		}
		Utility.fillRemoteAccessDetails();
		wabot.button("Publish").click();
		wabot.sleep(sleepTime);
		SWTBot botView = wabot.viewByTitle(Messages.azLog).bot();
		assertTrue("testPublishRemoteAccessOverwriteDply", botView.table().cell(0, 1).isEmpty());
	}

	@Test
	public void testPublishCertificateUpload() throws Exception {
		Utility.projectRefresh(Messages.projWithCmpnt);
		Utility.selectPageUsingContextMenu(Messages.projWithCmpnt, Messages.role1, Messages.certPage);
		if (wabot.table().rowCount() <= 0) {
			Utility.createAddNewCert(Messages.strPwd);
		}
		wabot.button("OK").click();
		Utility.selProjFromExplorer(Messages.projWithCmpnt).select();
		SWTBotToolbarButton pubBtn = wabot.
				toolbarButtonWithTooltip(Messages.pubBtn);
		pubBtn.click();
		// publish setting file may or may not be present - wait for around 1 min
		wabot.sleep(30000);
		int sleepTime = 600000;
		if (Utility.checkForNewCreation()) {
			sleepTime = 900000;
			lastServiceUsed = Messages.serviceName;
		} else {
			wabot.comboBoxWithLabel(Messages.serNmLbl).setSelection(decideIndex());
		}
		wabot.button("Publish").click();
		wabot.sleep(30000);
		if (wabot.shell(Messages.upCerTtl).isOpen()) {
			Utility.fillCertDetails(Messages.strPwd);
		}
		wabot.sleep(sleepTime);
		SWTBot botView = wabot.viewByTitle(Messages.azLog).bot();
		assertTrue("testPublishCertificateUpload", botView.table().cell(0, 1).isEmpty());
	}

	/*
	Caching Y
	SSL Y
	Sticky sessions Y
	Various endpoints Y
	Remote debugging Y
	Local storage 1

	Multiple roles Y
	Apps Y

	Cloud OS 4
	RDP Y

	VM Size Small
	No. of instances 3
	 */
	@Test
	public void testPublishConf1() throws Exception {
		Utility.projectRefresh(Messages.projWithCmpnt);
		// VM size and instance
		Utility.selectPageUsingContextMenu(Messages.projWithCmpnt, Messages.role1, Messages.roleTreeRoot);
		wabot.textWithLabel(Messages.instance).setText("3");
		wabot.button("OK").click();
		configuration();
		// local storage
		Utility.selPageNode(Messages.role1, Messages.lclStrPage);
		Utility.addLocalStorage();
		Utility.selProjFromExplorer(Messages.projWithCmpnt).select();
		SWTBotToolbarButton pubBtn = wabot.
				toolbarButtonWithTooltip(Messages.pubBtn);
		pubBtn.click();
		// publish setting file may or may not be present - wait for around 1 min
		wabot.sleep(30000);
		// Target Os
		wabot.comboBoxWithLabel(Messages.tarOs).setSelection(2);
		// remote access
		Utility.fillRemoteAccessDetails();
		int sleepTime = 600000;
		if (Utility.checkForNewCreation()) {
			sleepTime = 900000;
			lastServiceUsed = Messages.serviceName;
		} else {
			wabot.comboBoxWithLabel(Messages.serNmLbl).setSelection(decideIndex());
		}
		wabot.button("Publish").click();
		wabot.sleep(30000);
		if (wabot.shell(Messages.upCerTtl).isOpen()) {
			Utility.fillCertDetails(Messages.strPwd);
		}
		wabot.sleep(sleepTime);
		SWTBot botView = wabot.viewByTitle(Messages.azLog).bot();
		assertTrue("testPublishConf1", botView.table().cell(0, 1).isEmpty());
	}

	/*
	Caching Y
	SSL Y
	Sticky sessions Y
	Various endpoints Y
	Remote debugging Y
	Local storage 3

	Multiple roles Y
	Apps Y

	Cloud OS 2
	RDP Y

	VM Size Large
	No. of instances 1
	 */
	@Test
	public void testPublishConf2() throws Exception {
		Utility.projectRefresh(Messages.projWithCmpnt);
		// VM size and instance
		Utility.selectPageUsingContextMenu(Messages.projWithCmpnt, Messages.role1, Messages.roleTreeRoot);
		wabot.comboBoxWithLabel(Messages.vmSizeLbl).setSelection("Large");
		wabot.button("OK").click();
		configuration();
		// local storage
		Utility.selPageNode(Messages.role1, Messages.lclStrPage);
		Utility.addLocalStorage();
		Utility.selProjFromExplorer(Messages.projWithCmpnt).select();
		SWTBotToolbarButton pubBtn = wabot.
				toolbarButtonWithTooltip(Messages.pubBtn);
		pubBtn.click();
		// publish setting file may or may not be present - wait for around 1 min
		wabot.sleep(30000);
		// Target Os
		wabot.comboBoxWithLabel(Messages.tarOs).setSelection(2);
		// remote access
		Utility.fillRemoteAccessDetails();
		int sleepTime = 600000;
		if (Utility.checkForNewCreation()) {
			sleepTime = 900000;
			lastServiceUsed = Messages.serviceName;
		} else {
			wabot.comboBoxWithLabel(Messages.serNmLbl).setSelection(decideIndex());
		}
		wabot.button("Publish").click();
		wabot.sleep(30000);
		if (wabot.shell(Messages.upCerTtl).isOpen()) {
			Utility.fillCertDetails(Messages.strPwd);
		}
		wabot.sleep(sleepTime);
		SWTBot botView = wabot.viewByTitle(Messages.azLog).bot();
		assertTrue("testPublishRemoteAccessOverwriteDply", botView.table().cell(0, 1).isEmpty());
	}

	private void unConfigure() {
		// Caching
		Utility.
				selectPageUsingContextMenu(Messages.projWithCmpnt,
						Messages.role1,
						Messages.cachePage);
		if (wabot.checkBox().isChecked()) {
			wabot.checkBox().click();
		}
		// Debugging
		Utility.selPageNode(Messages.role1, Messages.dbgPage);
		if (wabot.checkBox().isChecked()) {
			wabot.checkBox().click();
			wabot.shell("Endpoint Error").activate();
			wabot.button("Yes").click();
		}
		// SSL
		Utility.selPageNode(Messages.role1, Messages.sslPage);
		if (wabot.checkBox().isChecked()) {
			wabot.checkBox().click();
		}
		// Sticky sessions
		Utility.selPageNode(Messages.role1, Messages.loadBlcPage);
		if (wabot.checkBox().isChecked()) {
			wabot.checkBox().click();
		}
		// certifiactes
		Utility.selPageNode(Messages.role1, Messages.certPage);
		if (wabot.table().rowCount() > 1
				&& wabot.table().cell(1, 0).startsWith("SelfSignedCertificate")) {
			wabot.table().select(1);
			wabot.button("Remove").click();
			wabot.shell("Remove Certificate").activate();
			wabot.button("OK").click();
			wabot.button("OK").click();
		} else if (wabot.table().rowCount() == 1) {
			String str = wabot.table().cell(0, 0);
			if (str.startsWith("SelfSignedCertificate")) {
				wabot.table().select(0);
				wabot.button("Remove").click();
				wabot.shell("Remove Certificate").activate();
				wabot.button("OK").click();
				wabot.button("OK").click();
			} else {
				wabot.button("OK").click();
			}
		}
	}

	private void configuration() {
		// certifiactes
		Utility.selectPageUsingContextMenu(Messages.projWithCmpnt, Messages.role1, Messages.certPage);
		if (wabot.table().rowCount() <= 0) {
			Utility.createAddNewCert(Messages.strPwd);
		}
		// Caching
		Utility.selPageNode(Messages.role1, Messages.cachePage);
		if (!wabot.checkBox().isChecked()) {
			wabot.checkBox().select();
		}
		// Debugging
		Utility.selPageNode(Messages.role1, Messages.dbgPage);
		if (!wabot.checkBox().isChecked()) {
			wabot.checkBox().select();
		}
		// SSL
		Utility.selPageNode(Messages.role1, Messages.sslPage);
		if (!wabot.checkBox().isChecked()) {
			wabot.checkBox().select();
			wabot.shell(Messages.sslPage).activate();
			wabot.button("Yes").click();
			wabot.comboBoxWithLabel(Messages.httpsCert).setSelection(0);
		}
		// Sticky sessions
		Utility.selPageNode(Messages.role1, Messages.loadBlcPage);
		if (!wabot.checkBox().isChecked()) {
			wabot.checkBox().select();
		}
	}

	private int decideIndex() {
		int index = 0;
		List<String> list = Arrays.asList(
				wabot.comboBoxWithLabel(Messages.serNmLbl).items());
		int lastIndex = list.indexOf(lastServiceUsed);
		index = lastIndex + 1;
		if (index == 4) {
			index = 0;
		}
		lastServiceUsed = list.get(index);
		return index;
	}
}
