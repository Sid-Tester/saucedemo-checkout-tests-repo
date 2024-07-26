package com.loonycorn.learningselenium.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverFactory {

    public enum BrowserType {
        CHROME,
        FIREFOX,
        EDGE,
    }

    public static WebDriver createDriver(BrowserType browserType) {
        WebDriver driver = null;

        switch (browserType) {
            case CHROME:
                System.setProperty("webdriver.chrome.driver",
                        "C://Users/cindy.schenk/Selenium Training/WebDriver/chromedriver-win64/chromedriver.exe");
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized");
                //chromeOptions.addArguments("disable-infobars"); // disabling infobars
                //chromeOptions.addArguments("--disable-extensions"); // disabling extensions
                //chromeOptions.addArguments("--disable-gpu"); // applicable to windows os only
                //chromeOptions.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
                //chromeOptions.addArguments("--no-sandbox"); // Bypass OS security model
                //chromeOptions.addArguments("--remote-debugging-pipe");
                driver = new ChromeDriver(chromeOptions);

                //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
                break;
            case FIREFOX:
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                driver = new FirefoxDriver(firefoxOptions);
                break;
            case EDGE:
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--start-maximized");
                driver = new EdgeDriver(edgeOptions);
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser type: " + browserType);
        }

        return driver;
    }
}