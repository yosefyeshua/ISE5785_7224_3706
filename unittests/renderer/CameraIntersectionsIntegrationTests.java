package renderer;

import geometries.Geometry;
import geometries.Sphere;
import geometries.Plane;
import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests for camera and geometry intersection logic.
 * Tests the number of intersection points between camera rays and various geometries.
 */
public class CameraIntersectionsIntegrationTests {
    /**
     * Default camera for intersection tests.
     */
    Camera camera1 = new Camera.Builder()
            .setLocation(Point.ZERO)
            .setDirection(Vector.AXIS_Z.scale(-1), Vector.AXIS_Y)
            .setVpDistance(1)
            .setVpSize(3, 3)
            .setResolution(3, 3)
            .build();

    /**
     * Counts the total number of intersection points between all camera rays and a geometry.
     *
     * @param camera   the camera to use
     * @param geometry the geometry to test
     * @param expected the expected number of intersection points
     */
    private void countIntersections(Camera camera, Geometry geometry, int expected) {
        int numOfIntersections = 0;
        for (int i = 0; i < camera.getWidth(); i++) {
            for (int j = 0; j < camera.getHeight(); j++) {
                var intersections = geometry.findIntersections(camera.constructRay(3, 3, j, i));
                numOfIntersections += intersections == null ? 0 : intersections.size();
            }
        }
        assertEquals(expected, numOfIntersections, "Incorrect number of intersections");
    }

    /**
     * Test intersections between camera rays and spheres.
     */
    @Test
    void testSphereIntersection() {
        Camera camera2 = new Camera.Builder()
                .setLocation(new Point(0, 0, 0.5))
                .setDirection(Vector.AXIS_Z.scale(-1), Vector.AXIS_Y)
                .setVpDistance(1)
                .setVpSize(3, 3)
                .setResolution(3, 3)
                .build();

        // TC01: Small sphere in front of camera
        Sphere sphere = new Sphere(1, new Point(0, 0, -3));
        countIntersections(camera1, sphere, 2);

        // TC02: Large sphere enclosing view plane
        sphere = new Sphere(2.5, new Point(0, 0, -2.5));
        countIntersections(camera2, sphere, 18);

        // TC03: Medium sphere
        sphere = new Sphere(2, new Point(0, 0, -2));
        countIntersections(camera2, sphere, 10);

        // TC04: Very large sphere enclosing camera
        sphere = new Sphere(4, Point.ZERO);
        countIntersections(camera1, sphere, 9);

        // TC05: Sphere behind camera
        sphere = new Sphere(0.5, new Point(0, 0, 1));
        countIntersections(camera2, sphere, 0);
    }

    /**
     * Test intersections between camera rays and planes.
     */
    @Test
    void testPlaneIntersection() {
        // TC01: Plane parallel to view plane
        Plane plane = new Plane(new Point(0, 0, -3), Vector.AXIS_Z);
        countIntersections(camera1, plane, 9);

        // TC02: Plane with small angle to view plane
        plane = new Plane(new Point(0, 0, -3), new Vector(0, 1, 2));
        countIntersections(camera1, plane, 9);

        // TC03: Plane with steep angle
        plane = new Plane(new Point(0, 0, -3), new Vector(0, 1, 1));
        countIntersections(camera1, plane, 6);
    }

    /**
     * Test intersections between camera rays and triangles.
     */
    @Test
    void testTriangleIntersection() {
        // TC01: Small triangle in center
        Triangle triangle = new Triangle(new Point(0, 1, -2), new Point(1, -1, -2), new Point(-1, -1, -2));
        countIntersections(camera1, triangle, 1);

        // TC02: Large triangle covering more of the view plane
        triangle = new Triangle(new Point(0, 20, -2), new Point(1, -1, -2), new Point(-1, -1, -2));
        countIntersections(camera1, triangle, 2);
    }
}