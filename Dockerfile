FROM maven:3.8.6-amazoncorretto-17

WORKDIR /api-easy-flow
COPY . .
RUN mvn clean install -DskipTests

CMD mvn spring-boot:run