package utils;

import com.google.inject.Inject;

import java.time.Duration;
import java.util.Objects;
import java.util.stream.Stream;

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
        return Integer.parseInt(s.replace("kcal", "").strip());
    }

    public Integer parseHeartRate(String s) {
        return Integer.parseInt(s.replace("ppm", "").replace("bpm", "").strip());
    }

    public Double parseSpeed(String speed) {
        return paceToSpeed(
                speed.replace("km/h", "")
                        .replace("m/s", "")
                        .replace(".", "")
                        .replace(",", ".")
                        .strip());
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
            return null;
        }
        double durationHours = (double) duration.getSeconds() / 3600;
        double result = distanceKm / durationHours;

        return Math.round(result * 100) / 100.0;
    }
}
