package it.nexoapp.env.config;

import io.smallrye.config.PropertiesConfigSource;
import io.smallrye.config.SmallRyeConfig;
import io.smallrye.config.SmallRyeConfigBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests of {@link EnvironmentConfig} and of the cache declarations of the environment properties.
 */
class EnvironmentConfigTest {

    /** Classpath location of the environment properties. */
    private static final String APPLICATION_PROPERTIES = "application.properties";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The zone defaults to Europe/Rome when not configured.
     */
    @Test
    void zoneDefaultsToEuropeRome() {
        final EnvironmentConfig config = buildConfig(Map.of());

        assertEquals("Europe/Rome", config.transcoding().zone(), "env.transcoding.zone should default to Europe/Rome");
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * An explicit zone overrides the default.
     */
    @Test
    void zoneOverrideIsHonoured() {
        final EnvironmentConfig config = buildConfig(Map.of("env.transcoding.zone", "UTC"));

        assertEquals("UTC", config.transcoding().zone(), "env.transcoding.zone should be mapped");
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The quoted cache names of the environment properties resolve exactly.
     *
     * @throws IOException if the properties cannot be read
     */
    @Test
    void quotedCacheNamesResolveExactly() throws IOException {
        final SmallRyeConfig config;
        try (final InputStream stream = getClass().getClassLoader().getResourceAsStream(APPLICATION_PROPERTIES)) {
            assertNotNull(stream, "environment application.properties should be on the classpath");
            final Properties properties = new Properties();
            properties.load(stream);
            final Map<String, String> map = new HashMap<>();
            properties.forEach((key, value) -> map.put(key.toString(), value.toString()));
            config = new SmallRyeConfigBuilder()
                    .withSources(new PropertiesConfigSource(map, "application.properties", 250))
                    .build();
        }

        assertEquals(
                "1H",
                config.getRawValue("quarkus.cache.redis.\"env.transcoding.list\".expire-after-write"),
                "the list cache should expire after one hour"
        );
        assertEquals(
                "30M",
                config.getRawValue("quarkus.cache.redis.\"env.transcoding.transcode\".expire-after-write"),
                "the transcode cache should expire after thirty minutes"
        );
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Builds the mapping from the given properties.
     *
     * @param properties the configuration properties
     * @return the mapped configuration
     */
    private static EnvironmentConfig buildConfig(final Map<String, String> properties) {
        final SmallRyeConfig config = new SmallRyeConfigBuilder()
                .withMapping(EnvironmentConfig.class)
                .withSources(new PropertiesConfigSource(properties, "test", 100))
                .build();
        return config.getConfigMapping(EnvironmentConfig.class);
    }

}
