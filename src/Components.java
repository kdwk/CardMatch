import java.util.HashMap;
import java.awt.Component;

public final class Components {
    private static HashMap<String, Component> register = new HashMap<>();
    public static <T extends Component> T get(String id) {
        if (register.containsKey(id)) {
            return (T)(register.get(id));
        }
        System.out.println(id + " is not present in the components register!");
        System.exit(0);
        return null;
    }
    public static <T extends Component> T reg(String id, T component) {
        if (!register.containsKey(id) && component!=null) {
            register.put(id, component);
            return component;
        }
        System.out.println(id + " is already present in the components register!");
        System.exit(0);
        return null;
    }
}
