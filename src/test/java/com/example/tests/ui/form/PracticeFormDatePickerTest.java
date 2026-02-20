package com.example.tests.ui.form;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PracticeFormDatePickerTest extends BasePracticeFormTest {

    @Test(description = "Should open date picker popup when input is clicked")
    public void testDatePickerOpens() {
        formPage.openDatePicker();
        Assert.assertTrue(formPage.isDatePickerVisible(), "Date picker popup should be visible");
    }

    @Test(description = "Should select a date from the date picker")
    public void testSelectDateFromPicker() {
        formPage.openDatePicker();
        formPage.selectDate(0, "1990", 15);

        String selectedDate = formPage.getDateValue();
        Assert.assertNotNull(selectedDate);
        Assert.assertFalse(selectedDate.isEmpty(), "Date input should have a value after selection");
        Assert.assertTrue(selectedDate.contains("1990"),
                "Selected date should contain the year 1990. Got: " + selectedDate);
    }
}
