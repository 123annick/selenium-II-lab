# Test Plan — XYZ Bank Web Application

| | |
| --- | --- |
| **Document** | Test Plan `TP-XYZBANK-001`, version 1.2 |
| **Project** | XYZ Bank — Test Automation with Selenium WebDriver II |
| **Author** | Annick Ujeneza, QA Engineer |
| **Date** | 22 Nov 2026 |
| **Status** | Final — submitted for review |
| **Related** | Jira/Xray Test Plan [TMLD4-104](https://amali-tech.atlassian.net/browse/TMLD4-104) (manual cycle) · Test Execution [TMLD4-113](https://amali-tech.atlassian.net/browse/TMLD4-113) · [Test workbook and Xray reports](./) · [Automation framework and results](../README.md) · [Live Allure report](https://123annick.github.io/selenium-II-lab/) |

> Format based on IEEE 829 / ISO/IEC/IEEE 29119-3, trimmed to what this project needs.

## 1. Test plan identifier

`TP-XYZBANK-001` — version 1.2 (1.0 initial plan · 1.1 aligned with Jira Xray results · 1.2 automation, Docker, CI and Allure added)

## 2. Introduction

The XYZ Bank app is an AngularJS single-page banking demo with two roles:

- **Bank Manager** — add customers, open accounts, list/search/delete customers.
- **Customer** — log in by choosing their name, view transactions, deposit, withdraw.

This plan describes how the app will be tested manually and automatically against User Story 1 (Bank Manager) and User Story 2 (Customer).

## 3. Objectives

1. Verify every acceptance criterion of User Stories 1 and 2.
2. Find and report defects, especially in input validation and transaction security.
3. Build a maintainable Selenium WebDriver framework (Page Object Model, data-driven tests).
4. Run the suite in Docker and in a GitHub Actions CI pipeline on every push / pull request.
5. Publish an Allure report that both engineers and non-technical stakeholders can read.

## 4. Scope

| In scope | Out of scope |
| --- | --- |
| Customer login / logout | Performance, load and stress testing |
| Add customer (incl. name and post-code validation) | Security / penetration testing (only the "cannot reset history" rule is tested) |
| Open account (Dollar, Pound, Rupee) | Back-end / API testing (the app has no real back end; data lives in browser localStorage) |
| Customers list: search, delete | Accessibility audit, localisation |
| Deposit, withdraw, balance update | Mobile native apps |
| Transactions list and transaction-history integrity | Date-range filter on the Transactions page |
| Compatibility: Chrome (primary), Edge, phone-size screen (375 × 667) | Firefox, Safari |

## 5. Features to be tested (traceability)

Requirement IDs, test case IDs and Jira keys are the same as in the manual-testing workbook (`XYZ_Bank_Test_Workbook.xlsx`) and in Jira project **TMLD4** with Xray (test plan [TMLD4-104](https://amali-tech.atlassian.net/browse/TMLD4-104), test execution [TMLD4-113](https://amali-tech.atlassian.net/browse/TMLD4-113)). Coverage status comes from the Xray *Requirement Traceability Report*: **7 OK, 11 NOK, 1 NOT RUN** out of 19 requirements. Automated tests link to the same Xray keys with `@TmsLink`, so manual and automated results trace to the same requirements.

| Req ID | Jira key | Requirement | Manual test cases | Coverage status (Xray) | Automated (class → Xray key) |
| --- | --- | --- | --- | --- | --- |
| REQ-01 | [TMLD4-65](https://amali-tech.atlassian.net/browse/TMLD4-65) | Manager can add a new customer | TC-001 – TC-006 | OK | `AddCustomerTest` → TMLD4-1 |
| REQ-02 | [TMLD4-66](https://amali-tech.atlassian.net/browse/TMLD4-66) | Names: alphabetic characters only | TC-007 – TC-011 | NOK | `AddCustomerTest` → TMLD4-7 |
| REQ-03 | [TMLD4-67](https://amali-tech.atlassian.net/browse/TMLD4-67) | Post code: numeric characters only | TC-012 – TC-015 | NOK | `AddCustomerTest` → TMLD4-7 |
| REQ-04 | [TMLD4-68](https://amali-tech.atlassian.net/browse/TMLD4-68) | Manager can create an account for an existing customer | TC-016 – TC-021 | OK | `AccountManagementTest` → TMLD4-16 |
| REQ-05 | [TMLD4-69](https://amali-tech.atlassian.net/browse/TMLD4-69) | No access until an account is created | TC-022, TC-023 | OK | `AccountManagementTest` → TMLD4-22, TMLD4-23 |
| REQ-06 | [TMLD4-70](https://amali-tech.atlassian.net/browse/TMLD4-70) | Manager can delete customer accounts | TC-024 – TC-026, TC-029 | NOK | `AccountManagementTest` → TMLD4-24 |
| REQ-07 | [TMLD4-71](https://amali-tech.atlassian.net/browse/TMLD4-71) | Deleted customer can no longer access the account | TC-027, TC-028 | OK | `AccountManagementTest` → TMLD4-24 |
| REQ-08 | [TMLD4-72](https://amali-tech.atlassian.net/browse/TMLD4-72) | Customer can view recent transactions | TC-036 – TC-040 | OK | `TransactionTest` → TMLD4-31 |
| REQ-09 | [TMLD4-73](https://amali-tech.atlassian.net/browse/TMLD4-73) | Customer can enter a deposit amount | TC-044, TC-049 | NOK | — (browser validation) |
| REQ-10 | [TMLD4-74](https://amali-tech.atlassian.net/browse/TMLD4-74) | Deposit amount validated (positive value) | TC-045 – TC-048 | NOK | `DepositTest` → TMLD4-41 |
| REQ-11 | [TMLD4-75](https://amali-tech.atlassian.net/browse/TMLD4-75) | Balance updated after a deposit | TC-043, TC-050 | OK | `DepositTest` → TMLD4-41 |
| REQ-12 | [TMLD4-76](https://amali-tech.atlassian.net/browse/TMLD4-76) | Customer can enter a withdrawal amount | TC-057, TC-058 | NOK | — (browser validation) |
| REQ-13 | [TMLD4-77](https://amali-tech.atlassian.net/browse/TMLD4-77) | Withdrawal validated (positive, sufficient balance) | TC-052 – TC-056 | NOK | `WithdrawTest` → TMLD4-50 |
| REQ-14 | [TMLD4-78](https://amali-tech.atlassian.net/browse/TMLD4-78) | Balance updated after a withdrawal | TC-051, TC-059 | OK | `WithdrawTest` → TMLD4-50 |
| REQ-15 | [TMLD4-79](https://amali-tech.atlassian.net/browse/TMLD4-79) | Customer cannot reset or alter transaction history | TC-041, TC-042 | NOK | `TransactionTest` → TMLD4-52 |
| REQ-16 | [TMLD4-80](https://amali-tech.atlassian.net/browse/TMLD4-80) | Customer can log in and log out | TC-030 – TC-034 | NOK | `CustomerLoginTest` → TMLD4-54, TMLD4-57 |
| REQ-17 | [TMLD4-81](https://amali-tech.atlassian.net/browse/TMLD4-81) | Only a bank manager can reach manager functions | TC-035 | NOK | — (manual) |
| REQ-18 | [TMLD4-82](https://amali-tech.atlassian.net/browse/TMLD4-82) | Compatibility: Chrome, Edge, phone-size screen | TC-062, TC-063 | NOT RUN | — (manual) |
| REQ-19 | [TMLD4-83](https://amali-tech.atlassian.net/browse/TMLD4-83) | Labels and messages correctly spelled and styled | TC-060, TC-061 | NOK | — (manual) |

## 6. Test approach

| Level / type | How |
| --- | --- |
| Manual functional testing | 64 scripted test cases managed in Jira Xray (63 executed) — see [Section 3](../README.md#3-part-b--manual-testing) |
| Automated UI regression | Selenium WebDriver 4 + TestNG, Page Object Model |
| Data-driven testing | CSV files → TestNG `@DataProvider` (valid, boundary and negative values) |
| Test design techniques | Equivalence partitioning (valid / invalid names, amounts), boundary values (0, 1, exact balance, balance + 1), state transition (customer → account → deleted) |
| Smoke vs. regression | TestNG groups: `smoke`, `regression`, `known-defect` |
| Reporting | Allure (steps, screenshots on failure, severity, features/stories, trends) |
| CI | GitHub Actions: build Docker image → run suite against Selenium Grid container → Allure report artifact + GitHub Pages |

## 7. Test environment

| Item | Value |
| --- | --- |
| URL | https://www.globalsqa.com/angularJs-protractor/BankingProject/ |
| Browsers | Google Chrome 131 (64-bit) — primary; Microsoft Edge; phone-size viewport 375 × 667 (iPhone SE) |
| OS / screen | Windows 11, 1920 × 1080 (manual and local runs); Ubuntu (GitHub Actions / Docker) |
| Tools | Jira + Xray (test management, defects), Java 17, Maven 3.9, Selenium 4, TestNG 7, Allure 2, Docker, GitHub Actions, IntelliJ IDEA |
| Test data | Pre-loaded customers (Harry Potter, Hermoine Granger, Ron Weasly, Albus Dumbledore, Neville Longbottom) + generated customers |

## 8. Entry and exit criteria

**Entry:** application URL reachable · user stories and acceptance criteria agreed · environment and tools installed · test data prepared.

**Exit:** 100 % of planned test cases executed · 100 % of smoke and ≥ 95 % of regression tests passing · no open Critical/Blocker defect without an agreed decision · Allure report published · defects logged with steps, evidence and severity.

**Suspension:** app unavailable or a Blocker defect prevents login for both roles. **Resumption:** fix deployed and smoke suite green.

## 9. Risks and mitigations

| Risk | Impact | Mitigation |
| --- | --- | --- |
| Public demo site down / slow | Tests fail for reasons unrelated to code | Explicit waits, retry in CI only on infrastructure errors, report categorises timeouts separately |
| AngularJS re-renders forms (Deposit ↔ Withdrawl) | Flaky tests | Wait for the correct form label before typing |
| Data shared between tests | Order-dependent results | New browser per test (fresh localStorage) + generated unique customers |
| Known product defects break the CI gate | Pipeline always red | Defect-proving tests in `known-defect` group, run nightly (`full` suite), linked to the Jira bug with `@Issue` |
| Locator changes | High maintenance | Locators centralised in page objects; stable `ng-click` / `ng-model` / `id` attributes |

## 10. Roles and responsibilities

| Role | Responsibility |
| --- | --- |
| QA Engineer (Annick) | Test plan, manual testing, automation framework, CI/CD, reporting |
| Trainer / Reviewer | Review plan and code, sign-off |
| Product Owner | Clarify acceptance criteria, prioritise defects |

## 11. Schedule

| Phase | Effort |
| --- | --- |
| Requirement analysis & test plan | 0.5 day |
| Manual test design & execution | 1 day |
| Framework set-up (Maven, POM, config, data) | 1 day |
| Automated tests | 1.5 days |
| Docker + GitHub Actions + Allure | 1 day |
| Defect reporting, review, final report | 0.5 day |

## 12. Deliverables

Test plan (this document) · manual test cases & execution results · defect reports · automation source code (GitHub) · Dockerfile & docker-compose · GitHub Actions workflow · Allure report.

## 13. Defect management

| Severity | Meaning |
| --- | --- |
| Blocker | Core flow impossible (e.g. nobody can log in) |
| Critical | Security or data-integrity failure (e.g. unauthenticated access, history deleted) |
| High | Required function missing or destructive behaviour |
| Medium | Acceptance criterion violated, workaround exists |
| Low | Cosmetic or feedback issue, no money moves |

Defects are logged in Jira (project TMLD4) with: ID, description, steps, expected vs. actual, environment, severity and priority (each with a reason), linked test case and requirement, and screenshot evidence.

## Approval

| Role | Name | Date |
| --- | --- | --- |
| Prepared by | Annick Ujeneza | 22 Nov 2026 |
| Reviewed and approved by | Trainer | |
