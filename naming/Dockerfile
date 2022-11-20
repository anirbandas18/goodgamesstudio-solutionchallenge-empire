FROM eclipse-temurin:11-jre-ubi9-minimal
MAINTAINER Anirban Das <anirbandas18@live.com>
RUN mkdir ~p -p /opt/teenthofabud/solution-challenge/goodgamestudios/empire
COPY target/*.jar /opt/teenthofabud/solution-challenge/goodgamestudios/empire/naming-app.jar
EXPOSE 8761
CMD ["java", "-jar", \
"-DNAMING_SERVER_PORT=${PORT}", \
"-DNAMING_LOGSTASH_ENABLED=${LOGSTASH_ENABLED}", \
"-DLOGBACK_LOGGING_LEVEL=${LOGGING_LEVEL}", \
"-DNAMING_LOGSTASH_HOST=${LOGSTASH_HOST}", \
"-DNAMING_LOGSTASH_PORT=${LOGSTASH_PORT}", \
"/opt/teenthofabud/solution-challenge/goodgamestudios/empire/naming-app.jar"]