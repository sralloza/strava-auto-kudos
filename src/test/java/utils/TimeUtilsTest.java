import config.ConfigRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import utils.TimeProvider;
import utils.TimeUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TimeUtilsTest {
    @Mock
    private TimeProvider timeProvider;

    @Mock
    private ConfigRepository configRepository;

    private TimeUtils timeUtils;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        timeUtils = new TimeUtils(timeProvider, configRepository);
        var today = LocalDate.of(2022, 4, 17);

        when(timeProvider.getTodayDate()).thenReturn(today);
        when(timeProvider.getYesterdayDate()).thenReturn(today.minusDays(1));
    }

    @ParameterizedTest(name = "{index} => dateTime={0}, expected={1}")
    @MethodSource("getSpanishDateTimeData")
    public void testParseDateTime(LocalDateTime expected, String input) {
        when(configRepository.getBoolean("general.spanishLocale")).thenReturn(true);
        LocalDateTime actual = timeUtils.parseDateTime(input);
        assertEquals(expected, actual);
    }

    public static Object[][] getSpanishDateTimeData() {
        return new Object[][]{
                {LocalDateTime.of(2022, 4, 15, 11, 18), "15 de abril de 2022 a las 11:18"},
                {LocalDateTime.of(2022, 4, 16, 16, 21), "Ayer a las 16:21"},
                {LocalDateTime.of(2022, 4, 17, 17, 46), "Hoy a las 17:46"},
                {LocalDateTime.of(2022, 4, 16, 9, 12), "16 de abril de 2022 a las 9:12"},
                {LocalDateTime.of(2022, 4, 3, 9, 26), "3 de abril de 2022 a las 9:26"},

        };
    }

    @ParameterizedTest(name = "{index} => dateTime={0}, expected={1}")
    @MethodSource("getEnglishDateTimeData")
    public void testEnglishParseDateTime(LocalDateTime expected, String input) {
        when(configRepository.getBoolean("general.spanishLocale")).thenReturn(false);
        LocalDateTime actual = timeUtils.parseDateTime(input);
        assertEquals(expected, actual);
    }

    public static Object[][] getEnglishDateTimeData() {
        return new Object[][]{
                {LocalDateTime.of(2022, 4, 21, 16, 21), "april 21, 2022 at 4:21 pm"},
                {LocalDateTime.of(2022, 4, 16, 8, 36), "Yesterday 8:36 AM"},
//                {LocalDateTime.of(2022, 4, 17, 17, 46), "Hoy a las 17:46"},
//                {LocalDateTime.of(2022, 4, 16, 9, 12), "16 de abril de 2022 a las 9:12"},
//                {LocalDateTime.of(2022, 4, 3, 9, 26), "3 de abril de 2022 a las 9:26"},

        };
    }
}
