package com.example.tests.ui.webtables;

import org.testng.Assert;
import org.testng.annotations.Test;

public class WebTablesCrudTest extends BaseWebTablesTest {

    @Test(description = "Should add a new record via the registration modal")
    public void testAddNewRecord() {
        webTablesPage.clickAddRecord();
        webTablesPage.fillModal("Jane", "Smith", "jane@example.com", "28", "75000", "Engineering");
        webTablesPage.submitModal();

        webTablesPage.waitForRowCount(4);
        boolean found = false;
        for (int i = 1; i <= webTablesPage.getRowCount(); i++) {
            if ("Jane".equals(webTablesPage.getCellText("first-name", i))) {
                found = true;
                break;
            }
        }
        Assert.assertTrue(found, "Newly added record 'Jane' should appear in the table");
    }

    @Test(description = "Should close registration modal without saving when cancel is clicked")
    public void testCancelModalDoesNotAddRecord() {
        webTablesPage.clickAddRecord();
        webTablesPage.clearAndTypeModalField("modal-first-name", "Ghost");
        webTablesPage.cancelModal();

        Assert.assertEquals(webTablesPage.getRowCount(), 3,
                "Row count should remain 3 after cancel");
    }

    @Test(description = "Should delete a record from the table")
    public void testDeleteRecord() {
        webTablesPage.clickDelete(1);
        webTablesPage.waitForRowCount(2);
        Assert.assertEquals(webTablesPage.getRowCount(), 2,
                "Table should have 2 rows after deleting one");
    }

    @Test(description = "Should delete the correct record by row position")
    public void testDeleteSpecificRecord() {
        String secondRowName = webTablesPage.getCellText("first-name", 2);
        webTablesPage.clickDelete(1);
        webTablesPage.waitForRowCount(2);

        String newFirstRowName = webTablesPage.getCellText("first-name", 1);
        Assert.assertEquals(newFirstRowName, secondRowName,
                "After deleting row 1, the old row 2 should shift up to row 1");
    }

    @Test(description = "Should edit an existing record via the registration modal")
    public void testEditRecord() {
        webTablesPage.clickEdit(1);
        webTablesPage.clearAndTypeModalField("modal-first-name", "UpdatedName");
        webTablesPage.submitModal();

        webTablesPage.waitForModalToClose();
        Assert.assertEquals(webTablesPage.getCellText("first-name", 1),
                "UpdatedName", "First name should be updated after editing");
    }

    @Test(description = "Edit modal should be pre-populated with existing record values")
    public void testEditModalPrePopulated() {
        webTablesPage.clickEdit(1);

        Assert.assertEquals(webTablesPage.getModalFieldValue("modal-first-name"),
                "Cierra", "Modal should be pre-filled with existing first name");
        Assert.assertEquals(webTablesPage.getModalFieldValue("modal-last-name"),
                "Vega", "Modal should be pre-filled with existing last name");
    }
}
