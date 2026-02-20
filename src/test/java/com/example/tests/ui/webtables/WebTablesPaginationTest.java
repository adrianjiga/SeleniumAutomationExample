package com.example.tests.ui.webtables;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class WebTablesPaginationTest extends BaseWebTablesTest {

    @Test(description = "Should show page 1 of 1 by default with 3 records")
    public void testDefaultPaginationState() {
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(webTablesPage.getCurrentPage(), "1", "Current page should be 1");
        soft.assertEquals(webTablesPage.getTotalPages(), "1", "Total pages should be 1 with 3 records");
        soft.assertAll();
    }

    @Test(description = "Should split into two pages when records exceed rows-per-page setting")
    public void testRowsPerPageSelector() {
        // Add 3 more records so 6 total exceed the minimum page size of 5
        String[][] newRecords = {
            {"Ann",  "Lee",  "ann@test.com",  "30", "60000", "HR"},
            {"Bob",  "Ray",  "bob@test.com",  "25", "55000", "IT"},
            {"Cara", "Kim",  "cara@test.com", "28", "65000", "QA"},
        };
        for (int i = 0; i < newRecords.length; i++) {
            String[] r = newRecords[i];
            webTablesPage.clickAddRecord();
            webTablesPage.fillModal(r[0], r[1], r[2], r[3], r[4], r[5]);
            webTablesPage.submitModal();
            webTablesPage.waitForRowCount(4 + i);
        }

        webTablesPage.setRowsPerPage(5);
        webTablesPage.waitForTotalPages("2");

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(webTablesPage.getRowCount(), 5,
                "Page 1 should show exactly 5 rows");
        soft.assertEquals(webTablesPage.getTotalPages(), "2",
                "6 records at 5 per page should give 2 total pages");
        soft.assertAll();
    }
}
