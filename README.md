# Bank Management System - Spring Boot CRUD Application

This project demonstrates a complete Bank Management System built with Spring Boot, Spring Data JPA, and MySQL. The application provides RESTful APIs for managing bank accounts with full CRUD operations.

## Features

- **Create Account**: Create new bank accounts
- **Read Account**: Get account details by account number or retrieve all accounts
- **Update Account**: Deposit and withdraw money from accounts
- **Delete Account**: Close/delete bank accounts

## Technology Stack

- **Spring Boot**: 3.2.3
- **Java**: 17
- **Spring Data JPA**: For database operations
- **MySQL**: Database
- **Maven**: Build tool

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── bank/app/BankManagementApp/
│   │       ├── BankManagementAppApplication.java
│   │       ├── controller/
│   │       │   └── AccountController.java
│   │       ├── entity/
│   │       │   └── Account.java
│   │       ├── repository/
│   │       │   └── AccountRepository.java
│   │       └── service/
│   │           ├── AccountService.java
│   │           └── AccountServiceImpl.java
│   └── resources/
│       └── application.properties
└── pom.xml
```

## Prerequisites

1. **Java 17** or higher
2. **Maven 3.6+**
3. **MySQL 8.0+**
4. **IDE** (IntelliJ IDEA, Eclipse, or VS Code)

## Setup Instructions

### 1. Database Setup

Create a MySQL database named `bankdb`:

```sql
CREATE DATABASE bankdb;
```

### 2. Update Database Configuration

Edit `src/main/resources/application.properties` and update the database credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bankdb
spring.datasource.username=root
spring.datasource.password=your_actual_password
```

### 3. Run the Application

```bash
# Navigate to project directory
cd "Backend SpringBoot"

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### 1. Create Account (POST)
- **URL**: `http://localhost:8080/account/create`
- **Method**: POST
- **Body**:
```json
{
  "accountHolderName": "John Doe",
  "accountBalance": 5000.0
}
```

### 2. Get Account by ID (GET)
- **URL**: `http://localhost:8080/account/{accountNumber}`
- **Method**: GET
- **Example**: `http://localhost:8080/account/1`

### 3. Get All Accounts (GET)
- **URL**: `http://localhost:8080/account/all`
- **Method**: GET

### 4. Deposit Money (PUT)
- **URL**: `http://localhost:8080/account/deposit/{accountNumber}/{amount}`
- **Method**: PUT
- **Example**: `http://localhost:8080/account/deposit/1/1000`

### 5. Withdraw Money (PUT)
- **URL**: `http://localhost:8080/account/withdraw/{accountNumber}/{amount}`
- **Method**: PUT
- **Example**: `http://localhost:8080/account/withdraw/1/500`

### 6. Delete Account (DELETE)
- **URL**: `http://localhost:8080/account/delete/{accountNumber}`
- **Method**: DELETE
- **Example**: `http://localhost:8080/account/delete/1`

## Testing with Postman

1. **Create Account**:
   - Method: POST
   - URL: `http://localhost:8080/account/create`
   - Body: `{"accountHolderName": "John", "accountBalance": 5000}`

2. **Get Account**:
   - Method: GET
   - URL: `http://localhost:8080/account/1`

3. **Get All Accounts**:
   - Method: GET
   - URL: `http://localhost:8080/account/all`

4. **Deposit Money**:
   - Method: PUT
   - URL: `http://localhost:8080/account/deposit/1/1000`

5. **Withdraw Money**:
   - Method: PUT
   - URL: `http://localhost:8080/account/withdraw/1/500`

6. **Delete Account**:
   - Method: DELETE
   - URL: `http://localhost:8080/account/delete/1`

## Database Schema

The application automatically creates the `account` table with the following structure:

```sql
CREATE TABLE account (
    account_number BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_holder_name VARCHAR(255),
    account_balance DOUBLE
);
```

## Error Handling

The application includes basic error handling:
- Returns "Account not found" error when trying to access a non-existent account
- Proper HTTP status codes (200, 201, 404, 500)

## Future Enhancements

- Add input validation
- Implement proper exception handling
- Add transaction logging
- Implement authentication and authorization
- Add unit and integration tests
- Add API documentation with Swagger/OpenAPI

## Troubleshooting

1. **Database Connection Issues**: Ensure MySQL is running and credentials are correct
2. **Port Already in Use**: Change the port in `application.properties` by adding `server.port=8081`
3. **Java Version Issues**: Ensure you're using Java 17 or higher

## Contributing

Feel free to fork this project and submit pull requests for any improvements.
