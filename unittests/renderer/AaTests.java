package renderer;

import org.junit.jupiter.api.Test;
import lighting.*;
import primitives.*;
import scene.Scene;
import geometries.*;
import primitives.Color;
import primitives.Material;

/**
 * Tests for AA functionality
 * @author Yosef Yeshua
 */
public class AaTests {

    @Test
    void AAinfiniteMirrorsWithPerson() {
        Scene scene = new Scene("Infinite Mirrors")
                .setAmbientLight(new AmbientLight(new Color(30, 30, 30)));

        // Mirror parameters
        double mirrorWidth = 200, mirrorHeight = 300, mirrorThickness = 5;
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

        // huge box (6 huge poligons) to simulate infinite mirrors (the box  is circle around the mirrors)
        double boxSize = 2000;
        Color boxColor = new Color(1, 1, 1); // gray color for the box
        Material boxMat = new Material().setKD(0.1).setKS(0.4).setNShininess(100).setKR(1); // reflective
        scene.geometries.add(
                // Bottom
                new Polygon(
                    new Point(-boxSize, -boxSize, -boxSize),
                    new Point(boxSize, -boxSize, -boxSize),
                    new Point(boxSize, boxSize, -boxSize),
                    new Point(-boxSize, boxSize, -boxSize)
                ).setEmission(Color.MAGENTA).setMaterial(boxMat),
                // Top
                new Polygon(
                    new Point(-boxSize, -boxSize, boxSize),
                    new Point(boxSize, -boxSize, boxSize),
                    new Point(boxSize, boxSize, boxSize),
                    new Point(-boxSize, boxSize, boxSize)
                ).setEmission(Color.BLUE).setMaterial(boxMat),
                // Left
                new Polygon(
                    new Point(-boxSize, -boxSize, -boxSize),
                    new Point(-boxSize, -boxSize, boxSize),
                    new Point(-boxSize, boxSize, boxSize),
                    new Point(-boxSize, boxSize, -boxSize)
                ).setEmission(Color.GREEN).setMaterial(boxMat),
                // Right
                new Polygon(
                    new Point(boxSize, -boxSize, -boxSize),
                    new Point(boxSize, -boxSize, boxSize),
                    new Point(boxSize, boxSize, boxSize),
                    new Point(boxSize, boxSize, -boxSize)
                ).setEmission(Color.RED).setMaterial(boxMat),
                // Front
                new Polygon(
                    new Point(-boxSize, -boxSize, -boxSize),
                    new Point(boxSize, -boxSize, -boxSize),
                    new Point(boxSize, boxSize, -boxSize),
                    new Point(-boxSize, boxSize, -boxSize)
                ).setEmission(Color.CYAN).setMaterial(boxMat),
                // Back
                new Polygon(
                    new Point(-boxSize, -boxSize, boxSize),
                    new Point(boxSize, -boxSize, boxSize),
                    new Point(boxSize, boxSize, boxSize),
                    new Point(-boxSize, boxSize, boxSize)
                ).setEmission(boxColor).setMaterial(boxMat)
        );

//        double sphereRadius = 4000;
//        scene.geometries.add(
//                new Sphere(new Point(0, 0, 0), sphereRadius)
//                        .setEmission(new Color(20,20,20)).setMaterial(new Material().setKD(0.1).setKS(0.4).setNShininess(100).setKR(0.5))
//        );

        // mirrors point light (above the mirrors) - color 1- light pink and color 2 - light blue
        scene.lights.add(
                new PointLight(new Color(255, 200, 200), new Point(0, mirrorY1 + mirrorHeight + 50, mirrorZ + mirrorHeight / 2)).setKL(0.0005).setKQ(0.0001));
        scene.lights.add(
                new PointLight(new Color(200, 200, 255), new Point(0, mirrorY2 - mirrorHeight - 50, mirrorZ + mirrorHeight / 2)).setKL(0.0005).setKQ(0.0001)
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
        Color badgeColor = new Color(200, 200, 0);

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
        double z1 = bodyZ, z2 = bodyZ + bodyHeight;

        // Shirt color and material (same as arms)
        Color shirtColor = green;

        // Shirt as a closed box (all 6 faces)
        scene.geometries.add(
                // Front (chest)
                new Polygon(
                        new Point(x1, y1, z1), new Point(x2, y1, z1), new Point(x2, y2, z1), new Point(x1, y2, z1)
                ).setEmission(shirtColor).setMaterial(shirtMat),
                // Back
                new Polygon(
                        new Point(x1, y1, z2), new Point(x2, y1, z2), new Point(x2, y2, z2), new Point(x1, y2, z2)
                ).setEmission(shirtColor).setMaterial(shirtMat),
                // Left
                new Polygon(
                        new Point(x1, y1, z1), new Point(x1, y2, z1), new Point(x1, y2, z2), new Point(x1, y1, z2)
                ).setEmission(shirtColor).setMaterial(shirtMat),
                // Right
                new Polygon(
                        new Point(x2, y1, z1), new Point(x2, y2, z1), new Point(x2, y2, z2), new Point(x2, y1, z2)
                ).setEmission(shirtColor).setMaterial(shirtMat),
                // Top (shoulders)
                new Polygon(
                        new Point(x1, y2, z1), new Point(x2, y2, z1), new Point(x2, y2, z2), new Point(x1, y2, z2)
                ).setEmission(shirtColor).setMaterial(shirtMat),
                // Bottom
                new Polygon(
                        new Point(x1, y1, z1), new Point(x2, y1, z1), new Point(x2, y1, z2), new Point(x1, y1, z2)
                ).setEmission(shirtColor).setMaterial(shirtMat)
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

//        // floor color badge
//        double floorZ = -50;
//        scene.geometries.add(
//                new Plane(new Point(0,0,-50), new Vector(0, 0, 1)
//                ).setEmission(badgeColor).setMaterial(new Material().setKD(0.5).setKS(0.1).setNShininess(20).setKR(0.3)
//                ));

        // Camera: just behind the eyes, looking at the mirror at y=mirrorY2, with a small vpDistance
        Point cameraLoc = new Point(-30, eyeY +15, eyeZ);
        Point lookAt = new Point(50, mirrorY2, eyeZ); // looking at the far mirror
        Vector up = new Vector(0, 0, 1);

        Camera camera = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(cameraLoc)
                .setDirection(lookAt, up)
                .setVpSize(100, 150)
                .setVpDistance(30)
                .setResolution(1550, 1550)
                .setMultithreading(3)
                .setDebugPrint(2)
                .build();

        camera.renderImage()
                .writeToImage("AAinfiniteMirrorsWithPerson");
    }
}
