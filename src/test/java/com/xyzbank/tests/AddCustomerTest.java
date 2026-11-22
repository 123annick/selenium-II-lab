package com.xyzbank.tests;

import com.xyzbank.base.BaseTest;
import com.xyzbank.dataproviders.TestDataProviders;
import com.xyzbank.pages.AddCustomerPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Issues;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/** User Story 1 – Adding customers. */
@Epic("XYZ Bank")
@Feature("Bank Manager - Customers")
@Owner("QA Team")
public class AddCustomerTest extends BaseTest {

    @Test(dataProvider = "validCustomers", dataProviderClass = TestDataProviders.class,
            groups = {"smoke", "regression"})
    @Story("Add customer with valid data")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("TMLD4-1") // TC-001
    public void addCustomerWithValidData(String firstName, String lastName, String postCode,
                                         String expectedOutcome, String description) {
        reportTitle("Add customer – " + description);

        AddCustomerPage addCustomer = openApp().goToManagerLogin().openAddCustomer();
        String alert = addCustomer.addCustomer(firstName, lastName, postCode);

        assertNotNull(alert, "A confirmation alert is expected");
        assertTrue(alert.contains("Customer added successfully"), "Unexpected alert: " + alert);
        assertTrue(addCustomer.openCustomers().isCustomerPresent(firstName, lastName),
                "New customer should be listed in the Customers table");
    }

    /**
     * Acceptance criteria: names = letters only, post code = digits only.
     * The current build has no such validation, so these are expected to fail
     * until the defect is fixed -> kept in the "known-defect" group (not a CI gate).
     */
    @Test(dataProvider = "invalidCustomers", dataProviderClass = TestDataProviders.class,
            groups = {"known-defect"})
    @Story("Reject customer with invalid data")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("TMLD4-7") // TC-007
    @Issues({
            @Issue("TMLD4-84"), @Issue("TMLD4-85"), @Issue("TMLD4-86"), @Issue("TMLD4-87"), // BUG-001..004
            @Issue("TMLD4-88"), @Issue("TMLD4-89"), @Issue("TMLD4-90")                       // BUG-005..007
    })
    @Description("Names must contain only letters and post codes only digits.")
    public void addCustomerWithInvalidDataIsRejected(String firstName, String lastName, String postCode,
                                                     String expectedOutcome, String description) {
        reportTitle("Reject customer – " + description);

        AddCustomerPage addCustomer = openApp().goToManagerLogin().openAddCustomer();
        String alert = addCustomer.addCustomer(firstName, lastName, postCode);

        assertNull(alert, "Validation defect: invalid customer was accepted -> " + alert);
        assertFalse(addCustomer.openCustomers().isCustomerPresent(firstName, lastName),
                "Validation defect: invalid customer appears in the Customers table");
    }
}
