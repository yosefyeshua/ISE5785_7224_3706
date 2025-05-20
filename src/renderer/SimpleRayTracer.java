package renderer;

import geometries.Intersectable.Intersection;
import primitives.Color;
import primitives.Ray;
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
        return calcColor(closestIntersection);
    }

    /**
     * Computes the color at a given point.
     * <p>
     * This simple implementation returns only the ambient light of the scene.
     *
     * @param intersection the {@link Intersection} at which to compute the color
     * @return the ambient light {@link Color}
     */
    private Color calcColor(Intersection intersection) {
        return scene.ambientLight.getIntensity().add(intersection.geometry.getEmission());
    }
}
