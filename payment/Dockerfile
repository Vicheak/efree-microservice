FROM openjdk:21-jdk AS build

MAINTAINER application_service

COPY target/payment-service-0.0.1-SNAPSHOT.jar payment-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/payment-service-0.0.1-SNAPSHOT.jar"]