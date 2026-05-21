FROM alpine:latest

# Install Java 17 and Maven
RUN apk add --no-cache openjdk17-jdk maven

# Set working directory
WORKDIR /app

# Default command
CMD ["mvn", "test"]

