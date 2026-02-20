package com.example.tests.ui.form;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PracticeFormHobbiesTest extends BasePracticeFormTest {

    @Test(description = "Should check Sports hobby checkbox")
    public void testCheckSportsHobby() {
        driver.findElement(By.cssSelector("label[data-cy='hobby-sports-label']")).click();
        Assert.assertTrue(driver.findElement(By.id("hobbies-checkbox-1")).isSelected(),
                "Sports checkbox should be checked");
    }

    @Test(description = "Should check multiple hobby checkboxes independently")
    public void testCheckMultipleHobbies() {
        driver.findElement(By.cssSelector("label[data-cy='hobby-sports-label']")).click();
        driver.findElement(By.cssSelector("label[data-cy='hobby-reading-label']")).click();

        Assert.assertTrue(driver.findElement(By.id("hobbies-checkbox-1")).isSelected(), "Sports should be checked");
        Assert.assertTrue(driver.findElement(By.id("hobbies-checkbox-2")).isSelected(), "Reading should be checked");
        Assert.assertFalse(driver.findElement(By.id("hobbies-checkbox-3")).isSelected(), "Music should remain unchecked");
    }

    @Test(description = "Should uncheck a previously checked hobby")
    public void testUncheckHobby() {
        driver.findElement(By.cssSelector("label[data-cy='hobby-reading-label']")).click();
        Assert.assertTrue(driver.findElement(By.id("hobbies-checkbox-2")).isSelected());

        driver.findElement(By.cssSelector("label[data-cy='hobby-reading-label']")).click();
        Assert.assertFalse(driver.findElement(By.id("hobbies-checkbox-2")).isSelected(),
                "Reading should be unchecked after clicking again");
    }
}
