package engine;

import java.util.ArrayList;
import java.util.List;

public final class GameObject {

    private final String name;
    private final List<Component> components;
    public Transform transform;

    public GameObject(String name, Transform transform) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
    }

    public GameObject(String name) {
        this(name, new Transform());
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        try {
            for (Component c : components) {
                if (componentClass.isAssignableFrom(c.getClass())) {
                    return componentClass.cast(c);
                }
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
            assert false : "Error: Casting component";
        }

        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i = 0; i < components.size(); i++) {
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component c) {
        components.add(c);
        c.setParent(this);
    }

    public void update(float dt) {
        for (Component c : components) {
            c.update(dt);
        }
    }
    
    public void start() {
        for (Component c : components) {
            c.start();
        }
    }
}
