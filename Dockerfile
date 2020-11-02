FROM adoptopenjdk/openjdk11

EXPOSE 9100
VOLUME /tmp
COPY target/*.jar app.jar
COPY suncloudstorage.properties suncloudstorage.properties
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.config.additional-location=suncloudstorage.properties","-jar","/app.jar"]