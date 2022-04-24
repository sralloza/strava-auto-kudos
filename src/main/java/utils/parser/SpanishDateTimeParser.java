package utils.parser;

import com.google.inject.Inject;
import utils.TimeProvider;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class SpanishDateTimeParser implements DateTimeParser {
    private final TimeProvider timeProvider;

    @Inject
    public SpanishDateTimeParser(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    @Override
    public LocalDateTime parseDateTime(String s) {
        Locale locale = Locale.forLanguageTag("es-ES");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", locale);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy H:mm", locale);

        s = s.replace("de ", "").replace("a las ", "").toLowerCase();
        if (s.contains("ayer")) {
            s = s.replace("ayer", timeProvider.getYesterdayDate().format(dateFormatter));
        }
        if (s.contains("hoy")) {
            s = s.replace("hoy", timeProvider.getTodayDate().format(dateFormatter));
        }

        return LocalDateTime.parse(s, dateTimeFormatter);

    }
}
