package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Represents a point light source located at a specific position in space,
 * emitting light uniformly in all directions.
 */
public class PointLight extends Light implements LightSource {

    /**
     * Position of the point light.
     */
    private final Point position;

    /**
     * Constant attenuation factor.
     */
    private double kC = 1;

    /**
     * Linear attenuation factor.
     */
    private double kL = 0;

    /**
     * Quadratic attenuation factor.
     */
    private double kQ = 0;

    /**
     * Constructs a point light with specified intensity and position.
     *
     * @param intensity the light intensity
     * @param position  the light position
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    @Override
    public Color getIntensity(Point p) {
        double distance = position.distance(p);
        double denominator = kC + kL * distance + kQ * position.distanceSquared(p);
        return intensity.scale(1 / denominator);
    }

    @Override
    public Vector getL(Point p) {
        return p.subtract(position).normalize();
    }

    /**
     * Sets the constant attenuation factor.
     *
     * @param kC constant factor
     * @return the current point light for chaining
     */
    public PointLight setKC(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * Sets the linear attenuation factor.
     *
     * @param kL linear factor
     * @return the current point light for chaining
     */
    public PointLight setKL(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * Sets the quadratic attenuation factor.
     *
     * @param kQ quadratic factor
     * @return the current point light for chaining
     */
    public PointLight setKQ(double kQ) {
        this.kQ = kQ;
        return this;
    }
    @Override
    public double getDistance(Point point) {
        return position.distance(point);
    }
}

