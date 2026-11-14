FROM maven:3.9-eclipse-temurin-25 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:25-jre

ARG TOMCAT_VERSION=10.1.61
RUN mkdir -p /opt/tomcat \
    && apt-get update \
    && apt-get install -y --no-install-recommends ca-certificates curl tar \
    && curl -fsSL "https://dlcdn.apache.org/tomcat/tomcat-10/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz" \
       | tar -xz --strip-components=1 -C /opt/tomcat \
    && rm -rf /opt/tomcat/webapps/ROOT \
              /opt/tomcat/webapps/docs \
              /opt/tomcat/webapps/examples \
              /opt/tomcat/webapps/host-manager \
              /opt/tomcat/webapps/manager \
    && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/medflow-1.0.0.war /opt/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["/opt/tomcat/bin/catalina.sh", "run"]
