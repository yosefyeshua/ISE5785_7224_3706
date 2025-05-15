package renderer;

import primitives.Color;
import primitives.Point;
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
        List<Point> intersections = scene.geometries.findIntersections(ray);
        if (intersections == null || intersections.isEmpty()) {
            return scene.background;
        }
        Point closestPoint = ray.findClosestPoint(intersections);
        return calcColor(closestPoint);
    }

    /**
     * Computes the color at a given point.
     * <p>
     * This simple implementation returns only the ambient light of the scene.
     *
     * @param point the {@link Point} at which to compute the color
     * @return the ambient light {@link Color}
     */
    private Color calcColor(Point point) {
        return scene.ambientLight.getIntensity();
    }
}
