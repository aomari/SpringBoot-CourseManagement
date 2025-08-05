#!/bin/bash

echo "🚀 Starting PostgreSQL container for Course Management..."

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker first."
    exit 1
fi

# Check if container already exists
if docker ps -a --format "table {{.Names}}" | grep -q "course-db"; then
    echo "📦 Container 'course-db' already exists."
    
    # Check if it's running
    if docker ps --format "table {{.Names}}" | grep -q "course-db"; then
        echo "✅ Container is already running!"
        echo "📊 Database is ready at localhost:5432"
        echo "🔗 Connect with: docker exec -it course-db psql -U admin -d course_management_db"
    else
        echo "🔄 Starting existing container..."
        docker start course-db
        echo "✅ Container started successfully!"
    fi
else
    echo "🆕 Creating new PostgreSQL container..."
    docker-compose up -d
    
    if [ $? -eq 0 ]; then
        echo "✅ Container created and started successfully!"
        echo "⏳ Waiting for database to be ready..."
        sleep 5
        echo "📊 Database is ready at localhost:5432"
    else
        echo "❌ Failed to start container. Check the logs with: docker-compose logs postgres"
        exit 1
    fi
fi

echo ""
echo "📋 Quick Commands:"
echo "  View logs: docker-compose logs -f postgres"
echo "  Connect to DB: docker exec -it course-db psql -U admin -d course_management_db"
echo "  Stop container: docker-compose down"
echo "  Check status: docker-compose ps" 