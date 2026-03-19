# =============================================================================
# MULTI-STAGE DOCKERFILE - Amazon Linux 2023
# =============================================================================

# Stage 1: Build JAR
FROM maven:3.9-amazoncorretto-17-al2023 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: Run
FROM public.ecr.aws/amazoncorretto/amazoncorretto:17-al2023-headless
RUN dnf update -y && dnf clean all

WORKDIR /app
COPY --from=build /app/target/job-tracker-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8080

ENV JAVA_TOOL_OPTIONS="\
-XX:+UseContainerSupport \
-XX:MaxRAMPercentage=75.0 \
-XX:+UseG1GC"

ENTRYPOINT ["java", "-jar", "app.jar"]
