package utils;

import com.google.inject.Inject;
import config.ConfigRepository;
import lombok.extern.slf4j.Slf4j;
import utils.parser.DateTimeParser;
import utils.parser.DateTimeParserFactory;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
public class TimeUtils {
    private final TimeProvider timeProvider;
    private final ConfigRepository configRepository;

    @Inject
    public TimeUtils(TimeProvider timeProvider, ConfigRepository configRepository) {
        this.timeProvider = timeProvider;
        this.configRepository = configRepository;
    }

    public Duration parseDuration(String duration) {
        duration = duration.replace(" ", "")
                .replace("min", "m")
                .toUpperCase();
        return Duration.parse("PT" + duration);
    }

    public LocalDateTime parseDateTime(String s) {
        DateTimeParserFactory factory = new DateTimeParserFactory(configRepository, timeProvider);
        DateTimeParser parser = factory.getParser();
        return parser.parseDateTime(s);
    }

}
