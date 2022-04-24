package exceptions;

public class LoginException extends RuntimeException {
    public LoginException(String currentUrl) {
        super("Login failed. Response URL: " + currentUrl);
    }
}
