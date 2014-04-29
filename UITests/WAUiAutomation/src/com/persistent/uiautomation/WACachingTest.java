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
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)

public class WACachingTest {
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
		if (Utility.isProjExist(Messages.projCache)) {
			// delete existing project
			Utility.selProjFromExplorer(Messages.projCache).select();
			Utility.deleteSelectedProject();
		}
	}

	@After
	public void cleanUp() throws Exception {
		if (Utility.isProjExist(Messages.projCache)) {
			Utility.selProjFromExplorer(Messages.projCache).select();
			Utility.deleteSelectedProject();
		}
	}

	@Test
	// (Test Cases for 1.8) test case 1
	public void testCachingPagePresent() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projCache,
						Messages.role1, Messages.cachePage);
		assertTrue("testCachingPagePresent",
				wabot.checkBox().isEnabled()
				&& !wabot.checkBox().isChecked()
				&& wabot.button("OK").isEnabled()
				&& wabot.button(Messages.cnclBtn).isEnabled());
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 2
	public void testEnableCaching() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projCache,
						Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		boolean isEnabled = wabot.checkBox().isChecked()
				&& wabot.label(Messages.cachScaleLbl).isEnabled()
				&& wabot.label(Messages.hostLbl).isEnabled()
				&& wabot.button(Messages.roleAddBtn).isEnabled()
				&& !wabot.button(Messages.roleEditBtn).isEnabled()
				&& wabot.comboBox().isEnabled()
				&& wabot.table().isEnabled();
		boolean cacheSizeVal = wabot.textWithLabel(
				Messages.cachScaleLbl).getText().
				equals(Messages.dfltSizeVal)
				&& (wabot.scale().getValue() == 30);
		boolean dfltCacheVal = wabot.table().
				containsItem(Messages.dfltCachName)
				&& wabot.table().cell(0, 1).equals(Messages.cachBckNo)
				&& wabot.table().cell(0, 2).equals(Messages.expPolAbs)
				&& wabot.table().cell(0, 3).equals("10")
				&& wabot.table().cell(0, 4).equals("11211");
		boolean hostNmVal = wabot.
				textWithLabel(Messages.hostLbl).
				getText().equals(String.format("%s%s",
						Messages.hostNm, Messages.role1.toLowerCase()));
		boolean StorageVal = wabot.comboBox().
				getText().equals("(auto)");
		Utility.selPageNode(Messages.role1, Messages.endptPage);
		boolean endPtPresent = wabot.table().
				cell(0, 0).equals(String.format("%s%s",
						Messages.cachEndPtName, Messages.dfltCachName))
						&& wabot.table().cell(0, 1).
						equals(Messages.typeIntrnl)
						&& wabot.table().cell(0, 3).equals("11211");
		propShell = Utility.selPageNode(Messages.role1, Messages.lclStrPage);
		boolean LclStrPresent = wabot.table().
				cell(0, 0).equals(Messages.cachLclStr)
				&& wabot.table().
				cell(0, 1).equals("20000");
		assertTrue("testEnableCaching", isEnabled
				&& cacheSizeVal
				&& dfltCacheVal
				&& hostNmVal
				&& StorageVal
				&& endPtPresent
				&& LclStrPresent);
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 3
	public void testDisableCaching() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projCache,
						Messages.role1, Messages.cachePage);
		// check
		wabot.checkBox().select();
		Utility.selPageNode(Messages.role1, Messages.endptPage);
		boolean endPtPresent = wabot.table().
				cell(0, 0).equals(String.format("%s%s",
						Messages.cachEndPtName, Messages.dfltCachName))
						&& wabot.table().cell(0, 1).
						equals(Messages.typeIntrnl)
						&& wabot.table().cell(0, 3).equals("11211");
		Utility.selPageNode(Messages.role1, Messages.lclStrPage);
		boolean LclStrPresent = wabot.table().
				cell(0, 0).equals(Messages.cachLclStr)
				&& wabot.table().
				cell(0, 1).equals("20000");
		Utility.selPageNode(Messages.role1,  Messages.cachePage);
		// un-check
		wabot.checkBox().click();
		boolean isDisabled = !wabot.checkBox().isChecked()
				&& !wabot.label(Messages.cachScaleLbl).isEnabled()
				&& !wabot.label(Messages.hostLbl).isEnabled()
				&& !wabot.button(Messages.roleAddBtn).isEnabled()
				&& !wabot.button(Messages.roleEditBtn).isEnabled()
				&& !wabot.comboBox().isEnabled()
				&& !wabot.table().isEnabled()
				&& !wabot.textWithLabel(Messages.cachScaleLbl).isEnabled()
				&& !wabot.scale().isEnabled();
		Utility.selPageNode(Messages.role1, Messages.endptPage);
		boolean endPtRmv = !wabot.table().containsItem(String.format("%s%s",
						Messages.cachEndPtName, Messages.dfltCachName));
		propShell = Utility.selPageNode(Messages.role1, Messages.lclStrPage);
		boolean lclStrRmv = !wabot.table().containsItem(Messages.cachLclStr);
		assertTrue("testDisableCaching", endPtPresent
				&& LclStrPresent
				&& isDisabled
				&& endPtRmv
				&& lclStrRmv);
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 4
	public void testInvalidCacheSizeOKPressed() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projCache,
						Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		// Cache size = 0
		// typeText and setting focus on OK is IMP.
		wabot.textWithLabel(
				Messages.cachScaleLbl).setText("");
		wabot.textWithLabel(
				Messages.cachScaleLbl).typeText("0");
		wabot.button("OK").setFocus();
		wabot.button("OK").click();
		SWTBotShell errorShell = wabot.shell(
				Messages.cachPerErrTtl).activate();
		boolean zeroErr = errorShell.getText().
				equals(Messages.cachPerErrTtl);
		wabot.button("OK").click();
		// Cache size < 0 i.e. Negative
		wabot.textWithLabel(
				Messages.cachScaleLbl).setText("");
		wabot.textWithLabel(
				Messages.cachScaleLbl).typeText("-2");
		wabot.button("OK").setFocus();
		wabot.button("OK").click();
		errorShell = wabot.shell(
				Messages.cachPerErrTtl).activate();
		boolean negErr = errorShell.getText().
				equals(Messages.cachPerErrTtl);
		wabot.button("OK").click();
		// Cache size > 100
		wabot.textWithLabel(
				Messages.cachScaleLbl).setText("");
		wabot.textWithLabel(
				Messages.cachScaleLbl).typeText("105%");
		wabot.button("OK").setFocus();
		wabot.button("OK").click();
		errorShell = wabot.shell(
				Messages.cachPerErrTtl).activate();
		boolean grtErr = errorShell.getText().
				equals(Messages.cachPerErrTtl);
		wabot.button("OK").click();
		assertTrue("testInvalidCacheSizeOKPressed",
				zeroErr
				&& negErr
				&& grtErr);
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 5
	public void testInvalidCacheSizeOKToLeave() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projCache,
						Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		// Cache size = 0
		wabot.textWithLabel(
				Messages.cachScaleLbl).setText("");
		wabot.textWithLabel(
				Messages.cachScaleLbl).typeText("0");
		SWTBotTree properties = propShell.bot().tree();
		properties.getTreeItem(Messages.roleTreeRoot).
		getNode(Messages.endptPage).select();
		SWTBotShell errorShell = wabot.shell(String.format(
				"%s%s", Messages.okToLeaveTtl, " ")).activate();
		boolean zeroErr = errorShell.getText().
				equals(String.format("%s%s",
						Messages.okToLeaveTtl, " "));
		wabot.button("OK").click();
		// Cache size < 0 i.e. Negative
		wabot.textWithLabel(
				Messages.cachScaleLbl).setText("");
		wabot.textWithLabel(
				Messages.cachScaleLbl).typeText("-2");
		properties = propShell.bot().tree();
		properties.getTreeItem(Messages.roleTreeRoot).
		getNode(Messages.endptPage).select();
		errorShell = wabot.shell(String.format(
				"%s%s", Messages.okToLeaveTtl, " ")).activate();
		boolean negErr = errorShell.getText().
				equals(String.format("%s%s",
						Messages.okToLeaveTtl, " "));
		wabot.button("OK").click();
		// Cache size > 100
		wabot.textWithLabel(
				Messages.cachScaleLbl).setText("");
		wabot.textWithLabel(
				Messages.cachScaleLbl).typeText("105%");
		properties = propShell.bot().tree();
		properties.getTreeItem(Messages.roleTreeRoot).
		getNode(Messages.endptPage).select();
		errorShell = wabot.shell(String.format(
				"%s%s", Messages.okToLeaveTtl, " ")).activate();
		boolean grtErr = errorShell.getText().
				equals(String.format("%s%s",
						Messages.okToLeaveTtl, " "));
		wabot.button("OK").click();
		assertTrue("testInvalidCacheSizeOKToLeave",
				zeroErr
				&& negErr
				&& grtErr);
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 6
	public void testNtNumericCacheSizeOKPressed() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projCache,
						Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		// Cache size = alphabet
		// typeText and setting focus on OK is IMP.
		wabot.textWithLabel(
				Messages.cachScaleLbl).setText("");
		wabot.textWithLabel(
				Messages.cachScaleLbl).typeText("ab");
		wabot.button("OK").setFocus();
		wabot.button("OK").click();
		SWTBotShell errorShell = wabot.shell(
				Messages.cachPerErrTtl).activate();
		boolean alphabtErr = errorShell.getText().
				equals(Messages.cachPerErrTtl);
		wabot.button("OK").click();
		// Cache size = special character
		wabot.textWithLabel(
				Messages.cachScaleLbl).setText("");
		wabot.textWithLabel(
				Messages.cachScaleLbl).typeText("#*");
		wabot.button("OK").setFocus();
		wabot.button("OK").click();
		errorShell = wabot.shell(
				Messages.cachPerErrTtl).activate();
		boolean splCharErr = errorShell.getText().
				equals(Messages.cachPerErrTtl);
		wabot.button("OK").click();
		assertTrue("testNtNumericCacheSizeOKPressed",
				alphabtErr
				&& splCharErr);
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 7
	public void testNtNumericCacheSizeOKToLeave() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projCache,
						Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		// Cache size = alphabet
		wabot.textWithLabel(
				Messages.cachScaleLbl).setText("");
		wabot.textWithLabel(
				Messages.cachScaleLbl).typeText("ab");
		SWTBotTree properties = propShell.bot().tree();
		properties.getTreeItem(Messages.roleTreeRoot).
		getNode(Messages.endptPage).select();
		SWTBotShell errorShell = wabot.shell(String.format(
				"%s%s", Messages.okToLeaveTtl, " ")).activate();
		boolean alphabtErr = errorShell.getText().
				equals(String.format("%s%s",
						Messages.okToLeaveTtl, " "));
		wabot.button("OK").click();
		// Cache size = special character
		wabot.textWithLabel(
				Messages.cachScaleLbl).setText("");
		wabot.textWithLabel(
				Messages.cachScaleLbl).typeText("#*");
		properties = propShell.bot().tree();
		properties.getTreeItem(Messages.roleTreeRoot).
		getNode(Messages.endptPage).select();
		errorShell = wabot.shell(String.format(
				"%s%s", Messages.okToLeaveTtl, " ")).activate();
		boolean splCharErr = errorShell.getText().
				equals(String.format("%s%s",
						Messages.okToLeaveTtl, " "));
		wabot.button("OK").click();
		assertTrue("testNtNumericCacheSizeOKToLeave",
				alphabtErr
				&& splCharErr);
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 8
	public void testCacheSizeBoundaryValues() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projCache,
						Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		// Cache size minimum
		// typeText and setting focus on OK is IMP.
		wabot.textWithLabel(
				Messages.cachScaleLbl).setText("");
		wabot.textWithLabel(
				Messages.cachScaleLbl).typeText(
						String.valueOf(wabot.scale().getMinimum()));
		wabot.button("OK").setFocus();
		wabot.button("OK").click();
		propShell = Utility.
				selectPageUsingContextMenu(Messages.projCache,
						Messages.role1, Messages.cachePage);
		boolean minVal = wabot.textWithLabel(
				Messages.cachScaleLbl).getText().equals(
						String.format("%s%s",
								wabot.scale().getMinimum(), "%"));
		wabot.textWithLabel(
				Messages.cachScaleLbl).setText("");
		wabot.textWithLabel(
				Messages.cachScaleLbl).typeText("100");
		wabot.button("OK").setFocus();
		wabot.button("OK").click();
		propShell = Utility.
				selectPageUsingContextMenu(Messages.projCache,
						Messages.role1, Messages.cachePage);
		boolean maxVal = wabot.textWithLabel(
				Messages.cachScaleLbl).getText().equals("100%");
		assertTrue("testCacheSizeBoundaryValues", minVal
				&& maxVal);
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 14
	public void testAddNamedCache() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		wabot.button(Messages.roleAddBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		wabot.textWithLabel(Messages.cachNameLbl).setText("abc");
		wabot.textWithLabel(Messages.portNumLbl).setText("1");
		wabot.textWithLabel(Messages.minLiveLbl).setText("10");
		wabot.button("OK").click();
		boolean entryAdded = wabot.table().cell(0, 0).equals("abc")
				&& wabot.table().cell(0, 1).equals(Messages.cachBckNo)
				&& wabot.table().cell(0, 2).equals(Messages.expPolAbs)
				&& wabot.table().cell(0, 3).equals("10")
				&& wabot.table().cell(0, 4).equals("1");
		Utility.selPageNode(Messages.role1, Messages.endptPage);
		boolean endPtPresent = wabot.table().containsItem(String.format("%s%s",
						Messages.cachEndPtName, "abc"));
		assertTrue("testAddNamedCache", entryAdded
				&& endPtPresent);
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 15
	public void testInvalidCacheNameDlg() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		wabot.button(Messages.roleAddBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		// name having special characters
		wabot.textWithLabel(Messages.cachNameLbl).setText("a*bc#");
		wabot.textWithLabel(Messages.portNumLbl).setText("1");
		wabot.textWithLabel(Messages.minLiveLbl).setText("10");
		wabot.button("OK").click();
		SWTBotShell errShell = wabot.shell(
				Messages.cachErr).activate();
		boolean specialChar = errShell.getText().
				equals(Messages.cachErr);
		wabot.button("OK").click();
		// name starting with number
		wabot.textWithLabel(Messages.cachNameLbl).setText("123abc");
		wabot.button("OK").click();
		errShell = wabot.shell(
				Messages.cachErr).activate();
		boolean startWithNum = errShell.getText().
				equals(Messages.cachErr);
		wabot.button("OK").click();
		/*
		 * Cache name starting with '_' is allowed
		 * as it is allowed to give variable name starting with
		 * '_' in eclipse as well.
		 */
		wabot.textWithLabel(Messages.cachNameLbl).setText("_abc");
		wabot.button("OK").click();
		boolean startWithUnderScore = wabot.table().
				containsItem("_abc");
		wabot.table().select(0);
		wabot.button(Messages.roleEditBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		// Use of underscore allowed at valid place
		wabot.textWithLabel(Messages.cachNameLbl).setText("abc_12");
		wabot.button("OK").click();
		boolean tableEntryAdded = wabot.table().containsItem("abc_12");
		assertTrue("testInvalidCacheNameDlg", specialChar
				&& startWithNum
				&& startWithUnderScore
				&& tableEntryAdded);
		propShell.close();
	}

	@Test
	// (Test Cases for 1.8) test case 16
	public void testInvalidCachePortDlg() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projCache,
						Messages.role1, Messages.endptPage);
		wabot.button(Messages.roleAddBtn).click();
		Utility.addEp(Messages.intEndPt, Messages.typeInpt, "80", "8080");
		Utility.selPageNode(Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		wabot.button(Messages.roleAddBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		// port having special characters
		wabot.textWithLabel(Messages.cachNameLbl).setText("abc");
		wabot.textWithLabel(Messages.portNumLbl).setText("-1");
		wabot.textWithLabel(Messages.minLiveLbl).setText("10");
		wabot.button("OK").click();
		SWTBotShell errShell = wabot.shell(
				Messages.cachPortErrTtl).activate();
		boolean specialChar = errShell.getText().
				equals(Messages.cachPortErrTtl);
		wabot.button("OK").click();
		// port having alphabets
		wabot.textWithLabel(Messages.portNumLbl).setText("1a");
		wabot.button("OK").click();
		errShell = wabot.shell(
				Messages.cachPortErrTtl).activate();
		boolean alphabets = errShell.getText().
				equals(Messages.cachPortErrTtl);
		wabot.button("OK").click();
		// port number < 1
		wabot.textWithLabel(Messages.portNumLbl).setText("0");
		wabot.button("OK").click();
		errShell = wabot.shell(
				Messages.cachPortErrTtl).activate();
		boolean lessThanRng = errShell.getText().
				equals(Messages.cachPortErrTtl);
		wabot.button("OK").click();
		// port number > 65535
		wabot.textWithLabel(Messages.portNumLbl).setText("65536");
		wabot.button("OK").click();
		errShell = wabot.shell(
				Messages.cachPortErrTtl).activate();
		boolean grtThanRng = errShell.getText().
				equals(Messages.cachPortErrTtl);
		wabot.button("OK").click();
		// Existing port value
		wabot.textWithLabel(Messages.portNumLbl).setText("8080");
		wabot.button("OK").click();
		errShell = wabot.shell(
				Messages.cachPortErrTtl).activate();
		boolean alreadyUsed = errShell.getText().
				equals(Messages.cachPortErrTtl);
		wabot.button("OK").click();
		wabot.button(Messages.cnclBtn).click();
		assertTrue("testInvalidCachePortDlg", specialChar
				&& alphabets
				&& lessThanRng
				&& grtThanRng
				&& alreadyUsed);
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 17
	public void testInvalidCacheExpPolDlg() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		wabot.button(Messages.roleAddBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		wabot.textWithLabel(Messages.cachNameLbl).setText("abc");
		wabot.textWithLabel(Messages.portNumLbl).setText("1");
		// Policy set to never expires
		wabot.comboBox().setSelection(Messages.expPolNvrExp);
		boolean typeNvrExp1 = wabot.textWithLabel(Messages.minLiveLbl).
				getText().equals(Messages.dlgDbgNA)
				&& !wabot.textWithLabel(Messages.minLiveLbl).isEnabled();
		// Policy set to sliding window
		wabot.comboBox().setSelection(Messages.expPolSlWn);
		boolean typeSldWindow = wabot.textWithLabel(Messages.minLiveLbl).
				getText().equals("")
				&& wabot.textWithLabel(Messages.minLiveLbl).isEnabled();
		wabot.comboBox().setSelection(Messages.expPolNvrExp);
		boolean typeNvrExp2 = wabot.textWithLabel(Messages.minLiveLbl).
				getText().equals(Messages.dlgDbgNA)
				&& !wabot.textWithLabel(Messages.minLiveLbl).isEnabled();
		// Policy set to Absolute
		wabot.comboBox().setSelection(Messages.expPolAbs);
		boolean typeAbsolute = wabot.textWithLabel(Messages.minLiveLbl).
				getText().equals("")
				&& wabot.textWithLabel(Messages.minLiveLbl).isEnabled();
		wabot.button(Messages.cnclBtn).click();
		assertTrue("testInvalidCacheExpPolDlg", typeNvrExp1
				&& typeSldWindow
				&& typeNvrExp2
				&& typeAbsolute);
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 18
	public void testInvalidCacheMtlDlg() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		wabot.button(Messages.roleAddBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		// MTL having special characters
		wabot.textWithLabel(Messages.cachNameLbl).setText("abc");
		wabot.textWithLabel(Messages.portNumLbl).setText("1");
		wabot.textWithLabel(Messages.minLiveLbl).setText("1*$");
		wabot.button("OK").click();
		SWTBotShell errShell = wabot.shell(
				Messages.cachMtlErrTtl).activate();
		boolean specialChar = errShell.getText().
				equals(Messages.cachMtlErrTtl);
		wabot.button("OK").click();
		// MTL having alphabets
		wabot.textWithLabel(Messages.minLiveLbl).setText("1a");
		wabot.button("OK").click();
		errShell = wabot.shell(
				Messages.cachMtlErrTtl).activate();
		boolean alphabets = errShell.getText().
				equals(Messages.cachMtlErrTtl);
		wabot.button("OK").click();
		// port number < 1
		wabot.textWithLabel(Messages.minLiveLbl).setText("0");
		wabot.button("OK").click();
		errShell = wabot.shell(
				Messages.cachMtlErrTtl).activate();
		boolean lessThanRng = errShell.getText().
				equals(Messages.cachMtlErrTtl);
		wabot.button("OK").click();
		wabot.button(Messages.cnclBtn).click();
		assertTrue("testInvalidCacheMtlDlg", specialChar
				&& alphabets
				&& lessThanRng);
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 19
	public void testCacheBackupDlgVMCntLess() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		wabot.button(Messages.roleAddBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		wabot.textWithLabel(Messages.cachNameLbl).setText("abc");
		wabot.textWithLabel(Messages.portNumLbl).setText("1");
		wabot.textWithLabel(Messages.minLiveLbl).setText("1");
		wabot.checkBox().select();
		SWTBotShell backupWarn = wabot.shell(
				Messages.cachErr).activate();
		boolean warning = backupWarn.getText().
				equals(Messages.cachErr);
		wabot.button("OK").click();
		assertTrue("testCacheBackupDlgVMCntLess", warning
				&& !wabot.checkBox().isChecked());
		wabot.button(Messages.cnclBtn).click();
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 20
	public void testCacheBackupDlgVMCnt() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.
				selGeneralPageUsingCnxtMenu(Messages.projCache);
		wabot.textWithLabel(Messages.instance).setText("2");
		SWTBotTree properties = propShell.bot().tree();
		properties.getTreeItem(Messages.roleTreeRoot).expand().
		getNode(Messages.cachePage).select();
		// enable and add cache
		wabot.checkBox().select();
		wabot.button(Messages.roleAddBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		wabot.textWithLabel(Messages.cachNameLbl).setText("abc");
		wabot.textWithLabel(Messages.portNumLbl).setText("1");
		wabot.textWithLabel(Messages.minLiveLbl).setText("1");
		wabot.checkBox().select();
		boolean chkChecked = wabot.checkBox().isChecked();
		wabot.checkBox().click();
		boolean chkUnChecked = !wabot.checkBox().isChecked();
		assertTrue("testCacheBackupDlgVMCnt", chkChecked
				&& chkUnChecked);
		wabot.button(Messages.cnclBtn).click();
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 21
	public void testCacheDlgCancel() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		wabot.button(Messages.roleAddBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		wabot.textWithLabel(Messages.cachNameLbl).setText("abc");
		wabot.textWithLabel(Messages.portNumLbl).setText("1");
		wabot.textWithLabel(Messages.minLiveLbl).setText("1");
		wabot.button(Messages.cnclBtn).click();
		assertTrue("testCacheDlgCancel",
				!wabot.table().containsItem("abc"));
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 22
	public void testCacheDlgExistingName() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		// Add first cache
		wabot.button(Messages.roleAddBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		wabot.textWithLabel(Messages.cachNameLbl).setText("abc");
		wabot.textWithLabel(Messages.portNumLbl).setText("1");
		wabot.textWithLabel(Messages.minLiveLbl).setText("1");
		wabot.button("OK").click();
		// Add second cache
		wabot.button(Messages.roleAddBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		wabot.textWithLabel(Messages.cachNameLbl).setText("abc");
		wabot.textWithLabel(Messages.portNumLbl).setText("2");
		wabot.textWithLabel(Messages.minLiveLbl).setText("2");
		wabot.button("OK").click();
		SWTBotShell errorShell = wabot.shell(
				Messages.cachErr).activate();
		boolean errorCame = errorShell.getText().
				equals(Messages.cachErr);
		wabot.button("OK").click();
		assertTrue("testCacheDlgExistingName", errorCame
				&& wabot.activeShell().
				getText().equals(Messages.cacheTtl));
		wabot.button(Messages.cnclBtn).click();
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 23
	public void testCacheDefaultCacheNotRenamed() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		wabot.table().select(0);
		wabot.button(Messages.roleEditBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		assertTrue("testCacheDefaultCacheNotRenamed",
				!wabot.textWithLabel(Messages.cachNameLbl).isEnabled());
		wabot.button(Messages.cnclBtn).click();
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 24
	public void testCacheDefaultCacheNotRemoved() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		wabot.table().select(0);
		assertTrue("testCacheDefaultCacheNotRemoved",
				!wabot.button(Messages.roleRemBtn).isEnabled());
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 25
	public void testCacheNameInPlaceEdit() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		// Add cache
		wabot.button(Messages.roleAddBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		wabot.textWithLabel(Messages.cachNameLbl).setText("abc");
		wabot.textWithLabel(Messages.portNumLbl).setText("1");
		wabot.textWithLabel(Messages.minLiveLbl).setText("1");
		wabot.button("OK").click();
		// In place edit
		wabot.table().click(0, 0);
		wabot.text("abc", 0).typeText("abc_1");
		wabot.table().click(0, 0);
		boolean valEdited1 = wabot.table().cell(0, 0).equals("abc_1");
		// In place edit
		wabot.table().click(0, 0);
		wabot.text("abc_1", 0).typeText("abc");
		wabot.table().click(0, 0);
		boolean valEdited2 = wabot.table().cell(0, 0).equals("abc");
		assertTrue("testCacheNameInPlaceEdit", valEdited1 && valEdited2);
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 26
	public void testCacheNameInPlaceEditInvalidVal() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		// Add cache
		wabot.button(Messages.roleAddBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		wabot.textWithLabel(Messages.cachNameLbl).setText("abc");
		wabot.textWithLabel(Messages.portNumLbl).setText("1");
		wabot.textWithLabel(Messages.minLiveLbl).setText("1");
		wabot.button("OK").click();
		// In place edit -- having special characters
		wabot.table().click(0, 0);
		wabot.text("abc", 0).typeText("a*bc#");
		// Click on Add button to activate error shell
		wabot.button(Messages.roleAddBtn).click();
		SWTBotShell errorShell = wabot.shell(
				Messages.cachErr).activate();
		boolean specialChar = errorShell.getText().
				equals(Messages.cachErr);
		wabot.button("OK").click();
		// Cancel Add cache shell
		wabot.shell(Messages.cacheTtl).activate();
		wabot.button(Messages.cnclBtn).click();
		// In place edit -- starting with number
		wabot.table().click(0, 0);
		wabot.text("abc", 0).typeText("123abc");
		wabot.button(Messages.roleAddBtn).click();
		errorShell = wabot.shell(
				Messages.cachErr).activate();
		boolean startWithNum = errorShell.getText().
				equals(Messages.cachErr);
		wabot.button("OK").click();
		// Cancel Add cache shell
		wabot.shell(Messages.cacheTtl).activate();
		wabot.button(Messages.cnclBtn).click();
		/*
		 * In place edit, cache name starting with '_' is allowed
		 * as it is allowed to give variable name starting with
		 * '_' in eclipse as well.
		 */
		wabot.table().click(0, 0);
		wabot.text("abc", 0).typeText("_abc");
		wabot.table().select(1);
		assertTrue("testCacheNameInPlaceEditInvalidVal", specialChar
				&& startWithNum
				&& wabot.table().cell(0, 0).equals("_abc"));
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 27
	public void testCacheInPlaceEditBackupVMCntLess() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		// In place edit backup option to Yes
		wabot.table().click(0, 1);
		wabot.ccomboBox().setSelection(0);
		// Click on Add button to activate error shell
		wabot.button(Messages.roleAddBtn).click();
		SWTBotShell backupWarn = wabot.shell(
				Messages.cachErr).activate();
		boolean warning = backupWarn.getText().
				equals(Messages.cachErr);
		wabot.button("OK").click();
		// Cancel Add cache shell
		wabot.shell(Messages.cacheTtl).activate();
		wabot.button(Messages.cnclBtn).click();
		assertTrue("testCacheInPlaceEditBackupVMCntLess", warning
				&& wabot.table().cell(0, 1).equals(Messages.cachBckNo));
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 28
	public void testCacheInPlaceEditBackupVMCntMore() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.
				selGeneralPageUsingCnxtMenu(Messages.projCache);
		wabot.textWithLabel(Messages.instance).setText("2");
		SWTBotTree properties = propShell.bot().tree();
		properties.getTreeItem(Messages.roleTreeRoot).expand().
		getNode(Messages.cachePage).select();
		wabot.checkBox().select();
		// In place edit backup option to Yes
		wabot.table().click(0, 1);
		wabot.ccomboBox().setSelection(0);
		wabot.table().click(0, 0);
		boolean isEditedYes = wabot.table().cell(0, 1).equals(Messages.cachBckYes);
		// In place edit backup option to No
		wabot.table().click(0, 1);
		wabot.ccomboBox().setSelection(1);
		wabot.table().click(0, 0);
		boolean isEditedNo = wabot.table().cell(0, 1).equals(Messages.cachBckNo);
		assertTrue("testCacheInPlaceEditBackupVMCntMore", isEditedYes 
				&& isEditedNo);
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 29
	public void testCacheInPlaceEditExpPol() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		// In place edit Expiration Policy to Never Expires
		wabot.table().click(0, 2);
		wabot.ccomboBox().setSelection(0);
		wabot.table().click(0, 0);
		boolean nvrExp = wabot.table().cell(0, 2).equals(Messages.expPolNvrExp)
				&& wabot.table().cell(0, 3).equals(Messages.dlgDbgNA);
		// In place edit Expiration Policy to Absolute
		wabot.table().click(0, 2);
		wabot.ccomboBox().setSelection(1);
		wabot.table().click(0, 0);
		boolean absolute = wabot.table().cell(0, 2).equals(Messages.expPolAbs)
				&& wabot.table().cell(0, 3).equals("10");
		// change MTL and expiration policy, check value of MTL is preserved
		wabot.table().click(0, 3);
		wabot.text("10", 0).typeText("5");
		wabot.table().click(0, 0);
		wabot.table().click(0, 2);
		wabot.ccomboBox().setSelection(2);
		wabot.table().click(0, 0);
		boolean sliding = wabot.table().cell(0, 2).equals(Messages.expPolSlWn)
				&& wabot.table().cell(0, 3).equals("5");
		// In place edit Expiration Policy to Never Expires
		wabot.table().click(0, 2);
		wabot.ccomboBox().setSelection(0);
		wabot.table().click(0, 0);
		boolean nvrExp1 = wabot.table().cell(0, 2).equals(Messages.expPolNvrExp)
				&& wabot.table().cell(0, 3).equals(Messages.dlgDbgNA);
		// In place edit Expiration Policy to Sliding window
		wabot.table().click(0, 2);
		wabot.ccomboBox().setSelection(2);
		wabot.table().click(0, 0);
		boolean sliding1 = wabot.table().cell(0, 2).equals(Messages.expPolSlWn)
				&& wabot.table().cell(0, 3).equals("10");
		// change MTL and expiration policy, check value of MTL is preserved
		wabot.table().click(0, 3);
		wabot.text("10", 0).typeText("5");
		wabot.table().click(0, 0);
		wabot.table().click(0, 2);
		wabot.ccomboBox().setSelection(1);
		wabot.table().click(0, 0);
		boolean absolute1 = wabot.table().cell(0, 2).equals(Messages.expPolAbs)
				&& wabot.table().cell(0, 3).equals("5");
		assertTrue("testCacheInPlaceEditExpPol", nvrExp
				&& nvrExp1 && absolute && absolute1 && sliding && sliding1);
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 30
	public void testCacheInPlaceEditMtl() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		wabot.table().click(0, 3);
		wabot.text("10", 0).typeText("5");
		wabot.table().click(0, 0);
		assertTrue("testCacheInPlaceEditMtl",
				wabot.table().cell(0, 3).equals("5"));
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 31
	public void testCacheInPlaceEditInvalidMtl() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		wabot.table().click(0, 3);
		// MTL having Special characters
		wabot.text("10", 0).typeText("1*$");
		wabot.button(Messages.roleAddBtn).click();
		SWTBotShell errorShell = wabot.shell(
				Messages.cachMtlErrTtl).activate();
		boolean specialChar = errorShell.getText().
				equals(Messages.cachMtlErrTtl);
		wabot.button("OK").click();
		// Cancel Add cache shell
		wabot.shell(Messages.cacheTtl).activate();
		wabot.button(Messages.cnclBtn).click();
		// MTL having alphabets
		wabot.table().click(0, 3);
		wabot.text("10", 0).typeText("1a");
		wabot.button(Messages.roleAddBtn).click();
		errorShell = wabot.shell(
				Messages.cachMtlErrTtl).activate();
		boolean alphabets  = errorShell.getText().
				equals(Messages.cachMtlErrTtl);
		wabot.button("OK").click();
		// Cancel Add cache shell
		wabot.shell(Messages.cacheTtl).activate();
		wabot.button(Messages.cnclBtn).click();
		// port number < 1
		wabot.table().click(0, 3);
		wabot.text("10", 0).typeText("0");
		wabot.button(Messages.roleAddBtn).click();
		errorShell = wabot.shell(
				Messages.cachMtlErrTtl).activate();
		boolean lessThanRng   = errorShell.getText().
				equals(Messages.cachMtlErrTtl);
		wabot.button("OK").click();
		// Cancel Add cache shell
		wabot.shell(Messages.cacheTtl).activate();
		wabot.button(Messages.cnclBtn).click();
		assertTrue("testCacheInPlaceEditInvalidMtl", specialChar
				&& alphabets
				&& lessThanRng);
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 32
	public void testCacheInPlaceEditPort() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		wabot.table().click(0, 4);
		wabot.text("11211", 0).typeText("1");
		wabot.table().click(0, 0);
		boolean isEdited = wabot.table().cell(0, 4).equals("1");
		SWTBotTree properties = propShell.bot().tree();
		properties.getTreeItem(Messages.roleTreeRoot).expand().
		getNode(Messages.endptPage).select();
		boolean endPtEdited = wabot.table().
				cell(0, 0).equals(String.format("%s%s",
						Messages.cachEndPtName, Messages.dfltCachName))
						&& wabot.table().cell(0, 1).
						equals(Messages.typeIntrnl)
						&& wabot.table().cell(0, 3).equals("1");
		assertTrue("testCacheInPlaceEditPort", isEdited
				&& endPtEdited);
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 33
	public void testCacheInPlaceEditInvalidPort() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projCache,
						Messages.role1, Messages.endptPage);
		wabot.button(Messages.roleAddBtn).click();
		Utility.addEp(Messages.intEndPt, Messages.typeInpt, "80", "8080");
		Utility.selPageNode(Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		// port having special characters
		wabot.table().click(0, 4);
		wabot.text("11211", 0).typeText("-1");
		wabot.button(Messages.roleAddBtn).click();
		SWTBotShell errorShell = wabot.shell(
				Messages.cachPortErrTtl).activate();
		boolean specialChar = errorShell.getText().
				equals(Messages.cachPortErrTtl);
		wabot.button("OK").click();
		// Cancel Add cache shell
		wabot.shell(Messages.cacheTtl).activate();
		wabot.button(Messages.cnclBtn).click();
		// port having alphabets
		wabot.table().click(0, 4);
		wabot.text("11211", 0).typeText("1a");
		wabot.button(Messages.roleAddBtn).click();
		errorShell = wabot.shell(
				Messages.cachPortErrTtl).activate();
		boolean alphabets = errorShell.getText().
				equals(Messages.cachPortErrTtl);
		wabot.button("OK").click();
		// Cancel Add cache shell
		wabot.shell(Messages.cacheTtl).activate();
		wabot.button(Messages.cnclBtn).click();
		// port number < 1
		wabot.table().click(0, 4);
		wabot.text("11211", 0).typeText("0");
		wabot.button(Messages.roleAddBtn).click();
		errorShell = wabot.shell(
				Messages.cachPortErrTtl).activate();
		boolean lessThanRng = errorShell.getText().
				equals(Messages.cachPortErrTtl);
		wabot.button("OK").click();
		// Cancel Add cache shell
		wabot.shell(Messages.cacheTtl).activate();
		wabot.button(Messages.cnclBtn).click();
		// port number > 65535
		wabot.table().click(0, 4);
		wabot.text("11211", 0).typeText("65536");
		wabot.button(Messages.roleAddBtn).click();
		errorShell = wabot.shell(
				Messages.cachPortErrTtl).activate();
		boolean grtThanRng = errorShell.getText().
				equals(Messages.cachPortErrTtl);
		wabot.button("OK").click();
		// Cancel Add cache shell
		wabot.shell(Messages.cacheTtl).activate();
		wabot.button(Messages.cnclBtn).click();
		// Existing port number is used
		wabot.table().click(0, 4);
		wabot.text("11211", 0).typeText("8080");
		wabot.button(Messages.roleAddBtn).click();
		errorShell = wabot.shell(
				Messages.cachPortErrTtl).activate();
		boolean alreadyUsed = errorShell.getText().
				equals(Messages.cachPortErrTtl);
		wabot.button("OK").click();
		// Cancel Add cache shell
		wabot.shell(Messages.cacheTtl).activate();
		wabot.button(Messages.cnclBtn).click();
		assertTrue("testCacheInPlaceEditInvalidPort", specialChar
				&& alphabets
				&& lessThanRng
				&& grtThanRng
				&& alreadyUsed);
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 34
	public void testCachePortExistingDlg() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.
				selectPageUsingContextMenu(Messages.projCache,
						Messages.role1, Messages.endptPage);
		wabot.button(Messages.roleAddBtn).click();
		Utility.addEp(Messages.intEndPt, Messages.typeInpt, "80", "8080");
		Utility.selPageNode(Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		// Add first cache
		wabot.button(Messages.roleAddBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		wabot.textWithLabel(Messages.cachNameLbl).setText("abc");
		wabot.textWithLabel(Messages.portNumLbl).setText("1");
		wabot.textWithLabel(Messages.minLiveLbl).setText("10");
		wabot.button("OK").click();
		// Add second cache with same port of that of previously added cache port
		wabot.button(Messages.roleAddBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		wabot.textWithLabel(Messages.cachNameLbl).setText("pqr");
		wabot.textWithLabel(Messages.portNumLbl).setText("1");
		wabot.textWithLabel(Messages.minLiveLbl).setText("10");
		wabot.button("OK").click();
		SWTBotShell errShell = wabot.shell(
				Messages.cachPortErrTtl).activate();
		boolean samePort = errShell.getText().
				equals(Messages.cachPortErrTtl);
		wabot.button("OK").click();
		wabot.textWithLabel(Messages.portNumLbl).setText("8080");
		wabot.button("OK").click();
		errShell = wabot.shell(
				Messages.cachPortErrTtl).activate();
		boolean samePort1 = errShell.getText().
				equals(Messages.cachPortErrTtl);
		wabot.button("OK").click();
		assertTrue("testCachePortExistingDlg", samePort && samePort1);
		wabot.button(Messages.cnclBtn).click();
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 35
	public void testCacheDlgEmptyTextBoxes() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		wabot.button(Messages.roleAddBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		// Keep name empty
		wabot.textWithLabel(Messages.portNumLbl).setText("1");
		wabot.textWithLabel(Messages.minLiveLbl).setText("10");
		wabot.button("OK").click();
		SWTBotShell errShell = wabot.shell(
				Messages.cachErrTtl).activate();
		boolean emptyName = errShell.getText().
				equals(Messages.cachErrTtl);
		wabot.button("OK").click();
		// Keep port empty
		wabot.textWithLabel(Messages.cachNameLbl).setText("abc");
		wabot.textWithLabel(Messages.portNumLbl).setText("");
		wabot.button("OK").click();
		errShell = wabot.shell(
				Messages.cachErrTtl).activate();
		boolean emptyPort = errShell.getText().
				equals(Messages.cachErrTtl);
		wabot.button("OK").click();
		// Keep mtl empty
		wabot.textWithLabel(Messages.portNumLbl).setText("1");
		wabot.textWithLabel(Messages.minLiveLbl).setText("");
		wabot.button("OK").click();
		errShell = wabot.shell(
				Messages.cachErrTtl).activate();
		boolean emptyMtl = errShell.getText().
				equals(Messages.cachErrTtl);
		wabot.button("OK").click();
		wabot.button(Messages.cnclBtn).click();
		assertTrue("testCacheDlgEmptyTextBoxes", emptyName
				&& emptyPort
				&& emptyMtl);
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 36
	public void testCacheDlgEditName() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		// Add cache
		wabot.button(Messages.roleAddBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		wabot.textWithLabel(Messages.cachNameLbl).setText("abc");
		wabot.textWithLabel(Messages.portNumLbl).setText("1");
		wabot.textWithLabel(Messages.minLiveLbl).setText("10");
		wabot.button("OK").click();
		// Edit cache
		wabot.table().select(0);
		wabot.button(Messages.roleEditBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		wabot.textWithLabel(Messages.cachNameLbl).setText("abc_1");
		wabot.button("OK").click();
		assertTrue("testCacheDlgEditName",
				wabot.table().cell(0, 0).equals("abc_1"));
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 37
	public void testCacheDlgEditBackup() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.
				selGeneralPageUsingCnxtMenu(Messages.projCache);
		wabot.textWithLabel(Messages.instance).setText("2");
		SWTBotTree properties = propShell.bot().tree();
		properties.getTreeItem(Messages.roleTreeRoot).expand().
		getNode(Messages.cachePage).select();
		// enable and add cache
		wabot.checkBox().select();
		boolean isNo = wabot.table().cell(0, 1).equals(Messages.cachBckNo);
		// Edit cache
		wabot.table().select(0);
		wabot.button(Messages.roleEditBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		wabot.checkBox().select();
		wabot.button("OK").click();
		boolean isYes = wabot.table().cell(0, 1).equals(Messages.cachBckYes);
		assertTrue("testCacheDlgEditBackup", isNo && isYes);
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 38
	public void testCacheDlgEditPort() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		// Edit cache
		wabot.table().select(0);
		wabot.button(Messages.roleEditBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		wabot.textWithLabel(Messages.portNumLbl).setText("1");
		wabot.button("OK").click();
		boolean portEdited = wabot.table().cell(0, 4).equals("1");
		SWTBotTree properties = propShell.bot().tree();
		properties.getTreeItem(Messages.roleTreeRoot).expand().
		getNode(Messages.endptPage).select();
		boolean endPtPresent = wabot.table().
				cell(0, 0).equals(String.format("%s%s",
						Messages.cachEndPtName, Messages.dfltCachName))
						&& wabot.table().cell(0, 3).equals("1");
		assertTrue("testCacheDlgEditPort", portEdited && endPtPresent);
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 39
	public void testCacheDlgEditExpPol() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		// Edit cache
		wabot.table().select(0);
		wabot.button(Messages.roleEditBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		wabot.comboBox().setSelection(0);
		wabot.button("OK").click();
		boolean expEdited = wabot.table().cell(0, 2).equals(Messages.expPolNvrExp)
				&& wabot.table().cell(0, 3).equals(Messages.dlgDbgNA);
		assertTrue("testCacheDlgEditExpPol", expEdited);
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 40
	public void testCacheDlgEditMtl() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		// Edit cache
		wabot.table().select(0);
		wabot.button(Messages.roleEditBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		wabot.textWithLabel(Messages.minLiveLbl).setText("5");
		wabot.button("OK").click();
		assertTrue("testCacheDlgEditMtl",
				wabot.table().cell(0, 3).equals("5"));
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 41
	public void testCacheTableRemoveYes() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		// Add cache
		wabot.button(Messages.roleAddBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		wabot.textWithLabel(Messages.cachNameLbl).setText("abc");
		wabot.textWithLabel(Messages.portNumLbl).setText("1");
		wabot.textWithLabel(Messages.minLiveLbl).setText("10");
		wabot.button("OK").click();
		wabot.table().select(0);
		wabot.button(Messages.roleRemBtn).click();
		wabot.shell(Messages.cachRmvTtl).activate();
		wabot.button(Messages.cachBckYes).click();
		boolean cachRemoved = !wabot.table().containsItem("abc");
		Utility.selPageNode(Messages.role1, Messages.endptPage);
		assertTrue("testCacheTableRemove", cachRemoved
				&& !wabot.table().containsItem(
						String.format("%s%s",
								Messages.cachEndPtName, "abc")));
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 42
	public void testCacheEndPtEditRemove() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		Utility.selPageNode(Messages.role1, Messages.endptPage);
		// select caching end point
		wabot.table().select(0);
		wabot.button(Messages.roleEditBtn).click();
		SWTBotShell warnShell = wabot.shell(
				Messages.cachDsblErTtl).activate();
		boolean warnCameEdt = warnShell.getText().
				equals(Messages.cachDsblErTtl);
		wabot.button("OK").click();
		wabot.table().select(0);
		wabot.button(Messages.roleRemBtn).click();
		warnShell = wabot.shell(
				Messages.cachDsblErTtl).activate();
		boolean warnCameRmv = warnShell.getText().
				equals(Messages.cachDsblErTtl);
		wabot.button("OK").click();
		assertTrue("testCacheEndPtEditRemove", warnCameEdt
				&& warnCameRmv
				&& wabot.table().containsItem(
						String.format("%s%s",
								Messages.cachEndPtName,
								Messages.dfltCachName)));
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 44
	public void testCacheLclStrEditRemove() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		Utility.selPageNode(Messages.role1, Messages.lclStrPage);
		// select caching local storage
		wabot.table().select(0);
		wabot.button(Messages.roleEditBtn).click();
		SWTBotShell warnShell = wabot.shell(
				Messages.cachDsblErTtl).activate();
		boolean warnCameEdt = warnShell.getText().
				equals(Messages.cachDsblErTtl);
		wabot.button("OK").click();
		wabot.table().select(0);
		wabot.button(Messages.roleRemBtn).click();
		warnShell = wabot.shell(
				Messages.cachDsblErTtl).activate();
		boolean warnCameRmv = warnShell.getText().
				equals(Messages.cachDsblErTtl);
		wabot.button("OK").click();
		assertTrue("testCacheLclStrEditRemove", warnCameEdt
				&& warnCameRmv
				&& wabot.table().containsItem(Messages.cachLclStr));
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 46
	public void testCacheHostName() throws Exception {
		Utility.createProject(Messages.projCache);
		// Select project and property page
		Utility.getPropertyPage(Messages.projCache, Messages.rolesPage);
		//Add WorkerRole2
		wabot.button(Messages.roleAddBtn).click();
		wabot.activeShell().activate();
		SWTBotTree properties = wabot.activeShell().bot().tree();
		properties.getTreeItem(Messages.roleTreeRoot).expand()
		.getNode(Messages.cachePage).select();
		wabot.checkBox().select();
		boolean hostName2 = wabot.textWithLabel(Messages.hostLbl).
				getText().equals(
						String.format("%s%s",Messages.hostNm,
								Messages.role2.toLowerCase()));
		wabot.button("OK").click();
		wabot.table().select(0);
		wabot.button(Messages.roleEditBtn).click();
		properties = wabot.activeShell().bot().tree();
		properties.getTreeItem(Messages.roleTreeRoot).expand()
		.getNode(Messages.cachePage).select();
		wabot.checkBox().select();
		boolean hostName1 = wabot.textWithLabel(Messages.hostLbl).
				getText().equals(
						String.format("%s%s",Messages.hostNm,
								Messages.role1.toLowerCase()));
		assertTrue("testCacheHostName", hostName1
				&& hostName2);
		wabot.button("OK").click();
		wabot.button("OK").click();
	}
	
	@Test
	// (Test Cases for 1.8) test case 63
	public void testCacheScaleMin() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		wabot.scale().setValue(wabot.scale().getMinimum());
		assertTrue("testCacheScaleMin", wabot.textWithLabel(
				Messages.cachScaleLbl).getText().equals(String.format("%s%s",
						wabot.scale().getMinimum(), "%")));
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 64
	public void testCacheScaleMaxAndIntermediate() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		wabot.scale().setValue(wabot.scale().getMaximum());
		boolean maxVal = wabot.textWithLabel(
				Messages.cachScaleLbl).getText().equals(String.format("%s%s",
						wabot.scale().getMaximum(), "%"));
		wabot.scale().setValue(46);
		boolean interVal = wabot.textWithLabel(
				Messages.cachScaleLbl).getText().equals("46%");
		assertTrue("testCacheScaleMaxAndIntermediate", maxVal && interVal);
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 65
	public void testRoleVMCntReduceBackupYes() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.
				selGeneralPageUsingCnxtMenu(Messages.projCache);
		wabot.textWithLabel(Messages.instance).setText("2");
		SWTBotTree properties = propShell.bot().tree();
		properties.getTreeItem(Messages.roleTreeRoot).expand().
		getNode(Messages.cachePage).select();
		// enable and add cache
		wabot.checkBox().select();
		wabot.button(Messages.roleAddBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		wabot.textWithLabel(Messages.cachNameLbl).setText("abc");
		wabot.textWithLabel(Messages.portNumLbl).setText("1");
		wabot.textWithLabel(Messages.minLiveLbl).setText("1");
		wabot.checkBox().select();
		wabot.button("OK").click();
		// Go to Role property page
		properties.getTreeItem(Messages.roleTreeRoot).select();
		wabot.textWithLabel(Messages.instance).setText("1");
		// OKPressed
		wabot.button("OK").click();
		SWTBotShell backupWarn = wabot.shell(
				Messages.highAvailTtl).activate();
		boolean errorOK = backupWarn.getText().
				equals(Messages.highAvailTtl);
		wabot.button(Messages.cnclBtn).click();
		// OKToLeave
		properties.getTreeItem(Messages.roleTreeRoot).expand().
		getNode(Messages.endptPage).select();
		backupWarn = wabot.shell(
				Messages.highAvailTtl).activate();
		boolean errorOKToLeave = backupWarn.getText().
				equals(Messages.highAvailTtl);
		wabot.button(Messages.cnclBtn).click();
		wabot.shell(String.format(
				"%s%s", Messages.okToLeaveTtl, " ")).activate();
		wabot.button("OK").click();
		assertTrue("testRoleVMCntReduceBackupYes", errorOK
				&& errorOKToLeave);
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 66
	public void testRoleVMCntReduceBackupNo() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		SWTBotTree properties = propShell.bot().tree();
		properties.getTreeItem(Messages.roleTreeRoot).select();
		// Default cache with backup option disabled and VMCount = 1
		// Try OKToLeave
		properties.getTreeItem(Messages.roleTreeRoot).expand().
		getNode(Messages.endptPage).select();
		properties.getTreeItem(Messages.roleTreeRoot).select();
		// OKPressed
		wabot.button("OK").click();
		SWTBotView view = Utility.getProjExplorer();
		assertTrue("testRoleVMCntReduceBackupNo", view.bot().tree().isActive());
	}
	
	@Test
	// (Test Cases for 1.8) test case 67
	public void testRoleVMCntReduceBackupYesCancel() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.
				selGeneralPageUsingCnxtMenu(Messages.projCache);
		wabot.textWithLabel(Messages.instance).setText("2");
		SWTBotTree properties = propShell.bot().tree();
		properties.getTreeItem(Messages.roleTreeRoot).expand().
		getNode(Messages.cachePage).select();
		// enable and add cache
		wabot.checkBox().select();
		wabot.button(Messages.roleAddBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		wabot.textWithLabel(Messages.cachNameLbl).setText("abc");
		wabot.textWithLabel(Messages.portNumLbl).setText("1");
		wabot.textWithLabel(Messages.minLiveLbl).setText("1");
		wabot.checkBox().select();
		wabot.button("OK").click();
		// Go to Role property page
		properties.getTreeItem(Messages.roleTreeRoot).select();
		wabot.textWithLabel(Messages.instance).setText("1");
		// OKPressed
		wabot.button("OK").click();
		wabot.shell(
				Messages.highAvailTtl).activate();
		wabot.button(Messages.cnclBtn).click();
		boolean onSamePage = wabot.activeShell().
				getText().equals(String.format("%s%s%s",
						Messages.propPageTtl, " ", Messages.role1));
		// OKToLeave
		properties.getTreeItem(Messages.roleTreeRoot).expand().
		getNode(Messages.endptPage).select();
		wabot.shell(Messages.highAvailTtl).activate();
		wabot.button(Messages.cnclBtn).click();
		wabot.shell(String.format(
				"%s%s", Messages.okToLeaveTtl, " ")).activate();
		wabot.button("OK").click();
		boolean onSamePage1 = wabot.activeShell().
				getText().equals(String.format("%s%s%s",
						Messages.propPageTtl, " ", Messages.role1));
		assertTrue("testRoleVMCntReduceBackupYesCancel", onSamePage
				&& onSamePage1);
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 68
	public void testRoleVMCntReduceBackupYesOK() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.
				selGeneralPageUsingCnxtMenu(Messages.projCache);
		wabot.textWithLabel(Messages.instance).setText("2");
		SWTBotTree properties = propShell.bot().tree();
		properties.getTreeItem(Messages.roleTreeRoot).expand().
		getNode(Messages.cachePage).select();
		// enable and add cache
		wabot.checkBox().select();
		// Add first cache
		wabot.button(Messages.roleAddBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		wabot.textWithLabel(Messages.cachNameLbl).setText("abc");
		wabot.textWithLabel(Messages.portNumLbl).setText("1");
		wabot.textWithLabel(Messages.minLiveLbl).setText("1");
		wabot.checkBox().select();
		wabot.button("OK").click();
		// Add second cache
		wabot.button(Messages.roleAddBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		wabot.textWithLabel(Messages.cachNameLbl).setText("pqr");
		wabot.textWithLabel(Messages.portNumLbl).setText("2");
		wabot.textWithLabel(Messages.minLiveLbl).setText("2");
		wabot.checkBox().select();
		wabot.button("OK").click();
		boolean setToYes = wabot.table().cell(0, 0).equals("abc")
				&& wabot.table().cell(0, 1).equals(Messages.cachBckYes)
				&& wabot.table().cell(2, 0).equals("pqr")
				&& wabot.table().cell(2, 1).equals(Messages.cachBckYes);
		// Go to Role property page
		properties.getTreeItem(Messages.roleTreeRoot).select();
		wabot.textWithLabel(Messages.instance).setText("1");
		properties.getTreeItem(Messages.roleTreeRoot).expand().
		getNode(Messages.cachePage).select();
		wabot.shell(Messages.highAvailTtl).activate();
		wabot.button("OK").click();
		boolean setToNo = wabot.table().cell(0, 0).equals("abc")
				&& wabot.table().cell(0, 1).equals(Messages.cachBckNo)
				&& wabot.table().cell(2, 0).equals("pqr")
				&& wabot.table().cell(2, 1).equals(Messages.cachBckNo);
		assertTrue("testRoleVMCntReduceBackupYesOK", setToYes && setToNo);
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 69
	public void testRoleVMCntReduceBackupYesOKPressed() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.
				selGeneralPageUsingCnxtMenu(Messages.projCache);
		wabot.textWithLabel(Messages.instance).setText("2");
		SWTBotTree properties = propShell.bot().tree();
		properties.getTreeItem(Messages.roleTreeRoot).expand().
		getNode(Messages.cachePage).select();
		// enable and add cache
		wabot.checkBox().select();
		// Add first cache
		wabot.button(Messages.roleAddBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		wabot.textWithLabel(Messages.cachNameLbl).setText("abc");
		wabot.textWithLabel(Messages.portNumLbl).setText("1");
		wabot.textWithLabel(Messages.minLiveLbl).setText("1");
		wabot.checkBox().select();
		wabot.button("OK").click();
		// Add second cache
		wabot.button(Messages.roleAddBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		wabot.textWithLabel(Messages.cachNameLbl).setText("pqr");
		wabot.textWithLabel(Messages.portNumLbl).setText("2");
		wabot.textWithLabel(Messages.minLiveLbl).setText("2");
		wabot.checkBox().select();
		wabot.button("OK").click();
		boolean setToYes = wabot.table().cell(0, 0).equals("abc")
				&& wabot.table().cell(0, 1).equals(Messages.cachBckYes)
				&& wabot.table().cell(2, 0).equals("pqr")
				&& wabot.table().cell(2, 1).equals(Messages.cachBckYes);
		// Go to Role property page
		properties.getTreeItem(Messages.roleTreeRoot).select();
		wabot.textWithLabel(Messages.instance).setText("1");
		wabot.button("OK").click();
		wabot.shell(Messages.highAvailTtl).activate();
		wabot.button("OK").click();
		// Again open caching page
		propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		boolean setToNo = wabot.table().cell(0, 0).equals("abc")
				&& wabot.table().cell(0, 1).equals(Messages.cachBckNo)
				&& wabot.table().cell(2, 0).equals("pqr")
				&& wabot.table().cell(2, 1).equals(Messages.cachBckNo);
		assertTrue("testRoleVMCntReduceBackupYesOKPressed", setToYes && setToNo);
		propShell.close();
	}
	
	@Test
	// (Test Cases for 1.8) test case 70
	public void testCacheTableRemoveNo() throws Exception {
		Utility.createProject(Messages.projCache);
		SWTBotShell propShell = Utility.selectPageUsingContextMenu(
				Messages.projCache,
				Messages.role1, Messages.cachePage);
		wabot.checkBox().select();
		// Add cache
		wabot.button(Messages.roleAddBtn).click();
		wabot.shell(Messages.cacheTtl).activate();
		wabot.textWithLabel(Messages.cachNameLbl).setText("abc");
		wabot.textWithLabel(Messages.portNumLbl).setText("1");
		wabot.textWithLabel(Messages.minLiveLbl).setText("10");
		wabot.button("OK").click();
		wabot.table().select(0);
		wabot.button(Messages.roleRemBtn).click();
		wabot.shell(Messages.cachRmvTtl).activate();
		wabot.button(Messages.cachBckNo).click();
		boolean cachNotRemoved = wabot.table().containsItem("abc");
		Utility.selPageNode(Messages.role1, Messages.endptPage);
		assertTrue("testCacheTableRemoveNo", cachNotRemoved
				&& wabot.table().containsItem(
						String.format("%s%s",
								Messages.cachEndPtName, "abc")));
		propShell.close();
	}
}
