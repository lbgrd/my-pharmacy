FROM openjdk:8-alpine

COPY target/uberjar/my-pharmacy.jar /my-pharmacy/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/my-pharmacy/app.jar"]
