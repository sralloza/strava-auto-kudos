# Strava Auto Kudos

Automatically give kudos to people you are following

## Configuration

Configuration is managed by environment variables.

### Required

- **STRAVA_USERNAME**: your Strava username.
- **STRAVA_PASSWORD**: your Strava password.

### Optional

#### Strava URLs

- **STRAVA_BASE_URL**: the base URL of your Strava instance. Defaults to `https://www.strava.com`.

#### CSS Selectors

- **STRAVA_CSS_SELECTOR_USERNAME**: the CSS selector for the username field. Defaults to `a[data-testid='owners-name']`.
- **STRAVA_CSS_SELECTOR_DATETIME**: the CSS selector for the datetime field. Defaults to `time[data-testid='date_at_time']`.
- **STRAVA_CSS_SELECTOR_LOCATION**: the CSS selector for the location field. Defaults to `div[data-testid='location']`.
- **STRAVA_CSS_SELECTOR_LOCATION_GROUPED_ACTIVITY**: the CSS selector for the location field in grouped activity. Defaults to `LocationAndTime--location--Djc4K`.
- **STRAVA_CSS_SELECTOR_DESCRIPTION**: the CSS selector for the description field. Defaults to `a[data-testid='activity_name']`.
- **STRAVA_CSS_SELECTOR_KUDOCOUNT**: the CSS selector for the kudo count field. Defaults to `div[data-testid='counts_wrapper']`.
- **STRAVA_CSS_SELECTOR_KUDOBTN**: the CSS selector for the kudo button. Defaults to `button[data-testid='kudos_button']`.
- **STRAVA_CSS_SELECTOR_STATS_SECTION**: the CSS selector for the stats section. Defaults to `.Stat--stat--AaawC`.
- **STRAVA_CSS_SELECTOR_ACCOUNT_DROPDOWN**: the CSS selector for the account dropdown. Defaults to `#dashboard-dropdown-arrow`.
- **STRAVA_CSS_SELECTOR_LOGOUT_BTN**: the CSS selector for the logout button. Defaults to `a[data-method='delete']`.
- **STRAVA_CSS_SELECTOR_ACTIVITY**: the CSS selector for the activity. Defaults to `.react-feed-entry`.
- **STRAVA_CSS_SELECTOR_GROUPED_ACTIVITY**: the CSS selector for a grouped activity. Defaults to `.GroupActivity--child-entry--0NEr-`.
- **STRAVA_CSS_SELECTOR_KUDOS_COMMENTS_AND_ACHIEVEMENTS_MODAL**: the CSS selector for the kudos comments and achievements modal. Defaults to `.KudosCommentsAndAchievementsModal--body--4PTIc`.
- **STRAVA_CSS_SELECTOR_PROMO**: the CSS selector for the promo section. Defaults to `div[data-testid='promo-img']`

#### Titles

- **STRAVA_TITLE_SPANISH_GIVEKUDOS**: spanish message meaning the kudo has not been given yet. Defaults to `otorga kudos`.
- **STRAVA_TITLE_SPANISH_NOKUDOSMSG**: spanish message meaning no kudos have been given. Defaults to `Sé el primero`.
- **STRAVA_TITLE_SPANISH_DISTANCE**: spanish title for the distance field. Defaults to `Distancia`.
- **STRAVA_TITLE_SPANISH_ELEVATIONGAIN**: spanish title for the elevation gain field. Defaults to `Desnivel positivo`.
- **STRAVA_TITLE_SPANISH_DURATION**: spanish title for the duration field. Defaults to `Tiempo`.
- **STRAVA_TITLE_SPANISH_CALORIES**: spanish title for the calories field. Defaults to `Cal.`.
- **STRAVA_TITLE_SPANISH_HEARTRATE**: spanish title for the heart rate field. Defaults to `RC medio`.
- **STRAVA_TITLE_SPANISH_PACE**: spanish title for the pace field. Defaults to `Ritmo`.
- **STRAVA_TITLE_SPANISH_JOINEDCHALLENGE**: spanish message meaning the user has joined a challenge. Defaults to `unido a un reto!`.

- **STRAVA_TITLE_ENGLISH_GIVEKUDOS**: english message meaning the kudo has not been given yet. Defaults to `Give kudos`.
- **STRAVA_TITLE_ENGLISH_NOKUDOSMSG**: english message meaning no kudos have been given. Defaults to `Be the first`.
- **STRAVA_TITLE_ENGLISH_DISTANCE**: english title for the distance field. Defaults to `Distance`.
- **STRAVA_TITLE_ENGLISH_ELEVATIONGAIN**: english title for the elevation gain field. Defaults to `Elev Gain`.
- **STRAVA_TITLE_ENGLISH_DURATION**: english title for the duration field. Defaults to `Time`.
- **STRAVA_TITLE_ENGLISH_CALORIES**: english title for the calories field. Defaults to `Cal.`.
- **STRAVA_TITLE_ENGLISH_HEARTRATE**: english title for the heart rate field. Defaults to `Avg HR`.
- **STRAVA_TITLE_ENGLISH_PACE**: english title for the pace field. Defaults to `Pace`.
- **STRAVA_TITLE_ENGLISH_JOINEDCHALLENGE**: english message meaning the user has joined a challenge. Defaults to `joined a challenge`.

#### General

- **STRAVA_FEED_SIZE**: the number of activities to be shown in the feed. Defaults to `0`, meaning the default. If set, 
  the feed will be open the url `/dashboard/following/$NUMBER` instead of the default `/dashboard`.
- **DRY_RUN**: if set to `true`, the script will not actually send the kudo. Defaults to `false`.
- **MIN_DISTANCE_KM**: the minimum distance in kilometers of each activity to consider giving the kudo. Defaults to `0`, meaning the filter is disabled.
- **HEADLESS**: if set to `true`, the script will open the browser in the background. Defaults to `true`.
- **SPANISH_LOCALE**: if set to `true`, the script will use the spanish locale. Defaults to `false`.
