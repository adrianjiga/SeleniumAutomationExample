package com.example.tests.ui.form;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PracticeFormHobbiesTest extends BasePracticeFormTest {

    @Test(description = "Should check Sports hobby checkbox")
    public void testCheckSportsHobby() {
        formPage.checkHobby("hobby-sports-label");
        Assert.assertTrue(formPage.isHobbyChecked(1), "Sports checkbox should be checked");
    }

    @Test(description = "Should check multiple hobby checkboxes independently")
    public void testCheckMultipleHobbies() {
        formPage.checkHobby("hobby-sports-label");
        formPage.checkHobby("hobby-reading-label");

        Assert.assertTrue(formPage.isHobbyChecked(1), "Sports should be checked");
        Assert.assertTrue(formPage.isHobbyChecked(2), "Reading should be checked");
        Assert.assertFalse(formPage.isHobbyChecked(3), "Music should remain unchecked");
    }

    @Test(description = "Should uncheck a previously checked hobby")
    public void testUncheckHobby() {
        formPage.checkHobby("hobby-reading-label");
        Assert.assertTrue(formPage.isHobbyChecked(2));

        formPage.checkHobby("hobby-reading-label");
        Assert.assertFalse(formPage.isHobbyChecked(2),
                "Reading should be unchecked after clicking again");
    }
}
