package utils.parser;

import utils.TimeProvider;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class EnglishDateTimeParser implements DateTimeParser {
    private final TimeProvider timeProvider;

    public EnglishDateTimeParser(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    private String titleCase(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    @Override
    public LocalDateTime parseDateTime(String s) {
        s = s.toLowerCase();
        if (s.contains("at ")) {
            s = s.replace("at ", "");
        }
        if (s.contains("am")) {
            s = s.replace("am", "AM");
        }
        if (s.contains("pm")) {
            s = s.replace("pm", "PM");
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(
                "MMMM d, yyyy", Locale.forLanguageTag("en-US"));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
                "MMMM d, yyyy h:mm a", Locale.forLanguageTag("en-US"));

        if (s.contains("yesterday")) {
            s = s.replace("yesterday", timeProvider.getYesterdayDate().format(dateFormatter));
        }
        if (s.contains("today")) {
            s = s.replace("today", timeProvider.getYesterdayDate().format(dateFormatter));
        }

        s = titleCase(s);
        return LocalDateTime.parse(s, dateTimeFormatter);
    }
}
