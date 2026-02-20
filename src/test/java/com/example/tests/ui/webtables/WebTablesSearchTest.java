package com.example.tests.ui.webtables;

import org.testng.Assert;
import org.testng.annotations.Test;

public class WebTablesSearchTest extends BaseWebTablesTest {

    @Test(description = "Should filter records by first name via search box")
    public void testSearchByFirstName() {
        webTablesPage.search("Cierra");
        webTablesPage.waitForRowCount(1);
        Assert.assertEquals(webTablesPage.getCellText("first-name", 1), "Cierra");
    }

    @Test(description = "Should filter records by department via search box")
    public void testSearchByDepartment() {
        webTablesPage.search("Legal");
        webTablesPage.waitForRowCount(1);
        Assert.assertEquals(webTablesPage.getCellText("department", 1), "Legal");
    }

    @Test(description = "Should show no rows when search matches nothing")
    public void testSearchWithNoResults() {
        webTablesPage.search("zzz_no_match_xyz");
        webTablesPage.waitForRowCount(0);
        Assert.assertEquals(webTablesPage.getRowCount(), 0,
                "No rows should be shown for unmatched search");
    }

    @Test(description = "Should restore all records when search is cleared")
    public void testClearSearchRestoresAllRows() {
        webTablesPage.search("Alden");
        webTablesPage.waitForRowCount(1);
        webTablesPage.clearSearch();
        webTablesPage.waitForRowCount(3);
        Assert.assertEquals(webTablesPage.getRowCount(), 3);
    }
}
