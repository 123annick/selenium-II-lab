package com.xyzbank.driver;

import com.xyzbank.config.ConfigReader;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;

/**
 * Creates WebDriver instances. Drivers are resolved automatically by Selenium Manager,
 * so no WebDriverManager / chromedriver.exe in the repository.
 * If grid.url is set (Docker / CI) a RemoteWebDriver is created instead.
 */
public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createDriver() {
        MutableCapabilities options = buildOptions(ConfigReader.browser(), ConfigReader.headless());
        String gridUrl = ConfigReader.gridUrl();

        WebDriver driver;
        if (!gridUrl.isEmpty()) {
            try {
                driver = new RemoteWebDriver(URI.create(gridUrl).toURL(), options);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("Invalid grid.url: " + gridUrl, e);
            }
        } else {
            driver = switch (ConfigReader.browser()) {
                case "firefox" -> new FirefoxDriver((FirefoxOptions) options);
                case "edge" -> new EdgeDriver((EdgeOptions) options);
                default -> new ChromeDriver((ChromeOptions) options);
            };
        }

        // Best practice: never mix implicit and explicit waits -> implicit wait stays at 0.
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.pageLoadTimeoutSeconds()));
        if (!ConfigReader.headless()) {
            driver.manage().window().maximize();
        }
        return driver;
    }

    private static MutableCapabilities buildOptions(String browser, boolean headless) {
        switch (browser) {
            case "firefox": {
                FirefoxOptions ff = new FirefoxOptions();
                if (headless) {
                    ff.addArguments("-headless");
                }
                ff.addArguments("--width=1920", "--height=1080");
                return ff;
            }
            case "edge": {
                EdgeOptions edge = new EdgeOptions();
                if (headless) {
                    edge.addArguments("--headless=new");
                }
                edge.addArguments("--window-size=1920,1080");
                return edge;
            }
            default: {
                ChromeOptions chrome = new ChromeOptions();
                if (headless) {
                    chrome.addArguments("--headless=new");
                }
                chrome.addArguments("--window-size=1920,1080", "--no-sandbox",
                        "--disable-dev-shm-usage", "--disable-search-engine-choice-screen");
                return chrome;
            }
        }
    }
}
