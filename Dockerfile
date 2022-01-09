FROM openjdk:11
MAINTAINER https://github.com/acuma14
COPY build/libs/k-shuffler-1.0.0.jar kshuffler-server-1.0.0.jar
ENTRYPOINT ["java","-jar","/kshuffler-server-1.0.0.jar"]