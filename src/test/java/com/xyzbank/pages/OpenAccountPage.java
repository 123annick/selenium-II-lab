package com.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class OpenAccountPage extends ManagerPage {

    private final By customerDropdown = By.id("userSelect");
    private final By currencyDropdown = By.id("currency");
    private final By processButton = By.cssSelector("form button[type='submit']");

    public OpenAccountPage() {
        visible(customerDropdown);
    }

    @Step("Select customer '{customerName}'")
    public OpenAccountPage selectCustomer(String customerName) {
        select(customerDropdown).selectByVisibleText(customerName);
        return this;
    }

    @Step("Select currency '{currency}'")
    public OpenAccountPage selectCurrency(String currency) {
        select(currencyDropdown).selectByVisibleText(currency);
        return this;
    }

    @Step("Click 'Process' and read confirmation")
    public String process() {
        click(processButton);
        return acceptAlertAndGetText();
    }

    /** @return the new account number parsed from the confirmation alert */
    @Step("Open {currency} account for '{customerName}'")
    public String openAccount(String customerName, String currency) {
        String alertText = selectCustomer(customerName).selectCurrency(currency).process();
        // e.g. "Account created successfully with account Number :1016"
        return alertText.substring(alertText.lastIndexOf(':') + 1).trim();
    }
}
