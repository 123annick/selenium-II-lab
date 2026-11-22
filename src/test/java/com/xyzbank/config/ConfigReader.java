package com.xyzbank.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Single source of configuration.
 * Lookup order: JVM system property -> environment variable -> config.properties.
 * This lets CI/Docker change behaviour without touching code.
 */
public final class ConfigReader {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) {
                throw new IllegalStateException("config.properties not found on the classpath");
            }
            PROPS.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Could not load config.properties", e);
        }
    }

    private ConfigReader() {
    }

    public static String get(String key) {
        String sys = System.getProperty(key);
        if (sys != null && !sys.isBlank()) {
            return sys.trim();
        }
        String env = System.getenv(key.replace('.', '_').toUpperCase());
        if (env != null && !env.isBlank()) {
            return env.trim();
        }
        return PROPS.getProperty(key, "").trim();
    }

    public static String baseUrl() {
        return get("base.url");
    }

    public static String browser() {
        return get("browser").toLowerCase();
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("headless"));
    }

    public static String gridUrl() {
        return get("grid.url");
    }

    public static int explicitWaitSeconds() {
        return Integer.parseInt(get("explicit.wait.seconds"));
    }

    public static int pageLoadTimeoutSeconds() {
        return Integer.parseInt(get("page.load.timeout.seconds"));
    }
}
