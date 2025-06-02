package lighting;

import primitives.Color;
import primitives.Double3;
import primitives.Point;
import primitives.Vector;

/**
 * Represents a spotlight, which is a point light with a specific direction.
 * The intensity is attenuated based on the angle between the spotlight direction
 * and the direction to the target point.
 */
public class SpotLight extends PointLight {

    /**
     * The direction of the spotlight beam.
     */
    private final Vector direction;

    private double narrowBeam = 1;

    /**
     * Constructs a spotlight with specified intensity, position, and direction.
     *
     * @param intensity the light intensity
     * @param position  the light position
     * @param direction the beam direction
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        if (narrowBeam == 1) {
            return super.getIntensity(p).scale(Math.max(0, direction.dotProduct(getL(p))));
        }
        return super.getIntensity(p).scale(Math.pow(Math.max(0d, direction.dotProduct(getL(p))),narrowBeam));
    }

    @Override
    public Vector getL(Point p) {
        return super.getL(p);
    }

    public SpotLight setNarrowBeam(double narrowBeam) {
        this.narrowBeam = narrowBeam;
        return this;
    }
}
