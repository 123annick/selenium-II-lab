# Test Plan – XYZ Bank

## 1. Introduction

*What is being tested and why this document exists. A test plan is the "game plan" for testing: what we test, how, who does it, when, and when we stop.*

This plan describes how the XYZ Bank web application will be tested, manually and with automation, so that the Bank Manager features (add customer, open account, delete account) and the Customer features (login, transactions, deposit, withdraw) meet the acceptance criteria of the user stories. Test cases, the requirement traceability matrix and the defect log will be produced during the test cycle and kept in the test management tool and a companion workbook.

| Item                         | Detail                                                                                                                                       | Meaning                                   |
|------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------|
| Purpose                      | Describe the scope, approach, resources, schedule and exit rules for testing XYZ Bank, both manual and automated                             | Why we are testing                        |
| Application under test (AUT) | XYZ Bank – https://www.globalsqa.com/angularJs-protractor/BankingProject/ (public AngularJS demo banking app; data is stored in the browser) | The website being tested                  |
| Source of requirements       | Lab brief "Test Automation with Selenium WebDriver II": User Story 1 (Bank Manager) and User Story 2 (Customer)                              | Where the rules we test against come from |

## 2. Test Objectives

*The goals of testing – what we want to prove works.*

| Objective          | Detail                                                                                                                                                                                  | Meaning                                          |
|--------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------|
| Functional         | Confirm a bank manager can add customers, open accounts and delete accounts; confirm a customer can log in, view transactions, deposit and withdraw, and that balances update correctly | Does each feature do what it should?             |
| Validation         | Confirm names accept letters only, post codes digits only, and amounts must be positive with sufficient balance                                                                         | Does the app reject wrong input?                 |
| Access and history | Confirm customers cannot reset or alter their transaction history and cannot use an account before it is created or after it is deleted                                                 | Is money and history protected?                  |
| Automation         | Build a maintainable Selenium WebDriver framework (Page Object Model, data-driven tests), run it in Docker and in a CI pipeline, and publish an Allure report                           | Can the checks be repeated quickly and reliably? |
| Business goal      | Give stakeholders confidence that customers' money and records are handled correctly, and clear information to decide on release                                                        | Why this matters to the bank                     |

## 3. Scope

*The boundary of testing – what we WILL test (in scope) and what we will NOT test (out of scope).*

### In scope

- Bank Manager – Add Customer: adding new customers; names contain letters only; post codes contain digits only
- Bank Manager – Open Account: creating accounts for customers already in the system; customers cannot access their accounts until an account is created
- Bank Manager – Delete Account: deleting customer accounts; a deleted customer can no longer access the account
- Customer – Login and Logout (from the project overview)
- Customer – Transactions: viewing a list of recent transactions
- Customer – Deposit: entering the amount, validation (positive value), balance update
- Customer – Withdraw: entering the amount, validation (positive value, sufficient balance), balance update
- Customer – Transaction security: transaction history cannot be reset or altered
- Test automation of these features, execution in Docker, CI pipeline and Allure reporting

### Out of scope

- Features not described in the two user stories (for example manager authentication and the date-range filter on the Transactions page)
- Cross-browser and mobile compatibility – testing uses Google Chrome
- UI styling and wording checks
- Performance, load and stress testing
- Penetration testing (only the access and history rules from the user stories are checked)
- Back-end or API testing – the demo app has no real back end
- Full accessibility audit and localisation

Reason: these are not covered by the two user stories in the lab brief, or need tools, access or time beyond this test cycle.

## 4. Test Coverage

*The parts of the application that will be tested, taken from the lab brief: the two user stories and customer login from the project overview.*

| User                        | Area of the app      | What will be covered                                                                       |
|-----------------------------|----------------------|--------------------------------------------------------------------------------------------|
| Bank Manager (User Story 1) | Add Customer         | Adding new customers and validation of the customer form (names and post code)             |
|                             | Open Account         | Creating accounts for customers already in the system, and access before an account exists |
|                             | Delete Account       | Deleting customer accounts, and access after deletion                                      |
| Customer (project overview) | Login and Logout     | Customer access to their own accounts                                                      |
| Customer (User Story 2)     | Transactions         | Viewing recent transactions                                                                |
|                             | Deposit              | Entering and validating deposit amounts, and balance update                                |
|                             | Withdraw             | Entering and validating withdrawal amounts, sufficient balance, and balance update         |
|                             | Transaction security | Customers cannot reset or alter their transaction history                                  |

