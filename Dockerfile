FROM openjdk:17-alpine
MAINTAINER https://github.com/QAcuma
ARG JAR_FILE=build/libs/*.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
ENV JAVA_OPTS="--add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED"
ENTRYPOINT ["java","-jar","app.jar"]