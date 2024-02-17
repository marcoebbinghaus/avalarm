FROM maven:3-amazoncorretto-21 AS builder

ADD ./pom.xml pom.xml
ADD ./src src/

RUN mvn clean package

From eclipse-temurin:21.0.2_13-jre-jammy

COPY --from=builder target/avalarm-0.0.1-SNAPSHOT.jar avalarm-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar", "avalarm-0.0.1-SNAPSHOT.jar"]