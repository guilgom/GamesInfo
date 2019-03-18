FROM openjdk:8-jdk as builder

# preserve Java 8  from the maven install.
RUN mv /etc/alternatives/java /etc/alternatives/java8
RUN apt-get update -y && apt-get install maven -y

# Restore Java 8
RUN mv -f /etc/alternatives/java8 /etc/alternatives/java
RUN ls -l /usr/bin/java && java -version

RUN mkdir /usr/local/src/prueba_servidor
COPY ./ /usr/local/src/prueba_servidor
WORKDIR /usr/local/src/prueba_servidor
RUN mvn clean package -B -DskipTests=true
RUN ls -la /usr/local/src/ 


FROM openjdk:8-jdk
RUN mkdir /prueba_servidor
COPY --from=builder /usr/local/src/prueba_servidor/target/prueba_servidor-0.0.1-SNAPSHOT.jar /prueba_servidor
WORKDIR /prueba_servidor
EXPOSE 8443
CMD ["java", "-jar", "/prueba_servidor/prueba_servidor-0.0.1-SNAPSHOT.jar"]
