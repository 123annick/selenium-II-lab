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

/** User Story 2 – Depositing funds (data-driven). */
@Epic("XYZ Bank")
@Feature("Customer - Deposit")
@Owner("QA Team")
public class DepositTest extends BaseTest {

    private static final String CUSTOMER = "Harry Potter";
    private static final String SUCCESS = "Deposit Successful";

    @Test(dataProvider = "deposits", dataProviderClass = TestDataProviders.class,
            groups = {"smoke", "regression"})
    @Story("Deposit amount is validated and balance updated")
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("TMLD4-41") // TC-043
    public void deposit(String amount, String expectedOutcome, String description) {
        reportTitle("Deposit – " + description + " (" + amount + ")");

        AccountPage account = openApp().goToCustomerLogin().loginAs(CUSTOMER);
        int balanceBefore = account.balance();

        account.deposit(amount);

        if ("should_pass".equals(expectedOutcome)) {
            assertEquals(account.statusMessage(), SUCCESS, "Status message");
            assertEquals(account.waitForBalance(balanceBefore + Integer.parseInt(amount)),
                    balanceBefore + Integer.parseInt(amount), "Balance after deposit");
        } else {
            assertNotEquals(account.statusMessage(), SUCCESS,
                    "Validation defect: invalid deposit '" + amount + "' was accepted");
            assertEquals(account.balance(), balanceBefore, "Balance must not change for an invalid deposit");
        }
    }
}
