package geometries;

import lighting.LightSource;
import primitives.Material;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Common interface for geometric objects that can be intersected by rays.
 * <p>
 * This abstract class defines the base functionality for all intersectable geometries,
 * including the ability to compute intersection points and associated data with a {@link Ray}.
 * It also contains a nested {@link Intersection} class that encapsulates detailed information
 * about a specific intersection, such as the intersected geometry, the point, and shading data.
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

    public final List<Intersection> calculateIntersections(Ray ray) {
        return calculateIntersections(ray, Double.POSITIVE_INFINITY);
    }
    public final List<Intersection> calculateIntersections(Ray ray, double maxDistance) {
        return calculateIntersectionsHelper(ray, maxDistance);
    }
    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance);

    /**
     * Encapsulates detailed information about a ray-geometry intersection.
     * This includes the intersected geometry, the intersection point, material,
     * surface normal, vectors for lighting calculations, and light source references.
     */
    public static class Intersection {
        /** The geometry that was intersected. */
        public final Geometry geometry;

        /** The point of intersection. */
        public final Point point;

        /** The material at the intersection point. */
        public final Material material;

        /** The surface normal at the intersection point. */
        public Vector normal;

        /** The view vector from the camera toward the point. */
        public Vector v;

        /** The dot product of the view vector and the surface normal. */
        public double vNormal;

        /** The light source relevant to this intersection. */
        public LightSource light;

        /** The light direction vector from the light source toward the point. */
        public Vector l;

        /** The dot product of the light direction vector and the surface normal. */
        public double lNormal;

        /**
         * Constructs an intersection data structure with a point and its corresponding geometry.
         *
         * @param geometry the intersected geometry
         * @param point    the intersection point
         */
        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
            this.material = geometry != null ? geometry.getMaterial() : null;
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
