FROM amazoncorretto:17
ARG JAR_FILE=build/libs/shopping-mall-0.0.1-SNAPSHOT.jar

RUN mkdir app
WORKDIR /app
COPY . .

RUN chmod +x gradlew && ./gradlew clean build -x test
RUN mv ${JAR_FILE} ./app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]