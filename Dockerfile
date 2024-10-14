# Use Maven base image with Java
FROM maven:3.9.8-openjdk-21

# Set the working directory in the Docker container
WORKDIR /app

# Copy the pom.xml file and source code
COPY pom.xml /app/
COPY src /app/src

# Build the project
RUN mvn clean package

# The command to run the application
CMD ["mvn", "test"]