Coverage is tracked during test design: every acceptance criterion is linked to at least one test case in the traceability matrix.

## 5. Test Approach / Strategy

*HOW we will test – the levels, kinds of tests and methods used.*

| Area              | Approach                                                                                                                                                                                                                                                                                                                                                                    | Meaning                                                                                                                |
|-------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------|
| Test levels       | System testing of the whole web app, plus acceptance-style checks against each acceptance criterion                                                                                                                                                                                                                                                                         | How big a piece is tested at once. System testing means testing the whole website together                             |
| Test types        | Functional (positive and negative), input validation, access and history rules, automated regression                                                                                                                                                                                                                                                                        | The kinds of checks: does it work, does it reject bad input, is it safe                                                |
| Techniques        | Black-box requirement-based testing (at least one test per acceptance criterion); equivalence partitioning for names, post codes and amounts (letters, digits, special characters); boundary values for amounts (zero, smallest valid amount, exact balance, balance plus one); state transition (customer → account → deleted); exploratory sessions on history and access | Equivalence partitioning: one test per group of similar inputs. Boundary values: testing at the edges, where bugs hide |
| Manual testing    | Test cases are designed from the requirements, stored in the test management tool, linked to their requirement and executed in a test execution cycle with a result, comment and screenshot per run                                                                                                                                                                         | A person runs each test and records what happened                                                                      |
| Automation        | Java, Selenium WebDriver, TestNG and Maven. Page Object Model: one class per screen with locators and actions; tests contain only logic and assertions. Explicit waits only. A fresh browser per test so tests do not depend on each other. Data-driven tests read their inputs from CSV files, reusing the data designed for the manual tests                              | A program repeats the checks quickly; POM keeps the code easy to maintain when the screens change                      |
| Test suites       | Smoke (a quick check of the core flows), Regression (the CI quality gate), Full (everything, including tests that track open defects)                                                                                                                                                                                                                                       | Groups of tests run for different purposes                                                                             |
| Containers and CI | Automated tests run in a Docker container against a Selenium Grid browser container. A CI pipeline runs the suite on every change, nightly and on demand, then publishes the Allure report                                                                                                                                                                                  | The same environment everywhere, and results on every change                                                           |
| Reporting         | Allure: steps per test, screenshot on failure, severity, links to tests and defects in the test management tool, failure categories, trend history                                                                                                                                                                                                                          | Clear results for engineers and non-technical readers                                                                  |
| Risk analysis     | High: money moving without validation, transaction history that can be changed, customers reaching accounts that do not exist or were deleted. Medium: wrong balance after a transaction, invalid customer data stored. High-risk areas are tested first and deepest                                                                                                        | Thinking ahead about what could go wrong, and testing the riskiest things first                                        |

### Project risks and mitigations

| Risk                                                                                    | Mitigation                                                                                                         |
|-----------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------|
| The public demo site is slow or unavailable                                             | Explicit waits; timeouts reported separately; re-run before logging a defect                                       |
| Dynamic pages (AngularJS) re-render or load data late, causing unstable automated tests | Wait for page-specific conditions instead of fixed delays; investigate every unstable test before logging a defect |
| Shared application data makes tests depend on each other                                | New browser session per test and unique generated test data                                                        |
| Open defects make every CI run fail and hide new failures                               | Tests that track open defects are grouped separately, run in the Full suite and linked to their defect             |
| Screens change and break locators                                                       | All locators kept in page objects, using stable attributes                                                         |
| Limited time for one tester to cover manual and automated work                          | Risk-based prioritisation; automate the regression set first                                                       |

## 6. Resources & Roles

*WHO does what in the testing team.*

