package com.example.tests.ui.buttons;

import com.example.pages.ButtonsPage;
import com.example.tests.ui.BaseUITest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class ButtonsVisibilityTest extends BaseUITest {

    private ButtonsPage buttonsPage;

    @BeforeMethod
    public void navigateToPage() {
        buttonsPage = new ButtonsPage(driver, wait);
        buttonsPage.navigate(BASE_URL);
    }

    @Test(description = "Should show only the triggered message, not others")
    public void testMessagesAreHiddenByDefault() {
        SoftAssert soft = new SoftAssert();
        soft.assertFalse(buttonsPage.isDoubleClickMessageDisplayed(),
                "Double click message should be hidden before interaction");
        soft.assertFalse(buttonsPage.isRightClickMessageDisplayed(),
                "Right click message should be hidden before interaction");
        soft.assertFalse(buttonsPage.isDynamicClickMessageDisplayed(),
                "Dynamic click message should be hidden before interaction");
        soft.assertAll();
    }

    @Test(description = "Should show only double-click message after double clicking")
    public void testOnlyDoubleClickMessageShownAfterDoubleClick() {
        buttonsPage.doubleClick();
        buttonsPage.getDoubleClickMessageText(); // wait for message visibility

        SoftAssert soft = new SoftAssert();
        soft.assertTrue(buttonsPage.isDoubleClickMessageDisplayed());
        soft.assertFalse(buttonsPage.isRightClickMessageDisplayed());
        soft.assertFalse(buttonsPage.isDynamicClickMessageDisplayed());
        soft.assertAll();
    }
}
