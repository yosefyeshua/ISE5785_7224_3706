package renderer;

import org.junit.jupiter.api.Test;
import lighting.*;
import primitives.*;
import scene.Scene;
import geometries.*;
import primitives.Color;
import primitives.Material;
/**
 * Test class for Depth of Field rendering using Camera Builder.
 */
public class DofTests {

    /**
     * Test a simple depth of field effect with spheres at varying depths.
     */
    @Test
    public void testDepthOfFieldEffect() {
        Scene scene = new Scene("DOF Test Scene")
                .setBackground(new Color(0, 0, 0))
                .setAmbientLight(new AmbientLight(Color.BLACK));
        Material material = new Material()
                .setKD(0.5)
                .setKS(0.5)
                .setNShininess(100);
        scene.geometries.add(
                new Sphere(new Point(40, 30, 100), 10).setEmission(Color.RED).setMaterial(material),
                new Sphere(new Point(15, 0, 150), 10).setEmission(Color.GREEN).setMaterial(material),
                new Sphere(new Point(0, 34, 200), 10).setEmission(Color.BLUE).setMaterial(material)
        );


        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, 0, 0))
                .setDirection(new Vector(0, 0, 1), new Vector(0, -1, 0))
                .setVpSize(150, 150)
                .setVpDistance(100)
                .setResolution(500, 500)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setApertureRadius(5)
                .setFocalDistance(150)
                .setDofSamples(10)
                .build();

        camera.renderImage()
                .writeToImage("dof_test_with_builder");
    }
}
