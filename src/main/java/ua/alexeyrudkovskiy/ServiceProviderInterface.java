package ua.alexeyrudkovskiy;

public interface ServiceProviderInterface {

    /**
     * Register services
     */
    void register();

    String[] dependsOn();

    boolean isLoaded();

    void toggleLoaded();

}
