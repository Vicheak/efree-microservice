FROM openjdk:21-jdk AS build

MAINTAINER application_service

COPY target/user-service-1.0.0.jar user-service-1.0.0.jar

ENTRYPOINT ["java", "-jar", "/user-service-1.0.0.jar"]