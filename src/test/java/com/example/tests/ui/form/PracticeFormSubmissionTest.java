package com.example.tests.ui.form;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class PracticeFormSubmissionTest extends BasePracticeFormTest {

    @Test(description = "Should submit form and show success modal with all fields")
    public void testFullFormSubmission() {
        formPage.setFirstName("John");
        formPage.setLastName("Doe");
        formPage.setEmail("john.doe@example.com");
        formPage.selectGender(1);
        formPage.setPhone("1234567890");
        formPage.setAddress("123 Main Street");
        formPage.selectCountry("Germany");
        formPage.selectCity("Berlin");
        formPage.openDatePicker();
        formPage.selectDate(0, "1990", 15);
        formPage.submit();

        Assert.assertTrue(formPage.isSuccessModalVisible(), "Success modal should be visible after submit");
        Assert.assertTrue(
                formPage.getSuccessModalTitleText().contains("Thanks for submitting the form"),
                "Modal title should confirm submission");
    }

    @Test(description = "Should close success modal when Close button is clicked")
    public void testSuccessModalClose() {
        fillMinimalForm();
        formPage.submit();
        formPage.getSuccessModalTitleText(); // wait for modal to appear
        formPage.closeSuccessModal();

        Assert.assertFalse(formPage.isSuccessModalVisible(),
                "Success modal should be hidden after closing");
    }

    @Test(description = "Should accept input in all text fields")
    public void testTextFieldsAcceptInput() {
        formPage.setFirstName("Alice");
        formPage.setLastName("Wonderland");
        formPage.setEmail("alice@example.com");
        formPage.setPhone("9876543210");
        formPage.setAddress("42 Fantasy Lane");

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(formPage.getFieldValue("firstName"), "Alice");
        soft.assertEquals(formPage.getFieldValue("lastName"), "Wonderland");
        soft.assertEquals(formPage.getFieldValue("userEmail"), "alice@example.com");
        soft.assertEquals(formPage.getFieldValue("userNumber"), "9876543210");
        soft.assertEquals(formPage.getFieldValue("currentAddress"), "42 Fantasy Lane");
        soft.assertAll();
    }

    private void fillMinimalForm() {
        formPage.setFirstName("Test");
        formPage.setLastName("User");
        formPage.setPhone("1234567890");
        formPage.selectGender(1);
        formPage.openDatePicker();
        formPage.selectDate(0, "1990", 1);
    }
}
