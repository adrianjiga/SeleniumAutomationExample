package com.example.tests.ui.webtables;

import com.example.tests.ui.BaseUITest;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.BeforeMethod;

public class BaseWebTablesTest extends BaseUITest {

    @BeforeMethod
    public void navigateToPage() {
        driver.get(BASE_URL + "/webtables");
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-cy='table-row-1']")));
    }
}
