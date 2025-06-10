package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Represents a directional light source, which emits light in a fixed direction
 * as if from an infinitely distant source (e.g., sunlight).
 */
public class DirectionalLight extends Light implements LightSource {

    /**
     * The normalized direction vector of the light
     */
    private final Vector direction;

    /**
     * Constructs a directional light with given intensity and direction.
     *
     * @param intensity the light intensity
     * @param direction the light direction
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        return intensity;
    }

    @Override
    public Vector getL(Point p) {
        return direction;
    }

    @Override
    public double getDistance(Point point) {
        // For directional light, the distance is considered infinite
        return Double.POSITIVE_INFINITY;
    }
}

