package com.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CustomersPage extends ManagerPage {

    private final By searchInput = By.cssSelector("input[ng-model='searchCustomer']");
    private final By tableRows = By.cssSelector("table tbody tr");
    private final By deleteButton = By.cssSelector("button[ng-click='deleteCust(cust)']");

    public CustomersPage() {
        visible(searchInput);
    }

    @Step("Search customers for '{text}'")
    public CustomersPage search(String text) {
        type(searchInput, text);
        return this;
    }

    public List<WebElement> rows() {
        return driver.findElements(tableRows);
    }

    @Step("Check customer '{firstName} {lastName}' is in the table")
    public boolean isCustomerPresent(String firstName, String lastName) {
        search(firstName);
        return rows().stream().anyMatch(row -> row.getText().startsWith(firstName + " " + lastName));
    }

    @Step("Read account numbers of '{firstName}'")
    public String accountNumbersOf(String firstName) {
        search(firstName);
        // columns: First Name | Last Name | Post Code | Account Number | Delete
        return rows().get(0).findElements(By.tagName("td")).get(3).getText().trim();
    }

    @Step("Delete customer '{firstName} {lastName}'")
    public CustomersPage deleteCustomer(String firstName, String lastName) {
        search(firstName);
        WebElement row = rows().stream()
                .filter(r -> r.getText().startsWith(firstName + " " + lastName))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Customer not found: " + firstName + " " + lastName));
        row.findElement(deleteButton).click();
        return this;
    }
}
