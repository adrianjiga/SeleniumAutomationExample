package com.example.tests.ui.form;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PracticeFormLocationTest extends BasePracticeFormTest {

    @Test(description = "Should populate city dropdown after selecting a country")
    public void testCitiesLoadAfterCountrySelection() {
        selectCountry("Germany");

        // Open the city dropdown so the menu becomes visible — getText() returns "" on hidden elements
        driver.findElement(By.cssSelector("#city .select-control")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("city-menu")));

        boolean berlinVisible = driver.findElements(By.cssSelector("#city-menu .select-option"))
                .stream().anyMatch(el -> el.getText().equals("Berlin"));
        Assert.assertTrue(berlinVisible, "Berlin should be available in city dropdown after selecting Germany");
    }

    @Test(description = "Should select a city after selecting a country")
    public void testSelectCountryAndCity() {
        selectCountry("France");
        selectCity("Paris");
        Assert.assertEquals(driver.findElement(By.id("city-display")).getText(), "Paris",
                "City display should show selected city");
    }
}
