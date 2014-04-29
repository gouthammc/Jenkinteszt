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

import java.util.Arrays;
import java.util.List;

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

public class WALoadBalancingTest {
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
        if (Utility.isProjExist(Messages.projWithLoadBlc)) {
            // delete existing project
            Utility.selProjFromExplorer(Messages.projWithLoadBlc).select();
            Utility.deleteSelectedProject();
        }
        Utility.createProject(Messages.projWithLoadBlc);
    }

     @After
     public void cleanUp() throws Exception {
         if(Utility.isProjExist(Messages.projWithLoadBlc)) {
             Utility.selProjFromExplorer(Messages.projWithLoadBlc).select();
             Utility.deleteSelectedProject();
             }
        }

     @Test
     // (New Test Cases for 1.6) test case 7
     public void testStickyChkPresent() throws Exception {
         SWTBotShell propShell = Utility.
                 selectPageUsingContextMenu(Messages.projWithLoadBlc, Messages.role1, Messages.loadBlcPage);
         assertTrue("testStickyChkPresent", wabot.
                 checkBox().isEnabled());
         propShell.close();
     }

     @Test
     // (New Test Cases for 1.6) test case 78
     public void testLoadBlcNodePresent() throws Exception {
    	 SWTBotShell propShell = Utility.
 				selectPageUsingContextMenu(Messages.projWithLoadBlc,
 						Messages.role1, Messages.loadBlcPage);
        SWTBotTree properties = propShell.bot().tree();
        SWTBotTreeItem node = properties.getTreeItem(Messages.roleTreeRoot).
                expand().getNode(Messages.loadBlcPage);
        assertTrue("testLoadBlcNodePresent", node.getText().
                equals(Messages.loadBlcPage));
        propShell.close();
     }

     @Test
     // (New Test Cases for 1.6) test case 79
     public void testLoadBclPrpPagePresent() throws Exception {
         SWTBotShell propShell = Utility.
                 selectPageUsingContextMenu(Messages.projWithLoadBlc, Messages.role1, Messages.loadBlcPage);
         assertTrue("testLoadBclPrpPagePresent", wabot.
                 checkBox().isEnabled());
         propShell.close();
     }

     @Test
     // (New Test Cases for 1.6) test case 80
     public void testEndPtComboDisabled() throws Exception {
         SWTBotShell propShell = Utility.
                 selectPageUsingContextMenu(Messages.projWithLoadBlc, Messages.role1, Messages.loadBlcPage);
         assertTrue("testEndPtComboDisabled", !wabot.comboBox().isEnabled());
         propShell.close();
     }

     @Test
     // (New Test Cases for 1.6) test case 81
     public void testChkChecked() throws Exception {
    	 SWTBotShell propShell = addHttpEndpoint();
         Utility.selPageNode(Messages.role1, Messages.loadBlcPage);
         wabot.checkBox().select();
         boolean val1 = wabot.comboBox().isEnabled();
         Utility.selPageNode(Messages.role1, Messages.endptPage);
         boolean val2 = wabot.table().cell(1, 0).equals(Messages.sesAfinityStr);
         assertTrue("testChkChecked", val1 && val2);
         propShell.close();
     }

     @Test
     // (New Test Cases for 1.6) test case 82
     public void testStkSesEnabledReopenPage() throws Exception {
    	 SWTBotShell propShell = addHttpEndpoint();
    	 Utility.selPageNode(Messages.role1, Messages.loadBlcPage);
         wabot.checkBox().select();
         // Navigate to other page
         Utility.selPageNode(Messages.role1, Messages.endptPage);
         Utility.selPageNode(Messages.role1, Messages.loadBlcPage);
         String str1= wabot.comboBox().getText();
         wabot.button("OK").click();
         propShell = Utility.
        		 selectPageUsingContextMenu(Messages.projWithLoadBlc, Messages.role1, Messages.loadBlcPage);
         String str2= wabot.comboBox().getText();
         assertTrue("testStkSesEnabledReopenPage", str1.equals(str2));
         propShell.close();
     }

     @Test
     // (New Test Cases for 1.6) test case 83
     public void testStkSesDisabledReopenPage() throws Exception {
    	 SWTBotShell propShell = addHttpEndpoint();
    	 Utility.selPageNode(Messages.role1, Messages.loadBlcPage);
    	 // Enable
    	 wabot.checkBox().select();
         // Disable
         wabot.checkBox().click();
         boolean val1 = !wabot.comboBox().isEnabled();
         wabot.button("OK").click();
         propShell = Utility.
                 selectPageUsingContextMenu(Messages.projWithLoadBlc, Messages.role1, Messages.loadBlcPage);
         boolean val2 = !wabot.comboBox().isEnabled();
         assertTrue("testStkSesDisabledReopenPage", val1 && val2);
         propShell.close();
     }

     @Test
     // (New Test Cases for 1.6) test case 84
     public void testInputEndPtRemove() throws Exception {
    	 SWTBotShell propShell = addHttpEndpoint();
    	 Utility.selPageNode(Messages.role1, Messages.loadBlcPage);
         wabot.checkBox().select();
         Utility.selPageNode(Messages.role1, Messages.endptPage);
         wabot.table().select(0);
         wabot.button(Messages.roleRemBtn).click();
         SWTBotShell cnfrmShell = wabot.shell(Messages.delEndPtTtl);
         cnfrmShell.activate();
         String confirmMsg = cnfrmShell.getText();
         assertTrue("testInputEndPtRemove",
                 confirmMsg.equals(Messages.delEndPtTtl));
         cnfrmShell.close();
         propShell.close();
     }

     @Test
     // (New Test Cases for 1.6) test case 85
     public void testInputEndPtRemNoPressed() throws Exception {
    	 SWTBotShell propShell = addHttpEndpoint();
    	 Utility.selPageNode(Messages.role1, Messages.loadBlcPage);
         wabot.checkBox().select();
         Utility.selPageNode(Messages.role1, Messages.endptPage);
         wabot.table().select(0);
         wabot.button(Messages.roleRemBtn).click();
         SWTBotShell cnfrmShell = wabot.shell(Messages.delEndPtTtl);
         cnfrmShell.activate();
         wabot.button("Cancel").click();
         assertTrue("testInputEndPtRemNoPressed", wabot.table().
                 cell(0, 0).equals(Messages.intEndPt));
         propShell.close();
     }

     @Test
     // (New Test Cases for 1.6) test case 86
     public void testInputEndPtRemYesPressed() throws Exception {
    	 addHttpEndpoint();
    	 Utility.selPageNode(Messages.role1, Messages.loadBlcPage);
         wabot.checkBox().select();
         Utility.selPageNode(Messages.role1, Messages.endptPage);
         wabot.table().select(0);
         wabot.button(Messages.roleRemBtn).click();
         SWTBotShell cnfrmShell = wabot.shell(Messages.delEndPtTtl);
         cnfrmShell.activate();
         wabot.button("OK").click();
         boolean val1 = !wabot.table().containsItem(Messages.sesAfinityStr);
         Utility.selPageNode(Messages.role1, Messages.loadBlcPage);
         boolean val2 = !wabot.comboBox().isEnabled();
         assertTrue("testInputEndPtRemYesPressed", val1 && val2); 
         wabot.button("OK").click();
     }

     @Test
     // (New Test Cases for 1.6) test case 87
     public void testInternalEndPtRemove() throws Exception {
    	 SWTBotShell propShell = addHttpEndpoint();
    	 Utility.selPageNode(Messages.role1, Messages.loadBlcPage);
         wabot.checkBox().select();
         Utility.selPageNode(Messages.role1, Messages.endptPage);
         wabot.table().select(1);
         wabot.button(Messages.roleRemBtn).click();
         SWTBotShell cnfrmShell = wabot.shell(Messages.delEndPtTtl);
         cnfrmShell.activate();
         String confirmMsg = cnfrmShell.getText();
         assertTrue("testInternalEndPtRemove",
                 confirmMsg.equals(Messages.delEndPtTtl));
         cnfrmShell.close();
         propShell.close();
     }

     @Test
     // (New Test Cases for 1.6) test case 88
     public void testInternalEndPtRemNoPressed() throws Exception {
    	 SWTBotShell propShell = addHttpEndpoint();
    	 Utility.selPageNode(Messages.role1, Messages.loadBlcPage);
         wabot.checkBox().select();
         Utility.selPageNode(Messages.role1, Messages.endptPage);
         wabot.table().select(1);
         wabot.button(Messages.roleRemBtn).click();
         SWTBotShell cnfrmShell = wabot.shell(Messages.delEndPtTtl);
         cnfrmShell.activate();
         wabot.button("Cancel").click();
         assertTrue("testInternalEndPtRemNoPressed", wabot.table().
                 cell(1, 0).equals(Messages.sesAfinityStr));
         propShell.close();
     }

     @Test
     // (New Test Cases for 1.6) test case 89
     public void testInternalEndPtRemYesPressed() throws Exception {
    	 SWTBotShell propShell = addHttpEndpoint();
    	 Utility.selPageNode(Messages.role1, Messages.loadBlcPage);
         wabot.checkBox().select();
         Utility.selPageNode(Messages.role1, Messages.endptPage);
         wabot.table().select(1);
         wabot.button(Messages.roleRemBtn).click();
         SWTBotShell cnfrmShell = wabot.shell(Messages.delEndPtTtl);
         cnfrmShell.activate();
         wabot.button("OK").click();
         boolean val1 = !wabot.table().containsItem(Messages.sesAfinityStr);
         Utility.selPageNode(Messages.role1, Messages.loadBlcPage);
         boolean val2 = !wabot.comboBox().isEnabled();
         assertTrue("testInternalEndPtRemYesPressed", val1 && val2);
         propShell.close();
     }

     /*
      * (New Test Cases for 1.6) test case 90 and 91
      * In place editing not allowed. Hence removed. 
      */

     @Test
     // (New Test Cases for 1.6) test case 92
     public void testDebugStkSesEndPtNotEql() throws Exception {
    	 SWTBotShell propShell = addHttpEndpoint();
    	 Utility.selPageNode(Messages.role1, Messages.loadBlcPage);
         wabot.checkBox().select();
         String loadBlcStr = wabot.comboBox().getText();
         Utility.selPageNode(Messages.role1, Messages.dbgEndPtName);
         wabot.checkBox().select();
         String debugStr = wabot.comboBox().getText();
         assertTrue("testDebugStkSesEndPtNotEql",
                 !loadBlcStr.equals(debugStr));
         propShell.close();
     }

     @Test
     // (New Test Cases for 1.6) test case 93
     public void testStkSesDebugEndPtNotEql() throws Exception {
    	 SWTBotShell propShell = addHttpEndpoint();
    	 Utility.selPageNode(Messages.role1, Messages.loadBlcPage);
         Utility.selPageNode(Messages.role1, Messages.dbgEndPtName);
         wabot.checkBox().select();
         String debugStr = wabot.comboBox().getText();
         Utility.selPageNode(Messages.role1, Messages.loadBlcPage);
         wabot.checkBox().select();
         String loadBlcStr = wabot.comboBox().getText();
         assertTrue("testStkSesDebugEndPtNotEql",
                 !debugStr.equals(loadBlcStr));
         propShell.close();
     }

     @Test
     // (New Test Cases for 1.6) test case 97
     public void testDefaultEndPt() throws Exception {
    	 SWTBotShell propShell = addHttpEndpoint();
    	 Utility.selPageNode(Messages.role1, Messages.loadBlcPage);
         wabot.checkBox().select();
         assertTrue("testDefaultEndPt", wabot.comboBox().isEnabled()
                 && wabot.comboBox().getText().equals(Messages.httpEndPtStr));
         propShell.close();
     }

     @Test
     // (New Test Cases for 1.6) test case 98
     public void testFirstInputEndPt() throws Exception {
    	SWTBotShell propShell = addHttpEndpoint();
        wabot.button(Messages.roleAddBtn).click();
        Utility.addEp("IntEndPt1", "Input", "11", "13");
        wabot.button(Messages.roleAddBtn).click();
        Utility.addEp("IntEndPt2", "Input", "21", "23");
        // Delete default http end point
        wabot.table().click(0, 0);
        wabot.button(Messages.roleRemBtn).click();
        SWTBotShell cnfrmShell = wabot.shell(Messages.delEndPtTtl);
        cnfrmShell.activate();
        wabot.button("Yes").click();
        // Enable Load Balancing option
        Utility.selPageNode(Messages.role1, Messages.loadBlcPage);
        wabot.checkBox().select();
        assertTrue("testFirstInputEndPt", wabot.comboBox().
                getText().equals(Messages.inputEndPtStr));
        propShell.close();
     }

     @Test
     // (New Test Cases for 1.6) test case 99
     public void testNoInputEndPtPresent() throws Exception {
    	SWTBotShell propShell = addHttpEndpoint();
        wabot.table().click(0, 0);
        wabot.button(Messages.roleRemBtn).click();
        SWTBotShell cnfrmShell = wabot.shell(Messages.delEndPtTtl);
        cnfrmShell.activate();
        wabot.button("Yes").click();
        // Enable Load Balancing option
        Utility.selPageNode(Messages.role1, Messages.loadBlcPage);
        wabot.checkBox().select();
        SWTBotShell errShell = wabot.shell(Messages.intEndPtErrTtl);
        errShell.activate();
        String errMsg = errShell.getText();
        assertTrue("testNoInputEndPtPresent",
                 errMsg.equals(Messages.intEndPtErrTtl));
        errShell.close();
        propShell.close();
     }

     @Test
     // (New Test Cases for 1.6) test case 100
     public void testNoInputEndPtCancel() throws Exception {
    	SWTBotShell propShell = addHttpEndpoint();
        wabot.table().click(0, 0);
        wabot.button(Messages.roleRemBtn).click();
        SWTBotShell cnfrmShell = wabot.shell(Messages.delEndPtTtl);
        cnfrmShell.activate();
        wabot.button("Yes").click();
        // Enable Load Balancing option
        Utility.selPageNode(Messages.role1, Messages.loadBlcPage);
        wabot.checkBox().select();
        SWTBotShell errShell = wabot.shell(Messages.intEndPtErrTtl);
        errShell.activate();
        wabot.button(Messages.cnclBtn).click();
        assertTrue("testNoInputEndPtCancel", !wabot.comboBox().isEnabled()
                 && !wabot.checkBox().isChecked());
        propShell.close();
     }

     @Test
     // (New Test Cases for 1.6) test case 101
     public void testNoInputEndPtOK() throws Exception {
    	SWTBotShell propShell = addHttpEndpoint();
        wabot.table().click(0, 0);
        wabot.button(Messages.roleRemBtn).click();
        SWTBotShell cnfrmShell = wabot.shell(Messages.delEndPtTtl);
        cnfrmShell.activate();
        wabot.button("Yes").click();
        // Enable Load Balancing option
        Utility.selPageNode(Messages.role1, Messages.loadBlcPage);
        wabot.checkBox().select();
        SWTBotShell errShell = wabot.shell(Messages.intEndPtErrTtl);
        errShell.activate();
        wabot.button("OK").click();
        boolean comboVal = wabot.comboBox().getText().
                equals(Messages.httpEndPtStr);
        Utility.selPageNode(Messages.role1, Messages.endptPage);
        assertTrue("testNoInputEndPtOK", wabot.table().
                     cell(0, 0).equals(Messages.intEndPt)
                 && wabot.table().
                     cell(1, 0).equals(Messages.sesAfinityStr)
                 && comboVal);
        propShell.close();
     }

     @Test
     // (New Test Cases for 1.7) test case 125
     public void testEndPtList() throws Exception {
    	 SWTBotShell propShell = addHttpEndpoint();
    	 wabot.button(Messages.roleAddBtn).click();
    	 Utility.addEp("InstncTest", Messages.typeInstnc, "13-15", "16");
    	 wabot.button(Messages.roleAddBtn).click();
    	 Utility.addEp("InputTest", "Input", "111", "111");
    	 Utility.selPageNode(Messages.role1, Messages.loadBlcPage);
    	 wabot.checkBox().select();
    	 String[] endpts = wabot.comboBox().items();
    	 List<String> endList = Arrays.asList(endpts);
    	 assertTrue("testEndPtList", endList.contains(Messages.httpEndPtStr)
    			 && endList.contains("InputTest (public:111,private:111)")
    			 && !endList.contains("InstncTest (public:13-15,private:16)"));
    	 propShell.close();
     }

     private SWTBotShell addHttpEndpoint() {
    	 SWTBotShell propShell = Utility.
    			 selectPageUsingContextMenu(Messages.projWithLoadBlc,
    					 Messages.role1, Messages.endptPage);
    	 wabot.button(Messages.roleAddBtn).click();
    	 Utility.addEp(Messages.intEndPt, Messages.typeInpt, "80", "8080");
    	 return propShell;
     }
}
