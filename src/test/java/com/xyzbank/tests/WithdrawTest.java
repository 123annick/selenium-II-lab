package com.xyzbank.tests;

import com.xyzbank.base.BaseTest;
import com.xyzbank.dataproviders.TestDataProviders;
import com.xyzbank.pages.AccountPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

/** User Story 2 – Withdrawing money (data-driven). */
@Epic("XYZ Bank")
@Feature("Customer - Withdraw")
@Owner("QA Team")
public class WithdrawTest extends BaseTest {

    private static final String CUSTOMER = "Harry Potter";
    private static final String SUCCESS = "Transaction successful";

    @Test(dataProvider = "withdrawals", dataProviderClass = TestDataProviders.class,
            groups = {"smoke", "regression"})
    @Story("Withdrawal amount is validated and balance updated")
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("TMLD4-50") // TC-051
    public void withdraw(String initialDeposit, String withdrawAmount, String expectedOutcome, String description) {
        reportTitle("Withdraw – " + description + " (" + withdrawAmount + ")");

        AccountPage account = openApp().goToCustomerLogin().loginAs(CUSTOMER);

        // Arrange: make the balance known instead of relying on existing data
        int start = account.balance();
        account.deposit(initialDeposit);
        int balanceBefore = account.waitForBalance(start + Integer.parseInt(initialDeposit));

        account.withdraw(withdrawAmount);

        if ("should_pass".equals(expectedOutcome)) {
            int expected = balanceBefore - Integer.parseInt(withdrawAmount);
            assertEquals(account.statusMessage(), SUCCESS, "Status message");
            assertEquals(account.waitForBalance(expected), expected, "Balance after withdrawal");
        } else {
            assertNotEquals(account.statusMessage(), SUCCESS,
                    "Validation defect: invalid withdrawal '" + withdrawAmount + "' was accepted");
            assertEquals(account.balance(), balanceBefore, "Balance must not change for an invalid withdrawal");
        }
    }
}
