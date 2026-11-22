package com.xyzbank.tests;

import com.xyzbank.base.BaseTest;
import com.xyzbank.dataproviders.TestDataProviders;
import com.xyzbank.pages.AccountPage;
import com.xyzbank.pages.CustomerLoginPage;
import io.qameta.allure.Description;
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

/** Practice tasks 1 & 2: POM refactor + data-driven login from CSV. */
@Epic("XYZ Bank")
@Feature("Customer Login")
@Owner("QA Team")
public class CustomerLoginTest extends BaseTest {

    @Test(dataProvider = "customerLogin", dataProviderClass = TestDataProviders.class,
            groups = {"smoke", "regression"})
    @Story("Customer logs in with their name")
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("TMLD4-54") // TC-030
    @Description("Runs once per CSV row: valid customers reach their account, unknown customers cannot log in.")
    public void customerLogin(String customerName, String expectedOutcome, String description) {
        reportTitle("Customer login – " + description);

        CustomerLoginPage loginPage = openApp().goToCustomerLogin();

        if ("should_pass".equals(expectedOutcome)) {
            AccountPage account = loginPage.loginAs(customerName);
            assertEquals(account.welcomeName(), customerName, "Welcome name");
            assertTrue(account.hasAccount(), "Customer should see their account details");
        } else {
            assertFalse(loginPage.isCustomerListed(customerName),
                    "Unknown customer '" + customerName + "' must not be available for login");
        }
    }

    @Test(groups = {"regression"})
    @Story("Customer logs out")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("TMLD4-57") // TC-033
    public void customerCanLogOut() {
        CustomerLoginPage loginPage = openApp().goToCustomerLogin()
                .loginAs("Harry Potter")
                .logout();

        assertTrue(loginPage.isCustomerListed("Harry Potter"), "User is back on the customer selection screen");
    }
}