| Role                   | Responsibilities                                                          | Meaning                                             |
|------------------------|---------------------------------------------------------------------------|-----------------------------------------------------|
| Test lead              | Planning, estimation, monitoring, reporting                               | Plans and organises the testing                     |
| QA engineer (manual)   | Test design, manual execution, defect logging and retesting               | Writes and runs the tests                           |
| QA automation engineer | Automation framework, automated tests, Docker, CI pipeline, Allure report | Builds and maintains the automated checks           |
| Development team       | Fixes defects, triages them with QA                                       | Builds the app and fixes bugs                       |
| Product owner          | Reviews scope, prioritises defects, accepts the final report              | Decides what the app must do and accepts the result |

## 7. Test Environment & Test Data

*WHERE testing happens – the computer, browser, website and example data used.*

| Item          | Detail                                                                                                                                                                                                                                                                                    | Meaning                                        |
|---------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------|
| Hardware / OS | Desktop PC with Windows (manual and local automated runs); Linux runners for the CI pipeline                                                                                                                                                                                              | The computer and its operating system          |
| Browsers      | Google Chrome, latest stable (manual and local automated runs); Chrome in a Selenium Grid container for CI                                                                                                                                                                                | The program used to open the website           |
| Network / URL | Public internet; https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login                                                                                                                                                                                                    | The internet connection and web address        |
| Test data     | Customers pre-loaded in the demo app; new customers created during testing; valid and invalid names (letters, digits, special characters); valid and invalid post codes; amounts covering valid, zero, negative and boundary values. The full data per test is defined during test design | The example information typed into the website |
| Data reset    | The app keeps its data in the browser, so a new browser session starts from the pre-loaded data                                                                                                                                                                                           | Tests do not affect each other                 |

## 8. Tools

*The software used to organise and carry out the testing.*

- **Test management:** Jira with Xray (requirements, test cases, test sets, test plan, test execution, defects, traceability reports); Excel workbook as a companion copy
- **Automation:** Java, Maven, Selenium WebDriver, TestNG, CSV test data files
- **Reporting:** Allure Report
- **Containers and CI:** Docker, Docker Compose, Selenium Grid browser image, GitHub Actions
- **Supporting tools:** code editor, Git and GitHub, browser developer tools, screenshots for evidence

## 9. Schedule & Estimation (Sprints)

*WHEN each activity happens and how long it should take. Estimation is a careful guess of the time needed; a sprint is a short, fixed block of work time (here, one week).*

Testing is planned in three one-week sprints (3 weeks in total), starting once this plan is approved. Work is sized in story points (1 = very small, 8 = large).

| Sprint            | Sprint goal                       | Work in the sprint                                                                                                                                          | Story points |
|-------------------|-----------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------|
| Sprint 1 (week 1) | Design and run manual tests       | Review requirements and risks; design test cases and link them to requirements; prepare test data; execute the manual test cycle; log defects with evidence | 13           |
| Sprint 2 (week 2) | Build the automation framework    | Project set-up, Page Object Model, configuration, data-driven tests for both user stories, Allure integration                                               | 13           |
| Sprint 3 (week 3) | Containers, CI, retest and report | Docker set-up, CI pipeline, published Allure report, stabilise automated tests, retest fixed defects, test summary report                                   | 8            |
| **Total**         |                                   |                                                                                                                                                             | **34**       |

Estimation method: each piece of work is sized in story points relative to the others. Automation and CI are larger because they include set-up and stabilisation. Retesting depends on when fixes are delivered, so Sprint 3 can move.

## 10. Entry, Suspension & Exit Criteria

*The rules for when testing can START, when it should PAUSE and CONTINUE, and when it is FINISHED.*

**Entry criteria – testing can start when:**

- This test plan is approved
- User stories and acceptance criteria are available and split into requirements
- The application URL is reachable
- Test cases are written, reviewed and linked to requirements
- Test data and tools are ready

**Suspension criteria – pause testing when:** the site is down, or a blocking defect stops a whole user story (for example, nobody can log in).

**Resumption criteria – continue testing when:** the site is back, or the blocking defect is fixed and the smoke suite passes again.

**Exit criteria – testing is complete when:**

