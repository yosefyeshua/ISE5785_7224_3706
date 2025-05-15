package lighting;

import primitives.Color;

/**
 * The {@code AmbientLight} class represents a uniform ambient light in a scene,
 * illuminating all objects equally regardless of their location or orientation.
 * <p>
 * This class provides a static constant for no ambient light, as well as a constructor
 * for specifying custom intensity.
 */
public class AmbientLight {
    /**
     * The intensity (color) of the ambient light.
     */
    private final Color intensity;

    /**
     * A constant representing no ambient light (black).
     */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK);

    /**
     * Constructs an {@code AmbientLight} with the specified intensity.
     *
     * @param intensity the {@link Color} of the ambient light
     */
    public AmbientLight(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * Returns the intensity (color) of the ambient light.
     *
     * @return the {@link Color} representing the ambient light's intensity
     */
    public Color getIntensity() {
        return intensity;
    }
}
