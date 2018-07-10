package ua.alexeyrudkovskiy.ioc;

public interface ServiceProviderInterface {

    /**
     * Register services
     */
    void register();

    String[] dependsOn();

    boolean isLoaded();

    void toggleLoaded();

}
