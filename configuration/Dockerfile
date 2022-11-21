FROM eclipse-temurin:11-jre-ubi9-minimal
MAINTAINER Anirban Das <anirbandas18@live.com>
RUN mkdir ~p -p /opt/teenthofabud/solution-challenge/goodgamestudios/empire
COPY target/*.jar /opt/teenthofabud/solution-challenge/goodgamestudios/empire/configuration-app.jar
EXPOSE 8888
CMD ["java", "-jar", \
"-DCONFIGURATION_SERVER_PORT=${PORT}", \
"-DCONFIGURATION_LOGSTASH_ENABLED=${LOGSTASH_ENABLED}", \
"-DCONFIGURATION_LOGBACK_LOGGING_LEVEL=${LOGBACK_LOGGING_LEVEL}", \
"-DCONFIGURATION_LOGSTASH_HOST=${LOGSTASH_HOST}", \
"-DCONFIGURATION_LOGSTASH_PORT=${LOGSTASH_PORT}", \
"-DCONFIGURATION_EUREKA_CLIENT_ENABLED=${EUREKA_CLIENT_ENABLED}", \
"-DCONFIGURATION_EUREKA_HOST=${EUREKA_HOST}", \
"-DCONFIGURATION_EUREKA_PORT=${EUREKA_PORT}", \
"/opt/teenthofabud/solution-challenge/goodgamestudios/empire/configuration-app.jar"]