package com.xyzbank.base;

import com.xyzbank.driver.DriverManager;
import com.xyzbank.pages.HomePage;
import io.qameta.allure.Allure;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * WebDriver life-cycle for every test.
 * A fresh browser per test method (@BeforeMethod / @AfterMethod) is used instead of
 * @BeforeTest / @AfterTest because the XYZ Bank app keeps its data in the browser's
 * localStorage: a new browser = clean, predictable data, and every data-driven row
 * is fully independent (safe to run in parallel).
 */
public abstract class BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverManager.initDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }

    /** Entry point for every test: opens the application home page. */
    protected HomePage openApp() {
        return new HomePage().open();
    }

    /** Gives each data-driven invocation a readable name in the Allure report. */
    protected void reportTitle(String title) {
        Allure.getLifecycle().updateTestCase(testCase -> testCase.setName(title));
    }
}
