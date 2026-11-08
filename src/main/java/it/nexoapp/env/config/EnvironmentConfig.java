package it.nexoapp.env.config;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

/**
 * Environment microservice settings, mapped from configuration properties with the {@code env} prefix.
 * <p>
 * Every property is optional and falls back to a default when not set.
 */
@StaticInitSafe
@ConfigMapping(prefix = "env")
public interface EnvironmentConfig {

    /**
     * Transcoding settings (properties with the {@code env.transcoding} prefix).
     */
    Transcoding transcoding();

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Settings of the transcoding feature, mapped from the {@code env.transcoding} prefix.
     */
    interface Transcoding {

        /**
         * Time zone used to compute the reference date of the reference lists when none is requested
         * (property {@code env.transcoding.zone}). Optional, defaults to {@code Europe/Rome}.
         */
        @WithDefault("Europe/Rome")
        String zone();

    }

}
