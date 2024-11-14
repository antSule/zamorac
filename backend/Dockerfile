FROM alpine/java:21-jre

COPY target/*.jar /app.jar

EXPOSE 8080

# Naredba kojom se pokrece aplikacija
ENTRYPOINT ["java","-jar","/app.jar"]