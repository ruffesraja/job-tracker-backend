# =============================================================================
# MINIMAL SECURE DOCKERFILE - Amazon Linux 2023
# =============================================================================
# Prerequisites:
#   1. Frontend already built and copied to src/main/resources/static
#   2. Maven build already done: mvn clean package -DskipTests
#   3. JAR file exists in target/
# =============================================================================

FROM public.ecr.aws/amazoncorretto/amazoncorretto:17-al2023-headless

RUN dnf update -y && dnf clean all

ENV APP_HOME=/app
WORKDIR $APP_HOME

COPY target/demo-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8080

ENV JAVA_TOOL_OPTIONS="\
-XX:+UseContainerSupport \
-XX:MaxRAMPercentage=75.0 \
-XX:+UseG1GC"

ENTRYPOINT ["java", "-jar", "app.jar"]
