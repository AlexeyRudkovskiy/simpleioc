package ua.alexeyrudkovskiy;

public abstract class AbstractServiceProvider implements ServiceProviderInterface {

    private boolean loaded = false;

    protected Container container = null;

    public AbstractServiceProvider() {
        this.container = Container.getInstance();
    }

    @Override
    public String[] dependsOn() {
        return new String[0];
    }

    @Override
    public boolean isLoaded() {
        return this.loaded;
    }

    @Override
    public void toggleLoaded() {
        this.loaded = !this.loaded;
    }

}
