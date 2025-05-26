package renderer;

import geometries.Intersectable.Intersection;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;

/**
 * A simple implementation of {@link RayTracerBase} that returns the ambient light color
 * for the closest intersection point with a geometry in the scene.
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Constructs a new {@code SimpleRayTracer} for the specified scene.
     *
     * @param scene the {@link Scene} to trace rays through
     */
    SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Traces the given ray and returns the ambient light color at the closest
     * intersection point, or the scene's background color if there is no intersection.
     *
     * @param ray the {@link Ray} to trace
     * @return the {@link Color} at the closest intersection, or background if none
     */
    @Override
    public Color traceRay(Ray ray) {
        List<Intersection> intersections = scene.geometries.calculateIntersections(ray);
        if (intersections == null || intersections.isEmpty()) {
            return scene.background;
        }
        Intersection closestIntersection = ray.findClosestIntersection(intersections);
        return calcColor(closestIntersection, ray);
    }

    /**
     * Computes the color at a given point.
     * <p>
     * This simple implementation returns only the ambient light of the scene.
     *
     * @param intersection the {@link Intersection} at which to compute the color
     * @return the ambient light {@link Color}
     */
    private Color calcColor(Intersection intersection, Ray ray) {
        if (!preprocessIntersection(intersection, ray.getDirection())) {
            return Color.BLACK;
        }
        return scene.ambientLight.getIntensity().scale(intersection.geometry.getMaterial().kA)
                .add(calcColorLocalEffects(intersection));
    }

    public boolean preprocessIntersection(Intersection intersection, Vector v) {
        intersection.v = v.normalize();
        intersection.normal = intersection.geometry.getNormal(intersection.point);
        intersection.vNormal = intersection.normal.dotProduct(v);
        return !Util.isZero(intersection.vNormal);
    }

    public boolean setLightSource(Intersection intersection, LightSource lightSource) {
        intersection.light = lightSource;
        intersection.l = lightSource.getL(intersection.point);
        intersection.lNormal = intersection.normal.dotProduct(intersection.l);
        return Util.alignZero(intersection.lNormal * intersection.vNormal) > 0;
    }

    private Color calcColorLocalEffects(Intersection intersection) {
        Color color = intersection.geometry.getEmission();
        for (LightSource light : scene.lights) {
            if (setLightSource(intersection, light)) {
                Color iL = light.getIntensity(intersection.point);
                color = color.add(iL.scale(calcDiffusive(intersection).add(calcSpecular(intersection))));
            }
        }
        return color;
    }

    private Double3 calcSpecular(Intersection intersection) {
        Vector r = intersection.l.subtract(intersection.normal.scale(2 * intersection.lNormal));
        Vector negV = intersection.v.scale(-1);
        double vr = Util.alignZero(negV.dotProduct(r));
        if (vr <= 0) {
            return Double3.ZERO;
        }
        return intersection.geometry.getMaterial().kS.scale(Math.pow(vr, intersection.geometry.getMaterial().nShininess));

    }

    private Double3 calcDiffusive(Intersection intersection) {
        double ln = Util.alignZero(intersection.lNormal);
        return intersection.material.kD.scale(Math.abs(ln));
    }
}
