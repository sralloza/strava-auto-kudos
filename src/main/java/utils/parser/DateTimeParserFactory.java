package utils.parser;

import com.google.inject.Inject;
import config.ConfigRepository;
import utils.TimeProvider;

public class DateTimeParserFactory {
    private final ConfigRepository config;
    private final TimeProvider timeProvider;

    @Inject
    public DateTimeParserFactory(ConfigRepository config, TimeProvider timeProvider) {
        this.config = config;
        this.timeProvider = timeProvider;
    }

    public DateTimeParser getParser() {
        if (config.getBoolean("general.spanishLocale")) {
            return new SpanishDateTimeParser(timeProvider);
        }
        return new EnglishDateTimeParser(timeProvider);
    }
}
