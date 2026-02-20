package com.example.tests.ui.form;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PracticeFormLocationTest extends BasePracticeFormTest {

    @Test(description = "Should populate city dropdown after selecting a country")
    public void testCitiesLoadAfterCountrySelection() {
        formPage.selectCountry("Germany");
        formPage.openCityDropdown();

        Assert.assertTrue(formPage.getCityOptions().contains("Berlin"),
                "Berlin should be available in city dropdown after selecting Germany");
    }

    @Test(description = "Should select a city after selecting a country")
    public void testSelectCountryAndCity() {
        formPage.selectCountry("France");
        formPage.selectCity("Paris");
        Assert.assertEquals(formPage.getCityDisplay(), "Paris",
                "City display should show selected city");
    }
}
