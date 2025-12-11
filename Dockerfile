# Step 1: Use Maven to build the WAR
FROM maven:3.8.7-openjdk-17 AS build

WORKDIR /app

# Copy all project files
COPY . .

# Build WAR
RUN mvn -q -DskipTests package

# Step 2: Tomcat server image
FROM tomcat:9.0

# Remove default ROOT app
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copy your WAR file into ROOT.war
COPY --from=build /app/target/event-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
