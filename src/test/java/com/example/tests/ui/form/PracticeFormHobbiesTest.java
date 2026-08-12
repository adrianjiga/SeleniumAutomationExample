package com.example.tests.ui.form;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class PracticeFormHobbiesTest extends BasePracticeFormTest {

    @Test(description = "Should check Sports hobby checkbox")
    public void testCheckSportsHobby() {
        formPage.checkHobby("hobbySportsLabel");
        Assert.assertTrue(formPage.isHobbyChecked(1), "Sports checkbox should be checked");
    }

    @Test(description = "Should check multiple hobby checkboxes independently")
    public void testCheckMultipleHobbies() {
        formPage.checkHobby("hobbySportsLabel");
        formPage.checkHobby("hobbyReadingLabel");

        SoftAssert soft = new SoftAssert();
        soft.assertTrue(formPage.isHobbyChecked(1), "Sports should be checked");
        soft.assertTrue(formPage.isHobbyChecked(2), "Reading should be checked");
        soft.assertFalse(formPage.isHobbyChecked(3), "Music should remain unchecked");
        soft.assertAll();
    }

    @Test(description = "Should uncheck a previously checked hobby")
    public void testUncheckHobby() {
        formPage.checkHobby("hobbyReadingLabel");
        Assert.assertTrue(formPage.isHobbyChecked(2));

        formPage.checkHobby("hobbyReadingLabel");
        Assert.assertFalse(formPage.isHobbyChecked(2),
                "Reading should be unchecked after clicking again");
    }
}
