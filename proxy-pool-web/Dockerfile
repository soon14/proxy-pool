FROM openjdk:8-jdk-alpine
#FROM primetoninc/jdk:1.8
VOLUME ["/mnt","/opt"]
EXPOSE 9002
ARG JAR_FILE
WORKDIR /opt
COPY ${JAR_FILE} proxy-pool.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Xms128m", "-Xmx256m","-jar","/opt/proxy-pool.jar"]
