package renderer;

import org.junit.jupiter.api.Test;
import lighting.*;
import primitives.*;
import scene.Scene;
import geometries.*;
import primitives.Color;
import primitives.Material;

import java.util.ArrayList;
import java.util.List;

public class ProjectTests {

    @Test
    void FinalProject2Image() {
        Scene scene = new Scene("FinalProject2Image")
                .setAmbientLight(new AmbientLight(new Color(15, 15, 18)));  // Reduced ambient light

        // Materials
        Material metallic = new Material()
                .setKD(0.4).setKS(0.7).setNShininess(100).setKR(0.3);
        Material glass = new Material()
                .setKD(0.1).setKS(0.5).setNShininess(300).setKT(0.8);
        Material matte = new Material()
                .setKD(0.8).setKS(0.2).setNShininess(30);

        // Main focal point object - metallic sphere in the center
        scene.geometries.add(
                new Sphere(new Point(0, 0, -50), 15)
                        .setEmission(new Color(60, 60, 90))  // Reduced emission
                        .setMaterial(metallic)
        );

        // Glass spheres arranged in front and behind the main sphere
        for (int i = -2; i <= 2; i++) {
            scene.geometries.add(
                    new Sphere(new Point(-20 + i*10, -5, -20), 5)
                            .setEmission(new Color(100, 100, 110))  // Reduced emission
                            .setMaterial(glass),
                    new Sphere(new Point(20 - i*10, 5, -130), 5)
                            .setEmission(new Color(110, 100, 100))  // Reduced emission
                            .setMaterial(glass)
            );
        }

        // Background cylinders at different depths
        for (int i = -3; i <= 3; i++) {
            scene.geometries.add(
                    new Cylinder(
                            3, new Ray(new Point(i*25, -20, -160), new Vector(0, 1, 0)), 40)
                            .setEmission(new Color(
                                    130 + i*20,  // Reduced color variation
                                    120 + i*15,  // Reduced color variation
                                    100))
                            .setMaterial(matte)
            );
        }

        // Reflective floor
        scene.geometries.add(
                new Plane(new Point(0, -20, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(30, 30, 30))  // Darker floor
                        .setMaterial(new Material().setKD(0.18).setKS(0.5).setNShininess(200)
                                .setKR(0.08))
        );

        // Hexagonal rug on the floor
        scene.geometries.add(
                new Polygon(
                        new Point(-30, -19.999998, 0),
                        new Point(0, -19.999998, 15),
                        new Point(30, -19.999998, 0),
                        new Point(30, -19.999998, -20),
                        new Point(0, -19.999998, -40),
                        new Point(-30, -19.999998, -20)
                ).setEmission(new Color(129, 70, 80))  // Darker rug
                        .setMaterial(matte)
        );

        scene.geometries.add(
                new Circle(50, new Point(0, -19.999999, -20), new Vector(0, 1, 0))
                        .setEmission(new Color(34, 48, 60))  // Darker circle rug
                        .setMaterial(matte)
        );
        // Background plane
        scene.geometries.add(
                new Plane(new Point(0, 0, -200), new Vector(0, 0, 1))
                        .setEmission(new Color(50, 20, 25))  // Darker background
                        .setMaterial(new Material().setKD(0.8))
        );

        // Lighting setup - Significantly reduced light intensities

        List<Color> sceneColors = new ArrayList<>();
        sceneColors.add(Color.RED);  // Red light
        sceneColors.add(Color.ORANGE); // Orange light
        sceneColors.add(Color.YELLOW); // Yellow light
        sceneColors.add(Color.GREEN);  // Green light
        sceneColors.add(Color.BLUE);  // Blue light

        for (int i = 0; i < 5; i++) {
            if (i == 2) continue; // Skip the middle light to avoid redundancy
            scene.lights.add(
                    new SpotLight(sceneColors.get(i).scale(10), new Point(-40 + 20 * i, 50, 70), new Point(80 - 40 * i, -20, -30).subtract(new Point(-40 + 20 * i, 50, 70)))
                            .setNarrowBeam(200).setKL(0.0005).setKQ(0.000001)
            );
        }

        scene.lights.add(
                new SpotLight(Color.WHITE, new Point(0, 200, 70), new Point(0, -20, -160).subtract(new Point(0, 200, 70)))
                        .setKL(0.0005).setKQ(0.00001)
        );
        /*scene.lights.add(new SpotLight(new Color(400, 300, 300), new Point(-50, 50, -50),  // Reduced from 1000
                    new Vector(1, -1, -1)).setKL(0.0004).setKQ(0.004)); // Increased attenuation
        scene.lights.add(new PointLight(new Color(300, 400, 300), new Point(50, 50, -50))  // Reduced from 1000
                    .setKL(0.0004).setKQ(0.004)); // Increased attenuation*/
        //scene.lights.add(new DirectionalLight(new Color(150, 150, 200), new Vector(0, -1, -1)));  // Reduced from 800


        // Camera setup
        Camera camera = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 20, 70))
                .setDirection(new Vector(0, -0.2, -1.5))
                .setVpDistance(150)
                .setVpSize(200, 200)
                .setResolution(1000, 1000)
                .setAaSamples(10)
                //.setASSdepth(5)
                .setMultithreading(8)
                .setDebugPrint(0.1)
                //.setDofSamples(81)
                .setFocalDistance(new Point(0, 0, -50).distance(new Point(0, 20, 70))) // Focal distance to the main sphere
                .setApertureRadius(2.0)
                .build();

        camera.renderImage().writeToImage("FinalProject2Image_MTTEST");
    }
}
