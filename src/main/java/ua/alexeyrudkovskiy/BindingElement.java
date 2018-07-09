package ua.alexeyrudkovskiy;

public class BindingElement {

    public enum BindingElementType {
        DEFAULT, SINGLETON, ACTION, ACTION_SINGLETON
    }

    public interface ExecutableTarget {
        Object execute();
    }

    public String tag;

    public BindingElementType type;

    public Object target;

    public Object metadata = null;

    public static BindingElement createElement(String tag, BindingElementType type, Object target) {
        BindingElement bindingElement = new BindingElement();
        bindingElement.tag = tag;
        bindingElement.target = target;
        bindingElement.type = type;
        return bindingElement;
    }

}
