package renderer;

import geometries.Intersectable.Intersection;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;

/**
 * {@code SimpleRayTracer} is a basic implementation of the {@link RayTracerBase} class.
 * <p>
 * This ray tracer computes lighting using ambient, diffuse, and specular components.
 * Shadows and reflection/refraction are not supported in this implementation.
 */
public class SimpleRayTracer extends RayTracerBase {

    private static final double DELTA = 0.1;
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final Double3 INITIAL_K = Double3.ONE;

    /**
     * Constructs a {@code SimpleRayTracer} for the given scene.
     *
     * @param scene the {@link Scene} to render
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Creates a new ray slightly shifted from the intersection point in the direction of the given vector.
     *
     * @param intersection the intersection point
     * @param v            the direction vector
     * @return the shifted {@link Ray}
     */
    private Ray newSecondaryRay(Intersection intersection, Vector v) {
        double nv = intersection.normal.dotProduct(v);
        if (Util.isZero(nv)) {
            return new Ray(intersection.point, v);
        }
        Vector delta = intersection.normal.scale(nv > 0 ? DELTA : -DELTA);
        return new Ray(intersection.point.add(delta), v);
    }

    /**
     * Determines if the point at the intersection is unshaded from a given light source.
     *
     * @param intersection the intersection point
     * @param light        the light source
     * @return true if the point is unshaded, false otherwise
     */
    private boolean unshaded(Intersection intersection, LightSource light) {
        Vector pointToLight = intersection.l.scale(-1);
        Ray shadowRay = newSecondaryRay(intersection, pointToLight);
        List<Intersection> intersections = scene.geometries.calculateIntersections(shadowRay);
        if (intersections == null || intersections.isEmpty()) {
            return true;
        }
        for (Intersection i : intersections) {
            if (i.point.distance(intersection.point) < light.getDistance(intersection.point)
                    && i.geometry.getMaterial().kT.lowerThan(MIN_CALC_COLOR_K)) {
                return false;
            }
        }
        return true;
    }

    private Double3 transparency(Intersection intersection) {
        Vector pointToLight = intersection.l.scale(-1);
        Ray shadowRay = newSecondaryRay(intersection, pointToLight);
        List<Intersection> intersections = scene.geometries.calculateIntersections(shadowRay);
        if (intersections == null || intersections.isEmpty()) {
            return Double3.ONE;
        }
        Double3 ktr = Double3.ONE;
        for (Intersection i : intersections) {
            if (i.point.distance(intersection.point) < intersection.light.getDistance(intersection.point)) {
                ktr = ktr.product(i.material.kT);
            }

            if (ktr.lowerThan(MIN_CALC_COLOR_K)) {
                return Double3.ZERO;
            }
        }
        return ktr;
    }

    /**
     * Constructs a refracted ray from the intersection point in the same direction as the incoming ray.
     *
     * @param intersection the intersection point
     * @param v            the incoming ray direction
     * @return the refracted {@link Ray}
     */
    private Ray constractRefractedRay(Intersection intersection, Vector v) {
        return newSecondaryRay(intersection, v);
    }

    /**
     * Constructs a reflected ray from the intersection point.
     *
     * @param intersection the intersection point
     * @param v            the incoming ray direction
     * @return the reflected {@link Ray}
     */
    private Ray constructReflectedRay(Intersection intersection, Vector v) {
        Vector r = v.subtract(intersection.normal.scale(2 * intersection.vNormal));
        return newSecondaryRay(intersection, r);
    }

    /**
     * Finds the closest intersection point of a given ray with the scene's geometries.
     *
     * @param ray the {@link Ray} to check
     * @return the closest {@link Intersection}, or null if none found
     */
    private Intersection findClosestIntersection(Ray ray) {
        List<Intersection> intersections = scene.geometries.calculateIntersections(ray);
        if (intersections == null || intersections.isEmpty()) {
            return null;
        }
        return ray.findClosestIntersection(intersections);
    }

    @Override
    public Color traceRay(Ray ray) {
        Intersection intersection = findClosestIntersection(ray);
        if (intersection == null) {
            return scene.background;
        }
        return calcColor(intersection, ray);
    }

    private Color calcColor(Intersection intersection, Ray ray) {
        if (!preprocessIntersection(intersection, ray.getDirection())) {
            return Color.BLACK;
        }

        return scene.ambientLight.getIntensity()
                .scale(intersection.geometry.getMaterial().kA)
                .add(calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K));
    }

    private Color calcColor(Intersection intersection, int level, Double3 k) {
        return calcColorLocalEffects(intersection)
                .add(calcGlobalEffects(intersection, level, k));
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
            if (!setLightSource(intersection, light)) {
                continue; // Skip if the light source is not relevant
            }
            Double3 ktr = transparency(intersection);
            if (!ktr.lowerThan(MIN_CALC_COLOR_K)) {
                Color iL = light.getIntensity(intersection.point).scale(ktr);
                color = color.add(iL.scale(calcDiffusive(intersection).add(calcSpecular(intersection))));
            }
        }
        return color;
    }

    private Double3 calcSpecular(Intersection intersection) {
        Vector r = intersection.l.subtract(
                intersection.normal.scale(2 * intersection.lNormal)
        );
        Vector negV = intersection.v.scale(-1);
        double vr = Util.alignZero(negV.dotProduct(r));
        if (vr <= 0) {
            return Double3.ZERO;
        }
        return intersection.geometry.getMaterial().kS.scale(
                Math.pow(vr, intersection.geometry.getMaterial().nShininess)
        );
    }

    private Double3 calcDiffusive(Intersection intersection) {
        double ln = Util.alignZero(intersection.lNormal);
        return intersection.material.kD.scale(Math.abs(ln));
    }

    private Color calcGlobalEffect(Ray secRay, int level, Double3 k, Double3 kRorT) {
        Intersection intersection = findClosestIntersection(secRay);
        if (intersection == null || !preprocessIntersection(intersection, secRay.getDirection())) {
            return scene.background;
        }
        return calcColor(intersection, level - 1, k.product(kRorT)).scale(kRorT);
    }

    private Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
        Double3 kt = intersection.geometry.getMaterial().kT;
        Double3 kr = intersection.geometry.getMaterial().kR;
        if (level == 0 || k.lowerThan(MIN_CALC_COLOR_K))
            return Color.BLACK;
        Color color = Color.BLACK;

        if (!kt.lowerThan(MIN_CALC_COLOR_K)) {
            color = color.add(calcGlobalEffect(constractRefractedRay(intersection, intersection.v), level - 1, k,kt));
        }
        if (!kr.lowerThan(MIN_CALC_COLOR_K)) {
            color = color.add(calcGlobalEffect(constructReflectedRay(intersection, intersection.v), level - 1, k, kr));
        }
        return color;
    }
}
