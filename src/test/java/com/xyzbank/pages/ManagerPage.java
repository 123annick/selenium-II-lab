package com.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;

/** Bank Manager landing page with the three tabs. */
public class ManagerPage extends BasePage {

    private final By addCustomerTab = By.cssSelector("button[ng-click='addCust()']");
    private final By openAccountTab = By.cssSelector("button[ng-click='openAccount()']");
    private final By customersTab = By.cssSelector("button[ng-click='showCust()']");

    public ManagerPage() {
        visible(addCustomerTab);
    }

    @Step("Open 'Add Customer' tab")
    public AddCustomerPage openAddCustomer() {
        click(addCustomerTab);
        return new AddCustomerPage();
    }

    @Step("Open 'Open Account' tab")
    public OpenAccountPage openOpenAccount() {
        click(openAccountTab);
        return new OpenAccountPage();
    }

    @Step("Open 'Customers' tab")
    public CustomersPage openCustomers() {
        click(customersTab);
        return new CustomersPage();
    }
}
