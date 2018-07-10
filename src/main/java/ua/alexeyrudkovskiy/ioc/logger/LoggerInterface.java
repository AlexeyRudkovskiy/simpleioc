package ua.alexeyrudkovskiy.ioc.logger;

public interface LoggerInterface {

    public void info(String tag, String message);

    public void warning(String tag, String message);

    public void error(String tag, String message);

}
