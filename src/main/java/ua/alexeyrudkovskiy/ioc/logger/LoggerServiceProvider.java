package ua.alexeyrudkovskiy.ioc.logger;

import ua.alexeyrudkovskiy.ioc.AbstractServiceProvider;

public class LoggerServiceProvider extends AbstractServiceProvider {

    @Override
    public void register() {
        container.bind(LoggerInterface.class, LoggerService.class);
        container.bind("logger", LoggerService.class);
    }

}
