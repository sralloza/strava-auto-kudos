package mappers;

import com.google.inject.Inject;
import config.ConfigRepository;
import lombok.extern.slf4j.Slf4j;
import models.Activity;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.NumberUtils;
import utils.TimeUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class ActivityBuilder {
    private final TimeUtils timeUtils;
    private final NumberUtils numberUtils;
    private final ConfigRepository config;
    private Map<String, String> statsMap;

    @Inject
    public ActivityBuilder(TimeUtils timeUtils, NumberUtils numberUtils, ConfigRepository config) {
        this.timeUtils = timeUtils;
        this.numberUtils = numberUtils;
        this.config = config;
    }

    public List<Activity> buildActivities(WebElement webElement) {
        log.debug("Finding nested abilities from element: {}", webElement.getText());

        if (webElement.getText().toLowerCase().contains(config.getTitle("joinedChallenge").toLowerCase())) {
            log.debug("Activity is a joined challenge event, skipping");
            return Collections.emptyList();
        }

        LocalDateTime datetime = buildDatetime(webElement);
        List<Activity> activities = webElement.findElements(config.getCssSelector("groupedActivity")).stream()
                .map(w -> buildActivity(w, datetime))
                .collect(Collectors.toList());

        if (!activities.isEmpty()) {
            return activities;
        }

        log.debug("No nested abilities found, parsing element as single ability");
        return List.of(buildActivity(webElement));
    }

    public Activity buildActivity(WebElement webElement) {
        return buildActivity(webElement, null);
    }

    public Activity buildActivity(WebElement webElement, LocalDateTime datetime) {
        log.debug("Building activity from element: {}", webElement.getText());

        datetime = Optional.ofNullable(datetime).orElse(buildDatetime(webElement));
        statsMap = getStatsMap(webElement);
        log.debug("Stats map: {}", statsMap);

        Double defaultSpeed = numberUtils.computeSpeed(buildDuration(), buildDistance());

        return new Activity()
                .setUsername(buildUsername(webElement))
                .setDatetime(datetime)
                .setLocation(buildLocation(webElement))
                .setDescription(buildDescription(webElement))
                .setDistance(buildDistance())
                .setElevationGain(buildElevationGain())
                .setTime(buildDuration())
                .setCalories(buildCalories())
                .setSpeed(buildSpeed(defaultSpeed))
                .setHeartRate(buildHeartRate())
                .setNKudos(buildNKudos(webElement))
                .setHasKudo(buildHasKudo(webElement))
                .setKudoButton(getKudoButton(webElement));
    }

    private String buildUsername(WebElement webElement) {
        return webElement.findElement(config.getCssSelector("username")).getText();
    }

    private LocalDateTime buildDatetime(WebElement webElement) {
        return webElement.findElements(config.getCssSelector("datetime")).stream()
                .findFirst()
                .map(WebElement::getText)
                .map(timeUtils::parseDateTime)
                .orElse(null);
    }

    private String buildLocation(WebElement webElement) {
        return webElement.findElements(config.getCssSelector("location")).stream()
                .findFirst()
                .map(WebElement::getText)
                .map(String::strip)
                .map(s -> s.substring(1))
                .map(String::strip)
                .orElse(null);
    }

    private String buildDescription(WebElement webElement) {
        return webElement.findElement(config.getCssSelector("description"))
                .getText().strip();
    }

    private Integer buildNKudos(WebElement webElement) {
        var kudosStr = webElement.findElement(config.getCssSelector("kudoCount"))
                .getText().replace("kudos", "");
        if (kudosStr.contains(config.getTitle("noKudosMsg"))) {
            return 0;
        }
        return Integer.parseInt(kudosStr.split(" ")[0].strip());
    }

    private boolean buildHasKudo(WebElement webElement) {
        var kudosBtn = getKudoButton(webElement);
        var giveKudosTitle = config.getTitle("giveKudos");
        String kudosBtnTitle = kudosBtn.getAttribute("title");
        boolean hasKudo = !giveKudosTitle.equalsIgnoreCase(kudosBtnTitle);
        log.debug("Kudos button title: '{}', received '{}', result {}", giveKudosTitle, kudosBtnTitle, hasKudo);
        return hasKudo;
    }

    private WebElement getKudoButton(WebElement webElement) {
        return webElement.findElement(config.getCssSelector("kudoBtn"));
    }

    private Map<String, String> getStatsMap(WebElement webElement) {
        var foo = webElement.findElements(config.getCssSelector("statsSection"));
        return foo.stream()
                .map(webElement1 -> webElement1.getText().split("\n"))
                .map(strings -> Map.entry(strings[0], strings[1]))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Double buildDistance() {
        return Optional.ofNullable(statsMap.getOrDefault(config.getTitle("distance"), null))
                .map(numberUtils::parseDistance)
                .orElse(null);
    }

    private Integer buildElevationGain() {
        return Optional.ofNullable(statsMap.getOrDefault(config.getTitle("elevationGain"), null))
                .map(numberUtils::parsePositiveSlope)
                .orElse(null);
    }

    private Duration buildDuration() {
        return Optional.ofNullable(statsMap.getOrDefault(config.getTitle("duration"), null))
                .map(timeUtils::parseDuration)
                .orElse(null);
    }

    private Integer buildCalories() {
        return Optional.ofNullable(statsMap.getOrDefault(config.getTitle("calories"), null))
                .map(numberUtils::parseCalories)
                .orElse(null);
    }

    private Integer buildHeartRate() {
        return Optional.ofNullable(statsMap.getOrDefault(config.getTitle("heartRate"), null))
                .map(numberUtils::parseHeartRate)
                .orElse(null);
    }

    private Double buildSpeed(Double defaultSpeed) {
        return Optional.ofNullable(statsMap.getOrDefault(config.getTitle("pace"), null))
                .map(numberUtils::parseSpeed)
                .orElse(defaultSpeed);
    }
}