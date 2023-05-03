package ru.acuma.shuffler.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings({"IllegalMethodCall"})
@Slf4j
@UtilityClass
public final class TimeMachine {

    private static final ZoneId ZONE_ID = ZoneId.of("Europe/Moscow");
    private static final AtomicReference<Clock> CLOCK_REF = new AtomicReference<>(Clock.system(ZONE_ID));

    public static Clock clock() {
        return CLOCK_REF.get();
    }

    public static LocalDateTime localDateTimeNow() {
        return LocalDateTime.now(clock()).truncatedTo(ChronoUnit.MICROS);
    }

    public static LocalDate localDateNow() {
        return LocalDate.now(clock());
    }

    public static Instant instantNow() {
        return Instant.now(clock()).truncatedTo(ChronoUnit.MICROS);
    }

    public static OffsetDateTime offsetDateTimeNow() {
        return OffsetDateTime.now(clock());
    }

    public static OffsetDateTime offsetDateTimeNow(final ZoneId zoneId) {
        return OffsetDateTime.now(clock().withZone(zoneId));
    }

    public static ZoneId systemDefaultZoneId() {
        return ZONE_ID;
    }
}
