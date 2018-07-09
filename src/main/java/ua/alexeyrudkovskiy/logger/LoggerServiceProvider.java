package ua.alexeyrudkovskiy.logger;

import ua.alexeyrudkovskiy.AbstractServiceProvider;

public class LoggerServiceProvider extends AbstractServiceProvider {

    @Override
    public void register() {
        container.bind(LoggerInterface.class, LoggerService.class);
        container.bind("logger", LoggerService.class);
    }

}
