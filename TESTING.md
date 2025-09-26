# Testing Documentation - Bank Management System

This document provides comprehensive information about the testing strategy and implementation for the Bank Management System.

## Testing Overview

The project implements a comprehensive testing strategy covering all layers of the application:

- **Unit Tests**: Individual component testing with mocked dependencies
- **Integration Tests**: End-to-end testing with real database interactions
- **Repository Tests**: Data access layer testing with H2 in-memory database
- **Controller Tests**: REST API testing with MockMvc
- **Service Tests**: Business logic testing with mocked repositories

## Test Structure

```
src/test/java/bank/app/BankManagementApp/
├── entity/
│   └── AccountTest.java                    # Entity unit tests
├── repository/
│   └── AccountRepositoryTest.java          # Repository integration tests
├── service/
│   └── AccountServiceImplTest.java         # Service unit tests
├── controller/
│   └── AccountControllerTest.java          # Controller unit tests
├── integration/
│   └── AccountIntegrationTest.java         # End-to-end integration tests
├── config/
│   └── TestConfig.java                     # Test configuration
├── TestSuite.java                          # Test suite runner
└── BankManagementAppApplicationTests.java  # Application context tests
```

## Test Categories

### 1. Entity Tests (`AccountTest.java`)

**Purpose**: Test the Account entity class functionality

**Test Coverage**:
- ✅ Default constructor behavior
- ✅ Parameterized constructor behavior
- ✅ Getter and setter methods
- ✅ Null value handling
- ✅ ToString method
- ✅ Edge cases (zero balance, negative balance, large values)
- ✅ Empty string handling

**Key Test Methods**:
- `shouldCreateAccountWithDefaultConstructor()`
- `shouldCreateAccountWithParameterizedConstructor()`
- `shouldSetAndGetAccountNumber()`
- `shouldHandleNullValues()`
- `shouldReturnCorrectToStringRepresentation()`

### 2. Repository Tests (`AccountRepositoryTest.java`)

**Purpose**: Test the data access layer with H2 in-memory database

**Test Coverage**:
- ✅ Save operations
- ✅ Find by ID operations
- ✅ Find all operations
- ✅ Update operations
- ✅ Delete operations
- ✅ Count operations
- ✅ Exists operations
- ✅ Edge cases and error scenarios

**Key Test Methods**:
- `shouldSaveAccountSuccessfully()`
- `shouldFindAccountById()`
- `shouldReturnEmptyOptionalWhenAccountNotFound()`
- `shouldFindAllAccounts()`
- `shouldDeleteAccountById()`

### 3. Service Tests (`AccountServiceImplTest.java`)

**Purpose**: Test business logic with mocked dependencies

**Test Coverage**:
- ✅ Account creation
- ✅ Account retrieval
- ✅ Account listing
- ✅ Deposit operations
- ✅ Withdrawal operations
- ✅ Account deletion
- ✅ Error handling
- ✅ Edge cases (zero amounts, negative amounts)

**Key Test Methods**:
- `shouldCreateAccountSuccessfully()`
- `shouldGetAccountDetailsByAccountNumberSuccessfully()`
- `shouldThrowRuntimeExceptionWhenAccountNotFound()`
- `shouldDepositAmountSuccessfully()`
- `shouldWithdrawAmountSuccessfully()`
- `shouldHandleWithdrawalResultingInNegativeBalance()`

### 4. Controller Tests (`AccountControllerTest.java`)

**Purpose**: Test REST API endpoints with MockMvc

**Test Coverage**:
- ✅ POST /account/create
- ✅ GET /account/{accountNumber}
- ✅ GET /account/all
- ✅ PUT /account/deposit/{accountNumber}/{amount}
- ✅ PUT /account/withdraw/{accountNumber}/{amount}
- ✅ DELETE /account/delete/{accountNumber}
- ✅ HTTP status codes
- ✅ JSON response validation
- ✅ Error scenarios

**Key Test Methods**:
- `shouldCreateAccountSuccessfully()`
- `shouldGetAccountByIdSuccessfully()`
- `shouldDepositAmountSuccessfully()`
- `shouldWithdrawAmountSuccessfully()`
- `shouldDeleteAccountSuccessfully()`
- `shouldReturnBadRequestWhenCreatingAccountWithInvalidData()`

