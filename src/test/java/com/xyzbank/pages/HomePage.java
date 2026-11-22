package com.xyzbank.pages;

import com.xyzbank.config.ConfigReader;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

/** Landing page: choose Customer Login or Bank Manager Login. */
public class HomePage extends BasePage {

    private final By customerLoginButton = By.cssSelector("button[ng-click='customer()']");
    private final By managerLoginButton = By.cssSelector("button[ng-click='manager()']");

    @Step("Open XYZ Bank home page")
    public HomePage open() {
        driver.get(ConfigReader.baseUrl());
        visible(customerLoginButton);
        return this;
    }

    @Step("Click 'Customer Login'")
    public CustomerLoginPage goToCustomerLogin() {
        click(customerLoginButton);
        return new CustomerLoginPage();
    }

    @Step("Click 'Bank Manager Login'")
    public ManagerPage goToManagerLogin() {
        click(managerLoginButton);
        return new ManagerPage();
    }
}
