package models;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@ToString(exclude = {"kudoButton"})
@Accessors(chain = true)
public class Activity {
    private String username;
    private LocalDateTime datetime;
    private String location;
    private String description;
    private Double distance;
    private Integer elevationGain;
    private Integer calories;
    private Double speed;
    private Integer heartRate;
    private Duration time;
    private Integer nKudos;
    private boolean hasKudo;
    private WebElement kudoButton;
}
