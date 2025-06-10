package lighting;

import primitives.Color;

/**
 * {@code Light} is an abstract base class representing a light source in the scene
 * <p>
 * It provides a common interface for retrieving light intensity, and is extended by
 * specific types of lights such as directional, point, and spotlights.
 */
abstract class Light {

    /**
     * The intensity of the light, represented as a {@link Color}.
     */
    protected final Color intensity;

    /**
     * Constructs a light with the specified intensity.
     *
     * @param intensity the light intensity color
     */
    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * Returns the intensity of the light.
     *
     * @return the intensity as a {@link Color}
     */
    public Color getIntensity() {
        return intensity;
    }


}
