# First stage: complete build environment
FROM maven:3.8.3-openjdk-17 AS builder

# add pom.xml and source code
ADD ./pom.xml pom.xml
ADD ./src src/

# package jar
RUN mvn clean package

# Second stage: minimal runtime environment
From openjdk:17-jdk-slim

# copy jar from the first stage
COPY --from=builder target/socials-0.0.1-SNAPSHOT.jar socials-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "socials-0.0.1-SNAPSHOT.jar"]