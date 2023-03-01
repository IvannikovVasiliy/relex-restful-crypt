FROM openjdk:17
ADD ./target/relex-restful-api.jar relex-restful-api.jar
ENTRYPOINT ["java", "-jar", "relex-restful-api.jar"]