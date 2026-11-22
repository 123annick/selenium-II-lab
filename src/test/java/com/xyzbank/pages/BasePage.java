package com.xyzbank.pages;

import com.xyzbank.config.ConfigReader;
import com.xyzbank.driver.DriverManager;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Parent of every page object. Holds the driver, the explicit wait and
 * small, reusable, synchronised interactions. Tests never call these directly.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.explicitWaitSeconds()));
    }

    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void type(By locator, String text) {
        WebElement element = visible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String textOf(By locator) {
        return visible(locator).getText().trim();
    }

    protected boolean isDisplayed(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }

    protected Select select(By locator) {
        return new Select(visible(locator));
    }

    protected boolean hasOption(By selectLocator, String visibleText) {
        return select(selectLocator).getOptions().stream()
                .anyMatch(option -> option.getText().trim().equals(visibleText));
    }

    /** Waits for a JavaScript alert, accepts it and returns its text. */
    protected String acceptAlertAndGetText() {
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        String text = alert.getText();
        alert.accept();
        return text;
    }

    /** Returns the alert text if an alert shows up shortly, otherwise null. */
    protected String acceptAlertIfPresent(Duration timeout) {
        try {
            Alert alert = new WebDriverWait(driver, timeout).until(ExpectedConditions.alertIsPresent());
            String text = alert.getText();
            alert.accept();
            return text;
        } catch (org.openqa.selenium.TimeoutException e) {
            return null;
        }
    }
}
