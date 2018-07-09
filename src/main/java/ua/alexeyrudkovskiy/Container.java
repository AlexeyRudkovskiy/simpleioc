package ua.alexeyrudkovskiy;

import ua.alexeyrudkovskiy.exceptions.CannotRegisterServiceProvidersException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class Container {

    private static Container instance = null;

    private ArrayList<ServiceProviderInterface> serviceProvidersArrayList = null;

    private HashMap<String, BindingElement> classesImplementations = null;

    private short registerServiceProvidersIterator = 0;

    private Container() {
        // empty
        serviceProvidersArrayList = new ArrayList<>();
        classesImplementations = new HashMap<>();
    }

    public static Container getInstance() {
        if (instance == null) {
            instance = new Container();
        }
        return instance;
    }

    public void registerServiceProvider(ServiceProviderInterface serviceProviderInterface) {
        serviceProvidersArrayList.add(serviceProviderInterface);
    }

    public void registerServiceProviders() throws CannotRegisterServiceProvidersException {
        if (this.registerServiceProvidersIterator == serviceProvidersArrayList.size()) {
            throw new CannotRegisterServiceProvidersException();
        }

        boolean shouldExecuteAgain = false;

        for (ServiceProviderInterface serviceProvider : this.serviceProvidersArrayList) {
            if (serviceProvider.isLoaded()) {
                continue;
            }

            try {
                String[] dependedFrom = serviceProvider.dependsOn();
                boolean isCanBeExecuted = true;
                for (String dependedFromItem : dependedFrom) {
                    isCanBeExecuted = this.classesImplementations.containsKey(dependedFromItem);
                }

                if (isCanBeExecuted) {
                    serviceProvider.register();
                    serviceProvider.toggleLoaded();
                } else {
                    shouldExecuteAgain = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (shouldExecuteAgain) {
            this.registerServiceProvidersIterator++;
            this.registerServiceProviders();
        }
    }

    public void bind(Type targetType, Object instanceClass) {
        this.bind(targetType.toString(), instanceClass, false);
    }

    public void bind(String tag, Object instance) {
        this.bind(tag, instance, false);
    }

    public void bind(String tag, BindingElement.ExecutableTarget executableTarget) {
        this.bind(tag, executableTarget, false);
    }

    public void bind(Type tag, BindingElement.ExecutableTarget executableTarget) {
        this.bind(tag.toString(), executableTarget, false);
    }

    public void bindSingleton(Type targetType, Object instanceClass) {
        this.bind(targetType.toString(), instanceClass, true);
    }

    public void bindSingleton(String targetType, Object instanceClass) {
        this.bind(targetType, instanceClass, true);
    }

    public void bindSingleton(Type targetType, BindingElement.ExecutableTarget instanceClass) {
        this.bind(targetType.toString(), instanceClass, true);
    }

    public void bindSingleton(String targetType, BindingElement.ExecutableTarget instanceClass) {
        this.bind(targetType, instanceClass, true);
    }

    public void bind(String tag, Object instance, boolean isSingleton) {
        BindingElement.BindingElementType bindingElementType = null;

        if (instance instanceof BindingElement.ExecutableTarget) {
            bindingElementType = !isSingleton
                    ? BindingElement.BindingElementType.ACTION
                    : BindingElement.BindingElementType.ACTION_SINGLETON;
        } else {
            bindingElementType = !isSingleton
                    ? BindingElement.BindingElementType.DEFAULT
                    : BindingElement.BindingElementType.SINGLETON;
        }

        BindingElement bindingElement = BindingElement.createElement(tag, bindingElementType, instance);
        classesImplementations.put(tag, bindingElement);
    }

    public Object createInstanceOf(Class instanceClass) {
        Constructor constructors[] = instanceClass.getDeclaredConstructors();
        Constructor constructor = constructors[0];
        Parameter[] parameters = constructor.getParameters();
        Object parametersValues[] = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Type type = parameters[i].getType();
            Inject injectAnnotation = parameters[i].getAnnotation(Inject.class);
            String tag = injectAnnotation != null && !(injectAnnotation.name().isEmpty())
                    ? injectAnnotation.name()
                    : type.toString();
            parametersValues[i] = getObjectFor(tag);
        }

        try {
            return constructor.newInstance(parametersValues);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Object getObjectFor(Class instanceClass) {
        return getObjectFor(instanceClass.toString());
    }

    public Object getObjectFor(String tag) {
        BindingElement valueToBeInjectedElement = this.classesImplementations.getOrDefault(tag, null);
        if (valueToBeInjectedElement != null) {
            if (
                valueToBeInjectedElement.type == BindingElement.BindingElementType.SINGLETON ||
                valueToBeInjectedElement.type == BindingElement.BindingElementType.ACTION_SINGLETON
            ) {
                Object metadata = valueToBeInjectedElement.metadata;
                if (metadata == null) {
                    metadata = getBindingElementValue(valueToBeInjectedElement);
                    valueToBeInjectedElement.metadata = metadata;
                }
                return metadata;
            } else {
                return getBindingElementValue(valueToBeInjectedElement);
            }
        }

        return null;
    }

    private Object getBindingElementValue(BindingElement bindingElement) {
        Object valueToBeInjected = bindingElement.target;
        if (
            bindingElement.type == BindingElement.BindingElementType.ACTION ||
            bindingElement.type == BindingElement.BindingElementType.ACTION_SINGLETON
        ) {
            return ((BindingElement.ExecutableTarget) valueToBeInjected).execute();
        } else {
            if (valueToBeInjected instanceof Class) {
                try {
                    return ((Class) valueToBeInjected).newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                return valueToBeInjected;
            }
        }
        return null;
    }

}
