package ua.alexeyrudkovskiy.logger;

public interface LoggerInterface {

    public void info(String tag, String message);

    public void warning(String tag, String message);

    public void error(String tag, String message);

}
