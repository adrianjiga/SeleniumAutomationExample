package com.example.tests.ui.form;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PracticeFormGenderTest extends BasePracticeFormTest {

    @Test(description = "Should select Male gender radio button")
    public void testSelectMaleGender() {
        formPage.selectGender(1);
        Assert.assertTrue(formPage.isGenderSelected(1), "Male radio should be selected");
        Assert.assertFalse(formPage.isGenderSelected(2), "Female radio should not be selected");
    }

    @Test(description = "Should select Female gender radio button")
    public void testSelectFemaleGender() {
        formPage.selectGender(2);
        Assert.assertTrue(formPage.isGenderSelected(2), "Female radio should be selected");
        Assert.assertFalse(formPage.isGenderSelected(1), "Male radio should not be selected");
    }

    @Test(description = "Should switch gender selection when a different radio is clicked")
    public void testGenderRadioSwitching() {
        formPage.selectGender(1);
        Assert.assertTrue(formPage.isGenderSelected(1));

        formPage.selectGender(3);
        Assert.assertTrue(formPage.isGenderSelected(3));
        Assert.assertFalse(formPage.isGenderSelected(1));
    }
}
