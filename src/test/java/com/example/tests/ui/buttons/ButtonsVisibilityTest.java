package com.example.tests.ui.buttons;

import com.example.pages.ButtonsPage;
import com.example.tests.ui.BaseUITest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ButtonsVisibilityTest extends BaseUITest {

    private ButtonsPage buttonsPage;

    @BeforeMethod
    public void navigateToPage() {
        buttonsPage = new ButtonsPage(driver, wait);
        buttonsPage.navigate(BASE_URL);
    }

    @Test(description = "Should show only the triggered message, not others")
    public void testMessagesAreHiddenByDefault() {
        Assert.assertFalse(buttonsPage.isDoubleClickMessageDisplayed(),
                "Double click message should be hidden before interaction");
        Assert.assertFalse(buttonsPage.isRightClickMessageDisplayed(),
                "Right click message should be hidden before interaction");
        Assert.assertFalse(buttonsPage.isDynamicClickMessageDisplayed(),
                "Dynamic click message should be hidden before interaction");
    }

    @Test(description = "Should show only double-click message after double clicking")
    public void testOnlyDoubleClickMessageShownAfterDoubleClick() {
        buttonsPage.doubleClick();
        buttonsPage.getDoubleClickMessageText(); // wait for message visibility
        Assert.assertTrue(buttonsPage.isDoubleClickMessageDisplayed());
        Assert.assertFalse(buttonsPage.isRightClickMessageDisplayed());
        Assert.assertFalse(buttonsPage.isDynamicClickMessageDisplayed());
    }
}
