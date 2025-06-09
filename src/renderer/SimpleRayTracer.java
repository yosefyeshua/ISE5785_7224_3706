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

    /**
     * A small value used to shift the intersection point slightly away from the surface
     * to avoid self-shadowing artifacts.
     */
    private static final double DELTA = 0.1;
    /**
     * The maximum recursion level for calculating color contributions from reflections and refractions.
     * This limits the depth of recursive calls to prevent excessive computation.
     */
    private static final int MAX_CALC_COLOR_LEVEL = 30;
    /**
     * The minimum value for calculating color contributions.
     * If the attenuation factor is below this threshold, the contribution is considered negligible.
     */
    private static final double MIN_CALC_COLOR_K = 0.001;
    /**
     * The initial attenuation factor for color calculations.
     * This is set to 1.0, meaning no attenuation at the start of the calculation.
     */
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
     * Computes the transparency coefficient for the light ray from the light source to the intersection point.
     *
     * @param intersection the intersection containing the light and geometry information
     * @return the total transparency coefficient as a {@link Double3}
     */
    private Double3 transparency(Intersection intersection) {
        List<Intersection> intersections = scene.geometries.calculateIntersections(
                newSecondaryRay(intersection, intersection.l.scale(-1)),
                intersection.light.getDistance(intersection.point));
        if (intersections == null || intersections.isEmpty()) {
            return Double3.ONE;
        }
        Double3 ktr = Double3.ONE;
        for (Intersection i : intersections) {
            ktr = ktr.product(i.material.kT);

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

    /**
     * Traces a ray and determines the color of the pixel it intersects with.
     * <p>
     * If no intersection is found, the scene's background color is returned.
     *
     * @param ray the {@link Ray} to trace
     * @return the computed {@link Color} at the intersection point, or background if none
     */
    @Override
    public Color traceRay(Ray ray) {
        Intersection intersection = findClosestIntersection(ray);
        if (intersection == null) {
            return scene.background;
        }
        return calcColor(intersection, ray);
    }

    /**
     * Computes the final color at a given intersection point.
     * <p>
     * Includes ambient lighting and local effects (diffuse and specular).
     *
     * @param intersection the intersection data
     * @param ray the incoming ray
     * @return the final computed {@link Color}
     */
    private Color calcColor(Intersection intersection, Ray ray) {
        if (!preprocessIntersection(intersection, ray.getDirection())) {
            return Color.BLACK;
        }

        return scene.ambientLight.getIntensity()
                .scale(intersection.geometry.getMaterial().kA)
                .add(calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K));
    }

    /**
     * Computes the color at a given intersection point, considering local and global effects.
     * <p>
     * This method is called recursively to handle reflection and refraction effects.
     *
     * @param intersection the intersection data
     * @param level        the current recursion level
     * @param k            the current accumulated attenuation factor
     * @return the final computed {@link Color} at the intersection point
     */
    private Color calcColor(Intersection intersection, int level, Double3 k) {
        return calcColorLocalEffects(intersection)
                .add(calcGlobalEffects(intersection, level, k));
    }

    /**
     * Prepares the intersection for lighting calculations by computing
     * the normalized viewing vector, surface normal, and their dot product.
     *
     * @param intersection the intersection to preprocess
     * @param v the direction of the incoming ray
     * @return {@code true} if the point is lit; {@code false} if it's facing away
     */
    public boolean preprocessIntersection(Intersection intersection, Vector v) {
        intersection.v = v.normalize();
        intersection.normal = intersection.geometry.getNormal(intersection.point);
        intersection.vNormal = intersection.normal.dotProduct(v);
        return !Util.isZero(intersection.vNormal);
    }

    /**
     * Sets the light direction and related dot product values for the intersection point.
     *
     * @param intersection the intersection to update
     * @param lightSource the current {@link LightSource}
     * @return {@code true} if the light and view vectors point in the same general direction,
     *         indicating a visible light contribution
     */
    public boolean setLightSource(Intersection intersection, LightSource lightSource) {
        intersection.light = lightSource;
        intersection.l = lightSource.getL(intersection.point);
        intersection.lNormal = intersection.normal.dotProduct(intersection.l);
        return Util.alignZero(intersection.lNormal * intersection.vNormal) > 0;
    }

    /**
     * Computes the local lighting effects (diffuse and specular) at the intersection point.
     *
     * @param intersection the intersection to shade
     * @return the total {@link Color} contribution from all light sources
     */
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

    /**
     * Computes the specular reflection component at the intersection point.
     *
     * @param intersection the intersection to evaluate
     * @return the {@link Double3} representing the specular reflection strength
     */
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

    /**
     * Computes the diffuse reflection component at the intersection point.
     *
     * @param intersection the intersection to evaluate
     * @return the {@link Double3} representing the diffuse reflection strength
     */
    private Double3 calcDiffusive(Intersection intersection) {
        double ln = Util.alignZero(intersection.lNormal);
        return intersection.material.kD.scale(Math.abs(ln));
    }

    /**
     * Computes the color contribution of a global effect (reflection or refraction).
     *
     * @param secRay the secondary (reflected or refracted) {@link Ray}
     * @param level  the remaining recursion depth
     * @param k      the current accumulated factor
     * @param kRorT  the reflection/refraction coefficient
     * @return the color contribution of the global effect
     */
    private Color calcGlobalEffect(Ray secRay, int level, Double3 k, Double3 kRorT) {
        Intersection intersection = findClosestIntersection(secRay);
        if (intersection == null || !preprocessIntersection(intersection, secRay.getDirection())) {
            return scene.background;
        }
        return calcColor(intersection, level - 1, k.product(kRorT)).scale(kRorT);
    }

    /**
     * Computes the global lighting effects (reflection and refraction).
     *
     * @param intersection the current intersection point
     * @param level        the current recursion level
     * @param k            the current accumulated attenuation factor
     * @return the {@link Color} contribution from global effects
     */
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
