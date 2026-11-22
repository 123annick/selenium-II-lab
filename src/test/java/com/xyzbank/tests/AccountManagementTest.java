package com.xyzbank.tests;

import com.xyzbank.base.BaseTest;
import com.xyzbank.pages.AccountPage;
import com.xyzbank.pages.CustomerLoginPage;
import com.xyzbank.pages.CustomersPage;
import com.xyzbank.pages.ManagerPage;
import com.xyzbank.utils.TestDataGenerator;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/** User Story 1 – Creating and deleting accounts. */
@Epic("XYZ Bank")
@Feature("Bank Manager - Accounts")
@Owner("QA Team")
public class AccountManagementTest extends BaseTest {

    @Test(groups = {"smoke", "regression"})
    @Story("Create account for an existing customer")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("TMLD4-16") // TC-016
    public void managerCanOpenAccountForNewCustomer() {
        String first = TestDataGenerator.name();
        String last = TestDataGenerator.name();

        ManagerPage manager = openApp().goToManagerLogin();
        manager.openAddCustomer().addCustomer(first, last, TestDataGenerator.postCode());
        String accountNumber = manager.openOpenAccount().openAccount(first + " " + last, "Dollar");

        assertFalse(accountNumber.isEmpty(), "Account number should be returned");
        assertTrue(manager.openCustomers().accountNumbersOf(first).contains(accountNumber),
                "Account number should be shown in the Customers table");
    }

    @Test(groups = {"regression"})
    @Story("Customer without account cannot access banking features")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("TMLD4-22") // TC-022
    public void customerWithoutAccountCannotTransact() {
        String first = TestDataGenerator.name();
        String last = TestDataGenerator.name();

        openApp().goToManagerLogin().openAddCustomer()
                .addCustomer(first, last, TestDataGenerator.postCode());

        AccountPage account = openApp().goToCustomerLogin().loginAs(first + " " + last);

        assertTrue(account.isNoAccountMessageShown(), "'Please open an account with us.' should be shown");
        assertFalse(account.hasAccount(), "No account details should be available");
    }

    @Test(groups = {"regression"})
    @Story("Customer with new account can access it")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("TMLD4-23") // TC-023
    public void customerCanAccessNewlyCreatedAccount() {
        String first = TestDataGenerator.name();
        String last = TestDataGenerator.name();

        ManagerPage manager = openApp().goToManagerLogin();
        manager.openAddCustomer().addCustomer(first, last, TestDataGenerator.postCode());
        String accountNumber = manager.openOpenAccount().openAccount(first + " " + last, "Pound");

        AccountPage account = openApp().goToCustomerLogin().loginAs(first + " " + last);

        assertTrue(account.hasAccount(), "Account should be available after creation");
        assertEquals(account.accountNumber(), accountNumber, "Account number");
        assertEquals(account.balance(), 0, "New account balance");
    }

    @Test(groups = {"smoke", "regression"})
    @Story("Delete customer account")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("TMLD4-24") // TC-024
    public void deletedCustomerCanNoLongerAccessAccount() {
        String first = TestDataGenerator.name();
        String last = TestDataGenerator.name();

        ManagerPage manager = openApp().goToManagerLogin();
        manager.openAddCustomer().addCustomer(first, last, TestDataGenerator.postCode());
        manager.openOpenAccount().openAccount(first + " " + last, "Rupee");

        CustomersPage customers = manager.openCustomers().deleteCustomer(first, last);
        assertFalse(customers.isCustomerPresent(first, last), "Customer should be removed from the table");

        CustomerLoginPage login = openApp().goToCustomerLogin();
        assertFalse(login.isCustomerListed(first + " " + last),
                "Deleted customer must not be able to log in");
    }
}
