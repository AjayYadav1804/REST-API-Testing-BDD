# REST Assured API Testing BDD Framework

## Overview
This project implements a BDD-style API testing framework using REST Assured, TestNG, and Allure reporting. It demonstrates various approaches to API testing, including data-driven testing using different file formats (CSV, Excel, JSON) and different ways of handling request payloads.

## Tech Stack
- Java 17
- REST Assured
- TestNG
- Maven
- Allure Reports
- BDD Framework
- Excel (Apache POI)
- JSON parsing libraries
- Log4j2

### API Operations
1. **GET Requests**
   - Basic GET operations
   - Authentication handling
   - Cookie-based requests
   - Parameter validation

2. **POST Requests**
   - Using POJO classes
   - Using JSON files
   - Using JSON Objects
   - Dynamic payload generation
   - UUID-based requests

3. **PUT/PATCH Requests**
   - Update operations
   - Partial updates

4. **File Operations**
   - File upload functionality
   - File handling

5. **End-to-End Flows**
   - Complete API workflows
   - Chained API requests

## Features Tested
- Authentication flows
- Cookie handling
- File upload operations
- Dynamic API requests
- Data-driven testing using:
  - CSV files
  - Excel files
  - JSON files
- CRUD operations (GET, POST, PUT, PATCH)
- End-to-end API flows

## Project Structure

```
RestAssuredAPITestingBDD/
├── src/
│   ├── main/java/
│   ├── test/java/
│   │   ├── com.testautomation.apitesting.pojos/
│   │   │   ├── Booking.java
│   │   │   └── BookingDates.java
│   │   ├── com.testautomation.apitesting.tests/
│   │   │   ├── AllureReportGeneration.java
│   │   │   ├── BasicAuth.java
│   │   │   ├── DataDrivenTesting*.java
│   │   │   ├── EndToEndAPITest.java
│   │   │   └── [Other test files]
│   │   └── com.testautomation.apitesting.utils/
│   │       ├── BaseTest.java
│   │       ├── ExtentReportManager.java
│   │       └── RestAPIHelper.java
│   └── test/resources/
│       ├── TestRunner.xlsx
│       ├── testdata.csv
│       ├── testdatajson.json
│       └── [Other resource files]
└── [Project configuration files]
```

### Key Components

1. **Test Classes** (`com.testautomation.apitesting.tests`)
   - Contain actual test implementations
   - Follow BDD pattern (Given-When-Then)
   - Support different authentication methods
   - Handle various data sources

2. **POJO Classes** (`com.testautomation.apitesting.pojos`)
   - Java objects for request/response serialization
   - Support for JSON mapping

3. **Utility Classes** (`com.testautomation.apitesting.utils`)
   - Base test setup
   - Report management
   - API helper methods

4. **Resources**
   - Test data files (CSV, Excel, JSON)
   - API request bodies
   - Configuration properties

## Running the Project

1. **Prerequisites**
   - Java 17 installed
   - Maven installed
   - Git (optional)

2. **Setup**
   ```bash
   git clone [repository-url]
   cd RestAssuredAPITestingBDD
   mvn clean install
   ```

3. **Running Tests**
   ```bash
   # Run all tests
   mvn test

   # Run specific test class
   mvn test -Dtest=EndToEndAPITest

   # Run with specific suite
   mvn test -DsuiteXmlFile=testng.xml
   ```

4. **Viewing Reports**
   - Allure reports: `allure serve target/allure-results`
   - Test output: Check `test-output` directory
   - Logs: Check `logs` directory
  
   **Test Execution Report:**
   ![Image](https://github.com/user-attachments/assets/b4798a62-9720-429b-9be3-7212bce9f261)
   ![Image](https://github.com/user-attachments/assets/ce4da854-351d-410d-af81-bbad62f9ce00)
   ![Image](https://github.com/user-attachments/assets/1ed51c74-3a75-4fb0-8535-174f6f22f45c)
   ![Image](https://github.com/user-attachments/assets/925cbe21-61e1-4a11-9e03-550bbe1cb3b9)

## Test Execution Notes
- Tests can be run in parallel (configured in TestNG)
- Data-driven tests use resources from `src/test/resources`
- Allure reports provide detailed test execution results
- Logs are generated at `log4j2-sample-[date].log`
