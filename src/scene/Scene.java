package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a 3D scene containing geometries, background color, and ambient lighting.
 * <p>
 * This class encapsulates all the components needed for rendering a scene using ray tracing.
 */
public class Scene {

    /**
     * The name of the scene.
     */
    public String name;

    /**
     * The background color of the scene. Default is black.
     */
    public Color background = Color.BLACK;

    /**
     * The ambient light of the scene. Default is {@link AmbientLight#NONE}.
     */
    public AmbientLight ambientLight = AmbientLight.NONE;

    /**
     * The collection of geometries present in the scene.
     */
    public Geometries geometries = new Geometries();

    public List<LightSource> lights = new LinkedList<>();
    /**
     * Constructs a new {@code Scene} with the given name.
     *
     * @param name the name of the scene
     */
    public Scene(String name) {
        this.name = name;
    }

    /**
     * Sets the background color of the scene.
     *
     * @param background the {@link Color} to use as background
     * @return the updated {@code Scene} instance (for chaining)
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Sets the ambient light of the scene.
     *
     * @param ambientLight the {@link AmbientLight} to use
     * @return the updated {@code Scene} instance (for chaining)
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Sets the geometries of the scene.
     *
     * @param geometries the {@link Geometries} collection to use
     * @return the updated {@code Scene} instance (for chaining)
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }

    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }
}
