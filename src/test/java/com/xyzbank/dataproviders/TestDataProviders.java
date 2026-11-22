package com.xyzbank.dataproviders;

import com.xyzbank.utils.CsvDataReader;
import org.testng.annotations.DataProvider;

/** All CSV-backed data providers in one place. Test data lives in src/test/resources/testdata. */
public final class TestDataProviders {

    private static final String SHOULD_PASS = "should_pass";
    private static final String SHOULD_FAIL = "should_fail";

    private TestDataProviders() {
    }

    @DataProvider(name = "customerLogin")
    public static Object[][] customerLogin() {
        return CsvDataReader.read("testdata/customer_login.csv");
    }

    @DataProvider(name = "validCustomers")
    public static Object[][] validCustomers() {
        return CsvDataReader.read("testdata/add_customer.csv",
                row -> SHOULD_PASS.equals(row.get("expectedOutcome")));
    }

    @DataProvider(name = "invalidCustomers")
    public static Object[][] invalidCustomers() {
        return CsvDataReader.read("testdata/add_customer.csv",
                row -> SHOULD_FAIL.equals(row.get("expectedOutcome")));
    }

    @DataProvider(name = "deposits")
    public static Object[][] deposits() {
        return CsvDataReader.read("testdata/deposit.csv");
    }

    @DataProvider(name = "withdrawals")
    public static Object[][] withdrawals() {
        return CsvDataReader.read("testdata/withdraw.csv");
    }
}
