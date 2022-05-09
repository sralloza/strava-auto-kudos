package repositories;

import com.google.inject.Inject;
import config.ConfigRepository;
import exceptions.LoginException;
import exceptions.LogoutException;
import lombok.extern.slf4j.Slf4j;
import mappers.ActivityBuilder;
import models.Activity;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class StravaRepository {
    private WebDriver driver;

    private final ConfigRepository config;
    private final URLsRepository urlsRepository;
    private final ActivityBuilder activityBuilder;

    @Inject
    public StravaRepository(ConfigRepository configRepository,
                            URLsRepository urlsRepository,
                            ActivityBuilder activityBuilder) {
        this.config = configRepository;
        this.urlsRepository = urlsRepository;
        this.activityBuilder = activityBuilder;
    }

    private void provisionDriver() {
        FirefoxOptions options = new FirefoxOptions();
//        options.addArguments("start-maximized");
        if (config.getBoolean("general.headless")) {
            options.setHeadless(true);
        }

        driver = new FirefoxDriver(options);
    }

    public void login() {
        log.info("Logging in to Strava");

        provisionDriver();
        var username = config.getString("strava.credentials.username");
        var password = config.getString("strava.credentials.password");
        driver.get(urlsRepository.getLoginURL());
        driver.findElement(By.id("email")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);

        driver.findElement(By.id("login-button")).click();
        waitPageLoads();
        log.debug("Redirected to {} after login", driver.getCurrentUrl());

        if (!driver.getCurrentUrl().equals(urlsRepository.getDashboardURL())) {
            throw new LoginException(driver.getCurrentUrl());
        }
    }

    public void logout() {
        log.info("Logging out from Strava");
        driver.navigate().refresh();
        List<WebElement> dropdownMenus = driver.findElements(config.getCssSelector("accountDropdown"));
        WebElement accountMenu = dropdownMenus.get(dropdownMenus.size() - 1);
        scrollToElement(accountMenu);
        accountMenu.click();

        WebElement logoutBtn = driver.findElement(config.getCssSelector("logoutBtn"));
        logoutBtn.click();

        log.debug("Redirected to {} after logout", driver.getCurrentUrl());

        if (driver.getCurrentUrl().equals(urlsRepository.getDashboardURL())) {
            throw new LogoutException(driver.getCurrentUrl());
        }
    }

    public void close() {
        driver.close();
        log.debug("Driver closed");
    }

    private void scrollToElement(WebElement element) {
        String scrollElementIntoMiddle = "var viewPortHeight = Math.max(document.documentElement.clientHeight, " +
                "window.innerHeight || 0);"
                + "var elementTop = arguments[0].getBoundingClientRect().top;"
                + "window.scrollBy(0, elementTop-(viewPortHeight/2));";

        runJavascript(scrollElementIntoMiddle, element);
        waitPageLoads();
    }

    protected void removeHeader() {
        try {
            ((JavascriptExecutor) driver).executeScript("document.getElementsByTagName(\"header\")[0].remove()");
        } catch (JavascriptException e) {
            log.warn("Failed to remove header", e);
        }
    }

    private void waitPageLoads() {
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(
                webDriver -> runJavascript("return document.readyState").equals("complete"));
    }

    private void simpleWaiter() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {
        }
    }

    private Object runJavascript(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }

    public List<Activity> getActivities() {
        Integer feedSize = config.getInt("strava.feedSize");
        if (feedSize > 0) {
            String newUrl = "https://www.strava.com/dashboard/following/" + feedSize;
            log.debug("Opening new url: {}", newUrl);
            driver.get(newUrl);
            waitPageLoads();
        }

        List<WebElement> activities = driver.findElements(config.getCssSelector("activity"));
        return activities.stream()
                .map(activityBuilder::buildActivities)
                .flatMap(Collection::stream)
                .peek(a -> log.debug("Parsed activity: {}", a))
                .collect(Collectors.toList());
    }

    public void giveKudos(List<Activity> activities) {
        removeHeader();
        activities.forEach(this::giveKudo);
    }

    private void giveKudo(Activity activity) {
        log.info("Giving kudo to {}", activity);
        if (config.getBoolean("strava.dryRun")) {
            log.info("dryRun is enabled, skipping kudo");
            return;
        }
        WebElement kudoButton = activity.getKudoButton();
        scrollToElement(kudoButton);
        simpleWaiter();

        kudoButton.click();
        waitPageLoads();
        log.info("Successfully gave kudo to {}", activity);
    }
}
