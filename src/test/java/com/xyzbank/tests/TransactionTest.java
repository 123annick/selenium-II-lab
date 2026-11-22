package com.xyzbank.tests;

import com.xyzbank.base.BaseTest;
import com.xyzbank.pages.AccountPage;
import com.xyzbank.pages.TransactionsPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/** User Story 2 – Viewing transactions and transaction security. */
@Epic("XYZ Bank")
@Feature("Customer - Transactions")
@Owner("QA Team")
public class TransactionTest extends BaseTest {

    private static final String CUSTOMER = "Harry Potter";

    @Test(groups = {"smoke", "regression"})
    @Story("Customer views recent transactions")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("TMLD4-31") // TC-037
    public void depositAndWithdrawalAppearInTransactions() {
        AccountPage account = openApp().goToCustomerLogin().loginAs(CUSTOMER);
        TransactionsPage history = account.openTransactions();
        int existing = history.rows().size();

        account = history.back();
        int start = account.balance();
        account.deposit("300");
        account.waitForBalance(start + 300);
        account.withdraw("100");
        account.waitForBalance(start + 200);

        TransactionsPage transactions = account.openTransactions();
        int total = transactions.waitForTransactions(existing + 2);

        assertEquals(total, existing + 2, "Two new transactions expected");
        assertTrue(transactions.amountsAndTypes().contains("300 Credit"), "Deposit listed as Credit");
        assertTrue(transactions.amountsAndTypes().contains("100 Debit"), "Withdrawal listed as Debit");
    }

    @Test(groups = {"known-defect"})
    @Story("Customer cannot reset or alter transaction history")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("TMLD4-52") // TC-041
    @Issue("TMLD4-95") // BUG-012
    @Description("Acceptance criterion: customers must not be able to reset their transaction history.")
    public void customerCannotResetTransactionHistory() {
        AccountPage account = openApp().goToCustomerLogin().loginAs(CUSTOMER);
        int start = account.balance();
        account.deposit("250");
        account.waitForBalance(start + 250);

        TransactionsPage transactions = account.openTransactions();
        int before = transactions.waitForTransactions(1);

        if (transactions.isResetButtonVisible()) {
            transactions.clickReset();
        }

        assertEquals(transactions.rows().size(), before,
                "Validation defect: customer was able to reset the transaction history "
                        + "(rows before=" + before + ", after=" + transactions.rows().size() + ")");
    }
}
