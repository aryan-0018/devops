# Build Stage / Runtime Stage using Eclipse Temurin Alpine JRE
FROM eclipse-temurin:17-jre-alpine

LABEL maintainer="1MS24CI026@msrit.edu" \
      service="sentinel-aiops" \
      student="Aryan M Desai" \
      version="1.0.0" \
      course="24CIAEC59"

WORKDIR /app

# Create unprivileged system group and user
RUN addgroup -S appgroup && adduser -S sentineluser -G appgroup

# Copy pre-built artifact
COPY target/sentinel-aiops-1.0.0.jar /app/sentinel-aiops.jar

# Enforce least privilege ownership
RUN chown -R sentineluser:appgroup /app

USER sentineluser

EXPOSE 8088

ENV SERVER_PORT=8088 \
    APP_ENV=production \
    DEFAULT_REGION=ap-south-1 \
    CLUSTER_NAME=sentinel-k8s

ENTRYPOINT ["java", "-Xms128m", "-Xmx512m", "-jar", "/app/sentinel-aiops.jar"]
