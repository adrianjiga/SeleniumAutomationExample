package com.example.tests.ui.buttons;

import com.example.pages.ButtonsPage;
import com.example.tests.ui.BaseUITest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ButtonsClickTest extends BaseUITest {

    private ButtonsPage buttonsPage;

    @BeforeMethod
    public void navigateToPage() {
        buttonsPage = new ButtonsPage(driver, wait);
        buttonsPage.navigate(BASE_URL);
    }

    @Test(description = "Should interact with double click button")
    public void testDoubleClick() {
        buttonsPage.doubleClick();
        String text = buttonsPage.getDoubleClickMessageText();
        Assert.assertTrue(text.contains("You have done a double click"),
                "Double click message should be displayed. Actual: " + text);
    }

    @Test(description = "Should interact with right click button")
    public void testRightClick() {
        buttonsPage.rightClick();
        String text = buttonsPage.getRightClickMessageText();
        Assert.assertTrue(text.contains("You have done a right click"),
                "Right click message should be displayed. Actual: " + text);
    }

    @Test(description = "Should interact with dynamic click button")
    public void testDynamicClick() {
        buttonsPage.dynamicClick();
        String text = buttonsPage.getDynamicClickMessageText();
        Assert.assertTrue(text.contains("You have done a dynamic click"),
                "Dynamic click message should be displayed. Actual: " + text);
    }
}
