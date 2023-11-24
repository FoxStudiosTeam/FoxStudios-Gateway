FROM gradle:8.4.0-jdk17-alpine AS build
LABEL authors="AgniaEndie"
WORKDIR /FoxStudios-Gateway
COPY . /FoxStudios-Gateway
RUN gradle bootJar
ENTRYPOINT ["java","-XX:+UseZGC", "-jar", "/FoxStudios-Gateway/build/libs/api-gateway-0.0.1-SNAPSHOT.jar"]
