package config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;

@Slf4j
@Singleton
public class ConfigRepository {
    private final Config config;

    @Inject
    private ConfigRepository(Config config) {
        this.config = config;
        log.debug("Config loaded. Strava config: {}", config.getConfig("strava"));
    }

    public String getString(String key) {
        return config.getString(key);
    }

    public Boolean getBoolean(String key) {
        return config.getBoolean(key);
    }

    public Integer getInt(String key) {
        return config.getInt(key);
    }

    public Double getDouble(String key) {
        return config.getDouble(key);
    }

    public By getCssSelector(String variable) {
        String cssSelector = config.getString("strava.cssSelector." + variable);
        return By.cssSelector(cssSelector);
    }

    public String getTitle(String variable) {
        if (config.getBoolean("general.spanishLocale")){
            return config.getString("strava.title.spanish." + variable);
        }
        return config.getString("strava.title.english." + variable);
    }
}
