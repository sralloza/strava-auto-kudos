package utils;

import config.ConfigRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumberUtilsTest {
    private NumberUtils numberUtils;

    @Mock
    ConfigRepository configRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        numberUtils = new NumberUtils(new TimeUtils(new TimeProvider(), configRepository));
    }

    public static Object[] distanceData() {
        return new Object[][] {
                {"131,30 km", 131.3},
                {"131.30 km", 131.3},
                {"23.96 km", 23.96},
                {"23,96 km", 23.96},
                {"8.22 km", 8.22},
                {"8,22 km", 8.22},
                {"4.05 km", 4.05},
                {"4,05 km", 4.05},
                {"2.083 m", 2.083},
                {"2,083 m", 2.083},
                {"1.399 m", 1.399},
                {"1,399 m", 1.399},
                {"725 m", 0.725}
        };
    }

    @ParameterizedTest(name = "{index} => input={0}, expected={1}")
    @MethodSource("distanceData")
    public void shouldParseDistanceKm(String input, Double expected) {
        // Given

        // When
        var actual = numberUtils.parseDistanceKm(input);

        // Then
        assertEquals(expected, actual);
    }

    public static Object[] positiveSlopeData() {
        return new Object[][] {
                {"1.411 m", 1411},
                {"1,411 m", 1411},
                {"1.249 m", 1249},
                {"1,249 m", 1249},
                {"789 m", 789},
                {"601 m", 601},
                {"241 m", 241},
                {"188 m", 188}
        };
    }

    @ParameterizedTest(name = "{index} => input={0}, expected={1}")
    @MethodSource("positiveSlopeData")
    public void shouldParsePositiveSlope(String input, Integer expected) {
        // Given

        // When
        var actual = numberUtils.parsePositiveSlope(input);

        // Then
        assertEquals(expected, actual);
    }

    public static Object[] caloriesData() {
        return new Object[][] {
                {"511 Kcal", 511},
                {"511 Cal", 511},
                {"238 kcal", 238},
                {"238 cal", 238},
                {"60 kcal", 60},
                {"60 cal", 60}
        };
    }

    @ParameterizedTest(name = "{index} => input={0}, expected={1}")
    @MethodSource("caloriesData")
    public void shouldParseCalories(String input, Integer expected) {
        // Given

        // When
        var actual = numberUtils.parseCalories(input);

        // Then
        assertEquals(expected, actual);
    }

    public static Object[] heartRateData() {
        return new Object[][] {
                {"139 PPM", 139},
                {"139 BPM", 139},
                {"136 ppm", 136},
                {"136 bpm", 136},
                {"118 ppm", 118},
                {"118 bpm", 118},
                {"96 ppm", 96},
                {"96 bpm", 96}
        };
    }

    @ParameterizedTest(name = "{index} => input={0}, expected={1}")
    @MethodSource("heartRateData")
    public void shouldParseHeartRate(String input, Integer expected) {
        // Given

        // When
        var actual = numberUtils.parseHeartRate(input);

        // Then
        assertEquals(expected, actual);
    }

    public static Object[] paceData() {
        return new Object[][] {
                {"6:20 /km", 9.47},
                {"5:15 /km", 11.43},
                {"5:08 /km", 11.69},
                {"4:06 /km", 14.63},
                {"2:01 /100 m", 2.98},
                {"2:01 /100m", 2.98},
                {"2:29 /100 m", 2.42},
                {"2:29 /100m", 2.42},
        };
    }

    @ParameterizedTest(name = "{index} => input={0}, expected={1}")
    @MethodSource("paceData")
    public void shouldParsePaceAndTransformToSpeed(String input, Double expected) {
        // Given

        // When
        var actual = numberUtils.paceToSpeed(input);

        // Then
        assertEquals(expected, actual);
    }


    public static Object[] SpeedData() {
        return new Object[][] {
                {380, 1.0, 9.47},
                {314, 1.0, 11.46},
                {308, 1.0, 11.69},
                {246, 1.0, 14.63},
                {121, 0.1, 2.98},
                {149, 0.1, 2.42},
        };
    }

    @ParameterizedTest(name = "{index} => duration={0}, distance={1}, expected={2}")
    @MethodSource("SpeedData")
    public void shouldComputeSpeed(Integer seconds, Double distanceKm, Double expected) {
        // Given
        var duration = Duration.ofSeconds(seconds);

        // When
        var actual = numberUtils.computeSpeed(duration, distanceKm);

        // Then
        assertEquals(expected, actual);
    }
}
