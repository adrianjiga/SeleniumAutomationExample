package com.example.tests.ui.webtables;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class WebTablesDefaultDataTest extends BaseWebTablesTest {

    @Test(description = "Should display three default records on page load")
    public void testDefaultRecordsAreDisplayed() {
        Assert.assertEquals(webTablesPage.getRowCount(), 3, "Table should show 3 default records");
    }

    @Test(description = "Should display Cierra Vega as the first default record")
    public void testFirstDefaultRecord() {
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(webTablesPage.getCellText("first-name", 1), "Cierra");
        soft.assertEquals(webTablesPage.getCellText("last-name", 1), "Vega");
        soft.assertEquals(webTablesPage.getCellText("age", 1), "39");
        soft.assertEquals(webTablesPage.getCellText("department", 1), "Insurance");
        soft.assertAll();
    }

    @Test(description = "Should display all default department values correctly")
    public void testDefaultDepartments() {
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(webTablesPage.getCellText("department", 1), "Insurance");
        soft.assertEquals(webTablesPage.getCellText("department", 2), "Compliance");
        soft.assertEquals(webTablesPage.getCellText("department", 3), "Legal");
        soft.assertAll();
    }
}
