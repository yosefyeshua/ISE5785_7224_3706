package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * A composite geometry class that represents a collection of {@link Intersectable} objects.
 * <p>
 * Provides functionality to add multiple geometries and to find all intersection points
 * between a given {@link Ray} and the contained geometries.
 */
public class Geometries implements Intersectable {
    private final List<Intersectable> geometries = new LinkedList<>();

    public Geometries() {}

    /**
     * Constructs a collection containing the given geometries.
     *
     * @param geometries the geometries to add
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds one or more geometries to the collection.
     *
     * @param geometries the geometries to add
     */
    public void add(Intersectable... geometries) {
        this.geometries.addAll(Arrays.asList(geometries));
    }

    /**
     * Finds all intersection points between the given {@link Ray} and all geometries
     * in the collection.
     * <p>
     * Iterates through all contained geometries and collects their intersection points
     * with the ray.
     *
     * @param ray the ray to intersect with the geometries
     * @return a list of all intersection points found, or {@code null} if none were found
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersections = null;
        for (Intersectable geometry : geometries) {
            List<Point> intersectionsByGeometry = geometry.findIntersections(ray);
            if (intersectionsByGeometry != null) {
                if (intersections == null) {
                    intersections = new LinkedList<>();
                }
                intersections.addAll(intersectionsByGeometry);
            }
        }
        return intersections;
    }
}
