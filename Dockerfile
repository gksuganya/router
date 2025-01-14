FROM openjdk:8-jre-alpine
WORKDIR /usr/src/app
#COPY package.json /usr/src/app/
COPY . /usr/src/app/
RUN cd /usr/src/app/

RUN ls
RUN pwd
#ADD target/universal/router-1.0.0-SNAPSHOT/ /
RUN rm -f RUNNING_PID
ENV JAVA_OPTS=""
ENV APPLICATION_SECRET=changeme
ENTRYPOINT [ "sh", "-c", "java -cp 'conf:lib/*' play.core.server.ProdServerStart"]
