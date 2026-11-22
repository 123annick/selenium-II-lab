package com.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/** Customer dashboard: account info + Transactions / Deposit / Withdrawl. */
public class AccountPage extends BasePage {

    private final By welcomeName = By.cssSelector("span.fontBig");
    private final By accountDropdown = By.id("accountSelect");
    private final By accountInfoValues = By.cssSelector("div.center strong");
    private final By noAccountMessage = By.cssSelector("span[ng-show='noAccount']");

    private final By transactionsTab = By.cssSelector("button[ng-click='transactions()']");
    private final By depositTab = By.cssSelector("button[ng-click='deposit()']");
    private final By withdrawTab = By.cssSelector("button[ng-click='withdrawl()']");
    private final By logoutButton = By.cssSelector("button.logout");

    private final By formLabel = By.cssSelector("form[name='myForm'] label");
    private final By amountInput = By.cssSelector("input[ng-model='amount']");
    private final By submitButton = By.cssSelector("form[name='myForm'] button[type='submit']");
    private final By statusMessage = By.cssSelector("span.error");

    public AccountPage() {
        visible(welcomeName);
    }

    // ---------- account information ----------

    @Step("Read welcome name")
    public String welcomeName() {
        return textOf(welcomeName);
    }

    public boolean hasAccount() {
        return isDisplayed(accountDropdown) && !isDisplayed(noAccountMessage);
    }

    public boolean isNoAccountMessageShown() {
        return isDisplayed(noAccountMessage);
    }

    @Step("Select account '{accountNumber}'")
    public AccountPage selectAccount(String accountNumber) {
        select(accountDropdown).selectByVisibleText(accountNumber);
        return this;
    }

    private List<WebElement> accountInfo() {
        return wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(accountInfoValues, 2));
    }

    public String accountNumber() {
        return accountInfo().get(0).getText().trim();
    }

    @Step("Read current balance")
    public int balance() {
        return Integer.parseInt(accountInfo().get(1).getText().trim());
    }

    /** Waits until the balance shown on screen equals the expected value. */
    @Step("Wait for balance to become {expected}")
    public int waitForBalance(int expected) {
        wait.until(d -> balance() == expected);
        return balance();
    }

    // ---------- deposit ----------

    @Step("Open 'Deposit' tab")
    public AccountPage openDeposit() {
        click(depositTab);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(formLabel, "Deposited"));
        return this;
    }

    @Step("Enter amount '{amount}'")
    public AccountPage enterAmount(String amount) {
        type(amountInput, amount);
        return this;
    }

    @Step("Click 'Deposit'")
    public AccountPage clickDeposit() {
        click(submitButton);
        return this;
    }

    @Step("Deposit {amount}")
    public AccountPage deposit(String amount) {
        return openDeposit().enterAmount(amount).clickDeposit();
    }

    // ---------- withdraw ----------

    @Step("Open 'Withdrawl' tab")
    public AccountPage openWithdraw() {
        click(withdrawTab);
        // The form is re-rendered by AngularJS: wait for the correct label
        // to avoid typing into the old Deposit form (classic flaky spot).
        wait.until(ExpectedConditions.textToBePresentInElementLocated(formLabel, "Withdrawn"));
        return this;
    }

    @Step("Click 'Withdraw'")
    public AccountPage clickWithdraw() {
        click(submitButton);
        return this;
    }

    @Step("Withdraw {amount}")
    public AccountPage withdraw(String amount) {
        return openWithdraw().enterAmount(amount).clickWithdraw();
    }

    /**
     * Message under the form, e.g. "Deposit Successful".
     * Waits briefly for it to appear; returns an empty string if the app shows nothing.
     */
    @Step("Read status message")
    public String statusMessage() {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(2)).until(d -> {
                List<WebElement> messages = d.findElements(statusMessage);
                String text = messages.isEmpty() ? "" : messages.get(0).getText().trim();
                return text.isEmpty() ? null : text;
            });
        } catch (TimeoutException e) {
            return "";
        }
    }

    // ---------- navigation ----------

    @Step("Open 'Transactions' tab")
    public TransactionsPage openTransactions() {
        click(transactionsTab);
        return new TransactionsPage();
    }

    @Step("Log out")
    public CustomerLoginPage logout() {
        click(logoutButton);
        return new CustomerLoginPage();
    }
}
