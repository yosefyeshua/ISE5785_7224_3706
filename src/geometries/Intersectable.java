package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

/**
 * Common interface for geometric objects that can be intersected by rays.
 */
public abstract class Intersectable {
    /**
     * Finds the intersection point(s) between the given {@link Ray} and the geometry.
     * <p>
     * Implementations should return only the intersection points that occur
     * in the direction of the ray (i.e., with positive distance from the ray origin).
     *
     * @param ray the ray to test for intersection
     * @return a list containing the intersection point(s), or {@code null} if there are none
     */
    public final List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list == null ? null : list.stream().map(intersection -> intersection.point).toList();
    }

    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);

    public final List<Intersection> calculateIntersections(Ray ray) {return calculateIntersectionsHelper(ray);}

    public static class Intersection {
        public final Geometry geometry;
        public final Point point;

        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            return o instanceof Intersection other && this.point.equals(other.point) &&
                    this.geometry == other.geometry;
        }

        @Override
        public String toString() {
            return "Intersection{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }
    }
}
