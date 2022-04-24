package utils;

import java.time.LocalDate;

public class TimeProvider {

    public LocalDate getTodayDate() {
        return LocalDate.now();
    }

    public LocalDate getYesterdayDate() {
        return getTodayDate().minusDays(1);
    }
}
