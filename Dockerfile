FROM openjdk:8-jre-alpine
ADD target/universal/router-1.0.0-SNAPSHOT/ /
ENV JAVA_OPTS=""
ENV APPLICATION_SECRET=changeme
ENTRYPOINT [ "sh", "-c", "./bin/router" ]
