package components;

import engine.Component;

public class FontRenderer extends Component {

    @Override
    public void start() {
        if (getParent().getComponent(SpriteRenderer.class) != null) {
            System.out.println("Found font Renderer");
        }
    }

    @Override
    public void update(float dt) {

    }
}
