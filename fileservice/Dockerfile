FROM openjdk:21-jdk AS build

MAINTAINER application_service

COPY target/file-service-0.0.1-SNAPSHOT.jar file-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/file-service-0.0.1-SNAPSHOT.jar"]