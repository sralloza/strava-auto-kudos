package exceptions;

public class NoActivitiesException extends RuntimeException{
    public NoActivitiesException(){
        super("No activities found");
    }
}
