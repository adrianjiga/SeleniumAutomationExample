package com.example.tests.ui.form;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PracticeFormGenderTest extends BasePracticeFormTest {

    @Test(description = "Should select Male gender radio button")
    public void testSelectMaleGender() {
        driver.findElement(By.cssSelector("label[for='gender-radio-1']")).click();
        Assert.assertTrue(driver.findElement(By.id("gender-radio-1")).isSelected(), "Male radio should be selected");
        Assert.assertFalse(driver.findElement(By.id("gender-radio-2")).isSelected(), "Female radio should not be selected");
    }

    @Test(description = "Should select Female gender radio button")
    public void testSelectFemaleGender() {
        driver.findElement(By.cssSelector("label[for='gender-radio-2']")).click();
        Assert.assertTrue(driver.findElement(By.id("gender-radio-2")).isSelected(), "Female radio should be selected");
        Assert.assertFalse(driver.findElement(By.id("gender-radio-1")).isSelected(), "Male radio should not be selected");
    }

    @Test(description = "Should switch gender selection when a different radio is clicked")
    public void testGenderRadioSwitching() {
        driver.findElement(By.cssSelector("label[for='gender-radio-1']")).click();
        Assert.assertTrue(driver.findElement(By.id("gender-radio-1")).isSelected());

        driver.findElement(By.cssSelector("label[for='gender-radio-3']")).click();
        Assert.assertTrue(driver.findElement(By.id("gender-radio-3")).isSelected());
        Assert.assertFalse(driver.findElement(By.id("gender-radio-1")).isSelected());
    }
}