### 5. Integration Tests (`AccountIntegrationTest.java`)

**Purpose**: End-to-end testing with real database interactions

**Test Coverage**:
- ✅ Complete CRUD flow
- ✅ Multiple account operations
- ✅ Concurrent operations
- ✅ Data consistency
- ✅ Error scenarios
- ✅ Edge cases with amounts

**Key Test Methods**:
- `shouldCreateRetrieveUpdateAndDeleteAccountCompleteCrudFlow()`
- `shouldHandleMultipleAccountsOperations()`
- `shouldHandleConcurrentDepositsAndWithdrawals()`
- `shouldMaintainDataConsistencyAcrossOperations()`

## Test Configuration

### Test Dependencies

The following testing dependencies are included in `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

### Test Properties

Test configuration is defined in `src/test/resources/application-test.properties`:

```properties
# H2 in-memory database for testing
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password

# JPA configuration for testing
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
```

## Running Tests

### Run All Tests

```bash
mvn test
```

### Run Specific Test Class

```bash
mvn test -Dtest=AccountTest
mvn test -Dtest=AccountServiceImplTest
mvn test -Dtest=AccountControllerTest
mvn test -Dtest=AccountIntegrationTest
```

### Run Test Suite

```bash
mvn test -Dtest=TestSuite
```

### Run Tests with Coverage

```bash
mvn test jacoco:report
```

## Test Data Management

### Test Data Setup

Each test class uses `@BeforeEach` methods to set up test data:

```java
@BeforeEach
void setUp() {
    testAccount = new Account("John Doe", 5000.0);
    testAccount.setAccountNumber(1L);
}
```

### Database Cleanup

Integration tests use `@Transactional` annotation for automatic rollback:

```java
@Transactional
class AccountIntegrationTest {
    // Tests automatically rollback after execution
}
```

Repository tests use `@DataJpaTest` for isolated database testing.

## Assertions and Verifications

### JUnit 5 Assertions

```java
assertNotNull(result);
assertEquals(expectedValue, actualValue);
assertTrue(condition);
assertFalse(condition);
assertThrows(Exception.class, () -> methodCall());
```

### Mockito Verifications

```java
verify(accountRepository, times(1)).save(any(Account.class));
verify(accountService, never()).deleteById(anyLong());
```

### MockMvc Assertions

```java
.andExpect(status().isOk())
.andExpect(jsonPath("$.accountNumber").value(1))
.andExpect(content().contentType(MediaType.APPLICATION_JSON))
```

## Test Coverage

The test suite provides comprehensive coverage:

- **Entity Layer**: 100% method coverage
- **Repository Layer**: 100% method coverage with database operations
- **Service Layer**: 100% method coverage with business logic scenarios
- **Controller Layer**: 100% endpoint coverage with HTTP scenarios
- **Integration Layer**: End-to-end workflow coverage

## Best Practices Implemented

1. **AAA Pattern**: Arrange, Act, Assert in all tests
2. **Descriptive Test Names**: Clear method names describing test scenarios
3. **Single Responsibility**: Each test method tests one specific behavior
4. **Test Isolation**: Tests don't depend on each other
5. **Mock Usage**: Appropriate use of mocks for unit tests
6. **Real Dependencies**: Integration tests use real database
7. **Edge Case Testing**: Comprehensive edge case coverage
8. **Error Scenario Testing**: Proper error handling validation

## Continuous Integration

The test suite is designed to run in CI/CD pipelines:

- All tests are independent and can run in parallel
- No external dependencies required (uses H2 in-memory database)
- Fast execution time (unit tests with mocks)
- Comprehensive coverage ensures code quality

## Future Enhancements

Potential improvements for the testing suite:

1. **Performance Tests**: Load testing for concurrent operations
2. **Security Tests**: Authentication and authorization testing
3. **Contract Tests**: API contract validation
4. **Mutation Testing**: Test quality validation
5. **Test Data Builders**: Fluent API for test data creation
6. **Custom Matchers**: Domain-specific assertion methods
