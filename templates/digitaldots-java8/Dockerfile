FROM maven:3.6.3-openjdk-8 as builder
WORKDIR /home/app
COPY . /home/app/



RUN cd function && mvn clean package -U -DskipTests
FROM openfaas/of-watchdog:0.7.6 as watchdog
FROM openjdk:8-jre-slim as ship
RUN addgroup --system app \
    && adduser --system --ingroup app app
COPY --from=watchdog /fwatchdog /usr/bin/fwatchdog
RUN chmod +x /usr/bin/fwatchdog
WORKDIR /home/app


COPY --from=builder /home/app/function/target/function-1.0-SNAPSHOT-jar-with-dependencies.jar ./function-1.0-SNAPSHOT-jar-with-dependencies.jar

user app
WORKDIR /home/app/
ENV upstream_url="http://127.0.0.1:8082"
ENV mode="http"
ENV fprocess="java -jar function-1.0-SNAPSHOT-jar-with-dependencies.jar"

EXPOSE 8080

HEALTHCHECK --interval=5s CMD [ -e /tmp/.lock ] || exit 1
CMD ["fwatchdog"]