- 100% of planned in-scope test cases are executed and every requirement shows coverage in the traceability matrix
- Every failed test is linked to a logged defect
- The automated smoke and regression suites pass in Docker and in the CI pipeline
- No Critical defect is open without a decision from the product owner
- The Allure report is published and the test summary report is shared

## 11. Defect Management

*How bugs are recorded, followed up and closed. A defect (bug) is something that does not work as it should.*

Bug life cycle – the stages a bug goes through:

1.  **New** – the tester finds the bug and logs it, linked to the test run, test case and requirement
2.  **Assigned** – the bug is confirmed and given to a developer
3.  **In Progress / Fixed** – the developer works on it and fixes it
4.  **Retest** – the tester checks the fix, manually and with the automated test
5.  **Closed** – the fix works, or **Reopened** if it does not

| Severity – how BAD the problem is | Meaning                                                               |
|-----------------------------------|-----------------------------------------------------------------------|
| Critical                          | Security or data-integrity failure with no workaround                 |
| High                              | A main feature is missing, broken or destructive                      |
| Medium                            | A rule is broken or invalid data is accepted, but a workaround exists |
| Low                               | Cosmetic or feedback issue; no money moves                            |

| Priority – how SOON it should be fixed | Meaning                 |
|----------------------------------------|-------------------------|
| Highest                                | Fix immediately         |
| High                                   | Fix before release      |
| Medium                                 | Fix in the next release |
| Low                                    | Fix when possible       |

## 12. Deliverables, Monitoring & Approvals

*The documents produced, how progress is checked, and who signs the plan off.*

| When           | Deliverable                                                                                                                                                          |
|----------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Before testing | Test Plan (this document)                                                                                                                                            |
| During testing | Test cases with results, requirement traceability matrix, defect log and evidence screenshots; automation source code, test data files, Docker files and CI pipeline |
| After testing  | Requirement and defect traceability reports; published Allure report; test summary report                                                                            |

**Monitoring & control** – checking progress against the plan and fixing course when it goes off track. The test management tool shows executed, passed and failed tests per requirement; the CI pipeline and the Allure trend show automated results on every change. Progress is compared weekly with the schedule; if testing falls behind, high-risk tests are done first or the plan is adjusted with the product owner.

**Approvals**

| Role                       | Signature | Date |
|----------------------------|-----------|------|
| Prepared by: QA Engineer   |           |      |
| Reviewed by: Test Lead     |           |      |
| Approved by: Product Owner |           |      |

## 13. Glossary

*Short meanings of the testing words used in this plan.*

| Term                        | Meaning                                                                                               |
|-----------------------------|-------------------------------------------------------------------------------------------------------|
| Acceptance criteria         | The checklist that says a user story is done correctly                                                |
| Allure report               | An HTML test report showing results, steps, screenshots and trends                                    |
| Black-box testing           | Testing like a user, without looking inside the code                                                  |
| Boundary value              | A value at the edge of what is allowed, e.g. withdrawing exactly the balance                          |
| CI (Continuous Integration) | Running the tests automatically every time code is changed                                            |
| Data-driven test            | One test that runs once for each row of data in a file                                                |
| Defect / Bug                | Something that does not work the way it should                                                        |
| Docker container            | A small, isolated package that runs software the same way on any computer                             |
| Entry / Exit criteria       | Conditions to start testing / rules that say testing is finished                                      |
| Equivalence partitioning    | Grouping similar inputs and testing one from each group                                               |
| Jira / Xray                 | Jira is a work-tracking tool; Xray is a Jira app for managing test cases, executions and traceability |
| Page Object Model (POM)     | A design where each screen has one class holding its locators and actions                             |
| Regression testing          | Re-running tests after a change to make sure nothing else broke                                       |
| RTM                         | Requirement Traceability Matrix: links each requirement to its tests, results and bugs                |
| Selenium WebDriver          | A tool that controls a real browser from code                                                         |
| Severity / Priority         | How bad a bug is / how soon it should be fixed                                                        |
| Test case / Test data       | A step-by-step check with its expected result / the example values used in it                         |
| User story                  | A short description of what a user wants to do and why                                                |
