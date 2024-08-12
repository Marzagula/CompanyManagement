FROM openjdk:21-jdk-bullseye
COPY target/*.jar eureka-service.jar
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java","-jar","/eureka-service.jar"]