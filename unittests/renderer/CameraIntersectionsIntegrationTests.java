package renderer;

import geometries.Geometry;
import geometries.Sphere;
import geometries.Plane;
import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CameraIntersectionsIntegrationTests {
    Camera camera1 = new Camera.Builder()
            .setLocation(Point.ZERO)
            .setDirection(Vector.AXIS_Z.scale(-1), Vector.AXIS_Y)
            .setVpDistance(1)
            .setVpSize(3, 3)
            .setResolution(3, 3)
            .build();

    private void countIntersections(Camera camera, Geometry geometry, int expected) {
        int numOfIntersections = 0;
        for (int i = 0; i < camera.getWidth(); i++) {
            for (int j = 0; j < camera.getHeight(); j++) {
                numOfIntersections += geometry.findIntersections(camera.constructRay(3, 3, j, i)).size();
            }
        }
        assertEquals(expected, numOfIntersections, "Incorrect number of intersections");
    }

    @Test
    void testSphereIntersection() {

        Camera camera2 = new Camera.Builder()
                .setLocation(new Point(0, 0, 0.5))
                .setDirection(Vector.AXIS_Z.scale(-1), Vector.AXIS_Y)
                .setVpDistance(1)
                .setVpSize(3, 3)
                .setResolution(3, 3)
                .build();

        // TC01
        Sphere sphere = new Sphere(1, new Point(0, 0, -3));
        countIntersections(camera1, sphere, 1);

        // TC02
        sphere = new Sphere(2.5, new Point(0, 0, -2.5));
        countIntersections(camera2, sphere, 18);

        // TC03
        sphere = new Sphere(2, new Point(0, 0, -2));
        countIntersections(camera2, sphere, 10);

        // TC04
        sphere = new Sphere(4, Point.ZERO);
        countIntersections(camera1, sphere, 9);

        // TC05
        sphere = new Sphere(0.5, new Point(0, 0, 1));
        countIntersections(camera2, sphere, 0);

    }

    @Test
    void testPlaneIntersection() {
        // TC01
        Plane plane = new Plane(new Point(0, 0, -3), Vector.AXIS_Z);
        countIntersections(camera1, plane, 9);

        // TC02
        plane = new Plane(new Point(0, 0, -3), new Vector(0, 1, 2));
        countIntersections(camera1, plane, 9);

        // TC03
        plane = new Plane(new Point(0, 0, -3), new Vector(0, 1, 1));
        countIntersections(camera1, plane, 6);

    }
    @Test
    void  testTriangleIntersection() {
        // TC01
        Triangle triangle = new Triangle(new Point(0, 1, -2), new Point(1, -1, -2),new Point(-1, -1, -2));
        countIntersections(camera1, triangle, 1);

        // TC02
        triangle = new Triangle(new Point(0, 20, -2), new Point(1, -1, -2), new Point(-1, -1, -2));
        countIntersections(camera1, triangle, 2);
    }
}