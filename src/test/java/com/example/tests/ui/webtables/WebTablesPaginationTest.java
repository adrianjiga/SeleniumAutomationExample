package com.example.tests.ui.webtables;

import org.testng.Assert;
import org.testng.annotations.Test;

public class WebTablesPaginationTest extends BaseWebTablesTest {

    @Test(description = "Should show page 1 of 1 by default with 3 records")
    public void testDefaultPaginationState() {
        Assert.assertEquals(webTablesPage.getCurrentPage(), "1");
        Assert.assertEquals(webTablesPage.getTotalPages(), "1");
    }

    @Test(description = "Should change rows-per-page and update pagination")
    public void testRowsPerPageSelector() {
        webTablesPage.setRowsPerPage(5);
        webTablesPage.waitForTotalPages("1");
        Assert.assertEquals(webTablesPage.getRowCount(), 3,
                "All 3 rows still visible with page size 5");
    }
}
