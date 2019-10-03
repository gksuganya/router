FROM openjdk:8-jre-alpine
RUN ls
RUN pwd
ADD /home/circleci/work/target/universal/router-1.0.0-SNAPSHOT/ /
RUN rm -f RUNNING_PID
ENV JAVA_OPTS=""
ENV APPLICATION_SECRET=changeme
ENTRYPOINT [ "sh", "-c", "java -cp 'conf:lib/*' play.core.server.ProdServerStart"]
