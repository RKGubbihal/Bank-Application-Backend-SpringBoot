# Bank Management System - Complete Project Overview

A full-stack bank management application built with Spring Boot (Backend) and React.js (Frontend).

## 🏗️ Project Structure

```
Bank Application/
├── Backend SpringBoot/           # Spring Boot Backend
│   ├── src/main/java/bank/app/
│   │   └── BankManagementApp/
│   │       ├── controller/       # REST Controllers
│   │       ├── entity/          # JPA Entities
│   │       ├── repository/      # Data Repositories
│   │       └── service/         # Business Logic
│   ├── src/main/resources/
│   │   └── application.properties
│   ├── pom.xml                  # Maven Dependencies
│   └── Bank_Management_API.postman_collection.json
├── Frontend ReactJs/             # React.js Frontend
│   ├── src/
│   │   ├── components/          # React Components
│   │   ├── services/           # API Services
│   │   └── styles/             # CSS Styles
│   ├── public/
│   ├── package.json
│   └── README.md
├── start-app.sh                 # Startup Script
└── PROJECT_OVERVIEW.md         # This file
```

## 🚀 Quick Start

### Option 1: Using the Startup Script (Recommended)
```bash
cd "Bank Application"
./start-app.sh
```

### Option 2: Manual Startup

#### Backend (Spring Boot)
```bash
cd "Backend SpringBoot"
./mvnw spring-boot:run
```
Backend will be available at: http://localhost:8080

#### Frontend (React)
```bash
cd "Frontend ReactJs"
npm install
npm start
```
Frontend will be available at: http://localhost:3000

## 🎯 Features

### Backend Features (Spring Boot)
- ✅ **RESTful API** - Complete CRUD operations for accounts
- ✅ **JPA/Hibernate** - Database abstraction layer
- ✅ **H2 Database** - In-memory database for development
- ✅ **Account Management** - Create, read, update, delete accounts
- ✅ **Transaction Operations** - Deposit and withdraw funds
- ✅ **Data Validation** - Input validation and error handling

### Frontend Features (React.js)
- ✅ **Modern UI** - Beautiful, responsive design
- ✅ **Account Dashboard** - Overview of all accounts
- ✅ **Account Management** - Create, view, edit, delete accounts
- ✅ **Transaction Operations** - Deposit and withdraw funds
- ✅ **Fund Transfers** - Transfer money between accounts
- ✅ **Transaction History** - View transaction records
- ✅ **Responsive Design** - Works on desktop, tablet, and mobile
- ✅ **Real-time Updates** - Live balance updates

## 🛠️ Technology Stack

### Backend
- **Java 17** - Programming language
- **Spring Boot 3.2.3** - Application framework
- **Spring Data JPA** - Data persistence
- **H2 Database** - In-memory database
- **Maven** - Build tool
- **JUnit 5** - Testing framework

### Frontend
- **React 18.2.0** - Frontend framework
- **React Router DOM 6.11.0** - Client-side routing
- **Axios 1.4.0** - HTTP client
- **CSS3** - Styling with modern features
- **npm** - Package manager

## 📊 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/account/create` | Create a new account |
| GET | `/account/{id}` | Get account by ID |
| GET | `/account/all` | Get all accounts |
| PUT | `/account/deposit/{id}/{amount}` | Deposit funds |
| PUT | `/account/withdraw/{id}/{amount}` | Withdraw funds |
| DELETE | `/account/delete/{id}` | Delete account |

## 🎨 UI Components

### Main Components
- **Login** - User authentication (demo mode)
- **Dashboard** - Main overview page
- **AccountDetails** - Account management interface
- **TransactionHistory** - Transaction records viewer
- **Transfer** - Fund transfer interface

### Key Features
- **Responsive Design** - Mobile-first approach
- **Modern Styling** - CSS Grid and Flexbox
- **Smooth Animations** - CSS transitions and hover effects
- **Error Handling** - User-friendly error messages
- **Loading States** - Visual feedback during operations

## 🔧 Development Setup

### Prerequisites
- Java 17 or higher
- Node.js 14 or higher
- npm or yarn
- Maven (or use included Maven wrapper)

### Backend Setup
```bash
cd "Backend SpringBoot"
./mvnw clean compile
./mvnw spring-boot:run
```

### Frontend Setup
```bash
cd "Frontend ReactJs"
npm install
npm start
```

## 🧪 Testing

### Backend Testing
```bash
cd "Backend SpringBoot"
./mvnw test
```

### Frontend Testing
```bash
cd "Frontend ReactJs"
npm test
```

## 📱 Screenshots & Demo

The application includes:
- **Login Page** - Clean authentication interface
- **Dashboard** - Account overview with quick actions
- **Account Management** - Full CRUD operations
- **Transaction Operations** - Deposit/withdraw with validation
- **Fund Transfers** - Inter-account transfers
- **Transaction History** - Detailed transaction records

## 🔒 Security Notes

- **Demo Mode**: The application uses simple authentication for demonstration
- **CORS**: Backend is configured to allow frontend requests
- **Validation**: Input validation on both frontend and backend
- **Error Handling**: Comprehensive error handling throughout

## 🚀 Deployment

### Backend Deployment
- Build: `./mvnw clean package`
- Run: `java -jar target/bank-management-app-0.0.1-SNAPSHOT.jar`

### Frontend Deployment
- Build: `npm run build`
- Serve: Use any static file server (nginx, Apache, etc.)

## 📈 Future Enhancements

- **Authentication** - Real user authentication system
- **Database** - Production database (PostgreSQL, MySQL)
- **Security** - JWT tokens, password encryption
- **Notifications** - Real-time notifications
- **Reports** - Financial reports and analytics
- **Mobile App** - React Native mobile application

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📄 License

This project is intended for educational purposes and demonstration of full-stack development skills.

## 🆘 Troubleshooting

### Common Issues

1. **Port Conflicts**
   - Backend: Change port in `application.properties`
   - Frontend: Change port in `package.json` scripts

2. **Database Issues**
   - H2 console: http://localhost:8080/h2-console
   - JDBC URL: `jdbc:h2:mem:testdb`

3. **CORS Issues**
   - Ensure backend allows frontend origin
   - Check browser console for CORS errors

4. **Build Issues**
   - Clear node_modules and reinstall
   - Check Java version compatibility
   - Verify Maven dependencies

## 📞 Support

For issues or questions:
1. Check the troubleshooting section
2. Review the README files in each directory
3. Check the console logs for error messages
4. Ensure all prerequisites are installed

---

**Happy Banking! 🏦✨**
