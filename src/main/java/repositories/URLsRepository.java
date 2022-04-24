package repositories;

import com.google.inject.Inject;
import config.ConfigRepository;

public class URLsRepository {
    private final String baseURL;
    private final String dashBoardURL;

    @Inject
    public URLsRepository(ConfigRepository config) {
        baseURL = config.getString("strava.web.baseURL");
        dashBoardURL = baseURL + config.getString("strava.web.dashboardPath");
    }

    public String getLoginURL() {
        return baseURL + "/login";
    }

    public String getDashboardURL() {
        return dashBoardURL;
    }
}
