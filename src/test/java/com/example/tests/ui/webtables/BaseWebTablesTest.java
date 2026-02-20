package com.example.tests.ui.webtables;

import com.example.pages.WebTablesPage;
import com.example.tests.ui.BaseUITest;
import org.testng.annotations.BeforeMethod;

public class BaseWebTablesTest extends BaseUITest {

    protected WebTablesPage webTablesPage;

    @BeforeMethod
    public void navigateToPage() {
        webTablesPage = new WebTablesPage(driver, wait);
        webTablesPage.navigate(BASE_URL);
    }
}
