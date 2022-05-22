package utils;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
public class NumberUtils {
    private final TimeUtils timeUtils;

    @Inject
    public NumberUtils(TimeUtils timeUtils) {
        this.timeUtils = timeUtils;
    }

    public Double parseDistanceKm(String distanceString) {
        double multiplier = 1;
        if (distanceString.contains("km")) {
            distanceString = distanceString.replace("km", "");
        }
        if (distanceString.contains("m")) {
            distanceString = distanceString.replace("m", "")
                    .replace(".", "")
                    .replace(",", "");
            multiplier = 0.001;
        }
        return Double.parseDouble(distanceString
                .replace(",", ".")
                .strip()) * multiplier;
    }

    public Integer parsePositiveSlope(String slopeString) {
        return Integer.parseInt(slopeString.replace("m", "")
                .replace(",", "")
                .replace(".", "").strip());
    }

    public Integer parseCalories(String s) {
        return Integer.parseInt(s.replace("kcal", "").replace("cal", "").strip());
    }

    public Integer parseHeartRate(String s) {
        return Integer.parseInt(s.replace("ppm", "").replace("bpm", "").strip());
    }

    public Double paceToSpeed(String pace) {
        double distanceKm;
        if (pace.contains("/100 m")) {
            distanceKm = 0.1;
            pace = pace.replace("/100 m", "");
        } else if (pace.contains("/100m")) {
            distanceKm = 0.1;
            pace = pace.replace("/100m", "");
        } else if (pace.contains("/km")) {
            distanceKm = 1;
            pace = pace.replace("/km", "");
        } else {
            throw new IllegalArgumentException("Unsupported pace format: " + pace);
        }

        Duration duration = timeUtils.parseDuration(pace.strip().replace(":", "m") + "s");
        return computeSpeed(duration, distanceKm);
    }

    public Double computeSpeed(Duration duration, Double distanceKm) {
        if (Stream.of(duration, distanceKm).anyMatch(Objects::isNull)) {
            log.warn("Received null arg while computing speed (duration={}, distanceKm={})", duration, distanceKm);
            return null;
        }
        if (duration.getSeconds() == 0) {
            log.warn("Received empty duration while computing speed (duration={}, distanceKm={})", duration, distanceKm);
            return null;
        }

        double durationSeconds = (double) duration.getSeconds() / 3600;
        System.out.println(durationSeconds);
        double result = distanceKm / durationSeconds;
        System.out.println(result);

        return roundTwoDecimals(result);
    }

    public Double roundTwoDecimals(Double input) {
        return Math.round(input * 100) / 100.0;
    }
}
