FROM openjdk:21-jdk AS build

MAINTAINER application_service

COPY target/user-service-0.0.1-SNAPSHOT.jar user-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/user-service-1.0.0.jar"]