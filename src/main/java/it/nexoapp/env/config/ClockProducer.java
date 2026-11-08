package it.nexoapp.env.config;

import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

import java.time.Clock;

/**
 * Produces the {@link Clock} the environment service reads the current instant from.
 * <p>
 * Production uses the UTC system clock: it only supplies instants, the calendar zone comes from the
 * {@code env.transcoding.zone} property. Tests replace the bean with a fixed clock.
 */
@ApplicationScoped
class ClockProducer {

    /**
     * Default system clock, replaceable by any other {@link Clock} bean.
     *
     * @return the UTC system clock
     */
    @Produces
    @Singleton
    @DefaultBean
    Clock clock() {
        return Clock.systemUTC();
    }

}
