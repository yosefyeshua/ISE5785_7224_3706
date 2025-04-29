package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

/**
 * Common interface for geometric objects that can be intersected by rays.
 */
public interface Intersectable {
    /**
     * Finds the intersection point(s) between the given {@link Ray} and the geometry.
     * <p>
     * Implementations should return only the intersection points that occur
     * in the direction of the ray (i.e., with positive distance from the ray origin).
     *
     * @param ray the ray to test for intersection
     * @return a list containing the intersection point(s), or {@code null} if there are none
     */
    List<Point> findIntersections(Ray ray);
}
