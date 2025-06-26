# syntax=docker/dockerfile:1

# Multi-stage build for optimal image size
FROM maven:3.9-eclipse-temurin-21 AS builder

# Set working directory
WORKDIR /app

# Copy pom.xml first for better Docker layer caching
COPY softwareengineering/pom.xml ./

# Download dependencies first
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY softwareengineering/src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-jammy AS final

# Install useful tools
RUN apt-get update && apt-get install -y \
    curl \
    sqlite3 \
    && rm -rf /var/lib/apt/lists/*

# Create app user for security
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid 10001 \
    appuser

# Set working directory
WORKDIR /app

# Copy JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Copy database and SQL scripts from root directory
COPY database.db ./
COPY sql/ ./sql/

# Create data directory for SQLite
RUN mkdir -p /app/data && \
    chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose port (your app runs on 7070)
EXPOSE 7070

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:7070/health || exit 1

# Start the application
CMD ["java", "-jar", "app.jar"]
