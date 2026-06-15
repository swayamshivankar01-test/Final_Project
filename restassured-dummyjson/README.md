# DummyJSON nopCommerce QA Capstone — Rest Assured + Newman

End-to-end API test automation for the **DummyJSON** API, generated from the Postman collection.

---

## Project Structure

```
restassured-dummyjson/
├── pom.xml
├── run-newman.sh                          ← Newman CLI runner
├── src/
│   └── test/
│       ├── java/com/qa/
│       │   ├── base/
│       │   │   └── BaseTest.java          ← Shared config & token state
│       │   └── tests/
│       │       ├── UserAuthenticationTest.java
│       │       ├── ProductCatalogueTest.java
│       │       ├── ShoppingCartTest.java
│       │       ├── CheckoutPaymentTest.java
│       │       ├── TokenRefreshTest.java
│       │       ├── WishlistTest.java
│       │       ├── OrderManagementTest.java
│       │       └── UserProfileTest.java
│       └── resources/
│           └── testng.xml                 ← TestNG suite (ordered)
```

---

## Prerequisites

| Tool | Version |
|------|---------|
| Java JDK | 11+ |
| Maven | 3.8+ |
| Node.js | 18+ (for Newman) |
| Newman | installed via npm |

---

## Option A — Run with Rest Assured (Maven + TestNG)

### 1. Run all tests
```bash
mvn clean test
```

### 2. Generate Allure report
```bash
mvn allure:report
# Open: target/site/allure-maven-plugin/index.html
```

### 3. Serve Allure report live
```bash
mvn allure:serve
```

---

## Option B — Run with Newman (Postman collection)

### 1. Install Newman & HTML reporter
```bash
npm install -g newman newman-reporter-htmlextra
```

### 2. Place your collection & environment files in the project root
```
DummyJSON_nopCommerce_QA_Capstone_API_Tests_FINAL_postman_collection.json
DummyJSON_nopCommerce_QA_Capstone_Environment.postman_environment.json
```

### 3. Run the script
```bash
chmod +x run-newman.sh
./run-newman.sh
```

### 4. View the report
Open `newman-reports/newman-report-<timestamp>.html` in any browser.

### Manual Newman command (Windows PowerShell)
```powershell
newman run `
  "DummyJSON_nopCommerce_QA_Capstone_API_Tests_FINAL_postman_collection.json" `
  --environment "DummyJSON_nopCommerce_QA_Capstone_Environment.postman_environment.json" `
  --reporters cli,htmlextra `
  --reporter-htmlextra-export "newman-reports/report.html" `
  --reporter-htmlextra-title "DummyJSON QA Capstone" `
  --reporter-htmlextra-darkTheme
```

---

## Test Coverage

| Module | Test Cases |
|--------|-----------|
| User Authentication | TC_001, NEG_001, TC_002, NEG_002, TC_003, NEG_003, NEG_004 |
| Product Catalogue & Search | TC_004–008, NEG_005–008 |
| Shopping Cart | TC_009–012, NEG_009–011 |
| Checkout & Payment | TC_013, TC_014, NEG_012, NEG_013 |
| Token Refresh | TC_015, NEG |
| Wishlist | TC_016, TC_017 |
| Order Management | TC_018, TC_019, NEG_014, NEG_015 |
| User Profile | TC_020–022, NEG_016, NEG_017 |

---

## Key Design Decisions

- **Token sharing** — `accessToken` and `refreshToken` are `static volatile` fields in `BaseTest`, set by `tc001_loginWithValidCredentials` and used by all authenticated tests (mirrors Postman `pm.environment.set`).
- **Test ordering** — `testng.xml` runs Authentication first, then all other suites. `dependsOnMethods` ensures auth-dependent tests skip if login fails.
- **Flexible assertions** — Status codes accept ranges (e.g. `200 or 201`) to handle DummyJSON's simulated responses.
- **Allure annotations** — Every test has `@Epic`, `@Feature`, `@Story`, `@Severity`, and `@Description` for rich reporting.
