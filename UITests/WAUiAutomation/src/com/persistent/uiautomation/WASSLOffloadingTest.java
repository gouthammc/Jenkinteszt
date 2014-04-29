/**
 * Copyright 2014 Persistent Systems Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.persistent.uiautomation;

import static org.junit.Assert.assertTrue;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class WASSLOffloadingTest {
	private static SWTWorkbenchBot wabot;

	@BeforeClass
	public static void beforeClass() throws Exception {
		wabot = new SWTWorkbenchBot();
		try {
			wabot.viewByTitle(Messages.welCm).close();
		} catch (Exception e) {
		}
	}

	@Before
	public void setUp() throws Exception {
		wabot.closeAllShells();
		if (Utility.isProjExist(Messages.projWithEp)) {
			//delete existing project
			Utility.selProjFromExplorer(Messages.projWithEp).select();
			Utility.deleteSelectedProject();
		}
		Utility.createProject(Messages.projWithEp);
	}

	@After
	public void cleanUp() throws Exception {
		if (Utility.isProjExist(Messages.projWithEp)) {
			Utility.selProjFromExplorer(Messages.projWithEp).select();
			Utility.deleteSelectedProject();
		}
	}

	@Test
	// Test cases SSL 2.2.1 number 1
	public void testSSLPage() throws Exception {
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projWithEp, Messages.role1, Messages.sslPage);
		assertTrue("testSSLPage", wabot.
				checkBox().isEnabled());
		propShell.close();
	}

	@Test
	// Test cases SSL 2.2.1 number 2
	public void testSSLEnable() throws Exception {
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projWithEp, Messages.role1, Messages.sslPage);
		wabot.checkBox().select();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("Yes").click();
		assertTrue("testSSLEnable", wabot.checkBox().isChecked()
				&& wabot.comboBoxWithLabel(Messages.inpCmbLbl).isEnabled()
				&& !wabot.comboBoxWithLabel(Messages.inpCmbLbl).getText().isEmpty()
				&& wabot.comboBoxWithLabel(Messages.httpsCert).isEnabled()
				&& wabot.comboBoxWithLabel(Messages.httpsCert).getText().isEmpty()
				&& wabot.link(0).isEnabled()
				&& wabot.link(1).isEnabled());
		propShell.close();
	}

	@Test
	// Test cases SSL 2.2.1 number 3
	public void testSSLDisable() throws Exception {
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projWithEp, Messages.role1, Messages.sslPage);
		wabot.checkBox().select();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("Yes").click();
		boolean enable = wabot.checkBox().isChecked()
				&& wabot.comboBoxWithLabel(Messages.inpCmbLbl).isEnabled()
				&& !wabot.comboBoxWithLabel(Messages.inpCmbLbl).getText().isEmpty()
				&& wabot.comboBoxWithLabel(Messages.httpsCert).isEnabled()
				&& wabot.comboBoxWithLabel(Messages.httpsCert).getText().isEmpty()
				&& wabot.link(0).isEnabled()
				&& wabot.link(1).isEnabled();
		wabot.checkBox().click();
		boolean disable = !wabot.checkBox().isChecked()
				&& !wabot.comboBoxWithLabel(Messages.inpCmbLbl).isEnabled()
				&& wabot.comboBoxWithLabel(Messages.inpCmbLbl).getText().isEmpty()
				&& !wabot.comboBoxWithLabel(Messages.httpsCert).isEnabled()
				&& wabot.comboBoxWithLabel(Messages.httpsCert).getText().isEmpty()
				&& wabot.link(0).isEnabled()
				&& wabot.link(1).isEnabled();
		assertTrue("testSSLDisable", enable
				&& disable);
		propShell.close();
	}

	@Test
	// Test cases SSL 2.2.1 number 4
	public void testComboboxContents() throws Exception {
		Utility.getPropertyPage(Messages.projWithEp, Messages.rAccPage);
		Utility.enableRemoteAccess();
		Utility.enterRemoteAccessDetails(Messages.projWithEp);
		wabot.button("OK").click();
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projWithEp,
						Messages.role1, Messages.certPage);
		wabot.button(Messages.roleAddBtn).click();
		Utility.addCert(Messages.test, Messages.test);
		Utility.selPageNode(Messages.role1, Messages.endptPage);
		//input
		wabot.button(Messages.roleAddBtn).click();
		Utility.addEp("IntEndPtAuto", Messages.typeInpt, "11", "");
		//Internal Ep
		wabot.button(Messages.roleAddBtn).click();
		Utility.addEp("InstanceEndPt", Messages.typeInstnc, "12", "13");
		//input
		wabot.button(Messages.roleAddBtn).click();
		Utility.addEp("IntEndPtRole1", Messages.typeInpt, "10", "10");
		//Internal Ep
		wabot.button(Messages.roleAddBtn).click();
		Utility.addEp("InlEndPtRole1", Messages.typeIntrnl, "22", "23");
		wabot.sleep(5000);
		Utility.selPageNode(Messages.role1, Messages.sslPage);
		wabot.checkBox().select();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("No").click();
		assertTrue("testComboboxContents",
				wabot.comboBoxWithLabel(Messages.inpCmbLbl).itemCount() == 1
				&& wabot.comboBoxWithLabel(Messages.httpsCert).itemCount() == 1);
		propShell.close();
	}

	@Test
	// Test cases SSL 2.2.1 number 5
	public void testStickyAt80And443InputIsPresent() throws Exception {
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projWithEp,
						Messages.role1, Messages.endptPage);
		//input
		wabot.button(Messages.roleAddBtn).click();
		Utility.addEp("http", Messages.typeInpt, "80", "8080");
		//input
		wabot.button(Messages.roleAddBtn).click();
		Utility.addEp("https", Messages.typeInpt, "443", "8081");
		// enable load balancing at 80
		Utility.selPageNode(Messages.role1, Messages.loadBlcPage);
		wabot.checkBox().select();
		Utility.selPageNode(Messages.role1, Messages.sslPage);
		wabot.checkBox().select();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("OK").click();
		propShell.activate();
		assertTrue("testStickyAt80And443InputIsPresent",
				!wabot.checkBox().isChecked());
		propShell.close();
	}

	@Test
	// Test cases SSL 2.2.1 number 6
	public void testStickyAt80And443InstanceIsPresent() throws Exception {
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projWithEp,
						Messages.role1, Messages.endptPage);
		//input
		wabot.button(Messages.roleAddBtn).click();
		Utility.addEp("http", Messages.typeInpt, "80", "8080");
		//input
		wabot.button(Messages.roleAddBtn).click();
		Utility.addEp("https", Messages.typeInstnc, "443", "8081");
		// enable load balancing at 80
		Utility.selPageNode(Messages.role1, Messages.loadBlcPage);
		wabot.checkBox().select();
		Utility.selPageNode(Messages.role1, Messages.sslPage);
		wabot.checkBox().select();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("No").click();
		propShell.activate();
		boolean no = !wabot.checkBox().isChecked();
		wabot.checkBox().select();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("Yes").click();
		assertTrue("testStickyAt80And443InstanceIsPresent",
				no
				&& wabot.checkBox().isChecked()
				&& wabot.comboBoxWithLabel(Messages.inpCmbLbl).getText().contains("public:8443"));
		propShell.close();
	}

	@Test
	// Test cases SSL 2.2.1 number 7
	public void testStickyAt80And443NotPresent() throws Exception {
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projWithEp,
						Messages.role1, Messages.endptPage);
		//input
		wabot.button(Messages.roleAddBtn).click();
		Utility.addEp("http", Messages.typeInpt, "80", "8080");
		Utility.selPageNode(Messages.role1, Messages.loadBlcPage);
		// enable load balancing at 80
		wabot.checkBox().select();
		Utility.selPageNode(Messages.role1, Messages.sslPage);
		wabot.checkBox().select();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("No").click();
		propShell.activate();
		boolean no = !wabot.checkBox().isChecked();
		wabot.checkBox().select();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("Yes").click();
		assertTrue("testStickyAt80And443NotPresent",
				no
				&& wabot.checkBox().isChecked()
				&& wabot.comboBoxWithLabel(Messages.inpCmbLbl).getText().contains("public:443"));
		propShell.close();
	}

	@Test
	// Test cases SSL 2.2.1 number 8
	public void testStickyNotAt80() throws Exception {
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projWithEp,
						Messages.role1, Messages.endptPage);
		//input
		wabot.button(Messages.roleAddBtn).click();
		Utility.addEp("IntEndPtAuto", Messages.typeInpt, "11", "11");
		Utility.selPageNode(Messages.role1, Messages.loadBlcPage);
		wabot.checkBox().select();
		Utility.selPageNode(Messages.role1, Messages.sslPage);
		wabot.checkBox().select();
		assertTrue("testStickyNotAt80",
				wabot.checkBox().isChecked()
				&& wabot.comboBoxWithLabel(Messages.inpCmbLbl).getText().contains("public:11"));
		propShell.close();
	}

	@Test
	// Test cases SSL 2.2.1 number 9
	public void testSSLAt443Input() throws Exception {
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projWithEp,
						Messages.role1, Messages.endptPage);
		//input
		wabot.button(Messages.roleAddBtn).click();
		Utility.addEp("https", Messages.typeInpt, "443", "8081");
		Utility.selPageNode(Messages.role1, Messages.sslPage);
		wabot.checkBox().select();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("OK").click();
		assertTrue("testSSLAt443Input",
				wabot.checkBox().isChecked()
				&& wabot.comboBoxWithLabel(Messages.inpCmbLbl).getText().contains("public:443"));
		propShell.close();
	}

	@Test
	// Test cases SSL 2.2.1 number 10
	public void testSSLAt443InstanceWith80() throws Exception {
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projWithEp,
						Messages.role1, Messages.endptPage);
		//input
		wabot.button(Messages.roleAddBtn).click();
		Utility.addEp("https", Messages.typeInstnc, "443", "8081");
		Utility.selPageNode(Messages.role1, Messages.sslPage);
		wabot.checkBox().select();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("No").click();
		wabot.button("OK").click();
		propShell.activate();
		boolean no = !wabot.checkBox().isChecked()
				&& wabot.comboBoxWithLabel(Messages.inpCmbLbl).getText().isEmpty();
		wabot.checkBox().select();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("Yes").click();
		assertTrue("testSSLAt443InstanceWith80",
				no
				&& wabot.checkBox().isChecked()
				&& wabot.comboBoxWithLabel(Messages.inpCmbLbl).getText().contains("public:8443"));
		propShell.close();
	}

	@Test
	// Test cases SSL 2.2.1 number 11
	public void testSSLAt443InstanceWithout80() throws Exception {
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projWithEp,
						Messages.role1, Messages.endptPage);
		//input
		wabot.button(Messages.roleAddBtn).click();
		Utility.addEp("https", Messages.typeInstnc, "443", "8081");
		//input
		wabot.button(Messages.roleAddBtn).click();
		Utility.addEp("IntEndPt", Messages.typeInpt, "11", "11");
		Utility.selPageNode(Messages.role1, Messages.sslPage);
		wabot.checkBox().select();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("No").click();
		propShell.activate();
		boolean no = wabot.checkBox().isChecked()
				&& wabot.comboBoxWithLabel(Messages.inpCmbLbl).getText().isEmpty();
		wabot.checkBox().click();
		wabot.checkBox().select();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("Yes").click();
		assertTrue("testSSLAt443InstanceWithout80",
				no
				&& wabot.checkBox().isChecked()
				&& wabot.comboBoxWithLabel(Messages.inpCmbLbl).getText().contains("public:8443"));
		propShell.close();
	}

	@Test
	// Test cases SSL 2.2.1 number 12
	public void testSSLWithout443With80() throws Exception {
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projWithEp,
						Messages.role1, Messages.sslPage);
		wabot.checkBox().select();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("No").click();
		wabot.button("OK").click();
		propShell.activate();
		boolean no = !wabot.checkBox().isChecked()
				&& wabot.comboBoxWithLabel(Messages.inpCmbLbl).getText().isEmpty();
		wabot.checkBox().select();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("Yes").click();
		assertTrue("testSSLWithout443With80",
				no
				&& wabot.checkBox().isChecked()
				&& wabot.comboBoxWithLabel(Messages.inpCmbLbl).getText().contains("public:443"));
		propShell.close();
	}

	@Test
	// Test cases SSL 2.2.1 number 13
	public void testSSLWithout443Without80() throws Exception {
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projWithEp,
						Messages.role1, Messages.endptPage);
		// input
		wabot.button(Messages.roleAddBtn).click();
		Utility.addEp("IntEndPt", Messages.typeInpt, "11", "11");
		Utility.selPageNode(Messages.role1, Messages.sslPage);
		wabot.checkBox().select();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("No").click();
		propShell.activate();
		boolean no = wabot.checkBox().isChecked()
				&& wabot.comboBoxWithLabel(Messages.inpCmbLbl).getText().isEmpty();
		wabot.checkBox().click();
		wabot.checkBox().select();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("Yes").click();
		assertTrue("testSSLWithout443Without80",
				no
				&& wabot.checkBox().isChecked()
				&& wabot.comboBoxWithLabel(Messages.inpCmbLbl).getText().contains("public:443"));
		propShell.close();
	}

	@Test
	// Test cases SSL 2.2.1 number 14
	public void testSSLZeroEndpoints() throws Exception {
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projWithEp,
						Messages.role1, Messages.endptPage);
		Utility.selPageNode(Messages.role1, Messages.sslPage);
		wabot.checkBox().select();
		// do not create endpoint as well
		wabot.shell(Messages.sslPage).activate();
		wabot.button("No").click();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("OK").click();
		propShell.activate();
		assertTrue("testSSLZeroEndpoints", !wabot.checkBox().isChecked());
		propShell.close();
	}

	@Test
	// Test cases SSL 2.2.1 number 15
	public void testComboSelTo443() throws Exception {
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projWithEp,
						Messages.role1, Messages.endptPage);
		// input
		wabot.button(Messages.roleAddBtn).click();
		Utility.addEp("IntEndPt", Messages.typeInpt, "11", "11");
		wabot.button(Messages.roleAddBtn).click();
		Utility.addEp("https", Messages.typeInpt, "443", "8081");
		Utility.selPageNode(Messages.role1, Messages.sslPage);
		wabot.checkBox().select();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("OK").click();
		propShell.activate();
		wabot.comboBoxWithLabel(Messages.inpCmbLbl).setSelection(0);
		boolean val = wabot.comboBoxWithLabel(Messages.inpCmbLbl).getText().contains("public:11");
		wabot.comboBoxWithLabel(Messages.inpCmbLbl).setSelection(1);
		wabot.shell(Messages.sslPage).activate();
		wabot.button("OK").click();
		assertTrue("testComboSelTo443",
				val
				&& wabot.checkBox().isChecked()
				&& wabot.comboBoxWithLabel(Messages.inpCmbLbl).getText().contains("public:443"));
		propShell.close();
	}

	@Test
	// Test cases SSL 2.2.1 number 16
	public void testComboSelTo80When443InputPresent() throws Exception {
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projWithEp,
						Messages.role1, Messages.endptPage);
		wabot.button(Messages.roleAddBtn).click();
		Utility.addEp("http", Messages.typeInpt, "80", "8080");
		wabot.button(Messages.roleAddBtn).click();
		Utility.addEp("https", Messages.typeInpt, "443", "8081");
		Utility.selPageNode(Messages.role1, Messages.sslPage);
		wabot.checkBox().select();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("OK").click();
		propShell.activate();
		wabot.comboBoxWithLabel(Messages.inpCmbLbl).setSelection(0);
		wabot.shell(Messages.sslPage).activate();
		wabot.button("OK").click();
		assertTrue("testComboSelTo80When443InputPresent",
				wabot.checkBox().isChecked()
				&& wabot.comboBoxWithLabel(Messages.inpCmbLbl).getText().isEmpty());
		propShell.close();
	}

	@Test
	// Test cases SSL 2.2.1 number 17
	public void testComboSelTo80When443InstancePresent() throws Exception {
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projWithEp,
						Messages.role1, Messages.endptPage);
		wabot.button(Messages.roleAddBtn).click();
		Utility.addEp("http", Messages.typeInpt, "80", "8080");
		wabot.button(Messages.roleAddBtn).click();
		Utility.addEp("https", Messages.typeInstnc, "443", "8081");
		Utility.selPageNode(Messages.role1, Messages.sslPage);
		wabot.checkBox().select();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("No").click();
		propShell.activate();
		wabot.comboBoxWithLabel(Messages.inpCmbLbl).setSelection(0);
		wabot.button("No").click();
		boolean no = wabot.checkBox().isChecked()
				&& wabot.comboBoxWithLabel(Messages.inpCmbLbl).getText().isEmpty();
		propShell.activate();
		wabot.comboBoxWithLabel(Messages.inpCmbLbl).setSelection(0);
		wabot.shell(Messages.sslPage).activate();
		wabot.button("Yes").click();
		assertTrue("testComboSelTo80When443InstancePresent",
				no
				&& wabot.checkBox().isChecked()
				&& wabot.comboBoxWithLabel(Messages.inpCmbLbl).getText().contains("public:8443"));
		propShell.close();
	}

	@Test
	// Test cases SSL 2.2.1 number 18
	public void testComboSelTo80When443NotPresent() throws Exception {
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projWithEp,
						Messages.role1, Messages.endptPage);
		wabot.button(Messages.roleAddBtn).click();
		Utility.addEp("http", Messages.typeInpt, "80", "8080");
		Utility.selPageNode(Messages.role1, Messages.sslPage);
		wabot.checkBox().select();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("No").click();
		propShell.activate();
		wabot.comboBoxWithLabel(Messages.inpCmbLbl).setSelection(0);
		wabot.shell(Messages.sslPage).activate();
		wabot.button("No").click();
		propShell.activate();
		boolean no = wabot.checkBox().isChecked()
				&& wabot.comboBoxWithLabel(Messages.inpCmbLbl).getText().isEmpty();
		wabot.comboBoxWithLabel(Messages.inpCmbLbl).setSelection(0);
		wabot.shell(Messages.sslPage).activate();
		wabot.button("Yes").click();
		assertTrue("testComboSelTo80When443NotPresent",
				no
				&& wabot.checkBox().isChecked()
				&& wabot.comboBoxWithLabel(Messages.inpCmbLbl).getText().contains("public:443"));
		propShell.close();
	}

	@Test
	// Test cases SSL 2.2.1 number 19
	public void testComboSelToOtherThan443And80() throws Exception {
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projWithEp,
						Messages.role1, Messages.endptPage);
		// input
		wabot.button(Messages.roleAddBtn).click();
		Utility.addEp("IntEndPt", Messages.typeInpt, "11", "11");
		Utility.selPageNode(Messages.role1, Messages.sslPage);
		wabot.checkBox().select();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("No").click();
		propShell.activate();
		wabot.comboBoxWithLabel(Messages.inpCmbLbl).setSelection(0);
		assertTrue("testComboSelToOtherThan443And80",
				wabot.comboBoxWithLabel(Messages.inpCmbLbl).getText().contains("public:11"));
		propShell.close();
	}


	@Test
	// Test cases SSL 2.2.1 number 20
	public void testEndpointsLink() throws Exception {
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projWithEp,
						Messages.role1, Messages.sslPage);
		wabot.link().click(Messages.endLink);
		SWTBotShell shell = wabot.shell(String.format("%s%s%s",
				Messages.propPageTtl, " " ,Messages.projWithEp));
		shell.activate();
		SWTBotTree properties = shell.bot().tree();
		assertTrue("testEndpointsLink",
				properties.getAllItems().length == 1);
		shell.close();
		propShell.close();
	}

	// Not implementing 21, 22, 23 (Will need latest SWTBot)

	//@Test
	// Test cases SSL 2.2.1 number 21
	public void testCertificatesLink() throws Exception {
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projWithEp,
						Messages.role1, Messages.certPage);
		wabot.button(Messages.roleAddBtn).click();
		Utility.addCert(Messages.test, Messages.test);
		Utility.selPageNode(Messages.role1, Messages.sslPage);
		wabot.link().click(Messages.certLink);
		SWTBotShell shell = wabot.shell(String.format("%s%s%s",
				Messages.propPageTtl, " " ,Messages.projWithEp));
		shell.activate();
		SWTBotTree properties = shell.bot().tree();
		assertTrue("testCertificatesLink",
				properties.getAllItems().length == 1
				&& wabot.table().cell(0, 0).equalsIgnoreCase(Messages.test));
		shell.close();
		propShell.close();
	}

	@Test
	// Test cases SSL 2.2.1 number 24
	public void testEndpointsLinkAddEndpoint() throws Exception {
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projWithEp,
						Messages.role1, Messages.sslPage);
		wabot.checkBox().select();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("Yes").click();
		wabot.link().click(Messages.endLink);
		wabot.shell(String.format("%s%s%s",
				Messages.propPageTtl, " " ,Messages.projWithEp)).activate();
		// input
		wabot.button(Messages.roleAddBtn).click();
		Utility.addEp("IntEndPt", Messages.typeInpt, "11", "11");
		wabot.button("OK").click();
		propShell.activate();
		assertTrue("testEndpointsLinkAddEndpoint",
				wabot.comboBoxWithLabel(Messages.inpCmbLbl).itemCount() == 2);
		propShell.close();
	}

	@Test
	// Test cases SSL 2.2.1 number 25
	public void testEndpointsLinkRemEndpoint() throws Exception {
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projWithEp,
						Messages.role1, Messages.sslPage);
		wabot.checkBox().select();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("Yes").click();
		wabot.link().click(Messages.endLink);
		wabot.shell(String.format("%s%s%s",
				Messages.propPageTtl, " " ,Messages.projWithEp)).activate();
		// input
		wabot.button(Messages.roleAddBtn).click();
		Utility.addEp("IntEndPt", Messages.typeInpt, "11", "11");
		wabot.table().select(0);
		wabot.button(Messages.roleRemBtn).click();
		wabot.button("Yes").click();
		wabot.button("OK").click();
		propShell.activate();
		assertTrue("testEndpointsLinkRemEndpoint",
				wabot.comboBoxWithLabel(Messages.inpCmbLbl).itemCount() == 1
				&& wabot.comboBoxWithLabel(Messages.inpCmbLbl).getText().isEmpty());
		propShell.close();
	}

	@Test
	// Test cases SSL 2.2.1 number 26
	public void testErrorHandling() throws Exception {
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projWithEp,
						Messages.role1, Messages.certPage);
		wabot.button(Messages.roleAddBtn).click();
		Utility.addCert(Messages.test, Messages.test);
		Utility.selPageNode(Messages.role1, Messages.sslPage);
		wabot.checkBox().select();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("Yes").click();
		// Certificate empty
		wabot.button("OK").click();
		SWTBotShell errorShell = wabot.shell(Messages.genericErrTtl).activate();
		boolean val = errorShell.getText().equals(Messages.genericErrTtl);
		wabot.button("OK").click();
		Utility.selPageNode(Messages.role1, Messages.endptPage);
		errorShell = wabot.shell(String.format(
				"%s%s", Messages.okToLeaveTtl, " ")).activate();
		boolean val1 = errorShell.getText().
				equals(String.format("%s%s",
						Messages.okToLeaveTtl, " "));
		wabot.button("OK").click();
		wabot.checkBox().click();
		Utility.selPageNode(Messages.role1, Messages.endptPage);
		// In place edit
		wabot.table().click(0, 2);
		wabot.text("443", 0).typeText("80");
		wabot.table().click(0, 0);
		Utility.selPageNode(Messages.role1, Messages.sslPage);
		wabot.checkBox().select();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("No").click();
		wabot.comboBoxWithLabel(Messages.httpsCert).setSelection(0);
		// Endpoint empty
		wabot.button("OK").click();
		errorShell = wabot.shell(Messages.genericErrTtl).activate();
		boolean val2 = errorShell.getText().equals(Messages.genericErrTtl);
		wabot.button("OK").click();
		Utility.selPageNode(Messages.role1, Messages.endptPage);
		errorShell = wabot.shell(String.format(
				"%s%s", Messages.okToLeaveTtl, " ")).activate();
		boolean val3 = errorShell.getText().
				equals(String.format("%s%s",
						Messages.okToLeaveTtl, " "));
		wabot.button("OK").click();
		assertTrue("testErrorHandling", val && val1 && val2 && val3);
		propShell.close();
	}

	@Test
	// Test cases SSL 2.2.1 number 27
	public void testSSLDeleteEndpoint() throws Exception {
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projWithEp,
						Messages.role1, Messages.certPage);
		wabot.button(Messages.roleAddBtn).click();
		Utility.addCert(Messages.test, Messages.test);
		Utility.selPageNode(Messages.role1, Messages.sslPage);
		wabot.checkBox().select();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("Yes").click();
		wabot.comboBoxWithLabel(Messages.httpsCert).setSelection(0);
		Utility.selPageNode(Messages.role1, Messages.endptPage);
		// internal endpoint cancel
		wabot.table().select(1);
		wabot.button(Messages.roleRemBtn).click();
		wabot.shell(Messages.delEndPtTtl).activate();
		wabot.button(Messages.cnclBtn).click();
		Utility.selPageNode(Messages.role1, Messages.sslPage);
		boolean val1 = wabot.checkBox().isChecked();
		// internal endpoint OK
		Utility.selPageNode(Messages.role1, Messages.endptPage);
		wabot.table().select(1);
		wabot.button(Messages.roleRemBtn).click();
		wabot.shell(Messages.delEndPtTtl).activate();
		wabot.button("OK").click();
		boolean val2 = wabot.table().rowCount() == 1;
		Utility.selPageNode(Messages.role1, Messages.sslPage);
		boolean val3 = !wabot.checkBox().isChecked();
		wabot.checkBox().select();
		wabot.shell(Messages.sslPage).activate();
		wabot.button("Yes").click();
		wabot.comboBoxWithLabel(Messages.httpsCert).setSelection(0);
		Utility.selPageNode(Messages.role1, Messages.endptPage);
		// input endpoint cancel
		wabot.table().select(0);
		wabot.button(Messages.roleRemBtn).click();
		wabot.shell(Messages.delEndPtTtl).activate();
		wabot.button(Messages.cnclBtn).click();
		Utility.selPageNode(Messages.role1, Messages.sslPage);
		boolean val4 = wabot.checkBox().isChecked();
		// input endpoint OK
		Utility.selPageNode(Messages.role1, Messages.endptPage);
		wabot.table().select(0);
		wabot.button(Messages.roleRemBtn).click();
		wabot.shell(Messages.delEndPtTtl).activate();
		wabot.button("OK").click();
		boolean val5 = wabot.table().rowCount() == 0;
		Utility.selPageNode(Messages.role1, Messages.sslPage);
		boolean val6 = !wabot.checkBox().isChecked();
		assertTrue("testSSLDeleteEndpoint", val1 && val2
				&& val3
				&& val4
				&& val5
				&& val6);
		propShell.close();
	}
}
