FROM gradle:7.6.1-jdk11 AS build
WORKDIR /app
COPY . /app
RUN gradle clean build --no-daemon

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/testWebApp.jar

ENV DB_URL test-db-container
ENV DB_NAME testDB
ENV DB_PORT 8081
ENV DB_USERNAME chocokon
ENV DB_PASSWORD abc123

EXPOSE 8080
CMD ["java", "-jar", "testWebApp.jar"]
