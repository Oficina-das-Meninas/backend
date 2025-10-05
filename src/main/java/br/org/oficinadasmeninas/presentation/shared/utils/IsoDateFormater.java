package br.org.oficinadasmeninas.presentation.shared.utils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class IsoDateFormater {

    public static String now() {
        return ZonedDateTime.now()
                .truncatedTo(ChronoUnit.MILLIS)
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
    public static String addHours(long hours) {
        return ZonedDateTime.now()
                .plusHours(hours)
                .truncatedTo(ChronoUnit.MILLIS)
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
