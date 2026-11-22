package com.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;

/** Customer "login": the customer picks their name and presses Login. */
public class CustomerLoginPage extends BasePage {

    private final By customerDropdown = By.id("userSelect");
    private final By loginButton = By.cssSelector("form button[type='submit']");

    public CustomerLoginPage() {
        visible(customerDropdown);
    }

    @Step("Check if customer '{customerName}' is listed")
    public boolean isCustomerListed(String customerName) {
        return hasOption(customerDropdown, customerName);
    }

    @Step("Select customer '{customerName}'")
    public CustomerLoginPage selectCustomer(String customerName) {
        select(customerDropdown).selectByVisibleText(customerName);
        return this;
    }

    @Step("Click 'Login'")
    public AccountPage clickLogin() {
        click(loginButton);
        return new AccountPage();
    }

    /** Convenience flow used by most customer tests. */
    @Step("Log in as customer '{customerName}'")
    public AccountPage loginAs(String customerName) {
        return selectCustomer(customerName).clickLogin();
    }
}
