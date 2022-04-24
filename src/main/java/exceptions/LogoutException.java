package exceptions;

public class LogoutException extends RuntimeException {
    public LogoutException(String currentUrl) {
        super("Logout failed. Current URL: " + currentUrl);
    }
}
