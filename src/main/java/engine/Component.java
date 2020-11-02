package engine;

public abstract class Component {

    private GameObject parent;

    public void start() {

    }

    public abstract void update(float dt);

    public GameObject getParent() {
        return parent;
    }

    public void setParent(GameObject parent) {
        this.parent = parent;
    }
}
