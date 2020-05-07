FROM ubuntu:bionic

RUN apt-get update
RUN apt-get install libssl1.0.0 libasound2 openjdk-11-jdk -y

RUN mkdir /aa

COPY .mvn/ /aa/.mvn
COPY src/ /aa/src
COPY mvnw /aa/.
COPY pom.xml /aa/.


WORKDIR /aa
RUN chmod +x mvnw
RUN ./mvnw package

ENTRYPOINT ["java", "-jar", "/aa/target/AudioWebpage-backend-0.0.1-SNAPSHOT.jar"]