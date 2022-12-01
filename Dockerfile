FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app
COPY . ./
RUN mvn -B package -Dmaven.test.skip=true


FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/target/apple-monitor-0.0.6.jar  /app
CMD ["java","-jar","/app/apple-monitor-0.0.6.jar"]
