import com.google.inject.Guice;
import com.google.inject.Injector;
import services.StravaService;

public class RunStrava {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new Module());
        StravaService stravaService = injector.getInstance(StravaService.class);

        stravaService.giveKudosToEveryone();
    }
}
