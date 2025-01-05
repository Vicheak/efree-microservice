FROM openjdk:21-jdk AS build

MAINTAINER application_service

COPY target/product-service-0.0.1-SNAPSHOT.jar product-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/product-service-0.0.1-SNAPSHOT.jar"]