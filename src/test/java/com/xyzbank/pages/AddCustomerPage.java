package com.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;

import java.time.Duration;

public class AddCustomerPage extends ManagerPage {

    private final By firstNameInput = By.cssSelector("input[ng-model='fName']");
    private final By lastNameInput = By.cssSelector("input[ng-model='lName']");
    private final By postCodeInput = By.cssSelector("input[ng-model='postCd']");
    private final By addCustomerButton = By.cssSelector("form button[type='submit']");

    public AddCustomerPage() {
        visible(firstNameInput);
    }

    @Step("Enter first name '{firstName}'")
    public AddCustomerPage enterFirstName(String firstName) {
        type(firstNameInput, firstName);
        return this;
    }

    @Step("Enter last name '{lastName}'")
    public AddCustomerPage enterLastName(String lastName) {
        type(lastNameInput, lastName);
        return this;
    }

    @Step("Enter post code '{postCode}'")
    public AddCustomerPage enterPostCode(String postCode) {
        type(postCodeInput, postCode);
        return this;
    }

    /**
     * Submits the form and returns the confirmation alert text,
     * or null when the application shows no alert (input rejected).
     */
    @Step("Click 'Add Customer' and read confirmation")
    public String submit() {
        click(addCustomerButton);
        return acceptAlertIfPresent(Duration.ofSeconds(3));
    }

    @Step("Add customer {firstName} {lastName} ({postCode})")
    public String addCustomer(String firstName, String lastName, String postCode) {
        return enterFirstName(firstName).enterLastName(lastName).enterPostCode(postCode).submit();
    }
}
