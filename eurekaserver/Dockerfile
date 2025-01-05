FROM openjdk:21-jdk AS build

MAINTAINER application_service

COPY target/eurekaserver-0.0.1-SNAPSHOT.jar eurekaserver-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/eurekaserver-0.0.1-SNAPSHOT.jar"]