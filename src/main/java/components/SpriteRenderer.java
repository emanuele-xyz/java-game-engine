package components;

import engine.Component;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;

public class SpriteRenderer extends Component {

    /*
        Texture Coordinates
        0: (0, 1)       // Bottom right
        1: (0, 0)       // Bottom Left
        2: (1, 1)       // Top Right
        3: (1, 0)       // Top Left
     */
    private static final Vector2f[] COORDINATES = {
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1),
    };

    private Vector4f color;

    private Texture texture;

    public SpriteRenderer(Vector4f color) {
        this.color = color;
        this.texture = null;
    }

    public SpriteRenderer(Texture texture) {
        this.texture = texture;
        this.color = new Vector4f(1, 1, 1, 1);
    }

    @Override
    public void start() {

    }

    @Override
    public void update(float dt) {

    }

    public Vector4f getColor() {
        return color;
    }

    public Texture getTexture() {
        return texture;
    }

    // TODO: we are leaking an array, not a good design
    public Vector2f[] getCoordinates() {
        return COORDINATES;
    }
}
