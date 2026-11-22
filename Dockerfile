# ---------- Test runner image ----------
# JDK + Maven + project sources. The browser itself runs in the separate
# selenium/standalone-chrome container (see docker-compose.yml) -> small, single-purpose images.
FROM maven:3.9-eclipse-temurin-17

WORKDIR /app

# 1) Copy only the pom first so the dependency layer is cached between builds
COPY pom.xml .
RUN mvn -B -q dependency:go-offline \
 && mvn -B -q dependency:get -Dartifact=org.aspectj:aspectjweaver:1.9.22.1

# 2) Copy sources last: code changes do not invalidate the dependency layer
COPY src ./src

# Defaults for container runs (can be overridden by docker-compose / -e)
ENV HEADLESS=true \
    GRID_URL=http://selenium:4444

# docker compose run --rm tests -Dsuite=smoke   -> overrides CMD only
ENTRYPOINT ["mvn", "-B", "test"]
CMD ["-Dsuite=regression"]
