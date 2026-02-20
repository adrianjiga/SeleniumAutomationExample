package com.example.tests.ui.form;

import com.example.pages.PracticeFormPage;
import com.example.tests.ui.BaseUITest;
import org.testng.annotations.BeforeMethod;

public class BasePracticeFormTest extends BaseUITest {

    protected PracticeFormPage formPage;

    @BeforeMethod
    public void navigateToPage() {
        formPage = new PracticeFormPage(driver, wait);
        formPage.navigate(BASE_URL);
    }
}
