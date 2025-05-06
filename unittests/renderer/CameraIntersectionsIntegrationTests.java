package renderer;

import geometries.Geometry;
import geometries.Sphere;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;

import geometries.Geometry;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CameraIntersectionsIntegrationTests {

    @Test
    void testSphereIntersection() {
        Camera camera1 = new Camera.Builder()
                .setLocation(Point.ZERO)
                .setDirection(Vector.AXIS_Z.scale(-1), Vector.AXIS_Y)
                .setVpDistance(1)
                .setVpSize(3, 3)
                .setResolution(3, 3)
                .build();

        Camera camera2 = new Camera.Builder()
                .setLocation(new Point(0, 0, 0.5))
                .setDirection(Vector.AXIS_Z.scale(-1), Vector.AXIS_Y)
                .setVpDistance(1)
                .setVpSize(3, 3)
                .setResolution(3, 3)
                .build();

        // TC01
        Sphere sphere = new Sphere(1, new Point(0, 0, -3));

        int numOfIntersections = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                numOfIntersections += sphere.findIntersections(camera1.constructRay(3, 3, j, i)).size();
            }
        }

        assertEquals(2, numOfIntersections);

        // TC02
        sphere = new Sphere(2.5, new Point(0, 0, -2.5));

        numOfIntersections = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                numOfIntersections += sphere.findIntersections(camera2.constructRay(3, 3, j, i)).size();
            }
        }

        assertEquals(18, numOfIntersections);

        // TC03
        sphere = new Sphere(2, new Point(0, 0, -2));

        numOfIntersections = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                numOfIntersections += sphere.findIntersections(camera2.constructRay(3, 3, j, i)).size();
            }
        }

        assertEquals(10, numOfIntersections);

        // TC04
        sphere = new Sphere(4, Point.ZERO);

        numOfIntersections = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                numOfIntersections += sphere.findIntersections(camera1.constructRay(3, 3, j, i)).size();
            }
        }

        assertEquals(9, numOfIntersections);

        // TC05
        sphere = new Sphere(0.5, new Point(0, 0, 1));

        numOfIntersections = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                numOfIntersections += sphere.findIntersections(camera2.constructRay(3, 3, j, i)).size();
            }
        }

        assertEquals(0, numOfIntersections);

    }


}

    int countIntersections(Camera camera, Geometry geometry) {
        int numOfIntersections = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                numOfIntersections += geometry.findIntersections(camera.constructRay(3, 3, j, i)).size();
            }
        }
        return numOfIntersections;
    }
    Camera camera = new Camera.Builder()
            .setLocation(Point.ZERO)
            .setDirection(Vector.AXIS_Z.scale(-1), Vector.AXIS_Y)
            .setVpDistance(1)
            .setVpSize(3, 3)
            .setResolution(3, 3)
            .build();
    @Test
    void testPlaneIntersection() {
        Plane plane = new Plane(new Point(0, 0, -3), Vector.AXIS_Z);
        assertEquals(9, countIntersections(camera, plane), "Wrong number of intersections");
        plane = new Plane(new Point(0, 0, -3), new Vector(0, 1, 2));
        assertEquals(9, countIntersections(camera, plane), "Wrong number of intersections");
        plane = new Plane(new Point(0, 0, -3), new Vector(0, 1, 1));
        assertEquals(6, countIntersections(camera, plane), "Wrong number of intersections");

    }
    @Test
    void  testTriangleIntersection() {
        // Implement triangle intersection tests here
        Triangle triangle = new Triangle(new Point(0, 1, -2), new Point(1, -1, -2),new Point(-1, -1, -2));
        assertEquals(1, countIntersections(camera, triangle), "Wrong number of intersections");
        triangle = new Triangle(new Point(0, 20, -2), new Point(1, -1, -2), new Point(-1, -1, -2));
        assertEquals(2, countIntersections(camera, triangle), "Wrong number of intersections");
    }
}