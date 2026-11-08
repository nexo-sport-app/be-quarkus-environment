package it.nexoapp.env.api.transcoding.service;

import it.nexoapp.common.domain.Date;
import it.nexoapp.env.api.transcoding.dto.ListType;
import it.nexoapp.env.config.EnvironmentConfig;
import it.nexoapp.env.infra.transcoding.TranscodingFacade;
import it.nexoapp.env.infra.transcoding.dto.CreateTranscodingCommand;
import it.nexoapp.env.infra.transcoding.dto.ListHandler;
import it.nexoapp.env.infra.transcoding.dto.TranscodeCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test of the reference date derivation of {@link TranscodingServiceImpl}: "today" is the calendar date of an
 * injected clock in the configured zone, also across the 2026 DST changes (2026-03-29 and 2026-10-25, Europe/Rome).
 */
class TranscodingReferenceDateTest {

    /** Zone of the production default. */
    private static final String ROME = "Europe/Rome";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Without a reference date, the list uses the Europe/Rome calendar date of the clock instant.
     *
     * @param instant      the fixed clock instant (UTC)
     * @param expectedDate the expected Europe/Rome calendar date
     */
    @ParameterizedTest(name = "list at {0} -> {1}")
    @CsvSource({
            "2026-03-28T23:30:00Z, 2026-03-29",
            "2026-03-29T01:30:00Z, 2026-03-29",
            "2026-03-29T22:30:00Z, 2026-03-30",
            "2026-10-24T22:30:00Z, 2026-10-25",
            "2026-10-25T00:30:00Z, 2026-10-25",
            "2026-10-25T01:30:00Z, 2026-10-25",
            "2026-10-25T23:30:00Z, 2026-10-26",
            "2026-07-01T10:00:00Z, 2026-07-01"
    })
    void listWithoutReferenceDateUsesTheRomeCalendarDate(final String instant, final String expectedDate) {
        final StubFacade facade = new StubFacade();
        final TranscodingServiceImpl service = serviceAt(facade, instant, ROME);

        service.list(ListType.STATE, null);

        assertEquals(Date.parse(expectedDate), facade.listDate, "list reference date at " + instant);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Without a date in the command, transcode derives it from the clock before calling the facade.
     *
     * @param instant      the fixed clock instant (UTC)
     * @param expectedDate the expected Europe/Rome calendar date
     */
    @ParameterizedTest(name = "transcode at {0} -> {1}")
    @CsvSource({
            "2026-10-24T22:30:00Z, 2026-10-25",
            "2026-03-28T23:30:00Z, 2026-03-29",
            "2026-10-25T01:30:00Z, 2026-10-25"
    })
    void transcodeWithoutDateUsesTheRomeCalendarDate(final String instant, final String expectedDate) throws Exception {
        final StubFacade facade = new StubFacade();
        final TranscodingServiceImpl service = serviceAt(facade, instant, ROME);

        service.transcode(commandDated(null));

        assertEquals(Date.parse(expectedDate), facade.command.date(), "transcode reference date at " + instant);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * An explicit reference date is passed through unchanged by list and transcode.
     */
    @Test
    void explicitReferenceDateIsPassedThroughUnchanged() throws Exception {
        final StubFacade facade = new StubFacade();
        final TranscodingServiceImpl service = serviceAt(facade, "2026-03-28T23:30:00Z", ROME);
        final Date explicit = Date.parse("2026-01-15");

        service.list(ListType.STATE, explicit);
        service.transcode(commandDated(explicit));

        assertEquals(explicit, facade.listDate, "explicit list date");
        assertEquals(explicit, facade.command.date(), "explicit transcode date");
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The configured zone decides the date: with UTC, 2026-03-28T23:30Z is still 2026-03-28.
     */
    @Test
    void configuredZoneDecidesTheDate() {
        final StubFacade facade = new StubFacade();
        final TranscodingServiceImpl service = serviceAt(facade, "2026-03-28T23:30:00Z", "UTC");

        service.list(ListType.STATE, null);

        assertEquals(Date.parse("2026-03-28"), facade.listDate, "UTC zone keeps the UTC date");
    }

    // --------------------------------------------------------
    // -- HELPERS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Builds the service under test on a fixed clock and zone.
     *
     * @param facade  the facade stub
     * @param instant the fixed instant (UTC)
     * @param zone    the configured zone id
     * @return the service
     */
    private static TranscodingServiceImpl serviceAt(final StubFacade facade, final String instant, final String zone) {
        final Clock clock = Clock.fixed(Instant.parse(instant), ZoneOffset.UTC);
        final EnvironmentConfig config = () -> () -> zone;
        return new TranscodingServiceImpl(facade, new ListHandlerRegistry(), clock, config);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Builds a transcode command with the given date.
     *
     * @param date the date, possibly {@code null}
     * @return the command
     */
    private static TranscodeCommand commandDated(final Date date) {
        return new TranscodeCommand("state", "nexo", "xx", "IT", "nexo", "xx", date);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Facade stub capturing the date and the command it receives.
     */
    private static final class StubFacade implements TranscodingFacade {

        /** The reference date received by {@code getList}. */
        private Date listDate;

        /** The command received by {@code transcode}. */
        private TranscodeCommand command;

        // -------------------------------------------------------------------------------------------------------------

        /**
         * {@inheritDoc}
         */
        @Override
        public <T> List<T> getList(final String list, final ListHandler<T> handler, final Date referenceDate) {
            this.listDate = referenceDate;
            return List.of();
        }

        // -------------------------------------------------------------------------------------------------------------

        /**
         * {@inheritDoc}
         */
        @Override
        public <T> List<T> getList(final String list, final ListHandler<T> handler) {
            throw new UnsupportedOperationException();
        }

        // -------------------------------------------------------------------------------------------------------------

        /**
         * {@inheritDoc}
         */
        @Override
        public String transcode(final TranscodeCommand command) {
            this.command = command;
            return "value";
        }

        // -------------------------------------------------------------------------------------------------------------

        /**
         * {@inheritDoc}
         */
        @Override
        public void save(final List<CreateTranscodingCommand> commands) {
            throw new UnsupportedOperationException();
        }

        // -------------------------------------------------------------------------------------------------------------

        /**
         * {@inheritDoc}
         */
        @Override
        public void save(final CreateTranscodingCommand commands) {
            throw new UnsupportedOperationException();
        }

    }

}
