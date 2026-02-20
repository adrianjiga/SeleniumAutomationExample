package com.example.tests.ui.webtables;

import org.testng.Assert;
import org.testng.annotations.Test;

public class WebTablesDefaultDataTest extends BaseWebTablesTest {

    @Test(description = "Should display three default records on page load")
    public void testDefaultRecordsAreDisplayed() {
        Assert.assertEquals(webTablesPage.getRowCount(), 3, "Table should show 3 default records");
    }

    @Test(description = "Should display Cierra Vega as the first default record")
    public void testFirstDefaultRecord() {
        Assert.assertEquals(webTablesPage.getCellText("first-name", 1), "Cierra");
        Assert.assertEquals(webTablesPage.getCellText("last-name", 1), "Vega");
        Assert.assertEquals(webTablesPage.getCellText("age", 1), "39");
        Assert.assertEquals(webTablesPage.getCellText("department", 1), "Insurance");
    }

    @Test(description = "Should display all default department values correctly")
    public void testDefaultDepartments() {
        Assert.assertEquals(webTablesPage.getCellText("department", 1), "Insurance");
        Assert.assertEquals(webTablesPage.getCellText("department", 2), "Compliance");
        Assert.assertEquals(webTablesPage.getCellText("department", 3), "Legal");
    }
}
