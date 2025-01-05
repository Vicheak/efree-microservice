FROM openjdk:21-jdk AS build

MAINTAINER application_service

COPY target/category-service-0.0.1-SNAPSHOT.jar category-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/category-service-0.0.1-SNAPSHOT.jar"]