package com.example.tests.ui.form;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class PracticeFormGenderTest extends BasePracticeFormTest {

    @DataProvider(name = "genderSelections")
    public Object[][] genderSelections() {
        // {radioToSelect, radioToCheckIsDeselected}
        return new Object[][] {
            {1, 2},   // Male selected → Female deselected
            {2, 1},   // Female selected → Male deselected
            {3, 1},   // Other selected → Male deselected
        };
    }

    @Test(dataProvider = "genderSelections",
          description = "Should select the correct gender radio and deselect the other")
    public void testGenderRadioSelection(int radioToSelect, int otherRadio) {
        formPage.selectGender(radioToSelect);

        SoftAssert soft = new SoftAssert();
        soft.assertTrue(formPage.isGenderSelected(radioToSelect),
                "Radio " + radioToSelect + " should be selected");
        soft.assertFalse(formPage.isGenderSelected(otherRadio),
                "Radio " + otherRadio + " should not be selected");
        soft.assertAll();
    }

    @Test(description = "Should switch gender selection when a different radio is clicked")
    public void testGenderRadioSwitching() {
        formPage.selectGender(1);
        Assert.assertTrue(formPage.isGenderSelected(1));

        formPage.selectGender(3);

        SoftAssert soft = new SoftAssert();
        soft.assertTrue(formPage.isGenderSelected(3));
        soft.assertFalse(formPage.isGenderSelected(1));
        soft.assertAll();
    }
}
