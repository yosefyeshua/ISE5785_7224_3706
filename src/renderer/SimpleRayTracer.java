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


    private Ray newSecondaryRay(Intersection intersection, Vector v) {
        Vector delta = intersection.normal.scale(intersection.lNormal < 0 ? DELTA : -DELTA);
        return new Ray(intersection.point.add(delta), v);
    }

    private boolean unshaded(Intersection intersection, LightSource light) {
        Vector pointToLight = intersection.l.scale(-1);
        Ray shadowRay = newSecondaryRay(intersection, pointToLight);
        List<Intersection> intersections = scene.geometries.calculateIntersections(shadowRay);
        if (intersections == null || intersections.isEmpty()) {
            return true; // No intersection means the point is unshaded
        }
        for (Intersection i : intersections) {
            if (i.point.distance(intersection.point) < light.getDistance(intersection.point) && !i.geometry.getMaterial().kT.lowerThan(MIN_CALC_COLOR_K)) {
                return false;
            }
        }
        return false;
    }

    private Ray constractRefractedRay(Intersection intersection, Vector  v){
        return new Ray(intersection.point, v);
    }

    private Ray constructReflectedRay(Intersection intersection, Vector v) {
        Vector r = v.subtract(intersection.normal.scale(2 * intersection.vNormal));
        return new Ray(intersection.point, r);
    }

    private Intersection findClosestIntersection(Ray ray){
        List<Intersection> intersections = scene.geometries.calculateIntersections(ray);
        if (intersections == null || intersections.isEmpty()) {
            return null;
        }
        return ray.findClosestIntersection(intersections);
    }


    /**
     * Constructs a {@code SimpleRayTracer} for the given scene.
     *
     * @param scene the {@link Scene} to render
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
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
            if (setLightSource(intersection, light) && unshaded(intersection, light)) {
                Color iL = light.getIntensity(intersection.point);
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

    private Color calcColor(Intersection intersection, int level, Double3 k) {
        return calcColorLocalEffects(intersection)
                .add(calcGlobalEffects(intersection, level, k));
    }

    private Color calcGlobalEffect(Ray secRay, int level, Double3 k, Double3 kRorT){
        Intersection intersection = findClosestIntersection(secRay);
        if (intersection == null) {
            return scene.background;
        }
        return calcColor(intersection, level - 1, k.product(kRorT));
    }

    private Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
        if (level == 0 || k.lowerThan(MIN_CALC_COLOR_K))
            return Color.BLACK;
        return calcGlobalEffect(constractRefractedRay(intersection, intersection.v), level - 1, k, intersection.geometry.getMaterial().kT).scale(intersection.geometry.getMaterial().kT)
                .add(calcGlobalEffect(constructReflectedRay(intersection, intersection.v), level - 1, k, intersection.geometry.getMaterial().kR)).scale(intersection.geometry.getMaterial().kR);
    }
}
