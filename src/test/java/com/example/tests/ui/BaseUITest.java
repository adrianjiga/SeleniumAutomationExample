package com.example.tests.ui;

import com.example.config.ConfigManager;
import com.example.listeners.ScreenshotListener;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Listeners(ScreenshotListener.class)
public class BaseUITest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static final String BASE_URL = ConfigManager.get("base.url");

    public WebDriver getDriver() {
        return driver;
    }

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        // Headless mode configuration
        if (ConfigManager.getBoolean("headless")) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        // Block ads and improve stability
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("profile.default_content_setting_values.ads", 2);
        options.setExperimentalOption("prefs", prefs);

        // Additional stability options for CI environments
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-browser-side-navigation");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigManager.getInt("wait.timeout.seconds")));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigManager.getInt("page.load.timeout.seconds")));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(ConfigManager.getInt("page.load.timeout.seconds")));
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
