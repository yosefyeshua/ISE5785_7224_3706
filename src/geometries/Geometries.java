package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Geometries implements Intersectable {
    private final List<Intersectable> geometries = new LinkedList<>();

    public Geometries() {}

    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    public void add(Intersectable... geometries) {
        this.geometries.addAll(Arrays.asList(geometries));
    }

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
