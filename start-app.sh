#!/bin/bash

# Bank Management System Startup Script
# This script starts both the Spring Boot backend and React frontend

echo "🏦 Starting Bank Management System..."
echo "=================================="

# Check if we're in the right directory
if [ ! -d "Backend SpringBoot" ] || [ ! -d "Frontend ReactJs" ]; then
    echo "❌ Error: Please run this script from the Bank Application root directory"
    echo "Expected structure:"
    echo "  Bank Application/"
    echo "  ├── Backend SpringBoot/"
    echo "  └── Frontend ReactJs/"
    exit 1
fi

# Function to check if a port is in use
check_port() {
    if lsof -Pi :$1 -sTCP:LISTEN -t >/dev/null ; then
        return 0
    else
        return 1
    fi
}

# Check if ports are available
if check_port 8080; then
    echo "⚠️  Port 8080 is already in use. Backend might already be running."
fi

if check_port 3000; then
    echo "⚠️  Port 3000 is already in use. Frontend might already be running."
fi

echo ""
echo "🚀 Starting Backend (Spring Boot)..."
echo "Backend will run on: http://localhost:8080"
echo ""

# Start backend in background
cd "Backend SpringBoot"
if [ -f "pom.xml" ]; then
    echo "📦 Building Spring Boot application..."
    ./mvnw clean compile
    echo "🏃 Starting Spring Boot server..."
    ./mvnw spring-boot:run &
    BACKEND_PID=$!
    echo "Backend PID: $BACKEND_PID"
else
    echo "❌ Error: pom.xml not found in Backend SpringBoot directory"
    exit 1
fi

# Wait a bit for backend to start
echo "⏳ Waiting for backend to start..."
sleep 10

# Check if backend is running
if check_port 8080; then
    echo "✅ Backend is running on port 8080"
else
    echo "❌ Backend failed to start on port 8080"
    exit 1
fi

echo ""
echo "🎨 Starting Frontend (React)..."
echo "Frontend will run on: http://localhost:3000"
echo ""

# Start frontend
cd "../Frontend ReactJs"
if [ -f "package.json" ]; then
    echo "📦 Installing React dependencies..."
    npm install
    echo "🏃 Starting React development server..."
    npm start &
    FRONTEND_PID=$!
    echo "Frontend PID: $FRONTEND_PID"
else
    echo "❌ Error: package.json not found in Frontend ReactJs directory"
    exit 1
fi

echo ""
echo "🎉 Bank Management System is starting up!"
echo "=================================="
echo "Backend API:  http://localhost:8080"
echo "Frontend UI:  http://localhost:3000"
echo ""
echo "📝 To stop the application:"
echo "   - Press Ctrl+C to stop this script"
echo "   - Or kill the processes manually:"
echo "     kill $BACKEND_PID"
echo "     kill $FRONTEND_PID"
echo ""
echo "🔧 Development Notes:"
echo "   - Backend uses H2 in-memory database"
echo "   - Frontend proxies API calls to backend"
echo "   - Both applications will auto-reload on changes"
echo ""

# Wait for user to stop
wait
