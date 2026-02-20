package com.example.tests.ui.webtables;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class WebTablesDefaultDataTest extends BaseWebTablesTest {

    @Test(description = "Should display three default records on page load")
    public void testDefaultRecordsAreDisplayed() {
        List<WebElement> rows = driver.findElements(By.cssSelector("[data-cy^='table-row-']"));
        Assert.assertEquals(rows.size(), 3, "Table should show 3 default records");
    }

    @Test(description = "Should display Cierra Vega as the first default record")
    public void testFirstDefaultRecord() {
        Assert.assertEquals(driver.findElement(By.cssSelector("[data-cy='cell-first-name-1']")).getText(), "Cierra");
        Assert.assertEquals(driver.findElement(By.cssSelector("[data-cy='cell-last-name-1']")).getText(), "Vega");
        Assert.assertEquals(driver.findElement(By.cssSelector("[data-cy='cell-age-1']")).getText(), "39");
        Assert.assertEquals(driver.findElement(By.cssSelector("[data-cy='cell-department-1']")).getText(), "Insurance");
    }

    @Test(description = "Should display all default department values correctly")
    public void testDefaultDepartments() {
        Assert.assertEquals(driver.findElement(By.cssSelector("[data-cy='cell-department-1']")).getText(), "Insurance");
        Assert.assertEquals(driver.findElement(By.cssSelector("[data-cy='cell-department-2']")).getText(), "Compliance");
        Assert.assertEquals(driver.findElement(By.cssSelector("[data-cy='cell-department-3']")).getText(), "Legal");
    }
}
