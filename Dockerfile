FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . ./
RUN mvn -B clean package -Dmaven.test.skip=true \
 && cp target/apple-monitor-*.jar /app/apple-monitor.jar


FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/apple-monitor.jar /app/apple-monitor.jar
# 程序从运行目录读取 config.json，需自行挂载，例如 -v ./config.json:/app/config.json
CMD ["java", "-jar", "/app/apple-monitor.jar"]
