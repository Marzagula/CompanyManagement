FROM openjdk:21-jdk-bullseye
COPY target/*.jar graphql-api.jar
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java","-jar","/graphql-api.jar"]