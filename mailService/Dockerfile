FROM openjdk:8-jdk as builder

# preserve Java 8  from the maven install.
RUN mv /etc/alternatives/java /etc/alternatives/java8
RUN apt-get update -y && apt-get install maven -y

# Restore Java 8
RUN mv -f /etc/alternatives/java8 /etc/alternatives/java
RUN ls -l /usr/bin/java && java -version

RUN mkdir /usr/local/src/mailService
COPY ./ /usr/local/src/mailService
WORKDIR /usr/local/src/mailService
RUN mvn clean package -B -DskipTests=true
RUN ls -la /usr/local/src/ 


FROM openjdk:8-jdk
RUN mkdir /mailService
COPY --from=builder /usr/local/src/mailService/target/mailService-0.0.1-SNAPSHOT.jar /mailService
WORKDIR /mailService
EXPOSE 8080
CMD ["java", "-jar", "/mailService/mailService-0.0.1-SNAPSHOT.jar"]
