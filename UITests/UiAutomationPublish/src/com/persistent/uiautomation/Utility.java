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

import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses;

import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;


public class Utility {

	protected static SWTWorkbenchBot wabot = new SWTWorkbenchBot();

	protected static boolean isProjExist(String name) {
		try {
			SWTBotView pkgExplorer = getProjExplorer();
			SWTBotTree tree = pkgExplorer.bot().tree();
			tree.getTreeItem(name);
			return true;
		} catch (WidgetNotFoundException e) {
			return false;
		}
	}

	protected static SWTBotView getProjExplorer() {
		SWTBotView view;
		if (wabot.activePerspective().getLabel().equals("Resource")
				|| wabot.activePerspective().getLabel().equals("Java EE")) {
			view = wabot.viewByTitle("Project Explorer");
		} else {
			view = wabot.viewByTitle("Package Explorer");
		}
		return view;
	}

	//create project with JDK and server configured
	public static boolean createProjectSrvJDKCloud(String projName,
			String srvPath) throws Exception {
		wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
		SWTBotShell sh = wabot.shell(Messages.newWAProjTtl);
		wabot.textWithLabel(Messages.projNm).setText(projName);
		wabot.button(Messages.nxtBtn).click();
		wabot.checkBox(Messages.jdkChkBxTxt).click();
		// Directly set JDK path as Native dialog box can not be handled
		// May fail if directory is not present in real
		wabot.tabItem(Messages.srv).activate();
		wabot.checkBox(Messages.srvChkBxTxt).click();
		wabot.textInGroup(Messages.emltrGrp).setText(srvPath);
		wabot.comboBoxWithLabelInGroup(Messages.sel,
				Messages.emltrGrp).setSelection(Messages.cmbValJet8);
		wabot.button(Messages.finBtn).click();
		wabot.waitUntil(shellCloses(sh));
		wabot.sleep(10000);
		return Utility.isProjExist(projName);
	}

	public static void createStorageAcc() {
		// create new storage account
		wabot.buttonWithLabel(Messages.strAcLbl).click();
		wabot.shell(Messages.strAcTtl).activate();
		wabot.textWithLabel(Messages.strAcLbl).setText(Messages.accName);
		wabot.comboBoxWithLabel(Messages.loc).setSelection(Messages.southAsia);
		wabot.textWithLabel(Messages.desc).setText("automationaccountdesc");
		wabot.button("OK").click();
		wabot.sleep(10000);
	}

	public static void createService() {
		// create new service
		wabot.buttonWithLabel(Messages.serNmLbl).click();
		wabot.shell(Messages.servTtl).activate();
		wabot.textWithLabel(Messages.serNmLbl).setText(Messages.serviceName);
		wabot.comboBoxWithLabel(Messages.loc).setSelection(Messages.southAsia);
		wabot.textWithLabel(Messages.desc).setText("automationservicedesc");
		wabot.button("OK").click();
		wabot.sleep(10000);
	}

	public static SWTBotTreeItem selProjFromExplorer(String projName) {
		SWTBotView packageExplorer = getProjExplorer();
		SWTBotTree tree = packageExplorer.bot().tree();
		return tree.getTreeItem(projName).select();
	}

	public static void deleteSelectedProject() {
		wabot.menu("Edit").menu("Delete").click();
		SWTBotShell shell = wabot.shell("Delete Resources");
		shell.activate();
		wabot.checkBox("Delete project contents on disk (cannot be undone)").select();
		wabot.button("OK").click();
		wabot.waitUntil(shellCloses(shell));
		wabot.sleep(10000);
	}

	// create dynamic web project
	public static void createDynamicWebProj(String projName) throws Exception {
		if (wabot.activePerspective().getLabel().equals("Resource")) {
			wabot.menu("Window").menu("Open Perspective").menu("Java EE").click();
			wabot.menu("File").menu("New").menu("Dynamic Web Project").click();
			SWTBotShell sh = wabot.shell("New Dynamic Web Project").activate();
			wabot.textWithLabel(Messages.projNm).setText(projName);
			wabot.button(Messages.finBtn).click();
			wabot.waitUntil(shellCloses(sh));
			wabot.sleep(10000);
			wabot.menu("Window").menu("Open Perspective").menu("Other...").click();
			SWTBotShell sh1 = wabot.shell("Open Perspective").activate();
			wabot.table().select("Resource (default)");
			wabot.button("OK").click();
			wabot.waitUntil(shellCloses(sh1));
			wabot.sleep(5000);
		} else {
			wabot.menu("File").menu("New").menu("Dynamic Web Project").click();
			SWTBotShell sh = wabot.shell("New Dynamic Web Project").activate();
			wabot.textWithLabel(Messages.projNm).setText(projName);
			wabot.button(Messages.finBtn).click();
			wabot.waitUntil(shellCloses(sh));
			wabot.sleep(10000);
		}
	}

