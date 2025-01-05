FROM openjdk:21-jdk AS build

MAINTAINER application_service

COPY target/gateway-0.0.1-SNAPSHOT.jar gateway-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/gateway-0.0.1-SNAPSHOT.jar"]