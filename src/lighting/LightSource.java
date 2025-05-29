package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Interface representing a light source that emits light toward a point in the scene.
 */
public interface LightSource {

    /**
     * Returns the intensity of the light at a given point.
     *
     * @param p the point to compute the light intensity at
     * @return the color representing the light intensity
     */
    Color getIntensity(Point p);

    /**
     * Returns the normalized direction vector from the light to the specified point.
     *
     * @param p the point to compute the direction to
     * @return the normalized vector from light to point
     */
    Vector getL(Point p);
}

