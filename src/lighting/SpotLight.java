package lighting;

import primitives.Color;
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
        return super.getIntensity(p).scale(Math.max(0, direction.dotProduct(getL(p))));
    }

    @Override
    public Vector getL(Point p) {
        return super.getL(p);
    }

    /**
     * Sets the constant attenuation factor.
     *
     * @param kC constant factor
     * @return the spotlight itself for chaining
     */
    public SpotLight SetKC(double kC) {
        super.setKC(kC);
        return this;
    }

    /**
     * Sets the linear attenuation factor.
     *
     * @param kL linear factor
     * @return the spotlight itself for chaining
     */
    public SpotLight SetKL(double kL) {
        super.setKL(kL);
        return this;
    }

    /**
     * Sets the quadratic attenuation factor.
     *
     * @param kQ quadratic factor
     * @return the spotlight itself for chaining
     */
    public SpotLight SetKQ(double kQ) {
        super.setKQ(kQ);
        return this;
    }
}
