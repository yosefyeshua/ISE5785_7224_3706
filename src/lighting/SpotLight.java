package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class SpotLight extends PointLight {

    private final Vector direction;

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

    public SpotLight SetKC(double kC) {
        super.setKC(kC);
        return this;
    }

    public SpotLight SetKL(double kL) {
        super.setKL(kL);
        return this;
    }
    public SpotLight SetKQ(double kQ) {
        super.setKQ(kQ);
        return this;
    }
}
