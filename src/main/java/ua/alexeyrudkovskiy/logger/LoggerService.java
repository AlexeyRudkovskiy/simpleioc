package ua.alexeyrudkovskiy.logger;

import java.util.Date;

public class LoggerService implements LoggerInterface {

    @Override
    public void info(String tag, String message) {
        this.print("INFO", tag, message);
    }

    @Override
    public void warning(String tag, String message) {
        this.print("WARNING", tag, message);
    }

    @Override
    public void error(String tag, String message) {
        this.print("ERROR", tag, message);
    }

    private synchronized void print(String type, String tag, String message) {
        Date date = new Date();
        System.out.println(String.format("[%s %s] %s", tag, type, message));
    }

}
