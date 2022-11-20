FROM eclipse-temurin:11-jre-ubi9-minimal
MAINTAINER Anirban Das <anirbandas18@live.com>
RUN mkdir ~p -p /opt/teenthofabud/solution-challenge/goodgamestudios/empire
COPY target/*.jar /opt/teenthofabud/solution-challenge/goodgamestudios/empire/gateway-app.jar
EXPOSE 8083
CMD ["java", "-jar", \
"-DSPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}", \
"-DSPRING_CLOUD_CONFIG_ENABLED=${SPRING_CLOUD_CONFIG_ENABLED}", \
"-DLOGSTASH_ENABLED=${LOGSTASH_ENABLED}", \
"-DLOGSTASH_HOST=${LOGSTASH_HOST}", \
"-DLOGSTASH_PORT=${LOGSTASH_PORT}", \
"-DSPRING_CLOUD_CONFIG_HOST=${SPRING_CLOUD_CONFIG_HOST}", \
"-DSPRING_CLOUD_CONFIG_PORT=${SPRING_CLOUD_CONFIG_PORT}", \
"-DGATEWAY_LOGGING_LEVEL=${GATEWAY_LOGGING_LEVEL}", \
"-DGATEWAY_EUERKA_HOST=${GATEWAY_EUERKA_HOST}", \
"-DGATEWAY_EUREKA_PORT=${GATEWAY_EUREKA_PORT}", \
"-DGATEWAY_ZIPKIN_HOST=${GATEWAY_ZIPKIN_HOST}", \
"-DGATEWAY_ZIPKIN_PORT=${GATEWAY_ZIPKIN_PORT}", \
"/opt/teenthofabud/solution-challenge/goodgamestudios/empire/gateway-app.jar"]