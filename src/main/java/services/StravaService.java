package services;

import com.google.inject.Inject;
import config.ConfigRepository;
import exceptions.NoActivitiesException;
import lombok.extern.slf4j.Slf4j;
import models.Activity;
import repositories.StravaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class StravaService {
    private final StravaRepository stravaRepository;
    private final ConfigRepository config;

    @Inject
    public StravaService(StravaRepository stravaRepository, ConfigRepository config) {
        this.stravaRepository = stravaRepository;
        this.config = config;
    }

    public void giveKudosToEveryone() {
        log.info("Starting StravaService");
        try {
            stravaRepository.login();

            List<Activity> activities = stravaRepository.getActivities();
            log.info("Found {} activities", activities.size());

            if (activities.isEmpty()) {
                throw new NoActivitiesException();
            }

            List<Activity> kudolessActivities = activities.stream()
                    .filter(this::filterByDistance)
                    .filter(activity -> !activity.isHasKudo())
                    .collect(Collectors.toList());

            long nullDistances = activities.stream()
                    .filter(activity -> activity.getDistance() == null)
                    .count();

            if (nullDistances > 0) {
                log.warn("{}/{} activities with null distance", nullDistances, activities.size());
            }

            long nullDatetimes = activities.stream()
                    .filter(activity -> activity.getDatetime() == null)
                    .count();
            if (nullDatetimes > 0) {
                log.error("{}/{} activities with null datetime", nullDatetimes, activities.size());
            }

            log.info("Found {} kudoless activities", kudolessActivities.size());
            stravaRepository.giveKudos(kudolessActivities);

            stravaRepository.logout();
        } catch (Exception exception) {
            log.error("Error while giving kudos", exception);
            throw exception;
        } finally {
            stravaRepository.close();
        }
    }

    private boolean filterByDistance(Activity activity) {
        var minDistance = config.getDouble("strava.minDistanceKM");
        if (minDistance == 0){
            return true;
        }
        return Optional.ofNullable(activity.getDistance()).orElse(0.0) >= config.getDouble("strava.minDistanceKM");
    }
}
