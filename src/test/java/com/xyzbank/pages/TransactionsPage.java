package com.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionsPage extends BasePage {

    private final By transactionRows = By.cssSelector("table tbody tr");
    private final By resetButton = By.cssSelector("button[ng-click='reset()']");
    private final By backButton = By.cssSelector("button[ng-click='back()']");
    private final By transactionsTable = By.cssSelector("table.table");

    public TransactionsPage() {
        visible(backButton);
    }

    public List<WebElement> rows() {
        return driver.findElements(transactionRows);
    }

    /**
     * Waits until the expected number of rows is shown.
     * App defect (BUG-021): the start/end date filters are pre-filled with the first/last
     * transaction time INCLUDING milliseconds, but the datetime-local inputs only allow whole
     * seconds. The values become invalid, the filter "date >= start && date <= end" then
     * hides every row (manual note in TC-037). The inputs' min/max also stop a user from
     * widening the range, so the test sets the filter range directly on the page.
     */
    @Step("Wait for at least {expectedRows} transaction(s)")
    public int waitForTransactions(int expectedRows) {
        new WebDriverWait(driver, Duration.ofSeconds(15), Duration.ofSeconds(1)).until(d -> {
            if (rows().size() >= expectedRows) {
                return true;
            }
            widenDateFilter();
            return false;
        });
        return rows().size();
    }

    /** Widens the Transactions date filter (2000-01-01 .. tomorrow) in the page's AngularJS scope. */
    private void widenDateFilter() {
        ((JavascriptExecutor) driver).executeScript(
                "var scope = angular.element(arguments[0]).scope();"
                        + "scope.$apply(function () {"
                        + "  scope.startDate = new Date(2000, 0, 1);"
                        + "  scope.end = new Date(Date.now() + 24 * 60 * 60 * 1000);"
                        + "});",
                driver.findElement(transactionsTable));
    }

    /**
     * Columns: Date-Time | Amount | Transaction Type.
     * @return one entry per row, e.g. "300 Credit" (order independent assertions)
     */
    @Step("Read amount and type of every transaction")
    public List<String> amountsAndTypes() {
        return rows().stream()
                .map(row -> row.findElements(By.tagName("td")))
                .map(cells -> cells.get(1).getText().trim() + " " + cells.get(2).getText().trim())
                .collect(Collectors.toList());
    }

    public boolean isResetButtonVisible() {
        return isDisplayed(resetButton);
    }

    @Step("Click 'Reset'")
    public TransactionsPage clickReset() {
        click(resetButton);
        return this;
    }

    @Step("Click 'Back'")
    public AccountPage back() {
        click(backButton);
        return new AccountPage();
    }
}
