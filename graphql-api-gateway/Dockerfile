FROM openjdk:21-jdk-bullseye
COPY target/*.jar graphql-api-gateway.jar
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java","-jar","/graphql-api-gateway.jar"]