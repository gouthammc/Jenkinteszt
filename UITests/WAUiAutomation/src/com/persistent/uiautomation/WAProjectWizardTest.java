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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
@RunWith(SWTBotJunit4ClassRunner.class)
public class WAProjectWizardTest {
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
        if (Utility.isProjExist(Messages.test)) {
        	Utility.selProjFromExplorer(Messages.test).select();
        	Utility.deleteSelectedProject();
        }
    }

    @After
    public void cleanUp() throws Exception {
    	if (Utility.isProjExist(Messages.projName)) {
    		Utility.selProjFromExplorer(Messages.projName).select();
    		Utility.deleteSelectedProject();
    	}
    }

    @Test
    public void testCreateNewProject() throws Exception {
        wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
        SWTBotShell sh = wabot.shell(Messages.newWAProjTtl).activate();
        wabot.textWithLabel(Messages.projNm).setText(Messages.projName);
        wabot.button(Messages.finBtn).click();
        wabot.waitUntil(shellCloses(sh));
        wabot.sleep(10000);
        assertTrue(Messages.waProjNotExist,
        		Utility.isProjExist(Messages.projName));
    }

    @Test
    public void testCreateNewProjectWithEp() throws Exception {
        Utility.createProjWithEp(Messages.projName);
        assertTrue(Messages.waProjNotExist,
        		Utility.isProjExist(Messages.projName));
    }


    @Test
    public void testCreateExistingProject() throws Exception {
        Utility.createProject(Messages.projName);
        wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
        wabot.shell(Messages.newWAProjTtl).activate();
        wabot.textWithLabel(Messages.projNm).setText(Messages.projName);
        assertFalse("testCreateExistingProject",
        		wabot.button(Messages.finBtn).isEnabled());
        wabot.button(Messages.cnclBtn).click();
    }

    @Test
    public void testCreateProjAtGivenLoc() throws Exception {
        // create proj
        wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
        SWTBotShell sh = wabot.shell(Messages.newWAProjTtl).activate();
        wabot.textWithLabel(Messages.projNm).setText(Messages.projAtOtherLoc);
        wabot.checkBox("Use default location").click();
        wabot.textWithLabel(Messages.loc).setText(Messages.otherLoc);
        wabot.button(Messages.finBtn).click();
        wabot.waitUntil(shellCloses(sh));
        wabot.sleep(10000);
        //verify
        SWTBotTreeItem proj1 = Utility.
        		selProjFromExplorer(Messages.projAtOtherLoc);
        proj1.contextMenu("Properties").click();
        SWTBotShell shell1 = wabot.shell(String.format("%s%s%s",
        		Messages.propPageTtl, " " ,
        		Messages.projAtOtherLoc));
        shell1.activate();
        SWTBotTree properties = shell1.bot().tree();
        properties.getTreeItem("Resource").select();
        assertTrue("testCreateProjAtGivenLoc",
        		wabot.textWithLabel(Messages.loc).getText().equalsIgnoreCase(
                Messages.otherLoc + Messages.projAtOtherLoc));
        wabot.button(Messages.cnclBtn).click();
        Utility.selProjFromExplorer(Messages.projAtOtherLoc).select();
        Utility.deleteSelectedProject();
    }

    @Test
    public void testCreateProjAtGivenLocErr() throws Exception {
        // create proj
        wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
        wabot.shell(Messages.newWAProjTtl).activate();
        wabot.textWithLabel(Messages.projNm).setText(Messages.projAtOtherLoc);
        wabot.checkBox("Use default location").click();
        wabot.textWithLabel(Messages.loc).setText(Messages.nonExistingLoc);
        wabot.button(Messages.finBtn).click();
        wabot.sleep(2000);
        //verify message
        SWTBotShell sh = wabot.shell(Messages.genericErrTtl).activate();
        String msg = sh.getText();
        wabot.button("OK").click();
        wabot.button(Messages.cnclBtn).click();
        assertEquals("testEmptyEpName", msg,
                Messages.genericErrTtl);
    }

    //Test Cases for version 1.7 starts from here.

    @Test
    //Test Case 140
    public void testEmptyProjectName() throws Exception {
        wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
        SWTBotShell sh = wabot.shell(Messages.newWAProjTtl).activate();
        wabot.textWithLabel(Messages.projNm).setText("");
        assertTrue("testEmptyProjectName",
        		!wabot.button(Messages.bckBtn).isEnabled()
        		&& !wabot.button(Messages.nxtBtn).isEnabled()
        		&& !wabot.button(Messages.finBtn).isEnabled()
        		&& wabot.button(Messages.cnclBtn).isEnabled());
        sh.close();
    }

    @Test
    //Test Case 141
    public void testEnterProjectName() throws Exception {
        wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
        SWTBotShell sh = wabot.shell(Messages.newWAProjTtl).activate();
        wabot.textWithLabel(Messages.projNm).setText(Messages.projName);
        assertTrue("testEnterProjectName",
        		!wabot.button(Messages.bckBtn).isEnabled()
        		&& wabot.button(Messages.nxtBtn).isEnabled()
        		&& wabot.button(Messages.finBtn).isEnabled()
        		&& wabot.button(Messages.cnclBtn).isEnabled());
        sh.close();
    }

    @Test
    //Test Case 142
    public void testNextClick() throws Exception {
        wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
        SWTBotShell sh = wabot.shell(Messages.newWAProjTtl).activate();
        wabot.textWithLabel(Messages.projNm).setText(Messages.projName);
        wabot.button(Messages.nxtBtn).click();
        assertTrue("testNextClick", wabot.button(Messages.bckBtn).isEnabled()
        		&& wabot.button(Messages.nxtBtn).isEnabled()
        		&& wabot.button(Messages.finBtn).isEnabled()
        		&& wabot.button(Messages.cnclBtn).isEnabled()
        		&& wabot.checkBox(Messages.jdkChkBxTxt).isEnabled());
        sh.close();
    }

    @Test
    //Test Case 143
    public void testBackClick() throws Exception {
        wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
        SWTBotShell sh = wabot.shell(Messages.newWAProjTtl).activate();
        wabot.textWithLabel(Messages.projNm).setText(Messages.projName);
        wabot.button(Messages.nxtBtn).click();
        wabot.button(Messages.bckBtn).click();
        assertTrue("testBackClick", !wabot.button(Messages.bckBtn).isEnabled()
        		&& wabot.button(Messages.nxtBtn).isEnabled()
        		&& wabot.button(Messages.finBtn).isEnabled()
        		&& wabot.button(Messages.cnclBtn).isEnabled());
        sh.close();
    }

    @Test
    //Test Case 144
    public void testJdkChecked() throws Exception {
    	wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
    	SWTBotShell sh = wabot.shell(Messages.newWAProjTtl).activate();
    	wabot.textWithLabel(Messages.projNm).setText(Messages.projName);
    	wabot.button(Messages.nxtBtn).click();
    	wabot.checkBox(Messages.jdkChkBxTxt).click();
    	// Finish is enabled due to auto detect JDK directory
    	boolean valJdk = wabot.button(Messages.nxtBtn).isEnabled()
    			&& wabot.button(Messages.debugBrowseBtn).isEnabled()
    			&& wabot.radioInGroup(
    					Messages.autoDlJdkCldRdBtnLbl, Messages.dlgDownloadGrp).isEnabled()
    			&& wabot.radioInGroup(
    					Messages.autoDlJdkCldRdBtnLbl, Messages.dlgDownloadGrp).isSelected()
    			&& wabot.labelInGroup(Messages.dlgDlUrlLbl,
    					Messages.dlgDownloadGrp).isEnabled()
    			&& wabot.labelInGroup(Messages.dlgDlAccessKey,
    					Messages.dlgDownloadGrp).isEnabled()
    			&& wabot.textWithLabelInGroup(Messages.dlgDlUrlLbl,
    					Messages.dlgDownloadGrp).isEnabled()
    			&& wabot.comboBoxWithLabelInGroup(Messages.dlgDlAccessKey,
    					Messages.dlgDownloadGrp).isEnabled()
    			&& wabot.button(Messages.finBtn).isEnabled();
    	wabot.tabItem(Messages.srv).activate();
    	boolean valSrv = wabot.button(Messages.nxtBtn).isEnabled()
    			&& wabot.button(Messages.finBtn).isEnabled()
    			&& wabot.checkBox(Messages.srvChkBxTxt).isEnabled()
    			&& !wabot.radioInGroup(
    					Messages.autoDlSrvCldRdBtnLbl, Messages.dlgDownloadGrp).isEnabled()
    			&& !wabot.labelInGroup(Messages.dlgDlUrlLbl,
    					Messages.dlgDownloadGrp).isEnabled()
    			&& !wabot.labelInGroup(Messages.dlgDlAccessKey,
    					Messages.dlgDownloadGrp).isEnabled()
    			&& !wabot.textWithLabelInGroup(Messages.dlgDlUrlLbl,
    					Messages.dlgDownloadGrp).isEnabled()
    			&& !wabot.comboBoxWithLabelInGroup(Messages.dlgDlAccessKey,
    					Messages.dlgDownloadGrp).isEnabled();
    	wabot.tabItem(Messages.app).activate();
    	boolean valApp = !wabot.table().isEnabled();
    	assertTrue("testJdkChecked", valJdk
    			&& valSrv
    			&& valApp);
    	sh.close();
    }

    @Test
    //Test Case 145
    public void testJdkCheckUncheck() throws Exception {
    	wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
    	SWTBotShell sh = wabot.shell(Messages.newWAProjTtl).activate();
    	wabot.textWithLabel(Messages.projNm).setText(Messages.projName);
    	wabot.button(Messages.nxtBtn).click();
    	wabot.checkBox(Messages.jdkChkBxTxt).click();
    	boolean isEnabledJdk = wabot.button(Messages.nxtBtn).isEnabled()
    			&& wabot.button(Messages.debugBrowseBtn).isEnabled()
    			&& wabot.radioInGroup(
    					Messages.autoDlJdkCldRdBtnLbl, Messages.dlgDownloadGrp).isEnabled()
    			&& wabot.radioInGroup(
    					Messages.autoDlJdkCldRdBtnLbl, Messages.dlgDownloadGrp).isSelected()
    			&& wabot.labelInGroup(Messages.dlgDlUrlLbl,
    					Messages.dlgDownloadGrp).isEnabled()
    			&& wabot.labelInGroup(Messages.dlgDlAccessKey,
    					Messages.dlgDownloadGrp).isEnabled()
    			&& wabot.textWithLabelInGroup(Messages.dlgDlUrlLbl,
    					Messages.dlgDownloadGrp).isEnabled()
    			&& wabot.comboBoxWithLabelInGroup(Messages.dlgDlAccessKey,
    					Messages.dlgDownloadGrp).isEnabled()
    			&& wabot.button(Messages.finBtn).isEnabled();
    	wabot.tabItem(Messages.srv).activate();
    	boolean valEnabledSrv = wabot.button(Messages.nxtBtn).isEnabled()
    			&& wabot.button(Messages.finBtn).isEnabled()
    			&& wabot.checkBox(Messages.srvChkBxTxt).isEnabled()
    			&& !wabot.radioInGroup(
    					Messages.autoDlSrvCldRdBtnLbl, Messages.dlgDownloadGrp).isEnabled()
    			&& !wabot.labelInGroup(Messages.dlgDlUrlLbl,
    					Messages.dlgDownloadGrp).isEnabled()
    			&& !wabot.labelInGroup(Messages.dlgDlAccessKey,
    					Messages.dlgDownloadGrp).isEnabled()
    			&& !wabot.textWithLabelInGroup(Messages.dlgDlUrlLbl,
    					Messages.dlgDownloadGrp).isEnabled()
    			&& !wabot.comboBoxWithLabelInGroup(Messages.dlgDlAccessKey,
    					Messages.dlgDownloadGrp).isEnabled();
    	wabot.tabItem(Messages.jdk).activate();
    	wabot.checkBox(Messages.jdkChkBxTxt).click();
    	boolean isDisabledJdk =
    			!wabot.button(Messages.debugBrowseBtn).isEnabled()
    			&& !wabot.radioInGroup(
    					Messages.autoDlJdkCldRdBtnLbl, Messages.dlgDownloadGrp).isEnabled()
    					&& wabot.button(Messages.finBtn).isEnabled();
    	wabot.tabItem(Messages.srv).activate();
    	boolean valDisabledSrv =
    			!wabot.checkBox(Messages.srvChkBxTxt).isEnabled();
    	assertTrue("testJdkCheckUncheck", isEnabledJdk
    			&& valEnabledSrv
    			&& isDisabledJdk
    			&& valDisabledSrv);
    	sh.close();
    }

    @Test
    //Test Case 147
    public void testValidJdkPath() throws Exception {
    	Utility.createProject(Messages.projName);
        wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
        SWTBotShell sh = wabot.shell(Messages.newWAProjTtl).activate();
        wabot.textWithLabel(Messages.projNm).setText(Messages.test);
        wabot.button(Messages.nxtBtn).click();
        wabot.checkBox(Messages.jdkChkBxTxt).click();
        wabot.textInGroup(Messages.emltrGrp).
        setText(Utility.getLoc(Messages.projName, "\\cert"));
        assertTrue("testValidJdkPath", wabot.button(Messages.finBtn).isEnabled());
        sh.close();
    }

    @Test
    //Test Case 148
    public void testSerChecked() throws Exception {
    	Utility.createProject(Messages.projName);
        wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
        SWTBotShell sh = wabot.shell(Messages.newWAProjTtl).activate();
        wabot.textWithLabel(Messages.projNm).setText(Messages.test);
        wabot.button(Messages.nxtBtn).click();
        wabot.checkBox(Messages.jdkChkBxTxt).click();
        wabot.textInGroup(Messages.emltrGrp).
        setText(Utility.getLoc(Messages.projName, "\\cert"));
        wabot.tabItem(Messages.srv).activate();
        wabot.checkBox(Messages.srvChkBxTxt).click();
        boolean isEnabled = wabot.comboBoxWithLabel(Messages.sel).isEnabled()
        		&& wabot.textInGroup(Messages.emltrGrp).isEnabled()
        		&& wabot.link().isEnabled()
        		&& wabot.button(Messages.debugBrowseBtn).isEnabled()
        		&& !wabot.button(Messages.finBtn).isEnabled()
        		&& wabot.radioInGroup(
    					Messages.autoDlSrvCldRdBtnLbl, Messages.dlgDownloadGrp).isEnabled()
    			&& wabot.labelInGroup(Messages.dlgDlUrlLbl,
    					Messages.dlgDownloadGrp).isEnabled()
    			&& wabot.labelInGroup(Messages.dlgDlAccessKey,
    					Messages.dlgDownloadGrp).isEnabled()
    			&& wabot.textWithLabelInGroup(Messages.dlgDlUrlLbl,
    					Messages.dlgDownloadGrp).isEnabled()
    			&& wabot.comboBoxWithLabelInGroup(Messages.dlgDlAccessKey,
    					Messages.dlgDownloadGrp).isEnabled();
        wabot.tabItem(Messages.app).activate();
        boolean isEnabledApp = wabot.table().isEnabled()
        		&& wabot.button(Messages.roleAddBtn).isEnabled()
        		&& !wabot.button(Messages.roleRemBtn).isEnabled()
        		&& !wabot.button(Messages.finBtn).isEnabled();
        assertTrue("testSerChecked", isEnabled
        		&& isEnabledApp);
        sh.close();
    }

    @Test
    //Test Case 149
    public void testSerCheckUncheckValidJdk() throws Exception {
    	Utility.createProject(Messages.projName);
        wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
        SWTBotShell sh = wabot.shell(Messages.newWAProjTtl).activate();
        wabot.textWithLabel(Messages.projNm).setText(Messages.test);
        wabot.button(Messages.nxtBtn).click();
        wabot.checkBox(Messages.jdkChkBxTxt).click();
        wabot.textInGroup(Messages.emltrGrp).
        setText(Utility.getLoc(Messages.projName, "\\cert"));
        wabot.tabItem(Messages.srv).activate();
        wabot.checkBox(Messages.srvChkBxTxt).click();
        boolean isEnabled = wabot.comboBoxWithLabel(Messages.sel).isEnabled()
        		&& wabot.textInGroup(Messages.emltrGrp).isEnabled()
        		&& wabot.link().isEnabled()
        		&& wabot.button(Messages.debugBrowseBtn).isEnabled()
        		&& !wabot.button(Messages.finBtn).isEnabled()
        		&& wabot.radioInGroup(
    					Messages.autoDlSrvCldRdBtnLbl, Messages.dlgDownloadGrp).isEnabled()
    			&& wabot.labelInGroup(Messages.dlgDlUrlLbl,
    					Messages.dlgDownloadGrp).isEnabled()
    			&& wabot.labelInGroup(Messages.dlgDlAccessKey,
    					Messages.dlgDownloadGrp).isEnabled()
    			&& wabot.textWithLabelInGroup(Messages.dlgDlUrlLbl,
    					Messages.dlgDownloadGrp).isEnabled()
    			&& wabot.comboBoxWithLabelInGroup(Messages.dlgDlAccessKey,
    					Messages.dlgDownloadGrp).isEnabled();
        wabot.tabItem(Messages.app).activate();
        boolean isEnabledApp =
        		wabot.table().isEnabled()
        		&& wabot.button(Messages.roleAddBtn).isEnabled()
        		&& !wabot.button(Messages.roleRemBtn).isEnabled()
        		&& !wabot.button(Messages.finBtn).isEnabled();
        wabot.tabItem(Messages.srv).activate();
        wabot.checkBox(Messages.srvChkBxTxt).click();
        boolean isDisabled =
        		!wabot.comboBoxWithLabel(Messages.sel).isEnabled()
        		&& !wabot.textInGroup(Messages.emltrGrp).isEnabled()
        		&& !wabot.link().isEnabled()
        		&& !wabot.button(Messages.debugBrowseBtn).isEnabled()
        		&& !wabot.radioInGroup(
    					Messages.autoDlSrvCldRdBtnLbl, Messages.dlgDownloadGrp).isEnabled()
    			&& !wabot.labelInGroup(Messages.dlgDlUrlLbl,
    					Messages.dlgDownloadGrp).isEnabled()
    			&& !wabot.labelInGroup(Messages.dlgDlAccessKey,
    					Messages.dlgDownloadGrp).isEnabled()
    			&& !wabot.textWithLabelInGroup(Messages.dlgDlUrlLbl,
    					Messages.dlgDownloadGrp).isEnabled()
    			&& !wabot.comboBoxWithLabelInGroup(Messages.dlgDlAccessKey,
    					Messages.dlgDownloadGrp).isEnabled();
        wabot.tabItem(Messages.app).activate();
        boolean isDisabledApp =	!wabot.table().isEnabled()
        		&& !wabot.button(Messages.roleAddBtn).isEnabled()
        		&& !wabot.button(Messages.roleRemBtn).isEnabled()
        		&& wabot.button(Messages.finBtn).isEnabled();
        assertTrue("testSerCheckUncheckValidJdk", isEnabled
        		&& isEnabledApp
        		&& isDisabled
        		&& isDisabledApp);
        sh.close();
    }

    @Test
    //Test Case 151
    public void testValidSerJdkPath() throws Exception {
    	Utility.createProject(Messages.projName);
        wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
        SWTBotShell sh = wabot.shell(Messages.newWAProjTtl).activate();
        wabot.textWithLabel(Messages.projNm).setText(Messages.test);
        wabot.button(Messages.nxtBtn).click();
        wabot.checkBox(Messages.jdkChkBxTxt).click();
        wabot.textInGroup(Messages.emltrGrp).
        setText(Utility.getLoc(Messages.projName, "\\cert"));
        wabot.tabItem(Messages.srv).activate();
        wabot.checkBox(Messages.srvChkBxTxt).click();
        wabot.textInGroup(Messages.emltrGrp).
        setText(Utility.getLoc(Messages.projName, "\\cert"));
        assertFalse("testValidSerJdkPath", wabot.button(Messages.finBtn).isEnabled());
        sh.close();
    }

    @Test
    //Test Case 152
    public void testEmptySerPath() throws Exception {
    	Utility.createProject(Messages.projName);
        wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
        SWTBotShell sh = wabot.shell(Messages.newWAProjTtl).activate();
        wabot.textWithLabel(Messages.projNm).setText(Messages.test);
        wabot.button(Messages.nxtBtn).click();
        wabot.checkBox(Messages.jdkChkBxTxt).click();
        wabot.textInGroup(Messages.emltrGrp).
        setText(Utility.getLoc(Messages.projName, "\\cert"));
        wabot.tabItem(Messages.srv).activate();
        wabot.checkBox(Messages.srvChkBxTxt).click();
        wabot.comboBoxWithLabel(Messages.sel).setSelection(Messages.cmbValJet7);
        assertFalse("testEmptySerPath", wabot.button(Messages.finBtn).isEnabled());
        sh.close();
    }

    @Test
    //Test Case 153
    public void testValidSerJdkData() throws Exception {
    	Utility.createProject(Messages.projName);
        wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
        SWTBotShell sh = wabot.shell(Messages.newWAProjTtl).activate();
        wabot.textWithLabel(Messages.projNm).setText(Messages.test);
        wabot.button(Messages.nxtBtn).click();
        wabot.checkBox(Messages.jdkChkBxTxt).click();
        wabot.textInGroup(Messages.emltrGrp).
        setText(Utility.getLoc(Messages.projName, "\\cert"));
        wabot.tabItem(Messages.srv).activate();
        wabot.checkBox(Messages.srvChkBxTxt).click();
        wabot.comboBoxWithLabel(Messages.sel).setSelection(Messages.cmbValJet7);
        wabot.textInGroup(Messages.emltrGrp).
        setText(Utility.getLoc(Messages.projName, "\\cert"));
        assertTrue("testValidSerJdkData", wabot.button(Messages.finBtn).isEnabled());
        sh.close();
    }

    @Test
    //Test Case 154
    public void testJdkDirBlankSerValid() throws Exception {
    	Utility.createProject(Messages.projName);
        wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
        SWTBotShell sh = wabot.shell(Messages.newWAProjTtl).activate();
        wabot.textWithLabel(Messages.projNm).setText(Messages.test);
        wabot.button(Messages.nxtBtn).click();
        wabot.checkBox(Messages.jdkChkBxTxt).click();
        wabot.tabItem(Messages.srv).activate();
        wabot.checkBox(Messages.srvChkBxTxt).click();
        wabot.comboBoxWithLabel(Messages.sel).setSelection(Messages.cmbValJet7);
        wabot.textInGroup(Messages.emltrGrp).
        setText(Utility.getLoc(Messages.projName, "\\cert"));
        // Due to JDK path auto discovery, Finish button gets enabled.
        assertTrue("testJdkDirBlankSerValid",
        		wabot.button(Messages.finBtn).isEnabled());
        sh.close();
    }
    /*
    @Test
    //Test Case 155
    public void testCustLinkClick() throws Exception {
        wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
        SWTBotShell sh = wabot.shell(Messages.newWAProjTtl).activate();
        wabot.textWithLabel(Messages.projNm).setText(Messages.projName);
        wabot.button(Messages.nxtBtn).click();
        wabot.checkBox(Messages.jdkChkBxTxt).click();
        wabot.tabItem(Messages.srv).activate();
        wabot.checkBox(Messages.srvChkBxTxt).click();
        wabot.linkInGroup(Messages.emltrGrp).click();
        SWTBotShell errShell = wabot.shell(Messages.custLinkTtl).activate();
        String msg = errShell.getText();
        errShell.close();
        assertTrue("testCustLinkClick",
        		msg.equalsIgnoreCase(Messages.custLinkTtl));
        sh.close();
    }

    @Test
    //Test Case 156
    public void testCustLinkCancelClick() throws Exception {
        wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
        SWTBotShell sh = wabot.shell(Messages.newWAProjTtl).activate();
        wabot.textWithLabel(Messages.projNm).setText(Messages.projName);
        wabot.button(Messages.nxtBtn).click();
        wabot.checkBox(Messages.jdkChkBxTxt).click();
        wabot.tabItem(Messages.srv).activate();
        wabot.checkBox(Messages.srvChkBxTxt).click();
        wabot.linkInGroup(Messages.emltrGrp).click();
        SWTBotShell errShell = wabot.shell(Messages.custLinkTtl).activate();
        String msg = errShell.getText();
        wabot.button(Messages.cnclBtn).click();
        assertTrue("testCustLinkCancelClick",
        		msg.equalsIgnoreCase(Messages.custLinkTtl)
        		&& wabot.activeShell().getText().
        		equalsIgnoreCase(Messages.newWAProjTtl));
        sh.close();
    }

    @Test
    //Test Case 157
    public void testCustLinkOkClick() throws Exception {
        wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
        wabot.textWithLabel(Messages.projNm).setText(Messages.projName);
        wabot.button(Messages.nxtBtn).click();
        wabot.checkBox(Messages.jdkChkBxTxt).click();
        wabot.tabItem(Messages.srv).activate();
        wabot.checkBox(Messages.srvChkBxTxt).click();
        wabot.linkInGroup(Messages.emltrGrp).click();
        SWTBotShell errShell = wabot.shell(Messages.custLinkTtl).activate();
        wabot.button("OK").click();
        wabot.waitUntil(shellCloses(errShell));
        assertTrue("testCustLinkOkClick",
        		wabot.activeEditor().getTitle().
        		equalsIgnoreCase(Messages.cmpntFile));
        wabot.activeEditor().close();
    }
	*/
    @Test
    //Test Case 158
    public void testAddClick() throws Exception {
        wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
        SWTBotShell sh = wabot.shell(Messages.newWAProjTtl).activate();
        wabot.textWithLabel(Messages.projNm).setText(Messages.projName);
        wabot.button(Messages.nxtBtn).click();
        wabot.checkBox(Messages.jdkChkBxTxt).click();
        wabot.tabItem(Messages.srv).activate();
        wabot.checkBox(Messages.srvChkBxTxt).click();
        wabot.tabItem(Messages.app).activate();
        wabot.button(Messages.roleAddBtn).click();
        SWTBotShell appShell = wabot.shell(Messages.addAppTtl).activate();
        String msg = appShell.getText();
        appShell.close();
        assertTrue("testAddClick", msg.equalsIgnoreCase(Messages.addAppTtl));
        sh.close();
    }

    @Test
    //Test Case 159
    public void testAddClickFirstRadioEnable() throws Exception {
        wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
        SWTBotShell sh = wabot.shell(Messages.newWAProjTtl).activate();
        wabot.textWithLabel(Messages.projNm).setText(Messages.projName);
        wabot.button(Messages.nxtBtn).click();
        wabot.checkBox(Messages.jdkChkBxTxt).click();
        wabot.tabItem(Messages.srv).activate();
        wabot.checkBox(Messages.srvChkBxTxt).click();
        wabot.tabItem(Messages.app).activate();
        wabot.button(Messages.roleAddBtn).click();
        SWTBotShell appShell = wabot.shell(Messages.addAppTtl).activate();
        assertTrue("testAddClickFirstRadioEnable",
        		wabot.radio(0).isSelected()
        		&& wabot.button(Messages.debugBrowseBtn).isEnabled()
        		&& !wabot.button("OK").isEnabled());
        appShell.close();
        sh.close();
    }

    @Test
    //Test Case 165
    public void testRmvBtnEnblDsbl() throws Exception {
        wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
        SWTBotShell sh = wabot.shell(Messages.newWAProjTtl).activate();
        wabot.textWithLabel(Messages.projNm).setText(Messages.projName);
        wabot.button(Messages.nxtBtn).click();
        wabot.checkBox(Messages.jdkChkBxTxt).click();
        wabot.tabItem(Messages.srv).activate();
        wabot.checkBox(Messages.srvChkBxTxt).click();
        wabot.tabItem(Messages.app).activate();
        boolean disVal = !wabot.button(Messages.roleRemBtn).isEnabled();
        wabot.table().select(0);
		boolean enblVal = wabot.button(Messages.roleRemBtn).isEnabled();
		assertTrue("testRmvBtnEnblDsbl", disVal
				&& enblVal);
        sh.close();
    }

    @Test
    //Test Case 176
    public void testRmvBtnYesPressed() throws Exception {
        wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
        SWTBotShell sh = wabot.shell(Messages.newWAProjTtl).activate();
        wabot.textWithLabel(Messages.projNm).setText(Messages.projName);
        wabot.button(Messages.nxtBtn).click();
        wabot.checkBox(Messages.jdkChkBxTxt).click();
        wabot.tabItem(Messages.srv).activate();
        wabot.checkBox(Messages.srvChkBxTxt).click();
        wabot.tabItem(Messages.app).activate();
        wabot.table().select(0);
		wabot.button(Messages.roleRemBtn).click();
		assertFalse("testRmvBtnYesPressed",
				wabot.table().containsItem(Messages.hlWrld));
        sh.close();
    }

    @Test
    //Test Case 177
    public void testAddAppCancelPressed() throws Exception {
        wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
        SWTBotShell sh = wabot.shell(Messages.newWAProjTtl).activate();
        wabot.textWithLabel(Messages.projNm).setText(Messages.projName);
        wabot.button(Messages.nxtBtn).click();
        wabot.checkBox(Messages.jdkChkBxTxt).click();
        wabot.tabItem(Messages.srv).activate();
        wabot.checkBox(Messages.srvChkBxTxt).click();
        wabot.tabItem(Messages.app).activate();
        wabot.button(Messages.roleAddBtn).click();
        wabot.shell(Messages.addAppTtl).activate();
		wabot.button(Messages.cnclBtn).click();
		assertTrue("testAddAppCancelPressed",
				wabot.activeShell().getText().
				equalsIgnoreCase(Messages.newWAProjTtl));
        sh.close();
    }

    @Test
    //Test Case 178
    public void testWizNextCancelClick() throws Exception {
    	wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
    	wabot.textWithLabel(Messages.projNm).setText(Messages.projName);
    	wabot.button(Messages.nxtBtn).click();
    	wabot.checkBox(Messages.jdkChkBxTxt).click();
    	wabot.tabItem(Messages.srv).activate();
    	wabot.checkBox(Messages.srvChkBxTxt).click();
    	wabot.button(Messages.cnclBtn).click();
    	assertFalse("testWizNextCancelClick",
    			Utility.isProjExist(Messages.newWaPrjName));
    }

    @Test
    //Test Case 232
    public void testInvalidJdkPath() throws Exception {
        wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
        SWTBotShell sh = wabot.shell(Messages.newWAProjTtl).activate();
        wabot.textWithLabel(Messages.projNm).setText(Messages.projName);
        wabot.button(Messages.nxtBtn).click();
        boolean finishEnbl = wabot.button(Messages.finBtn).isEnabled();
        wabot.checkBox(Messages.jdkChkBxTxt).click();
        wabot.textInGroup(Messages.emltrGrp).setText(Messages.test);
        boolean finishDsbl = !wabot.button(Messages.finBtn).isEnabled();
        assertTrue("testInvalidJdkPath",
        		finishEnbl
        		&& finishDsbl);
        sh.close();
    }

    @Test
    //Test Case 233
    public void testInvalidSrvPath() throws Exception {
    	Utility.createProject(Messages.projName);
        wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
        SWTBotShell sh = wabot.shell(Messages.newWAProjTtl).activate();
        wabot.textWithLabel(Messages.projNm).setText(Messages.test);
        wabot.button(Messages.nxtBtn).click();
        wabot.checkBox(Messages.jdkChkBxTxt).click();
        wabot.textInGroup(Messages.emltrGrp).
        setText(Utility.getLoc(Messages.projName, "\\cert"));
        boolean finishEnbl = wabot.button(Messages.finBtn).isEnabled();
        wabot.tabItem(Messages.srv).activate();
        wabot.checkBox(Messages.srvChkBxTxt).click();
		wabot.comboBoxWithLabel(Messages.sel).
		setSelection(Messages.cmbValJet7);
		wabot.textInGroup(Messages.emltrGrp).setText(Messages.test);
        boolean finishDsbl = !wabot.button(Messages.finBtn).isEnabled();
        assertTrue("testInvalidSrvPath",
        		finishEnbl
        		&& finishDsbl);
        sh.close();
    }

    @Test
    //Test Case 256
    public void testUncheckSrvCheckApp() throws Exception {
        wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
        SWTBotShell sh = wabot.shell(Messages.newWAProjTtl).activate();
        wabot.textWithLabel(Messages.projNm).setText(Messages.test);
        wabot.button(Messages.nxtBtn).click();
        wabot.checkBox(Messages.jdkChkBxTxt).click();
        wabot.tabItem(Messages.srv).activate();
        wabot.checkBox(Messages.srvChkBxTxt).click();
        // Uncheck
        wabot.checkBox(Messages.srvChkBxTxt).click();
        boolean valSrvDisable =
        		!wabot.comboBoxWithLabel(Messages.sel).isEnabled()
				&& !wabot.textInGroup(Messages.emltrGrp).isEnabled()
				&& !wabot.link().isEnabled();
        wabot.tabItem(Messages.app).activate();
		boolean valAppDisable = !wabot.table().isEnabled()
				&& !wabot.button(Messages.roleAddBtn).isEnabled()
				&& !wabot.button(Messages.roleRemBtn).isEnabled();
		boolean appPresent = wabot.table().cell(0, 0).
				equalsIgnoreCase(Messages.hlWrld);
		assertTrue("testUncheckSrvCheckApp",
				valSrvDisable
				&& appPresent
				&& valAppDisable);
        sh.close();
    }

    @Test
    //Test Case 257
    public void testUncheckJdkCheckApp() throws Exception {
        wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
        SWTBotShell sh = wabot.shell(Messages.newWAProjTtl).activate();
        wabot.textWithLabel(Messages.projNm).setText(Messages.test);
        wabot.button(Messages.nxtBtn).click();
        wabot.checkBox(Messages.jdkChkBxTxt).click();
        wabot.tabItem(Messages.srv).activate();
        wabot.checkBox(Messages.srvChkBxTxt).click();
        // Uncheck
        wabot.tabItem(Messages.jdk).activate();
        wabot.checkBox(Messages.jdkChkBxTxt).click();
        boolean valJdkDisable =
        		!wabot.button(Messages.debugBrowseBtn).isEnabled()
				&& !wabot.textInGroup(Messages.emltrGrp).isEnabled();
		wabot.tabItem(Messages.srv).activate();	
        boolean valSrvDisable = !wabot.checkBox(Messages.srvChkBxTxt).isEnabled()
        		&& !wabot.comboBoxWithLabel(Messages.sel).isEnabled()
				&& !wabot.textInGroup(Messages.emltrGrp).isEnabled()
				&& !wabot.link().isEnabled();
		wabot.tabItem(Messages.app).activate();
        boolean valAppDisable = !wabot.table().isEnabled()
				&& !wabot.button(Messages.roleAddBtn).isEnabled()
				&& !wabot.button(Messages.roleRemBtn).isEnabled();
		boolean appPresent = wabot.table().cell(0, 0).
				equalsIgnoreCase(Messages.hlWrld);
		assertTrue("testUncheckSrvCheckApp", valJdkDisable
				&& valSrvDisable
				&& appPresent
				&& valAppDisable);
        sh.close();
    }

    @Test
    //Test Case 262
    public void testCheckHlwrldApp() throws Exception {
    	wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
    	wabot.shell(Messages.newWAProjTtl).activate();
    	wabot.textWithLabel(Messages.projNm).setText(Messages.projName);
    	wabot.button(Messages.nxtBtn).click();
    	wabot.tabItem(Messages.app).activate();
    	boolean appPresent = wabot.table().cell(0, 0).
    			equalsIgnoreCase(Messages.hlWrld);
    	wabot.button(Messages.finBtn).click();
    	SWTBotShell propShell = Utility.
    			selectPageUsingContextMenu(Messages.projName, Messages.role1, Messages.serConfPage);
    	wabot.tabItem(Messages.app).activate();
    	boolean appSrvConfPresent = wabot.table().cell(0, 0).
    			equalsIgnoreCase(Messages.hlWrld);
    	Utility.selPageNode(Messages.role1, Messages.cmntPage);
    	boolean cmpntPresent = wabot.table().cell(0, 2).
    			equalsIgnoreCase(Messages.hlWrld);
    	assertTrue("testCheckHlwrldApp", appPresent
    			&& appSrvConfPresent
    			&& cmpntPresent);
    	propShell.close();
    }

    @Test
    //Test Case 263
    public void testCheckHlwrldAppWithJdk() throws Exception {
    	Utility.createProject(Messages.test);
    	wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
    	wabot.shell(Messages.newWAProjTtl).activate();
    	wabot.textWithLabel(Messages.projNm).setText(Messages.projName);
    	wabot.button(Messages.nxtBtn).click();
    	wabot.tabItem(Messages.app).activate();
    	boolean appPresent = wabot.table().cell(0, 0).
    			equalsIgnoreCase(Messages.hlWrld);
    	wabot.tabItem(Messages.jdk).activate();
    	wabot.checkBox(Messages.jdkChkBxTxt).click();
    	wabot.textInGroup(Messages.emltrGrp).
    	setText(Utility.getLoc(Messages.test, "\\cert"));
    	wabot.button(Messages.finBtn).click();
    	SWTBotShell propShell = Utility.
    			selectPageUsingContextMenu(Messages.projName, Messages.role1, Messages.serConfPage);
    	wabot.tabItem(Messages.app).activate();
    	boolean appSrvConfPresent = wabot.table().cell(0, 0).
    			equalsIgnoreCase(Messages.hlWrld);
    	Utility.selPageNode(Messages.role1, Messages.cmntPage);
    	boolean cmpntPresent = wabot.table().cell(1, 2).
    			equalsIgnoreCase(Messages.hlWrld);
    	assertTrue("testCheckHlwrldAppWithJdk", appPresent
    			&& appSrvConfPresent
    			&& cmpntPresent);
    	propShell.close();
    	Utility.selProjFromExplorer(Messages.test).select();
    	Utility.deleteSelectedProject();
    }

    @Test
    //Test Case 264
    public void testCheckHlwrldAppWithJdkSrv() throws Exception {
    	Utility.createProject(Messages.test);
    	wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
    	wabot.shell(Messages.newWAProjTtl).activate();
    	wabot.textWithLabel(Messages.projNm).setText(Messages.projName);
    	wabot.button(Messages.nxtBtn).click();
    	wabot.tabItem(Messages.app).activate();
    	boolean appPresent = wabot.table().cell(0, 0).
    			equalsIgnoreCase(Messages.hlWrld);
    	wabot.tabItem(Messages.jdk).activate();
    	wabot.checkBox(Messages.jdkChkBxTxt).click();
    	wabot.textInGroup(Messages.emltrGrp).
    	setText(Utility.getLoc(Messages.test, "\\cert"));
    	wabot.tabItem(Messages.srv).activate();
    	wabot.checkBox(Messages.srvChkBxTxt).click();
    	wabot.comboBoxWithLabel(Messages.sel).setSelection(Messages.cmbValJet7);
    	wabot.textInGroup(Messages.emltrGrp).
    	setText(Utility.getLoc(Messages.test, "\\cert"));
    	wabot.button(Messages.finBtn).click();
    	SWTBotShell propShell = Utility.
    			selectPageUsingContextMenu(Messages.projName, Messages.role1, Messages.serConfPage);
    	wabot.tabItem(Messages.app).activate();
    	boolean appSrvConfPresent = wabot.table().cell(0, 0).
    			equalsIgnoreCase(Messages.hlWrld);
    	Utility.selPageNode(Messages.role1, Messages.cmntPage);
    	boolean cmpntPresent = wabot.table().cell(2, 2).
    			equalsIgnoreCase(Messages.hlWrld);
    	assertTrue("testCheckHlwrldAppWithJdkSrv", appPresent
    			&& appSrvConfPresent
    			&& cmpntPresent);
    	propShell.close();
    	Utility.selProjFromExplorer(Messages.test).select();
    	Utility.deleteSelectedProject();
    }

    @Test
    // (Test Cases for 1.8) test case 47
    public void testKeyFeaturePgPresent() throws Exception {
    	wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
    	wabot.shell(Messages.newWAProjTtl).activate();
    	wabot.textWithLabel(Messages.projNm).setText(Messages.projName);
    	wabot.button(Messages.nxtBtn).click();
    	boolean secondPage = wabot.button(Messages.nxtBtn).isEnabled()
    			&& wabot.button(Messages.finBtn).isEnabled()
    			&& wabot.button(Messages.bckBtn).isEnabled()
    			&& wabot.button(Messages.cnclBtn).isEnabled();
    	wabot.button(Messages.nxtBtn).click();
    	boolean thirdPage = !wabot.button(Messages.nxtBtn).isEnabled()
    			&& wabot.button(Messages.finBtn).isEnabled()
    			&& wabot.button(Messages.bckBtn).isEnabled()
    			&& wabot.button(Messages.cnclBtn).isEnabled()
    			&& wabot.checkBox(Messages.chkLbSsnAff).isEnabled()
    			&& !wabot.checkBox(Messages.chkLbSsnAff).isChecked()
    			&& wabot.checkBox(Messages.cachePage).isEnabled()
    			&& !wabot.checkBox(Messages.cachePage).isChecked()
    			&& wabot.checkBox(Messages.chkLblDebug).isEnabled()
    			&& !wabot.checkBox(Messages.chkLblDebug).isChecked();
    	assertTrue("testKeyFeaturePgPresent", secondPage && thirdPage);
    	wabot.button(Messages.cnclBtn).click();
    }

    @Test
    // (Test Cases for 1.8) test case 48
    public void testEnableKeyFeatures() throws Exception {
    	Utility.createProject(Messages.test);
    	wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
    	SWTBotShell sh = wabot.shell(Messages.newWAProjTtl).activate();
    	wabot.textWithLabel(Messages.projNm).setText(Messages.projName);
    	wabot.button(Messages.nxtBtn).click();
    	wabot.checkBox(Messages.jdkChkBxTxt).click();
    	// Directly set JDK path as Native dialog box can not be handled
    	// May fail if directory is not present in real
    	wabot.tabItem(Messages.srv).activate();
    	wabot.checkBox(Messages.srvChkBxTxt).click();
    	wabot.comboBoxWithLabel(Messages.sel).setSelection(Messages.cmbValJet7);
    	wabot.textInGroup(Messages.emltrGrp).setText(Utility.getLoc(Messages.test, "\\cert"));
    	wabot.button(Messages.nxtBtn).click();
    	wabot.button(Messages.nxtBtn).click();
    	wabot.checkBox(Messages.chkLbSsnAff).select();
    	wabot.checkBox(Messages.cachePage).select();
    	wabot.checkBox(Messages.chkLblDebug).select();
    	wabot.button(Messages.finBtn).click();
    	wabot.waitUntil(shellCloses(sh));
    	wabot.sleep(10000);
    	// Validate load balancing page
    	SWTBotShell propShell =
    			Utility.selectPageUsingContextMenu(Messages.projName, Messages.role1, Messages.loadBlcPage);
    	boolean loadBalPage = wabot.checkBox().isChecked()
    			&& wabot.comboBox().isEnabled()
    			&& wabot.comboBox().getText().equals(Messages.sessionEndPt);
    	Utility.selPageNode(Messages.role1, Messages.dbgEndPtName);
    	boolean debugPage = wabot.checkBox(Messages.debugLabel).isChecked()
    			&& !wabot.checkBox(Messages.dbgJVMChkLbl).isChecked()
    			&& wabot.comboBox().isEnabled()
    			&& wabot.comboBox().getText().
    			equals(Messages.dbgEndptStr);
    	Utility.selPageNode(Messages.role1, Messages.cachePage);
    	boolean cachePage = wabot.checkBox().isChecked()
    			&& wabot.textWithLabel(
    					Messages.cachScaleLbl).getText().
    					equals(Messages.dfltSizeVal)
    					&& (wabot.scale().getValue() == 30)
    					&& wabot.table().
    					containsItem(Messages.dfltCachName)
    					&& wabot.comboBox().
    					getText().equals("(auto)");
    	Utility.selPageNode(Messages.role1, Messages.endptPage);
    	boolean endPtPage = wabot.table().cell(0, 0).equals(Messages.intEndPt)
    			&& wabot.table().cell(0, 1).equals(Messages.typeInpt)
    			&& wabot.table().cell(0, 2).equals("80")
    			&& wabot.table().cell(0, 3).equals("31221")
    			&& wabot.table().cell(1, 0).equals(Messages.dbgEndPtName)
    			&& wabot.table().cell(1, 1).equals(Messages.typeInpt)
    			&& wabot.table().cell(1, 2).equals("8090")
    			&& wabot.table().cell(1, 3).equals("8090")
    			&& wabot.table().cell(2, 0).equals(Messages.sesAfinityStr)
    			&& wabot.table().cell(2, 1).equals(Messages.typeIntrnl)
    			&& wabot.table().cell(2, 2).equals(Messages.dlgDbgNA)
    			&& wabot.table().cell(2, 3).equals("8080")
    			&& wabot.table().
    			cell(3, 0).equals(String.format("%s%s",
    					Messages.cachEndPtName, Messages.dfltCachName))
    					&& wabot.table().cell(3, 1).
    					equals(Messages.typeIntrnl)
    					&& wabot.table().cell(3, 3).equals("11211");
    	assertTrue("testEnableKeyFeatures", loadBalPage
    			&& debugPage && cachePage && endPtPage);
    	propShell.close();
    	Utility.selProjFromExplorer(Messages.test).select();
    	Utility.deleteSelectedProject();
    }

    @Test
    // (Test Cases for 1.8.1) test case 71
    public void testTabsPresent() throws Exception {
    	wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
    	wabot.shell(Messages.newWAProjTtl).activate();
    	wabot.textWithLabel(Messages.projNm).setText(Messages.projName);
    	wabot.button(Messages.nxtBtn).click();
    	assertTrue("testTabsPresent", wabot.tabItem(Messages.jdk).isEnabled()
    			&& wabot.tabItem(Messages.srv).isEnabled()
    			&& wabot.tabItem(Messages.app).isEnabled());
    	wabot.button(Messages.cnclBtn).click();
    }
    
    @Test
    // (Test Cases for 1.8.1) test case 72
    public void testNextBtnFunctionalityChk() throws Exception {
    	Utility.createProject(Messages.test);
    	wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
    	wabot.shell(Messages.newWAProjTtl).activate();
    	wabot.textWithLabel(Messages.projNm).setText(Messages.projName);
    	wabot.button(Messages.nxtBtn).click();
    	boolean jdk = wabot.checkBox(Messages.jdkChkBxTxt).isVisible()
    			&& wabot.checkBox(Messages.jdkChkBxTxt).isEnabled();
    	wabot.checkBox(Messages.jdkChkBxTxt).click();
    	wabot.button(Messages.nxtBtn).click();
    	boolean srv = wabot.checkBox(Messages.srvChkBxTxt).isVisible()
    			&& wabot.checkBox(Messages.srvChkBxTxt).isEnabled();
    	wabot.checkBox(Messages.srvChkBxTxt).click();
    	wabot.comboBoxWithLabel(Messages.sel).setSelection(Messages.cmbValJet7);
    	wabot.textInGroup(Messages.emltrGrp).
    	setText(Utility.getLoc(Messages.test, "\\cert"));
    	wabot.button(Messages.nxtBtn).click();
    	boolean app = wabot.table().isVisible()
    			&& wabot.table().isEnabled();
    	wabot.button(Messages.nxtBtn).click();
    	boolean keyFtPg = wabot.checkBox(Messages.chkLbSsnAff).isEnabled()
    			&& wabot.checkBox(Messages.cachePage).isEnabled()
    			&& wabot.checkBox(Messages.chkLblDebug).isEnabled();
    	wabot.button(Messages.cnclBtn).click();
    	assertTrue("testNextBtnFunctionalityChk", jdk
    			&& srv
    			&& app
    			&& keyFtPg);
    	Utility.selProjFromExplorer(Messages.test).select();
    	Utility.deleteSelectedProject();
    }
    
    @Test
    // (Test Cases for 1.8.1) test case 73
    public void testNextBtnFunctionalityUnChk() throws Exception {
    	wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
    	wabot.shell(Messages.newWAProjTtl).activate();
    	wabot.textWithLabel(Messages.projNm).setText(Messages.projName);
    	wabot.button(Messages.nxtBtn).click();
    	boolean jdk = wabot.checkBox(Messages.jdkChkBxTxt).isVisible()
    			&& wabot.checkBox(Messages.jdkChkBxTxt).isEnabled();
    	wabot.button(Messages.nxtBtn).click();
    	boolean keyFtPg = wabot.checkBox(Messages.chkLbSsnAff).isEnabled()
    			&& wabot.checkBox(Messages.cachePage).isEnabled()
    			&& wabot.checkBox(Messages.chkLblDebug).isEnabled();
    	wabot.button(Messages.bckBtn).click();
    	wabot.checkBox(Messages.jdkChkBxTxt).click();
    	wabot.button(Messages.nxtBtn).click();
    	boolean srv = wabot.checkBox(Messages.srvChkBxTxt).isVisible()
    			&& wabot.checkBox(Messages.srvChkBxTxt).isEnabled();
    	wabot.button(Messages.nxtBtn).click();
    	boolean keyFtPg1 = wabot.checkBox(Messages.chkLbSsnAff).isEnabled()
    			&& wabot.checkBox(Messages.cachePage).isEnabled()
    			&& wabot.checkBox(Messages.chkLblDebug).isEnabled();
    	wabot.button(Messages.cnclBtn).click();
    	assertTrue("testNextBtnFunctionalityUnChk", jdk
    			&& srv
    			&& keyFtPg1
    			&& keyFtPg);
    }
    
    @Test
    // (Test Cases for 1.8.1) test case 74
    public void testBackBtnFunctionality() throws Exception {
    	wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
    	wabot.shell(Messages.newWAProjTtl).activate();
    	wabot.textWithLabel(Messages.projNm).setText(Messages.projName);
    	wabot.button(Messages.nxtBtn).click();
    	wabot.tabItem(Messages.app).activate();
    	wabot.button(Messages.bckBtn).click();
    	boolean srv = wabot.checkBox(Messages.srvChkBxTxt).isVisible();
    	wabot.button(Messages.bckBtn).click();
    	boolean jdk = wabot.checkBox(Messages.jdkChkBxTxt).isVisible()
    			&& wabot.checkBox(Messages.jdkChkBxTxt).isEnabled();
    	wabot.button(Messages.nxtBtn).click();
    	wabot.button(Messages.nxtBtn).click();
    	boolean keyFtPg = wabot.checkBox(Messages.chkLbSsnAff).isEnabled()
    			&& wabot.checkBox(Messages.cachePage).isEnabled()
    			&& wabot.checkBox(Messages.chkLblDebug).isEnabled();
    	wabot.button(Messages.bckBtn).click();
    	boolean jdk1 = wabot.checkBox(Messages.jdkChkBxTxt).isVisible()
    			&& wabot.checkBox(Messages.jdkChkBxTxt).isEnabled();
    	wabot.button(Messages.cnclBtn).click();
    	assertTrue("testBackBtnFunctionality", jdk
    			&& srv
    			&& jdk1
    			&& keyFtPg);
    }
    
    @Test
    // (Test Cases for 1.8.1) test case 75
    public void testJdkActiveDefault() throws Exception {
    	wabot.menu("File").menu("New").menu(Messages.newWaPrjName).click();
    	wabot.shell(Messages.newWAProjTtl).activate();
    	wabot.textWithLabel(Messages.projNm).setText(Messages.projName);
    	wabot.button(Messages.nxtBtn).click();
    	wabot.checkBox(Messages.jdkChkBxTxt).select();
    	wabot.button(Messages.bckBtn).click();
    	wabot.button(Messages.nxtBtn).click();
    	boolean jdk = wabot.checkBox(Messages.jdkChkBxTxt).isVisible()
    			&& wabot.checkBox(Messages.jdkChkBxTxt).isEnabled();
    	// uncheck JDK
    	wabot.checkBox(Messages.jdkChkBxTxt).click();
    	// go on key feature page
    	wabot.button(Messages.nxtBtn).click();
    	wabot.button(Messages.bckBtn).click();
    	boolean jdk1 = wabot.checkBox(Messages.jdkChkBxTxt).isVisible()
    			&& wabot.checkBox(Messages.jdkChkBxTxt).isEnabled();
    	wabot.button(Messages.cnclBtn).click();
    	assertTrue("testJdkActiveDefault", jdk
    			&& jdk1);
    }
}