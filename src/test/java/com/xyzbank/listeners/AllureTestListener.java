package com.xyzbank.listeners;

import com.xyzbank.config.ConfigReader;
import com.xyzbank.driver.DriverManager;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

/**
 * - On failure: attaches screenshot, page source and URL to the failing test in Allure
 *   (runs right after the test method, BEFORE @AfterMethod quits the browser).
 * - On suite start: writes environment.properties and categories.json into allure-results.
 */
public class AllureTestListener implements IInvokedMethodListener, ISuiteListener {

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {
        if (!method.isTestMethod() || result.isSuccess() || !DriverManager.hasDriver()) {
            return;
        }
        WebDriver driver = DriverManager.getDriver();
        try {
            byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Screenshot on failure", "image/png", new ByteArrayInputStream(png), "png");
            Allure.addAttachment("Page URL", "text/plain", driver.getCurrentUrl());
            Allure.addAttachment("Page source", "text/html",
                    new ByteArrayInputStream(driver.getPageSource().getBytes(StandardCharsets.UTF_8)), "html");
        } catch (Exception e) {
            // An open alert or a dead session must never hide the real test failure
            Allure.addAttachment("Attachment error", "text/plain", e.toString());
        }
    }

    @Override
    public void onStart(ISuite suite) {
        Path results = Paths.get(System.getProperty("allure.results.directory", "target/allure-results"));
        try {
            Files.createDirectories(results);

            Properties env = new Properties();
            env.setProperty("Application", ConfigReader.baseUrl());
            env.setProperty("Browser", ConfigReader.browser());
            env.setProperty("Headless", String.valueOf(ConfigReader.headless()));
            env.setProperty("Execution", ConfigReader.gridUrl().isEmpty() ? "Local" : "Selenium Grid");
            env.setProperty("Suite", suite.getName());
            env.setProperty("Java", System.getProperty("java.version"));
            env.setProperty("OS", System.getProperty("os.name"));
            try (OutputStream out = Files.newOutputStream(results.resolve("environment.properties"))) {
                env.store(out, "Allure environment");
            }

            try (InputStream categories = getClass().getClassLoader().getResourceAsStream("categories.json")) {
                if (categories != null) {
                    Files.copy(categories, results.resolve("categories.json"), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (IOException e) {
            System.err.println("Could not write Allure metadata: " + e.getMessage());
        }
    }
}
