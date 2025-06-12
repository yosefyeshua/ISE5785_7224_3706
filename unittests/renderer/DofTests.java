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
                .setResolution(600, 600)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setApertureRadius(5)
                .setFocalDistance(new Point(15, 0, 150).distance(new Point(0, 0, 0)))
                .setDofSamples(50)
                .setAaSamples(50)
                .setMultithreading(8)
                .setDebugPrint(0.1)
                .build();

        camera.renderImage()
                .writeToImage("dof_test_with_builder");
    }

    @Test
    void DOFinfiniteMirrorsWithPerson() {
        Scene scene = new Scene("Infinite Mirrors")
                .setAmbientLight(new AmbientLight(new Color(30, 30, 30)));

        // Mirror parameters
        double mirrorWidth = 200, mirrorHeight = 300;
        double mirrorDistance = 200;
        double mirrorZ = 0;
        double mirrorY1 = -mirrorDistance / 2;
        double mirrorY2 = mirrorDistance / 2 + 50;

        // Brown rectangle (wooden frame)
        Color frameColor = new Color(70, 35, 35);
        Material frameMat = new Material().setKD(0.5).setKS(0.2).setNShininess(50);

        // Gray mirror (fully reflective)
        Color mirrorColor = new Color(0, 0, 0);
        Material mirrorMat = new Material().setKD(0.1).setKS(0.4).setNShininess(300).setKR(1); // reflective

        // Mirror 1 (at y = mirrorY1)
        scene.geometries.add(
                // Frame
                new Polygon(
                        new Point(-mirrorWidth / 2, mirrorY1, mirrorZ),
                        new Point(mirrorWidth / 2, mirrorY1, mirrorZ),
                        new Point(mirrorWidth / 2, mirrorY1, mirrorZ + mirrorHeight),
                        new Point(-mirrorWidth / 2, mirrorY1, mirrorZ + mirrorHeight)
                ).setEmission(frameColor).setMaterial(frameMat),
                // Mirror surface
                new Polygon(
                        new Point(-mirrorWidth / 2 + 5, mirrorY1 + 0.1, mirrorZ + 5),
                        new Point(mirrorWidth / 2 - 5, mirrorY1 + 0.1, mirrorZ + 5),
                        new Point(mirrorWidth / 2 - 5, mirrorY1 + 0.1, mirrorZ + mirrorHeight - 5),
                        new Point(-mirrorWidth / 2 + 5, mirrorY1 + 0.1, mirrorZ + mirrorHeight - 5)
                ).setEmission(mirrorColor).setMaterial(mirrorMat)
        );

        // Mirror 2 (at y = mirrorY2)
        scene.geometries.add(
                // Frame
                new Polygon(
                        new Point(-mirrorWidth / 2, mirrorY2, mirrorZ),
                        new Point(mirrorWidth / 2, mirrorY2, mirrorZ),
                        new Point(mirrorWidth / 2, mirrorY2, mirrorZ + mirrorHeight),
                        new Point(-mirrorWidth / 2, mirrorY2, mirrorZ + mirrorHeight)
                ).setEmission(frameColor).setMaterial(frameMat),
                // Mirror surface
                new Polygon(
                        new Point(-mirrorWidth / 2 + 5, mirrorY2 - 0.1, mirrorZ + 5),
                        new Point(mirrorWidth / 2 - 5, mirrorY2 - 0.1, mirrorZ + 5),
                        new Point(mirrorWidth / 2 - 5, mirrorY2 - 0.1, mirrorZ + mirrorHeight - 5),
                        new Point(-mirrorWidth / 2 + 5, mirrorY2 - 0.1, mirrorZ + mirrorHeight - 5)
                ).setEmission(mirrorColor).setMaterial(mirrorMat)
        );

        // Person parameters (centered at y=0, z=ground)
        double personY = 0;
        double personZ = 10;
        double bodyHeight = 80;
        double bodyWidth = 20;
        double bodyDepth = 10;
        double legHeight = 40;
        double legRadius = 5;
        double armLength = 35;
        double armRadius = 4;
        double headRadius = 13;
        double neckRadius = 4;
        double neckHeight = 8;
        double shoeRadius = 6;


        // Colors
        Color skinColor = new Color(80, 30, 30);
        Color blue = new Color(30, 60, 180);
        Color green = new Color(40, 180, 40);
        Color black = new Color(20, 20, 20);
        Color white = new Color(240, 240, 240);

        // Materials
        Material matte = new Material().setKD(0.7).setKS(0.1).setNShininess(20);

        // Shoes (spheres)
        scene.geometries.add(
                new Sphere(new Point(-bodyWidth / 3, personY, personZ), shoeRadius).setEmission(black).setMaterial(matte),
                new Sphere(new Point(bodyWidth / 3, personY, personZ), shoeRadius).setEmission(black).setMaterial(matte)
        );

        // Legs (blue cylinders)
        scene.geometries.add(
                new Cylinder(legRadius, new Ray(new Point(-bodyWidth / 3, personY, personZ + shoeRadius), new Vector(0, 0, 1)), legHeight).setEmission(blue).setMaterial(matte),
                new Cylinder(legRadius, new Ray(new Point(bodyWidth / 3, personY, personZ + shoeRadius), new Vector(0, 0, 1)), legHeight).setEmission(blue).setMaterial(matte)
        );

        // Body (green box)
        // Body (green box, full 3D rectangular prism)
        double bodyZ = personZ + shoeRadius + legHeight;
        Material shirtMat = new Material().setKD(0.7).setKS(0.1).setNShininess(20).setKT(0); // Opaque green

        double x1 = -bodyWidth / 2, x2 = bodyWidth / 2;
        double y1 = personY - bodyDepth / 2, y2 = personY + bodyDepth / 2;
        double z2 = bodyZ + bodyHeight;

        // Shirt color and material (same as arms)

        // Shirt as a closed box (all 6 faces)
        scene.geometries.add(
                // Front (chest)
                new Polygon(
                        new Point(x1, y1, bodyZ), new Point(x2, y1, bodyZ), new Point(x2, y2, bodyZ), new Point(x1, y2, bodyZ)
                ).setEmission(green).setMaterial(shirtMat),
                // Back
                new Polygon(
                        new Point(x1, y1, z2), new Point(x2, y1, z2), new Point(x2, y2, z2), new Point(x1, y2, z2)
                ).setEmission(green).setMaterial(shirtMat),
                // Left
                new Polygon(
                        new Point(x1, y1, bodyZ), new Point(x1, y2, bodyZ), new Point(x1, y2, z2), new Point(x1, y1, z2)
                ).setEmission(green).setMaterial(shirtMat),
                // Right
                new Polygon(
                        new Point(x2, y1, bodyZ), new Point(x2, y2, bodyZ), new Point(x2, y2, z2), new Point(x2, y1, z2)
                ).setEmission(green).setMaterial(shirtMat),
                // Top (shoulders)
                new Polygon(
                        new Point(x1, y2, bodyZ), new Point(x2, y2, bodyZ), new Point(x2, y2, z2), new Point(x1, y2, z2)
                ).setEmission(green).setMaterial(shirtMat),
                // Bottom
                new Polygon(
                        new Point(x1, y1, bodyZ), new Point(x2, y1, bodyZ), new Point(x2, y1, z2), new Point(x1, y1, z2)
                ).setEmission(green).setMaterial(shirtMat)
        );

        // Arms (cylinders, left and right)
        double armZ = bodyZ + bodyHeight * 0.7;
        scene.geometries.add(
                new Cylinder(armRadius, new Ray(new Point(-bodyWidth / 2, personY, armZ), new Vector(-1, 0, 0)), armLength).setEmission(green).setMaterial(matte),
                new Cylinder(armRadius, new Ray(new Point(bodyWidth / 2, personY, armZ), new Vector(1, 0, 0)), armLength).setEmission(green).setMaterial(matte)
        );

        // Hands (skin spheres)
        scene.geometries.add(
                new Sphere(new Point(-bodyWidth / 2 - armLength, personY, armZ), armRadius * 1.2).setEmission(skinColor).setMaterial(matte),
                new Sphere(new Point(bodyWidth / 2 + armLength, personY, armZ), armRadius * 1.2).setEmission(skinColor).setMaterial(matte)
        );

        // Neck (small skin cylinder)
        double neckZ = bodyZ + bodyHeight;
        scene.geometries.add(
                new Cylinder(neckRadius, new Ray(new Point(0, personY, neckZ), new Vector(0, 0, 1)), neckHeight).setEmission(skinColor).setMaterial(matte)
        );

        // Head (skin sphere)
        double headZ = neckZ + neckHeight + headRadius;
        scene.geometries.add(
                new Sphere(new Point(0, personY, headZ), 13).setEmission(skinColor).setMaterial(matte)
        );
        double eyeZ = headZ + 5;
        double eyeY = personY + 11;
        double eyeX = 7;
        double eyeRadius = 4;
        double pupilRadius = 3;
        double pupilForwardOffset = 1.5;
        Material pupilmat = new Material().setKD(0.1).setKS(0.9).setNShininess(100).setKR(0.4); // Opaque black

        scene.geometries.add(
                // Eyeballs (white spheres)
                new Sphere(eyeRadius, new Point(-eyeX, eyeY, eyeZ)).setEmission(white).setMaterial(matte),
                new Sphere(eyeRadius, new Point(eyeX, eyeY, eyeZ)).setEmission(white).setMaterial(matte),

                // Pupils (black spheres)
                new Sphere(pupilRadius, new Point(-eyeX, eyeY + pupilForwardOffset, eyeZ)).setEmission(black).setMaterial(pupilmat),
                new Sphere(pupilRadius, new Point(eyeX, eyeY + pupilForwardOffset, eyeZ)).setEmission(black).setMaterial(pupilmat)
        );


        // Kippah (small blue sphere on top back of head)
        double kippahRadius = 6;
        scene.geometries.add(
                new Sphere(
                        new Point(0, personY - headRadius + kippahRadius / 2, headZ + headRadius - kippahRadius / 2), 6).setEmission(blue).setMaterial(matte)
        );



        // Lighting
        scene.lights.add(
                new PointLight(new Color(255, 255 ,255), new Point(30, 100, 300)).setKL(0.0005).setKQ(0.0001)
        );

        // floor
        scene.geometries.add(
                new Plane(new Point(0,0,-50), new Vector(0, 0, 1)
                ).setEmission(new Color(200, 200, 200)).setMaterial(new Material().setKD(0.5).setKS(0.1).setNShininess(20).setKR(0.5)
                ));

        // Camera: just behind the eyes, looking at the mirror at y=mirrorY2, with a small vpDistance
        Point cameraLoc = new Point(-50, eyeY + 8, eyeZ);
        Point lookAt = new Point(50, mirrorY2, eyeZ); // looking at the far mirror
        Vector up = new Vector(0, 0, 1);

        Camera camera = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(cameraLoc)
                .setDirection(lookAt, up)
                .setVpSize(100, 150)
                .setVpDistance(30)
                .setResolution(2000, 2000)
                .setFocalDistance(cameraLoc.distance(lookAt))
                .setApertureRadius(0.4)
                .setDofSamples(5)
                .setMultithreading(8)
                .setDebugPrint(5)
                .build();

        camera.renderImage()
                .writeToImage("DOFinfiniteMirrorsWithPerson");
    }

    @Test
    void testDOFScene() {
        Scene scene = new Scene("DOF Test Scene")
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
                new Sphere(new Point(0, 0, -100), 15)
                        .setEmission(new Color(60, 60, 90))  // Reduced emission
                        .setMaterial(metallic)
        );

        // Glass spheres arranged in front and behind the main sphere
        for (int i = -2; i <= 2; i++) {
            scene.geometries.add(
                    new Sphere(new Point(-20 + i*10, -5, -70), 5)
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
                        .setMaterial(new Material().setKR(0.3).setKS(0.8).setNShininess(100))
        );

        // Background plane
        scene.geometries.add(
                new Plane(new Point(0, 0, -200), new Vector(0, 0, 1))
                        .setEmission(new Color(20, 20, 25))  // Darker background
                        .setMaterial(new Material().setKD(0.8))
        );

        // Lighting setup - Significantly reduced light intensities

        scene.lights.add(new SpotLight(new Color(400, 300, 300), new Point(-50, 50, -50),  // Reduced from 1000
                    new Vector(1, -1, -1)).setKL(0.0004).setKQ(0.0004)); // Increased attenuation
        scene.lights.add(new PointLight(new Color(300, 400, 300), new Point(50, 50, -50))  // Reduced from 1000
                    .setKL(0.0004).setKQ(0.0004)); // Increased attenuation
        scene.lights.add(new DirectionalLight(new Color(150, 150, 200), new Vector(0, -1, -1)));  // Reduced from 800


        // Camera setup
        Camera camera = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 20, 50))
                .setDirection(new Vector(0, -0.2, -1))
                .setVpDistance(150)
                .setVpSize(200, 200)
                .setResolution(1000, 1000)
                .setAaSamples(81)
                .setMultithreading(10)
                .setDebugPrint(1)
                // For DOF version, add:
                .setDofSamples(50)
                .setFocalDistance(new Point(0, 0, -100).distance(new Point(0, 20, 50))) // Focal distance to the main sphere
                .setApertureRadius(2.0)
                .build();

        camera.renderImage().writeToImage("DOFSceneComparison");
    }
}