	public static void importPubFile() {
		wabot.menu(Messages.window).menu("Preferences").click();
		SWTBotShell prefShell = wabot.
				shell("Preferences");
		prefShell.activate();
		SWTBotTree properties = prefShell.bot().tree();
		properties.getTreeItem(Messages.waPage).expand().
		getNode("Storage Accounts").select();
		wabot.sleep(30000);
		wabot.button(Messages.imBtn).click();
		SWTBotShell importShell = wabot.shell(Messages.impSubTtl);
		importShell.activate();
		wabot.text().setText(Messages.pubFlPath);
		wabot.button("OK").click();
		wabot.sleep(60000);
		wabot.button("OK").click();
	}

	public static boolean isStorageAccExists(String name) {
		String[] accounts = wabot.comboBoxWithLabel(Messages.strAcLbl).items();
		return Arrays.asList(accounts).contains(name);
	}

	public static boolean isServiceExists(String name) {
		String[] services = wabot.comboBoxWithLabel(Messages.serNmLbl).items();
		return Arrays.asList(services).contains(name);
	}

	public static boolean checkForNewCreation() {
		boolean newCreation = false;
		if (!Utility.isStorageAccExists(Messages.accName)) {
			Utility.createStorageAcc();
			newCreation = true;
		}
		if (!Utility.isServiceExists(Messages.serviceName)) {
			Utility.createService();
			newCreation = true;
		}
		return newCreation;
	}

	public static SWTBotShell selectPageUsingContextMenu(
			String projName, String role, String page) {
		SWTBotTreeItem proj1 = Utility.
				selProjFromExplorer(projName);
		SWTBotTreeItem workerRoleNode = proj1.expand().
				getNode(role).select();
		workerRoleNode.contextMenu("Properties").click();
		SWTBotShell propShell = wabot.
				shell(String.format("%s%s%s", Messages.
						propPageTtl, " ", role));
		propShell.activate();
		SWTBotTree properties = propShell.bot().tree();
		if (page.equalsIgnoreCase(Messages.roleTreeRoot)) {
			properties.getTreeItem(Messages.roleTreeRoot).select();
		} else {
			properties.getTreeItem(Messages.roleTreeRoot).expand().
			getNode(page).select();
		}
		return propShell;
	}

	public static void projectRefresh(String projName) {
		SWTBotTreeItem proj = Utility.
				selProjFromExplorer(projName);
		proj.contextMenu("Refresh").click();
		wabot.sleep(10000);
	}

	public static String getLoc(String projName, String member) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject project = root.getProject(projName);
		IResource resource = project.findMember(member);
		return resource.getLocation().toOSString();
	}

	public static void createAddNewCert(String pwd) {
		wabot.button(Messages.roleAddBtn).click();
		wabot.shell(Messages.addCertTtl).activate();
		wabot.button(Messages.newCertButton).click();
		wabot.shell(Messages.newCertTtl).activate();
		wabot.textWithLabel(Messages.pwd).setText(pwd);
		wabot.textWithLabel(Messages.confirm).setText(pwd);
		wabot.textWithLabelInGroup(Messages.cerLbl, Messages.grpSave).
		setText(String.format("%s%s", getLoc(Messages.projWithCmpnt, "\\cert"), Messages.certPath));
		wabot.textWithLabelInGroup(Messages.pfxLbl, Messages.grpSave).
		setText(String.format("%s%s", getLoc(Messages.projWithCmpnt, "\\cert"), Messages.pfxPath));
		wabot.button("OK").click();
		wabot.button("OK").click();
	}

	public static SWTBotShell selPageNode(String role, String page) {
		SWTBotShell propShell = wabot.
				shell(String.format("%s%s%s",
						Messages.propPageTtl, " ", role));
		propShell.activate();
		SWTBotTree properties = propShell.bot().tree();
		properties.getTreeItem(Messages.roleTreeRoot).expand().
		getNode(page).select();
		return propShell;
	}

	public static void addLocalStorage() {
		// local storage
		wabot.button(Messages.roleAddBtn).click();
		wabot.button("OK").click();
		wabot.button("OK").click();
	}

	public static void fillCertDetails(String pwd) {
		wabot.sleep(1000);
		wabot.shell(Messages.upCerTtl).activate();
		wabot.textWithLabel(Messages.selPfxLbl).setText(
				String.format("%s%s", Utility.getLoc(Messages.projWithCmpnt, "\\cert"), Messages.pfxPath));
		wabot.textWithLabel(Messages.certPwd).setText(pwd);
		wabot.button("OK").click();
	}

	public static void fillRemoteAccessDetails() {
		wabot.textWithLabel(Messages.userName).setText(Messages.test);
		wabot.textWithLabel(Messages.pwd).setText(Messages.strPwd);
		wabot.textWithLabel(Messages.confirm).setText(Messages.strPwd);
	}
}

