# Use OpenJDK 17 base image
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy Maven wrapper and build files
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Build the app
RUN ./mvnw package -DskipTests

# Run the app
EXPOSE 8082
CMD ["java", "-jar", "target/translator-0.0.1-SNAPSHOT.jar"]