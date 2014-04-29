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
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
public class WARemotePageTest {

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
        if (Utility.isProjExist(Messages.rProjName)) {
            // delete existing project
            Utility.selProjFromExplorer(Messages.rProjName).select();
            Utility.deleteSelectedProject();
        }
        if (Utility.isProjExist(Messages.raNewProj)) {
        	// delete existing project
        	Utility.selProjFromExplorer(Messages.raNewProj).select();
        	Utility.deleteSelectedProject();
        }
        Utility.createProject(Messages.rProjName);
        Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
    }

    @After
    public void cleanUp() throws Exception {
        if (Utility.isProjExist(Messages.rProjName)) {
            Utility.selProjFromExplorer(Messages.rProjName).select();
            Utility.deleteSelectedProject();
            }
    }

    @Test
    // test case 37
    public void testRemoteAccessPresent() throws Exception {
    	// By default remote access will be off due to changes for v1.8.1
        assertTrue("testRemoteAccessPresent",
        		!wabot.checkBox(Messages.remoteCheckText).isChecked());
        wabot.sleep(1000);
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 38
    public void testRemoteAccessDetailsEmpty() throws Exception {
        boolean value = false;
        // Enable remote access
        wabot.checkBox(Messages.remoteCheckText).select();
        value = wabot.textWithLabel(Messages.raUnameLbl).getText().isEmpty()
        		&& wabot.textWithLabel(Messages.raPwdLbl).getText().isEmpty()
        		&& wabot.textWithLabel(Messages.raCnfPwdLbl).getText().isEmpty()
        		&& wabot.textWithLabel(Messages.raPathLbl).getText().isEmpty()
        		&& !wabot.textWithLabel(Messages.raExDateLbl).getText()
        		.isEmpty();
        assertTrue("testRemoteAccessDetailsEmpty", value);
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 39
    public void testRemoteAccessDisable() throws Exception {
    	assertTrue("testRemoteAccessDisable",
    			!wabot.textWithLabel(Messages.raUnameLbl).isEnabled()
    			&& !wabot.checkBox(Messages.remoteCheckText).isChecked()
    			&& !wabot.textWithLabel(Messages.raPwdLbl).isEnabled()
    			&& !wabot.textWithLabel(Messages.raCnfPwdLbl).isEnabled()
    			&& !wabot.textWithLabel(Messages.raPathLbl).isEnabled());
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 40
    public void testRemoteAccessEnable() throws Exception {
        Utility.enableRemoteAccess();
        assertTrue("testRemoteAccessEnable",
        		wabot.textWithLabel(Messages.raUnameLbl).isEnabled()
        		&& wabot.checkBox(Messages.remoteCheckText).isChecked()
        		&& wabot.textWithLabel(Messages.raPwdLbl).isEnabled()
        		&& wabot.textWithLabel(Messages.raCnfPwdLbl).isEnabled()
        		&& wabot.textWithLabel(Messages.raPathLbl).isEnabled());
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 41
    public void testRemoteAccessDisbleOK() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
    	wabot.button("OK").click();
    	Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
    	// Disable
    	Utility.disableRemoteAccess();
    	wabot.button("OK").click();
    	Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
    	assertTrue("testRemoteAccessDisbleOK",
    			!(wabot.textWithLabel(Messages.raUnameLbl).isEnabled())
    			&& !(wabot.textWithLabel(Messages.raPwdLbl).isEnabled())
    			&& !(wabot.textWithLabel(Messages.raExDateLbl).isEnabled())
    			&& !wabot.checkBox(Messages.remoteCheckText).isChecked());
    	Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 42
    public void testReopenRemoteCheckDisbledProject() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
    	wabot.button("OK").click();
    	Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
    	// Disable
        Utility.disableRemoteAccess();
        wabot.button("OK").click();
        Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
        Utility.enableRemoteAccess();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        GregorianCalendar currentCal = new GregorianCalendar();
        currentCal.add(Calendar.YEAR, 1);
        Date today = currentCal.getTime();
        String nextdate = df.format(today).toString();
        assertTrue("testReopenRemoteCheckDisbledProject",
        		(wabot.textWithLabel(Messages.raUnameLbl).isEnabled())
                && (wabot.textWithLabel(Messages.raPwdLbl).isEnabled())
                && (wabot.textWithLabel(Messages.raExDateLbl).isEnabled())
                && (wabot.checkBox(Messages.remoteCheckText).isChecked())
                && wabot.textWithLabel(Messages.raUnameLbl).getText().isEmpty()
                && (wabot.textWithLabel(Messages.raPwdLbl).getText().isEmpty())
                && (wabot.textWithLabel(Messages.raExDateLbl).getText()
                        .equals(nextdate)));
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 43
    public void testSaveWithoutModification() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
    	wabot.button("OK").click();
    	Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
        wabot.button("OK").click();
        // asserting that active shell is parent shell only. if some error comes
        // in that case active shell will be the error message box.
        assertTrue("testSaveWithoutModification",
        		wabot.activeShell().getText().
        		equals(Messages.activeShell));
    }

    @Test
    // test case 44
    public void testModifyUserName() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
    	wabot.button("OK").click();
    	Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
        wabot.textWithLabel(Messages.raUnameLbl).setText(Messages.rUserName);
        wabot.button("OK").click();
        Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
        assertTrue("testModifyUserName",
        		wabot.textWithLabel(Messages.raUnameLbl).getText()
                .equals(Messages.rUserName));
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 45
    public void testUserNameBlank() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
        wabot.textWithLabel(Messages.raUnameLbl).setText("");
        wabot.button("OK").click();
        SWTBotShell sa = wabot.shell(Messages.rAccErrTtl).activate();
        String msg = sa.getText();
        wabot.button("OK").click();
        assertTrue("testUserNameBlank",
        		msg.equals(Messages.rAccErrTtl));
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 46
    public void testExpirationDateBlank() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
        wabot.textWithLabel(Messages.raExDateLbl).setText("");
        wabot.button("OK").click();
        SWTBotShell sa = wabot.shell(Messages.rAccErrTtl).activate();
        String msg = sa.getText();
        wabot.button("OK").click();
        assertTrue("testExpirationDateBlank",
        		msg.equals(Messages.rAccErrTtl));
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 47
    public void testCertPathBlank() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
        wabot.textWithLabel(Messages.raPathLbl).setText("");
        wabot.button("OK").click();
        SWTBotShell sa = wabot.shell(Messages.rAccErrTtl).activate();
        String msg = sa.getText();
        wabot.button("OK").click();
        assertTrue("testCertPathBlank",
        		msg.equals(Messages.rAccErrTtl));
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 48
    public void testPwdBlankNOPressed() throws Exception {
    	Utility.enableRemoteAccess();
    	wabot.textWithLabel(Messages.raUnameLbl).setText(Messages.test);
    	wabot.textWithLabel(Messages.raPathLbl).
		setText(Utility.getCertOfOtherProject(Messages.rProjName));
        wabot.textWithLabel(Messages.raPwdLbl).setText("");
        wabot.textWithLabel(Messages.raCnfPwdLbl).setText("");
        wabot.button("OK").click();
        wabot.shell(Messages.rAccErrTtl).activate();
        wabot.button("No").click();
        assertTrue("testPwdBlankNOPressed",
        		wabot.activeShell().getText().
        		equals(String.format(
        				"%s%s%s", Messages.propPageTtl, " ",
        				Messages.rProjName)));
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 49
    public void testPwdBlankYesPressed() throws Exception {
    	Utility.enableRemoteAccess();
    	wabot.textWithLabel(Messages.raUnameLbl).setText(Messages.test);
    	wabot.textWithLabel(Messages.raPathLbl).
    	setText(Utility.getCertOfOtherProject(Messages.rProjName));
        wabot.textWithLabel(Messages.raPwdLbl).setText("");
        wabot.textWithLabel(Messages.raCnfPwdLbl).setText("");
        wabot.button("OK").click();
        wabot.shell(Messages.rAccErrTtl).activate();
        wabot.button("Yes").click();
        Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
        assertTrue("testPwdBlankYesPressed",
        		wabot.textWithLabel(Messages.raPwdLbl).
        		getText().equals("")
                && wabot.textWithLabel(Messages.raCnfPwdLbl).
                getText().equals(""));
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 50
    public void testPwdNotStrong() throws Exception {
    	Utility.enableRemoteAccess();
    	wabot.textWithLabel(Messages.raUnameLbl).setText(Messages.test);
    	wabot.textWithLabel(Messages.raPathLbl).
    	setText(Utility.getCertOfOtherProject(Messages.rProjName));
        wabot.textWithLabel(Messages.raPwdLbl).typeText(Messages.raWeakPwd);
        wabot.button("OK").click();
        SWTBotShell sh = wabot.shell(Messages.raPwdNotStrngTtl).activate();
        String msg = sh.getText();
        sh.close();
        wabot.button("OK").click();
        wabot.button(Messages.cnclBtn).click();
        assertTrue("testPwdNotStrong",
        		msg.equals(Messages.raPwdNotStrngTtl));
    }

    @Test
    // test case 51
    public void testStrongPassword() throws Exception {
    	Utility.enableRemoteAccess();
    	wabot.textWithLabel(Messages.raUnameLbl).setText(Messages.test);
    	wabot.textWithLabel(Messages.raPathLbl).
		setText(Utility.getCertOfOtherProject(Messages.rProjName));
        wabot.textWithLabel(Messages.raPwdLbl).setText(Messages.raStrPwd);
        wabot.textWithLabel(Messages.raCnfPwdLbl).setFocus();
        assertTrue("testStrongPassword",
        		wabot.textWithLabel(Messages.raCnfPwdLbl).getText()
                .equals(""));
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 52
    public void testPasswordMismatch() throws Exception {
    	Utility.enableRemoteAccess();
    	wabot.textWithLabel(Messages.raUnameLbl).setText(Messages.test);
    	wabot.textWithLabel(Messages.raPathLbl).
		setText(Utility.getCertOfOtherProject(Messages.rProjName));
        wabot.textWithLabel(Messages.raPwdLbl).setText(Messages.raStrPwd);
        wabot.textWithLabel(Messages.raCnfPwdLbl).setText(Messages.raWeakPwd);
        wabot.button("OK").click();
        SWTBotShell sh = wabot.shell(Messages.rAccErrTtl).activate();
        String msg = sh.getText();
        wabot.button("OK").click();
        assertTrue("testPasswordMismatch",
        		msg.equals(Messages.rAccErrTtl));
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 53
    public void testPasswordMatched() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
        wabot.button("OK").click();
        // asserting that active shell is parent shell only. if some error comes
        // in that
        // case active shell will be the error message box.
        assertTrue("testPasswordMatched",
        		wabot.activeShell().getText().
        		equals(Messages.activeShell));
    }

    @Test
    // test case 54
    public void testWrongExpDate() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        GregorianCalendar currentCal = new GregorianCalendar();
        currentCal.add(Calendar.DATE, -1);
        Date today = currentCal.getTime();
        String nextdate = df.format(today).toString();
        wabot.textWithLabel(Messages.raExDateLbl).setText(nextdate);
        wabot.button("OK").click();
        SWTBotShell sh = wabot.shell(Messages.rAccErrTtl).activate();
        String msg = sh.getText();
        wabot.button("OK").click();
        assertTrue("testWrongExpDate",
        		msg.equals(Messages.rAccErrTtl));
        Utility.closeProjPropertyPage(Messages.rProjName);

    }

    @Test
    // test case 55
    public void testCorrectExpDate() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        GregorianCalendar currentCal = new GregorianCalendar();
        currentCal.add(Calendar.YEAR, 1);
        Date today = currentCal.getTime();
        String nextdate = df.format(today).toString();
        wabot.textWithLabel(Messages.raExDateLbl).setText(nextdate);
        wabot.button("OK").click();
        assertTrue("testCorrectExpDate",
        		wabot.activeShell().getText().
        		equals(Messages.activeShell));
    }

    @Test
    // test case 56
    public void testPwdFocusIn() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
        wabot.textWithLabel(Messages.raPwdLbl).setFocus();
        assertTrue("testPwdFocusIn",
        		wabot.textWithLabel(Messages.raPwdLbl).
        		getText().equals(""));
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 57
    public void testPwdFocusOut() throws Exception {
    	// Focus out logic works when values are saved in package.xml
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
    	wabot.button("OK").click();
    	Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
    	wabot.textWithLabel(Messages.raPwdLbl).setFocus();
    	wabot.textWithLabel(Messages.raCnfPwdLbl).setFocus();
    	assertTrue("testPwdFocusOut",
    			!wabot.textWithLabel(Messages.raPwdLbl).
    			getText().equals(""));
    	Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 58
    public void testCnfPwdFocusIn() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
        wabot.textWithLabel(Messages.raCnfPwdLbl).setFocus();
        assertTrue("testCnfPwdFocusIn",
        		wabot.textWithLabel(Messages.raCnfPwdLbl).
        		getText().equals(""));
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 59
    public void testCnfPwdFocusOut() throws Exception {
    	// Focus out logic works when values are saved in package.xml
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
    	wabot.button("OK").click();
    	Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
        wabot.textWithLabel(Messages.raCnfPwdLbl).setFocus();
        wabot.textWithLabel(Messages.raPathLbl).setFocus();
        assertTrue("testCnfPwdFocusOut",
        		!wabot.textWithLabel(Messages.raCnfPwdLbl).
        		getText().equals(""));
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 60
    public void testEncryptPwd() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
        wabot.textWithLabel(Messages.raPwdLbl).typeText(Messages.raStrPwd);
        wabot.textWithLabel(Messages.raCnfPwdLbl).typeText(Messages.raStrPwd);
        wabot.button("OK").click();
        int oldLength = Messages.raStrPwd.length();
        Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
        int newLength = (wabot.textWithLabel(Messages.raPwdLbl).
        		getText()).length();
        assertTrue("testEncryptPwd", oldLength != newLength);
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 61
    public void testCertChanged() throws Exception {
       Utility.enableRemoteAccess();
       Utility.enterRemoteAccessDetails(Messages.rProjName);
       wabot.button("OK").click();
       Utility.createProject(Messages.raNewProj);
       String str = Utility.getCertOfOtherProject(Messages.raNewProj);
       Utility.selProjFromExplorer(Messages.rProjName);
       Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
       wabot.textWithLabel(Messages.raPathLbl).setText(str);
       wabot.button("OK").click();
       SWTBotShell sh = wabot.shell(Messages.rAccErrTtl).activate();
       String msg = sh.getText();
       wabot.button("OK").click();
       assertTrue("testCertChanged",
    		   msg.equals(Messages.rAccErrTtl)
               && wabot.textWithLabel(Messages.raPwdLbl).getText().equals("")
               && wabot.textWithLabel(Messages.raCnfPwdLbl).
               getText().equals(""));
       Utility.closeProjPropertyPage(Messages.rProjName);
       Utility.selProjFromExplorer(Messages.raNewProj).select();
       Utility.deleteSelectedProject();
    }

    @Test
    // test case 62
    public void testCertChangedPwdNo() throws Exception {
       Utility.enableRemoteAccess();
       Utility.enterRemoteAccessDetails(Messages.rProjName);
       wabot.button("OK").click();
       Utility.createProject(Messages.raNewProj);
       String str = Utility.getCertOfOtherProject(Messages.raNewProj);
       Utility.selProjFromExplorer(Messages.rProjName);
       Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
       wabot.textWithLabel(Messages.raPathLbl).setText(str);
       wabot.button("OK").click();
       wabot.shell(Messages.rAccErrTtl).activate();
       wabot.button("OK").click();
       wabot.button("OK").click();
       wabot.shell(Messages.rAccErrTtl).activate();
       wabot.button("No").click();
       assertTrue("testCertChangedPwdNo",
    		   wabot.activeShell().getText().
    		   equals(String.format(
       				"%s%s%s", Messages.propPageTtl, " ",
       				Messages.rProjName)));
       Utility.closeProjPropertyPage(Messages.rProjName);
       Utility.selProjFromExplorer(Messages.raNewProj).select();
       Utility.deleteSelectedProject();
    }

    @Test
    // test case 63
    public void testCertChangedPwdYes() throws Exception {
       Utility.enableRemoteAccess();
       Utility.enterRemoteAccessDetails(Messages.rProjName);
       wabot.button("OK").click();
       Utility.createProject(Messages.raNewProj);
       String str = Utility.getCertOfOtherProject(Messages.raNewProj);
       Utility.selProjFromExplorer(Messages.rProjName);
       Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
       wabot.textWithLabel(Messages.raPathLbl).setText(str);
       wabot.button("OK").click();
       wabot.shell(Messages.rAccErrTtl).activate();
       wabot.button("OK").click();
       wabot.button("OK").click();
       wabot.shell(Messages.rAccErrTtl).activate();
       wabot.button("Yes").click();
       wabot.shell(Messages.synErr).activate();
       wabot.button("OK").click();
       assertTrue("testCertChangedPwdYes",
    		   wabot.textWithLabel(Messages.raPwdLbl).
    		   getText().equals("")
               && wabot.textWithLabel(Messages.raCnfPwdLbl).
               getText().equals(""));
       Utility.closeProjPropertyPage(Messages.rProjName);
       Utility.selProjFromExplorer(Messages.raNewProj).select();
       Utility.deleteSelectedProject();
    }

    @Test
    // test case 64
    public void testExpDateWrongVal() throws Exception {
        Utility.enableRemoteAccess();
        Utility.enterRemoteAccessDetails(Messages.rProjName);
        wabot.textWithLabel(Messages.raExDateLbl).
        setText(Messages.raWrongDateVal);
        wabot.button("OK").click();
        SWTBotShell sh = wabot.shell(Messages.genericErrTtl).activate();
        String msg = sh.getText();
        wabot.button("OK").click();
        assertTrue("testExpDateWrongVal",
        		msg.equals(Messages.genericErrTtl));
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 65
    public void testCertChangedPwdNotChanged() throws Exception {
    	Utility.enableRemoteAccess();
        Utility.enterRemoteAccessDetails(Messages.rProjName);
        wabot.button("OK").click();
        Utility.createProject(Messages.raNewProj);
        String str = Utility.getCertOfOtherProject(Messages.raNewProj);
        Utility.selProjFromExplorer(Messages.rProjName);
        Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
        wabot.textWithLabel(Messages.raPathLbl).setText(str);
        wabot.button("OK").click();
        wabot.shell(Messages.rAccErrTtl).activate();
        wabot.button("OK").click();
        wabot.textWithLabel(Messages.raPwdLbl).setFocus();
        wabot.textWithLabel(Messages.raPathLbl).setFocus();
        assertTrue("testCertChangedPwdNotChanged",
        		!wabot.textWithLabel(Messages.raPwdLbl).
        		getText().equals(""));
        Utility.closeProjPropertyPage(Messages.rProjName);
        Utility.selProjFromExplorer(Messages.raNewProj).select();
        Utility.deleteSelectedProject();
    }

    @Test
    // test case 66
    public void testCertChangedCnfPwdFocusOut() throws Exception {
    	Utility.enableRemoteAccess();
        Utility.enterRemoteAccessDetails(Messages.rProjName);
        wabot.button("OK").click();
        Utility.createProject(Messages.raNewProj);
        String str = Utility.getCertOfOtherProject(Messages.raNewProj);
        Utility.selProjFromExplorer(Messages.rProjName);
        Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
        wabot.textWithLabel(Messages.raPathLbl).setText(str);
        wabot.button("OK").click();
        wabot.shell(Messages.rAccErrTtl).activate();
        wabot.button("OK").click();
        wabot.textWithLabel(Messages.raCnfPwdLbl).setFocus();
        wabot.textWithLabel(Messages.raPathLbl).setFocus();
        assertTrue("testCertChangedCnfPwdFocusOut",
        		wabot.textWithLabel(Messages.raPwdLbl).
        		getText().equals("")
                && wabot.textWithLabel(Messages.raCnfPwdLbl).
                getText().equals(""));
        Utility.closeProjPropertyPage(Messages.rProjName);
        Utility.selProjFromExplorer(Messages.raNewProj).select();
        Utility.deleteSelectedProject();
    }

    @Test
    // test case 67
    public void testCertChangedPwdCnfPwdFocusOut() throws Exception {
    	Utility.enableRemoteAccess();
        Utility.enterRemoteAccessDetails(Messages.rProjName);
        wabot.button("OK").click();
        Utility.createProject(Messages.raNewProj);
        String str = Utility.getCertOfOtherProject(Messages.raNewProj);
        Utility.selProjFromExplorer(Messages.rProjName);
        Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
        wabot.textWithLabel(Messages.raPathLbl).setText(str);
        wabot.button("OK").click();
        wabot.shell(Messages.rAccErrTtl).activate();
        wabot.button("OK").click();
        wabot.textWithLabel(Messages.raPwdLbl).setFocus();
        wabot.textWithLabel(Messages.raCnfPwdLbl).setFocus();
        wabot.textWithLabel(Messages.raPathLbl).setFocus();
        assertTrue("testCertChangedPwdCnfPwdFocusOut",
        		!wabot.textWithLabel(Messages.raPwdLbl).
        		getText().equals("")
                && !wabot.textWithLabel(Messages.raCnfPwdLbl).
                getText().equals(""));
        Utility.closeProjPropertyPage(Messages.rProjName);
        Utility.selProjFromExplorer(Messages.raNewProj).select();
        Utility.deleteSelectedProject();
    }

    @Test
    // test case 68
    public void testCertChangedPwdFocusInOut() throws Exception {
    	Utility.enableRemoteAccess();
        Utility.enterRemoteAccessDetails(Messages.rProjName);
        wabot.button("OK").click();
        Utility.createProject(Messages.raNewProj);
        String str = Utility.getCertOfOtherProject(Messages.raNewProj);
        Utility.selProjFromExplorer(Messages.rProjName);
        Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
        wabot.textWithLabel(Messages.raPathLbl).setText(str);
        wabot.button("OK").click();
        wabot.shell(Messages.rAccErrTtl).activate();
        wabot.button("OK").click();
        wabot.textWithLabel(Messages.raPwdLbl).setFocus();
        wabot.textWithLabel(Messages.raPathLbl).setFocus();
        wabot.button("OK").click();
        SWTBotShell sh = wabot.shell(Messages.rAccErrTtl).activate();
        String msg = sh.getText();
        wabot.button("OK").click();
        assertTrue("testCertChangedPwdFocusInOut",
        		msg.equals(Messages.rAccErrTtl));
        Utility.closeProjPropertyPage(Messages.rProjName);
        Utility.selProjFromExplorer(Messages.raNewProj).select();
        Utility.deleteSelectedProject();
    }

    @Test
    // test case 69
    public void testCertChangedPwdFocusChangeCnfPWd() throws Exception {
    	Utility.enableRemoteAccess();
        Utility.enterRemoteAccessDetails(Messages.rProjName);
        wabot.button("OK").click();
        Utility.createProject(Messages.raNewProj);
        String str = Utility.getCertOfOtherProject(Messages.raNewProj);
        Utility.selProjFromExplorer(Messages.rProjName);
        Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
        wabot.textWithLabel(Messages.raPathLbl).setText(str);
        wabot.button("OK").click();
        wabot.shell(Messages.rAccErrTtl).activate();
        wabot.button("OK").click();
        wabot.textWithLabel(Messages.raPwdLbl).setFocus();
        wabot.textWithLabel(Messages.raCnfPwdLbl).typeText(Messages.raWeakPwd);
        wabot.button("OK").click();
        SWTBotShell sh = wabot.shell(Messages.rAccErrTtl).activate();
        String msg = sh.getText();
        wabot.button("OK").click();
        assertTrue("testCertChangedPwdFocusChangeCnfPWd",
        		msg.equals(Messages.rAccErrTtl));
        Utility.closeProjPropertyPage(Messages.rProjName);
        Utility.selProjFromExplorer(Messages.raNewProj).select();
        Utility.deleteSelectedProject();
    }

    @Test
    // test case 70
    public void testSameCertPath() throws Exception {
    	Utility.enableRemoteAccess();
        Utility.enterRemoteAccessDetails(Messages.rProjName);
        String path = wabot.textWithLabel(Messages.raPathLbl).getText();
        wabot.textWithLabel(Messages.raPathLbl).setText("");
        wabot.textWithLabel(Messages.raPathLbl).setText(path);
        wabot.button("OK").click();
        assertTrue("testSameCertPath",
        		wabot.activeShell().getText().
        		equals(Messages.activeShell));
    }

    @Test
    // test case 71
    public void testWorkspaceButton() throws Exception {
    	Utility.enableRemoteAccess();
        wabot.button(Messages.wrkSpcButton).click();
        SWTBotShell sh = wabot.shell(Messages.wrkSpcTitle).activate();
        String msg = sh.getText();
        sh.close();
        assertTrue("testWorkspaceButton",
        		msg.equals(Messages.wrkSpcTitle));
        Utility.closeProjPropertyPage(Messages.rProjName);
    }
	
    @Test
    // test case 72
    public void testCertPathFromSameProject() throws Exception {
    	/*
    	 * Note : cert file path will start with "${basedir}"
    	 * only when selected using buttons and not copy paste.
    	 */
    	Utility.enableRemoteAccess();
    	wabot.textWithLabel(Messages.raUnameLbl).setText(Messages.test);
    	wabot.textWithLabel(Messages.raPwdLbl).setText(Messages.raStrPwd);
    	wabot.textWithLabel(Messages.raCnfPwdLbl).setText(Messages.raStrPwd);
    	wabot.textWithLabel(Messages.raPathLbl).
    	setText(Messages.raDefPath);
    	assertTrue("testCertPathFromSameProject",
    			wabot.textWithLabel(Messages.raPathLbl).getText().
    			startsWith(Messages.raBasePath));
    	Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 73
    public void testCertPathFromOtherProject() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
    	wabot.button("OK").click();
        Utility.createProject(Messages.raNewProj);
        String path = Utility.getCertOfOtherProject(Messages.raNewProj);
        Utility.selProjFromExplorer(Messages.rProjName);
        Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
        wabot.textWithLabel(Messages.raPathLbl).setText(path);
        assertTrue("testCertPathFromOtherProject",
        		wabot.textWithLabel(Messages.raPathLbl).
        		getText().equals(path));
        Utility.closeProjPropertyPage(Messages.rProjName);
        Utility.selProjFromExplorer(Messages.raNewProj).select();
        Utility.deleteSelectedProject();
    }

    @Test
    // test case 74
    public void testCertPathWrongFile() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
    	wabot.button("OK").click();
    	Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
        wabot.textWithLabel(Messages.raPathLbl).setText(Messages.raPfxPath);
        wabot.button("OK").click();
        wabot.shell(Messages.rAccErrTtl).activate();
        wabot.button("OK").click();
        wabot.button("OK").click();
        wabot.shell(Messages.rAccErrTtl).activate();
        wabot.button("Yes").click();
        SWTBotShell sh = wabot.shell(Messages.rAccErrTtl).activate();
        String msg = sh.getText();
        wabot.button("OK").click();
        assertTrue("testCertPathWrongFile",
        		msg.equals(Messages.rAccErrTtl));
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 79
    public void testCertPathWrongData() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
    	wabot.button("OK").click();
    	Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
        wabot.textWithLabel(Messages.raPathLbl).setText(Messages.wrongPathData);
        wabot.button("OK").click();
        wabot.shell(Messages.rAccErrTtl).activate();
        wabot.button("OK").click();
        wabot.button("OK").click();
        wabot.shell(Messages.rAccErrTtl).activate();
        wabot.button("Yes").click();
        SWTBotShell sh = wabot.shell(Messages.rAccErrTtl).activate();
        String msg = sh.getText();
        wabot.button("OK").click();
        assertTrue("testCertPathWrongData",
        		msg.equals(Messages.rAccErrTtl));
        Utility.closeProjPropertyPage(Messages.rProjName);
    }


    @Test
    // test case 80
    public void testWrongCertPathDisabledProj() throws Exception {
        Utility.enableRemoteAccess();
        wabot.textWithLabel(Messages.raUnameLbl).setText(Messages.rUserName);
        wabot.textWithLabel(Messages.raPwdLbl).setText(Messages.raStrPwd);
        wabot.textWithLabel(Messages.raCnfPwdLbl).setText(Messages.raStrPwd);
        wabot.textWithLabel(Messages.raPathLbl).setText(Messages.wrongPathData);
        wabot.button("OK").click();
        SWTBotShell sh = wabot.shell(Messages.rAccErrTtl).activate();
        String msg = sh.getText();
        wabot.button("OK").click();
        assertTrue("testWrongCertPathDisabledProj",
        		msg.equals(Messages.rAccErrTtl));
        Utility.closeProjPropertyPage(Messages.rProjName);
    }
   
    @Test
    // test case 81
    public void testRemoteAccProperValues() throws Exception {
        Utility.enableRemoteAccess();
        wabot.textWithLabel(Messages.raUnameLbl).setText(Messages.rUserName);
        wabot.textWithLabel(Messages.raPwdLbl).setText(Messages.raStrPwd);
        wabot.textWithLabel(Messages.raCnfPwdLbl).setText(Messages.raStrPwd);
        wabot.textWithLabel(Messages.raPathLbl).setText(Messages.raDefPath);
        wabot.button("OK").click();
        assertTrue("testRemoteAccProperValues",
        		wabot.activeShell().getText().
        		equals(Messages.activeShell));
    }

    @Test
    // test case 82
    public void testNewCertButton() throws Exception {
    	Utility.enableRemoteAccess();
        wabot.button(Messages.newCertButton).click();
        wabot.shell(Messages.newCertDlgTtl).activate();
        assertTrue("testNewCertButton",
        		wabot.textWithLabel(Messages.newCertPwdLbl).isVisible()
                && wabot.textWithLabel(Messages.newCertCnfPwdLbl).isVisible()
                && wabot.textWithLabel(Messages.newCertCertLbl).isVisible()
                && wabot.textWithLabel(Messages.newCertPFXLbl).isVisible());
        wabot.button(Messages.cnclBtn).click();
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 83,84
    public void testNewCertBlankValues() throws Exception {
    	Utility.enableRemoteAccess();
        wabot.button(Messages.newCertButton).click();
        wabot.shell(Messages.newCertDlgTtl).activate();
        wabot.button("OK").click();
        SWTBotShell sh = wabot.shell(Messages.certAccErrTitle).activate();
        String msg = sh.getText();
        wabot.button("OK").click();
        wabot.button(Messages.cnclBtn).click();
        assertTrue("testNewCertBlankValues",
        		msg.equals(Messages.certAccErrTitle));
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 85
    public void testNewCertPwdWithSpace() throws Exception {
    	Utility.enableRemoteAccess();
        wabot.button(Messages.newCertButton).click();
        wabot.shell(Messages.newCertDlgTtl).activate();
        wabot.textWithLabel(Messages.newCertPwdLbl).
        setText(Messages.pwdWithSpace);
        wabot.textWithLabel(Messages.newCertCnfPwdLbl).
        setText(Messages.pwdWithSpace);
        wabot.button("OK").click();
        SWTBotShell sh = wabot.shell(Messages.newCertPwdWrngTtl).activate();
        String msg = sh.getText();
        wabot.button("OK").click();
        wabot.button(Messages.cnclBtn).click();
        assertTrue("testNewCertPwdWithSpace",
        		msg.equals(Messages.newCertPwdWrngTtl));
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 86
    public void testNewCertCertPathBlank() throws Exception {
    	Utility.enableRemoteAccess();
        wabot.button(Messages.newCertButton).click();
        wabot.shell(Messages.newCertDlgTtl).activate();
        wabot.textWithLabel(Messages.newCertPwdLbl).setText(Messages.raStrPwd);
        wabot.textWithLabel(Messages.newCertCnfPwdLbl).
        setText(Messages.raStrPwd);
        wabot.button("OK").click();
        SWTBotShell sh = wabot.shell(Messages.certAccErrTitle).activate();
        String msg = sh.getText();
        wabot.button("OK").click();
        wabot.button(Messages.cnclBtn).click();
        assertTrue("testNewCertCertPathBlank",
        		msg.equals(Messages.certAccErrTitle));
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 87
    public void testNewCertPfxPathBlank() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
    	wabot.button("OK").click();
        Utility.createProject(Messages.raNewProj);
        String path = Utility.getCertOfOtherProject(Messages.raNewProj);
        Utility.selProjFromExplorer(Messages.rProjName);
        Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
        wabot.button(Messages.newCertButton).click();
        wabot.shell(Messages.newCertDlgTtl).activate();
        wabot.textWithLabel(Messages.newCertPwdLbl).
        setText(Messages.raStrPwd);
        wabot.textWithLabel(Messages.newCertCnfPwdLbl).
        setText(Messages.raStrPwd);
        wabot.textWithLabel(Messages.newCertCertLbl).setText(path);
        wabot.button("OK").click();
        SWTBotShell sh = wabot.shell(Messages.certAccErrTitle).activate();
        String msg = sh.getText();
        wabot.button("OK").click();
        wabot.button(Messages.cnclBtn).click();
        assertTrue("testNewCertPfxPathBlank",
        		msg.equals(Messages.certAccErrTitle));
        Utility.closeProjPropertyPage(Messages.rProjName);
        Utility.selProjFromExplorer(Messages.raNewProj).select();
        Utility.deleteSelectedProject();
    }

    @Test
    // test case 100
    public void testNewCertWrongCertPath() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
    	wabot.button("OK").click();
        Utility.createProject(Messages.raNewProj);
        String pfxPath = Utility.getPfxOfOtherProject(Messages.raNewProj);
        Utility.selProjFromExplorer(Messages.rProjName);
        Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
        wabot.button(Messages.newCertButton).click();
        wabot.shell(Messages.newCertDlgTtl).activate();
        wabot.textWithLabel(Messages.newCertPwdLbl).setText(Messages.raStrPwd);
        wabot.textWithLabel(Messages.newCertCnfPwdLbl).
        setText(Messages.raStrPwd);
        wabot.textWithLabel(Messages.newCertCertLbl).
        setText(Messages.wrongPathData);
        wabot.textWithLabel(Messages.newCertPFXLbl).setText(pfxPath);
        wabot.button("OK").click();
        SWTBotShell sh = wabot.shell(Messages.certAccErrTitle).activate();
        String msg = sh.getText();
        wabot.button("OK").click();
        wabot.button(Messages.cnclBtn).click();
        assertTrue("testNewCertWrongCertPath",
        		msg.equals(Messages.certAccErrTitle));
        Utility.closeProjPropertyPage(Messages.rProjName);
        Utility.selProjFromExplorer(Messages.raNewProj).select();
        Utility.deleteSelectedProject();
    }

    @Test
    // test case 101
    public void testNewCertWrongPfxPath() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
    	wabot.button("OK").click();
        Utility.createProject(Messages.raNewProj);
        String cerPath = Utility.getCertOfOtherProject(Messages.raNewProj);
        Utility.selProjFromExplorer(Messages.rProjName);
        Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
        wabot.button(Messages.newCertButton).click();
        wabot.shell(Messages.newCertDlgTtl).activate();
        wabot.textWithLabel(Messages.newCertPwdLbl).setText(Messages.raStrPwd);
        wabot.textWithLabel(Messages.newCertCnfPwdLbl).
        setText(Messages.raStrPwd);
        wabot.textWithLabel(Messages.newCertPFXLbl).
        setText(Messages.wrongPfxPathData);
        wabot.textWithLabel(Messages.newCertCertLbl).setText(cerPath);
        wabot.button("OK").click();
        SWTBotShell sh = wabot.shell(Messages.certAccErrTitle).activate();
        String msg = sh.getText();
        wabot.button("OK").click();
        wabot.button(Messages.cnclBtn).click();
        assertTrue("testNewCertWrongPfxPath",
        		msg.equals(Messages.certAccErrTitle));
        Utility.closeProjPropertyPage(Messages.rProjName);
        Utility.selProjFromExplorer(Messages.raNewProj).select();
        Utility.deleteSelectedProject();
    }

    @Test
    // test case 102
    public void testNewCertCertExtnWrong() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
    	wabot.button("OK").click();
        Utility.createProject(Messages.raNewProj);
        String pfxPath = Utility.getPfxOfOtherProject(Messages.raNewProj);
        Utility.selProjFromExplorer(Messages.rProjName);
        Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
        wabot.button(Messages.newCertButton).click();
        wabot.shell(Messages.newCertDlgTtl).activate();
        wabot.textWithLabel(Messages.newCertPwdLbl).setText(Messages.raStrPwd);
        wabot.textWithLabel(Messages.newCertCnfPwdLbl).
        setText(Messages.raStrPwd);
        wabot.textWithLabel(Messages.newCertCertLbl).
        setText(Messages.wrongPfxPathData);
        wabot.textWithLabel(Messages.newCertPFXLbl).setText(pfxPath);
        wabot.button("OK").click();
        SWTBotShell sh = wabot.shell(Messages.certAccErrTitle).activate();
        String msg = sh.getText();
        wabot.button("OK").click();
        wabot.button(Messages.cnclBtn).click();
        assertTrue("testNewCertCertExtnWrong",
        		msg.equals(Messages.certAccErrTitle));
        Utility.closeProjPropertyPage(Messages.rProjName);
        Utility.selProjFromExplorer(Messages.raNewProj).select();
        Utility.deleteSelectedProject();
    }

    @Test
    // test case 103
    public void testNewCertPfxExtnWrong() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
    	wabot.button("OK").click();
        Utility.createProject(Messages.raNewProj);
        String cerPath = Utility.getCertOfOtherProject(Messages.raNewProj);
        Utility.selProjFromExplorer(Messages.rProjName);
        Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
        wabot.button(Messages.newCertButton).click();
        wabot.shell(Messages.newCertDlgTtl).activate();
        wabot.textWithLabel(Messages.newCertPwdLbl).setText(Messages.raStrPwd);
        wabot.textWithLabel(Messages.newCertCnfPwdLbl).
        setText(Messages.raStrPwd);
        wabot.textWithLabel(Messages.newCertPFXLbl).
        setText(Messages.wrongPathData);
        wabot.textWithLabel(Messages.newCertCertLbl).setText(cerPath);
        wabot.button("OK").click();
        SWTBotShell sh = wabot.shell(Messages.certAccErrTitle).activate();
        String msg = sh.getText();
        wabot.button("OK").click();
        wabot.button(Messages.cnclBtn).click();
        assertTrue("testNewCertPfxExtnWrong",
        		msg.equals(Messages.certAccErrTitle));
        Utility.closeProjPropertyPage(Messages.rProjName);
        Utility.selProjFromExplorer(Messages.raNewProj).select();
        Utility.deleteSelectedProject();
    }

    @Test
    // test case 104
    public void testNewCertProperValues() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
    	wabot.button("OK").click();
        Utility.createProject(Messages.raNewProj);
        String cerPath = Utility.getCertOfOtherProject(Messages.raNewProj);
        String pfxPath = Utility.getPfxOfOtherProject(Messages.raNewProj);
        Utility.selProjFromExplorer(Messages.rProjName);
        Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
        wabot.button(Messages.newCertButton).click();
        SWTBotShell sh = wabot.shell(Messages.newCertDlgTtl).activate();
        wabot.textWithLabel(Messages.newCertPwdLbl).setText(Messages.raStrPwd);
        wabot.textWithLabel(Messages.newCertCnfPwdLbl).
        setText(Messages.raStrPwd);
        wabot.textWithLabel(Messages.newCertPFXLbl).typeText(pfxPath);
        wabot.textWithLabel(Messages.newCertCertLbl).typeText(cerPath);
        wabot.button("OK").click();
        wabot.waitUntil(shellCloses(sh));
        wabot.shell(String.format(
				"%s%s%s", Messages.propPageTtl, " ",
				Messages.rProjName)).activate();
        assertTrue("testNewCertProperValues",
        		wabot.textWithLabel(Messages.raPathLbl).
        		getText().equals(cerPath));
        Utility.closeProjPropertyPage(Messages.rProjName);
        Utility.selProjFromExplorer(Messages.raNewProj).select();
        Utility.deleteSelectedProject();
    }

    @Test
    // test case 105
    public void testNewCertCnfPwdBlank() throws Exception {
    	Utility.enableRemoteAccess();
        wabot.button(Messages.newCertButton).click();
        wabot.shell(Messages.newCertDlgTtl).activate();
        wabot.textWithLabel(Messages.newCertPwdLbl).setText(Messages.raStrPwd);
        wabot.button("OK").click();
        SWTBotShell sh = wabot.shell(Messages.certAccErrTitle).activate();
        String msg = sh.getText();
        wabot.button("OK").click();
        wabot.button(Messages.cnclBtn).click();
        assertTrue("testNewCertCnfPwdBlank",
        		msg.equals(Messages.certAccErrTitle));
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 106
    public void testNewCertPwdNotMatch() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
    	wabot.button("OK").click();
        Utility.createProject(Messages.raNewProj);
        String cerPath = Utility.getCertOfOtherProject(Messages.raNewProj);
        String pfxPath = Utility.getPfxOfOtherProject(Messages.raNewProj);
        Utility.selProjFromExplorer(Messages.rProjName);
        Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
        wabot.button(Messages.newCertButton).click();
        wabot.shell(Messages.newCertDlgTtl).activate();
        wabot.textWithLabel(Messages.newCertPwdLbl).setText(Messages.raStrPwd);
        wabot.textWithLabel(Messages.newCertCnfPwdLbl).
        setText(Messages.raWeakPwd);
        wabot.textWithLabel(Messages.newCertCertLbl).typeText(cerPath);
        wabot.textWithLabel(Messages.newCertPFXLbl).typeText(pfxPath);
        wabot.button("OK").click();
        SWTBotShell errShell =
        		wabot.shell(Messages.newCertPwdWrngTtl).activate();
        String msg = errShell.getText();
        wabot.button("OK").click();
        wabot.button(Messages.cnclBtn).click();
        assertTrue("testNewCertPwdNotMatch",
        		msg.equals(Messages.newCertPwdWrngTtl));
        Utility.closeProjPropertyPage(Messages.rProjName);
        Utility.selProjFromExplorer(Messages.raNewProj).select();
        Utility.deleteSelectedProject();
    }

    @Test
    // test case 131
    public void testOkToLeave() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
        wabot.textWithLabel(Messages.raUnameLbl).setText("");
        SWTBotShell shell1 = wabot.activeShell();
        shell1.activate();
        SWTBotTree properties = shell1.bot().tree();
        SWTBotTreeItem  item = properties.getTreeItem(Messages.waPage);
        item.expand().getNode("Roles").select();
        wabot.activeShell().activate();
        wabot.button("OK").click();
        assertTrue("testOkToLeave",
        		wabot.activeShell().getText().equals(String.format(
				"%s%s%s", Messages.propPageTtl, " ",
				Messages.rProjName)));
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 132
    public void testNewCertCnfPwdwithSpace() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
    	wabot.button("OK").click();
        Utility.createProject(Messages.raNewProj);
        String cerPath = Utility.getCertOfOtherProject(Messages.raNewProj);
        String pfxPath = Utility.getPfxOfOtherProject(Messages.raNewProj);
        Utility.selProjFromExplorer(Messages.rProjName);
        Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
        wabot.button(Messages.newCertButton).click();
        wabot.shell(Messages.newCertDlgTtl).activate();
        wabot.textWithLabel(Messages.newCertPwdLbl).setText(Messages.raStrPwd);
        wabot.textWithLabel(Messages.newCertPwdLbl).
        setText(Messages.pwdWithSpace);
        wabot.textWithLabel(Messages.newCertCnfPwdLbl).
        setText(Messages.pwdWithSpace);
        wabot.textWithLabel(Messages.newCertCertLbl).setText(cerPath);
        wabot.textWithLabel(Messages.newCertPFXLbl).setText(pfxPath);
        wabot.button("OK").click();
        SWTBotShell errShell = wabot.shell(
        		Messages.newCertPwdWrngTtl).activate();
        String msg = errShell.getText();
        wabot.button("OK").click();
        wabot.button(Messages.cnclBtn).click();
        assertTrue("testNewCertCnfPwdwithSpace",
        		msg.equals(Messages.newCertPwdWrngTtl));
        Utility.closeProjPropertyPage(Messages.rProjName);
        Utility.selProjFromExplorer(Messages.raNewProj).select();
        Utility.deleteSelectedProject();
    }

    @Test
    // test case 133
    public void testNewCertPathWithFolderOnly() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
    	wabot.button("OK").click();
        Utility.createProject(Messages.raNewProj);
        String cerPath = Utility.getCertOfOtherProject(Messages.raNewProj);
        String pfxPath = Utility.getPfxOfOtherProject(Messages.raNewProj);
        Utility.selProjFromExplorer(Messages.rProjName);
        Utility.getPropertyPage(Messages.rProjName, Messages.rAccPage);
        String certfPath = cerPath.substring(0,
        		cerPath.lastIndexOf("\\"));
        String pfxfPath = pfxPath.substring(0,
        		pfxPath.lastIndexOf("\\"));
        wabot.button(Messages.newCertButton).click();
        wabot.shell(Messages.newCertDlgTtl).activate();
        wabot.textWithLabel(Messages.newCertPwdLbl).setText(Messages.raStrPwd);
        wabot.textWithLabel(Messages.newCertCnfPwdLbl).
        setText(Messages.raStrPwd);
        wabot.textWithLabel(Messages.newCertCertLbl).setText(certfPath);
        wabot.textWithLabel(Messages.newCertPFXLbl).setText(pfxfPath);
        wabot.button("OK").click();
        SWTBotShell errShell = wabot.shell(Messages.certAccErrTitle).activate();
        String msg = errShell.getText();
        wabot.button("OK").click();
        wabot.button(Messages.cnclBtn).click();
        assertTrue("testNewCertPathWithFolderOnly",
        		msg.equals(Messages.certAccErrTitle));
        Utility.closeProjPropertyPage(Messages.rProjName);
        Utility.selProjFromExplorer(Messages.raNewProj).select();
        Utility.deleteSelectedProject();
    }

    @Test
    // test case 139
    public void testExpDateUI() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
        wabot.button(Messages.raExpDateLbl).click();
        SWTBotShell sh = wabot.shell(Messages.raDatePickTtl).activate();
        String msg = sh.getText();
        wabot.button("OK").click();
        wabot.shell(Messages.rAccErrTtl).activate();
        wabot.button("OK").click();
        sh.close();
        assertTrue("testExpDateUI",
        		msg.equals(Messages.raDatePickTtl));
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 140
    public void testNewCertPathWithNoFolder() throws Exception {
    	Utility.enableRemoteAccess();
        wabot.button(Messages.newCertButton).click();
        wabot.shell(Messages.newCertDlgTtl).activate();
        wabot.textWithLabel(Messages.newCertPwdLbl).setText(Messages.raStrPwd);
        wabot.textWithLabel(Messages.newCertCnfPwdLbl).
        setText(Messages.raStrPwd);
        wabot.textWithLabel(Messages.newCertCertLbl).
        setText(Messages.raWeakPwd);
        wabot.textWithLabel(Messages.newCertPFXLbl).setText(Messages.raWeakPwd);
        wabot.button("OK").click();
        SWTBotShell errShell = wabot.shell(Messages.certAccErrTitle).activate();
        String msg = errShell.getText();
        wabot.button("OK").click();
        wabot.button(Messages.cnclBtn).click();
        assertTrue("testNewCertPathWithNoFolder",
        		msg.equals(Messages.certAccErrTitle));
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 141
    public void testNewCertPathSameProj() throws Exception {
    	Utility.enableRemoteAccess();
        String cerPath = Utility.getCertOfOtherProject(Messages.rProjName);
        String pfxPath = Utility.getPfxOfOtherProject(Messages.rProjName);
        wabot.button(Messages.newCertButton).click();
        wabot.shell(Messages.newCertDlgTtl).activate();
        wabot.textWithLabel(Messages.newCertPwdLbl).setText(Messages.raStrPwd);
        wabot.textWithLabel(Messages.newCertCnfPwdLbl).
        setText(Messages.raStrPwd);
        wabot.textWithLabel(Messages.newCertCertLbl).setText(cerPath);
        wabot.textWithLabel(Messages.newCertPFXLbl).setText(pfxPath);
        wabot.button("OK").click();
        assertTrue("testNewCertPathSameProj",
        		wabot.textWithLabel(Messages.raPathLbl).
        		getText().startsWith(Messages.raBasePath));
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 142
    public void testOkToLeaveExpDateBlank() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
        wabot.textWithLabel(Messages.raExDateLbl).setText("");
        SWTBotShell shell1 = wabot.activeShell();
        shell1.activate();
        SWTBotTree properties = shell1.bot().tree();
        SWTBotTreeItem  item = properties.getTreeItem(Messages.waPage);
        item.expand().getNode("Roles").select();
        wabot.activeShell().activate();
        wabot.button("OK").click();
        assertTrue("testOkToLeaveExpDateBlank",
        		wabot.activeShell().getText().equals(String.format(
				"%s%s%s", Messages.propPageTtl, " ",
				Messages.rProjName)));
        Utility.closeProjPropertyPage(Messages.rProjName);
    }

    @Test
    // test case 143
    public void testOkToLeaveCertPathWrong() throws Exception {
    	Utility.enableRemoteAccess();
    	Utility.enterRemoteAccessDetails(Messages.rProjName);
        wabot.textWithLabel(Messages.raPathLbl).setText(Messages.raStrPwd);
        SWTBotShell shell1 = wabot.activeShell();
        shell1.activate();
        SWTBotTree properties = shell1.bot().tree();
        SWTBotTreeItem  item = properties.getTreeItem(Messages.waPage);
        item.expand().getNode("Roles").select();
        wabot.activeShell().activate();
        wabot.button("OK").click();
        assertTrue("testOkToLeaveCertPathWrong",
        		wabot.activeShell().getText().equals(String.format(
        				"%s%s%s", Messages.propPageTtl, " ",
        				Messages.rProjName)));
        Utility.closeProjPropertyPage(Messages.rProjName);
    }
}
