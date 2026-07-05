# ---------- Build stage: Maven + Java 21 ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -q dependency:go-offline

COPY src ./src
RUN mvn -q clean package -DskipTests

# ---------- Run stage: Tomcat 9 + Java 21 ----------
FROM tomcat:9.0-jdk21-temurin
LABEL description="Bakers.in on Tomcat 9 / Java 21"

# Strip Tomcat's default sample webapps and deploy ours as the ROOT app,
# so it serves from "/" instead of "/bakers-ecommerce".
RUN rm -rf /usr/local/tomcat/webapps/* \
    && rm -rf /usr/local/tomcat/webapps.dist

COPY --from=build /app/target/bakers-ecommerce.war /usr/local/tomcat/webapps/ROOT.war

# Cloud platforms (Render, Railway, Fly.io, etc.) inject a dynamic $PORT and expect
# the process to listen on it. Tomcat's server.xml hardcodes the connector port, so
# this entrypoint rewrites it to $PORT (defaulting to 8080) before starting Catalina.
COPY docker-entrypoint.sh /usr/local/bin/docker-entrypoint.sh
RUN chmod +x /usr/local/bin/docker-entrypoint.sh

EXPOSE 8080
ENTRYPOINT ["/usr/local/bin/docker-entrypoint.sh"]
CMD ["catalina.sh", "run"]
