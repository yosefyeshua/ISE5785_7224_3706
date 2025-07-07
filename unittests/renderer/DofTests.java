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
                .setResolution(2000, 2000)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setApertureRadius(5)
                .setFocalDistance(new Point(15, 0, 150).distance(new Point(0, 0, 0)))
                .setDofSamples(50)
                .setASSdepthDOF(4)
                .setAaSamples(36)
                .setMultithreading(8)
                .setDebugPrint(0.1)
                .build();

        camera.renderImage()
                .writeToImage("_testASS4_CORNERS_DOF4_36AA");
    }

    // This code replaces all cylinders with rectangular boxes, updates skin color, adds shoulders,
// and includes golden triangle decorations on mirrors for a system without Cylinder support.

    @Test
    void DOFinfiniteMirrorsWithBoxPerson() {
        Scene scene = new Scene("Infinite Mirrors")
                .setAmbientLight(new AmbientLight(new Color(30, 30, 30)));

        double mirrorWidth = 200, mirrorHeight = 300;
        double mirrorDistance = 200;
        double mirrorZ = 0;
        double mirrorY1 = -mirrorDistance / 2;
        double mirrorY2 = mirrorDistance / 2 + 50;

        Color frameColor = new Color(70, 35, 35);
        Material frameMat = new Material().setKD(0.5).setKS(0.2).setNShininess(50);

        Color mirrorColor = new Color(0, 0, 0);
        Material mirrorMat = new Material().setKD(0.1).setKS(0.4).setNShininess(300).setKR(1);

        Color gold = new Color(255, 215, 0);
        Material goldMat = new Material().setKD(0.5).setKS(0.5).setNShininess(100).setKT(0.3);

        // Mirror 1
        scene.geometries.add(
                new Polygon(new Point(-mirrorWidth / 2, mirrorY1, mirrorZ),
                        new Point(mirrorWidth / 2, mirrorY1, mirrorZ),
                        new Point(mirrorWidth / 2, mirrorY1, mirrorZ + mirrorHeight),
                        new Point(-mirrorWidth / 2, mirrorY1, mirrorZ + mirrorHeight))
                        .setEmission(frameColor).setMaterial(frameMat),
                new Polygon(new Point(-mirrorWidth / 2 + 5, mirrorY1 + 0.1, mirrorZ + 5),
                        new Point(mirrorWidth / 2 - 5, mirrorY1 + 0.1, mirrorZ + 5),
                        new Point(mirrorWidth / 2 - 5, mirrorY1 + 0.1, mirrorZ + mirrorHeight - 5),
                        new Point(-mirrorWidth / 2 + 5, mirrorY1 + 0.1, mirrorZ + mirrorHeight - 5))
                        .setEmission(mirrorColor).setMaterial(mirrorMat)
        );

        // Gold corner triangles for mirror 1
        scene.geometries.add(
                new Triangle(new Point(-mirrorWidth / 2, mirrorY1 + 0.11, mirrorZ),
                        new Point(-mirrorWidth / 2 + 10, mirrorY1 + 0.11, mirrorZ),
                        new Point(-mirrorWidth / 2, mirrorY1 + 0.11, mirrorZ + 10))
                        .setEmission(gold).setMaterial(goldMat),
                new Triangle(new Point(mirrorWidth / 2, mirrorY1 + 0.11, mirrorZ),
                        new Point(mirrorWidth / 2 - 10, mirrorY1 + 0.11, mirrorZ),
                        new Point(mirrorWidth / 2, mirrorY1 + 0.11, mirrorZ + 10))
                        .setEmission(gold).setMaterial(goldMat),
                new Triangle(new Point(-mirrorWidth / 2, mirrorY1 + 0.11, mirrorZ + mirrorHeight),
                        new Point(-mirrorWidth / 2 + 10, mirrorY1 + 0.11, mirrorZ + mirrorHeight),
                        new Point(-mirrorWidth / 2, mirrorY1 + 0.11, mirrorZ + mirrorHeight - 10))
                        .setEmission(gold).setMaterial(goldMat),
                new Triangle(new Point(mirrorWidth / 2, mirrorY1 + 0.11, mirrorZ + mirrorHeight),
                        new Point(mirrorWidth / 2 - 10, mirrorY1 + 0.11, mirrorZ + mirrorHeight),
                        new Point(mirrorWidth / 2, mirrorY1 + 0.11, mirrorZ + mirrorHeight - 10))
                        .setEmission(gold).setMaterial(goldMat)
        );

        // Mirror 2
        scene.geometries.add(
                new Polygon(new Point(-mirrorWidth / 2, mirrorY2, mirrorZ),
                        new Point(mirrorWidth / 2, mirrorY2, mirrorZ),
                        new Point(mirrorWidth / 2, mirrorY2, mirrorZ + mirrorHeight),
                        new Point(-mirrorWidth / 2, mirrorY2, mirrorZ + mirrorHeight))
                        .setEmission(frameColor).setMaterial(frameMat),
                new Polygon(new Point(-mirrorWidth / 2 + 5, mirrorY2 - 0.1, mirrorZ + 5),
                        new Point(mirrorWidth / 2 - 5, mirrorY2 - 0.1, mirrorZ + 5),
                        new Point(mirrorWidth / 2 - 5, mirrorY2 - 0.1, mirrorZ + mirrorHeight - 5),
                        new Point(-mirrorWidth / 2 + 5, mirrorY2 - 0.1, mirrorZ + mirrorHeight - 5))
                        .setEmission(mirrorColor).setMaterial(mirrorMat)
        );

        // Gold triangles for mirror 2
        scene.geometries.add(
                new Triangle(new Point(-mirrorWidth / 2, mirrorY2 - 0.11, mirrorZ),
                        new Point(-mirrorWidth / 2 + 10, mirrorY2 - 0.11, mirrorZ),
                        new Point(-mirrorWidth / 2, mirrorY2 - 0.11, mirrorZ + 10))
                        .setEmission(gold).setMaterial(goldMat),
                new Triangle(new Point(mirrorWidth / 2, mirrorY2 - 0.11, mirrorZ),
                        new Point(mirrorWidth / 2 - 10, mirrorY2 - 0.11, mirrorZ),
                        new Point(mirrorWidth / 2, mirrorY2 - 0.11, mirrorZ + 10))
                        .setEmission(gold).setMaterial(goldMat),
                new Triangle(new Point(-mirrorWidth / 2, mirrorY2 - 0.11, mirrorZ + mirrorHeight),
                        new Point(-mirrorWidth / 2 + 10, mirrorY2 - 0.11, mirrorZ + mirrorHeight),
                        new Point(-mirrorWidth / 2, mirrorY2 - 0.11, mirrorZ + mirrorHeight - 10))
                        .setEmission(gold).setMaterial(goldMat),
                new Triangle(new Point(mirrorWidth / 2, mirrorY2 - 0.11, mirrorZ + mirrorHeight),
                        new Point(mirrorWidth / 2 - 10, mirrorY2 - 0.11, mirrorZ + mirrorHeight),
                        new Point(mirrorWidth / 2, mirrorY2 - 0.11, mirrorZ + mirrorHeight - 10))
                        .setEmission(gold).setMaterial(goldMat)
        );


        // --- Person Parameters ---
        double personY = 0;
        double personZ = 10;
        double bodyHeight = 80;
        double bodyWidth = 20;
        double bodyDepth = 10;
        double legHeight = 40;
        double legWidth = 5 * 2;
        double legDepth = 8;
        double armLength = 35;
        double armWidth = 4 * 2;
        double armDepth = 6;
        double neckHeight = 8;
        double neckWidth = 4 * 2;
        double neckDepth = 6;
        double shoeRadius = 6;
        double headRadius = 13;

        Color skinColor = new Color(255, 219, 172); // בהיר
        Color blue = new Color(30, 60, 180);
        Color green = new Color(40, 180, 40);
        Color black = new Color(20, 20, 20);
        Material matte = new Material().setKD(0.7).setKS(0.1).setNShininess(20);

        double bodyZ = personZ + shoeRadius + legHeight;
        double z2 = bodyZ + bodyHeight;
        double x1 = -bodyWidth / 2, x2 = bodyWidth / 2;
        double y1 = personY - bodyDepth / 2, y2 = personY + bodyDepth / 2;



        // זרועות (4 מלבנים לכל צד)

        double armZ = z2 - 10;

        for (int i = -1; i <= 1; i += 2) {
            double xArm = i * bodyWidth / 2;
            double xA1 = (i == -1) ? xArm - armLength : xArm;
            double xA2 = (i == -1) ? xArm : xArm + armLength;
            double yA1 = personY - armDepth / 2, yA2 = personY + armDepth / 2;
            double zA1 = armZ - armWidth / 2, zA2 = armZ + armWidth / 2;

            // Front
            scene.geometries.add(new Polygon(
                    new Point(xA1, yA1, zA1), new Point(xA2, yA1, zA1),
                    new Point(xA2, yA1, zA2), new Point(xA1, yA1, zA2)
            ).setEmission(new Color(40, 180, 40)).setMaterial(matte));

            // Back
            scene.geometries.add(new Polygon(
                    new Point(xA1, yA2, zA1), new Point(xA2, yA2, zA1),
                    new Point(xA2, yA2, zA2), new Point(xA1, yA2, zA2)
            ).setEmission(new Color(40, 180, 40)).setMaterial(matte));

            // Top
            scene.geometries.add(new Polygon(
                    new Point(xA1, yA1, zA2), new Point(xA2, yA1, zA2),
                    new Point(xA2, yA2, zA2), new Point(xA1, yA2, zA2)
            ).setEmission(new Color(40, 180, 40)).setMaterial(matte));

            // Bottom
            scene.geometries.add(new Polygon(
                    new Point(xA1, yA1, zA1), new Point(xA2, yA1, zA1),
                    new Point(xA2, yA2, zA1), new Point(xA1, yA2, zA1)
            ).setEmission(new Color(40, 180, 40)).setMaterial(matte));
        }





        // --- Person Parameters ---
// Add a grass plane under the feet and mirrors
        scene.geometries.add(
                new Plane(new Point(0, 0, -50), new Vector(0, 0, 1))
                        .setEmission(new Color(34, 139, 34))  // Grass green color
                        .setMaterial(new Material().setKD(0.5).setKS(0.1).setNShininess(20))
        );

// Right arm (4 boxes)
        for (int i = 1; i <= 1; i++) {
            double xArm = i * bodyWidth / 2;
            double xA1 = xArm - armLength, xA2 = xArm;
            double yA1 = personY - armDepth / 2, yA2 = personY + armDepth / 2;
            double zA1 = armZ - armWidth / 2, zA2 = armZ + armWidth / 2;
            // Front
            scene.geometries.add(new Polygon(
                    new Point(xA1, yA1, zA1), new Point(xA2, yA1, zA1),
                    new Point(xA2, yA1, zA2), new Point(xA1, yA1, zA2)
            ).setEmission(green).setMaterial(matte));
            // Back
            scene.geometries.add(new Polygon(
                    new Point(xA1, yA2, zA1), new Point(xA2, yA2, zA1),
                    new Point(xA2, yA2, zA2), new Point(xA1, yA2, zA2)
            ).setEmission(green).setMaterial(matte));
            // Top
            scene.geometries.add(new Polygon(
                    new Point(xA1, yA1, zA2), new Point(xA2, yA1, zA2),
                    new Point(xA2, yA2, zA2), new Point(xA1, yA2, zA2)
            ).setEmission(green).setMaterial(matte));
            // Bottom
            scene.geometries.add(new Polygon(
                    new Point(xA1, yA1, zA1), new Point(xA2, yA1, zA1),
                    new Point(xA2, yA2, zA1), new Point(xA1, yA2, zA1)
            ).setEmission(green).setMaterial(matte));
        }
        // כפות ידיים
        scene.geometries.add(
                new Sphere(new Point(-bodyWidth / 2 - armLength, personY, armZ), 5).setEmission(skinColor).setMaterial(matte),
                new Sphere(new Point(bodyWidth / 2 + armLength, personY, armZ), 5).setEmission(skinColor).setMaterial(matte)
        );

        // צוואר (4 מלבנים)
        double neckZ1 = z2;
        double neckZ2 = neckZ1 + neckHeight;
        double xN1 = -neckWidth / 2, xN2 = neckWidth / 2;
        double yN1 = personY - neckDepth / 2, yN2 = personY + neckDepth / 2;
        scene.geometries.add(
                new Polygon(new Point(xN1, yN1, neckZ1), new Point(xN2, yN1, neckZ1), new Point(xN2, yN1, neckZ2), new Point(xN1, yN1, neckZ2)).setEmission(skinColor).setMaterial(matte),
                new Polygon(new Point(xN1, yN2, neckZ1), new Point(xN2, yN2, neckZ1), new Point(xN2, yN2, neckZ2), new Point(xN1, yN2, neckZ2)).setEmission(skinColor).setMaterial(matte),
                new Polygon(new Point(xN1, yN1, neckZ1), new Point(xN1, yN2, neckZ1), new Point(xN1, yN2, neckZ2), new Point(xN1, yN1, neckZ2)).setEmission(skinColor).setMaterial(matte),
                new Polygon(new Point(xN2, yN1, neckZ1), new Point(xN2, yN2, neckZ1), new Point(xN2, yN2, neckZ2), new Point(xN2, yN1, neckZ2)).setEmission(skinColor).setMaterial(matte)
        );

        // ראש
        double headZ = neckZ2 + headRadius;
        scene.geometries.add(new Sphere(new Point(0, personY, headZ), headRadius).setEmission(skinColor).setMaterial(matte));

        // עיניים + אישונים
        double eyeZ = headZ + 5;
        double eyeY = personY + 11;
        double eyeX = 7;
        double eyeRadius = 4;
        double pupilRadius = 3;
        double pupilForwardOffset = 1.5;
        Material pupilmat = new Material().setKD(0.1).setKS(0.9).setNShininess(100).setKR(0.4);
        Color white = new Color(240, 240, 240);

        scene.geometries.add(
                new Sphere(eyeRadius, new Point(-eyeX, eyeY, eyeZ)).setEmission(white).setMaterial(matte),
                new Sphere(eyeRadius, new Point(eyeX, eyeY, eyeZ)).setEmission(white).setMaterial(matte),
                new Sphere(pupilRadius, new Point(-eyeX, eyeY + pupilForwardOffset, eyeZ)).setEmission(black).setMaterial(pupilmat),
                new Sphere(pupilRadius, new Point(eyeX, eyeY + pupilForwardOffset, eyeZ)).setEmission(black).setMaterial(pupilmat)
        );

        // כיפה
        double kippahRadius = 6;
        scene.geometries.add(
                new Sphere(new Point(0, personY - headRadius + kippahRadius / 2, headZ + headRadius - kippahRadius / 2), kippahRadius).setEmission(blue).setMaterial(matte)
        );


        // Shoes
        scene.geometries.add(
                new Sphere(new Point(-bodyWidth / 3, personY, personZ), shoeRadius).setEmission(black).setMaterial(matte),
                new Sphere(new Point(bodyWidth / 3, personY, personZ), shoeRadius).setEmission(black).setMaterial(matte)
        );

        // Legs: 4 boxes each (8 total)
        for (int i = -1; i <= 1; i += 2) {
            double xLeg = i * bodyWidth / 3;
            double zBase = personZ + shoeRadius;
            double xL1 = xLeg - legWidth / 2, xL2 = xLeg + legWidth / 2;
            double yL1 = personY - legDepth / 2, yL2 = personY + legDepth / 2;
            double zL2 = zBase + legHeight;
            // Front
            scene.geometries.add(new Polygon(
                    new Point(xL1, yL1, zBase), new Point(xL2, yL1, zBase),
                    new Point(xL2, yL1, zL2), new Point(xL1, yL1, zL2)
            ).setEmission(blue).setMaterial(matte));
            // Back
            scene.geometries.add(new Polygon(
                    new Point(xL1, yL2, zBase), new Point(xL2, yL2, zBase),
                    new Point(xL2, yL2, zL2), new Point(xL1, yL2, zL2)
            ).setEmission(blue).setMaterial(matte));
            // Sides
            scene.geometries.add(new Polygon(
                    new Point(xL1, yL1, zBase), new Point(xL1, yL2, zBase),
                    new Point(xL1, yL2, zL2), new Point(xL1, yL1, zL2)
            ).setEmission(blue).setMaterial(matte));
            scene.geometries.add(new Polygon(
                    new Point(xL2, yL1, zBase), new Point(xL2, yL2, zBase),
                    new Point(xL2, yL2, zL2), new Point(xL2, yL1, zL2)
            ).setEmission(blue).setMaterial(matte));
        }

        // Body (6 sides)
        Material shirtMat = new Material().setKD(0.7).setKS(0.1).setNShininess(20);
        scene.geometries.add(
                new Polygon(new Point(x1, y1, bodyZ), new Point(x2, y1, bodyZ), new Point(x2, y2, bodyZ), new Point(x1, y2, bodyZ)).setEmission(green).setMaterial(shirtMat),
                new Polygon(new Point(x1, y1, z2), new Point(x2, y1, z2), new Point(x2, y2, z2), new Point(x1, y2, z2)).setEmission(green).setMaterial(shirtMat),
                new Polygon(new Point(x1, y1, bodyZ), new Point(x1, y2, bodyZ), new Point(x1, y2, z2), new Point(x1, y1, z2)).setEmission(green).setMaterial(shirtMat),
                new Polygon(new Point(x2, y1, bodyZ), new Point(x2, y2, bodyZ), new Point(x2, y2, z2), new Point(x2, y1, z2)).setEmission(green).setMaterial(shirtMat),
                new Polygon(new Point(x1, y2, bodyZ), new Point(x2, y2, bodyZ), new Point(x2, y2, z2), new Point(x1, y2, z2)).setEmission(green).setMaterial(shirtMat),
                new Polygon(new Point(x1, y1, bodyZ), new Point(x2, y1, bodyZ), new Point(x2, y1, z2), new Point(x1, y1, z2)).setEmission(green).setMaterial(shirtMat)
        );

        // כתפיים: כדורים
        scene.geometries.add(
                new Sphere(new Point(x1, personY, z2 - 4), 5).setEmission(green).setMaterial(matte),
                new Sphere(new Point(x2, personY, z2 - 4), 5).setEmission(green).setMaterial(matte)
        );


        // מצלמה
        Camera camera = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(-50, eyeY + 8, eyeZ))
                .setDirection(new Point(50, mirrorY2, eyeZ), new Vector(0, 0, 1))
                .setVpSize(100, 150)
                .setVpDistance(30)
                .setResolution(1000, 1000)
                .setMultithreading(8)
                .setDebugPrint(1.2)
                .build();

        camera.renderImage().writeToImage("infiniteMirrors2");
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
                .setASSdepth(4)
                .setMultithreading(8)
                .setDebugPrint(1)
                .setDofSamples(50)
                .setFocalDistance(new Point(0, 0, -100).distance(new Point(0, 20, 50))) // Focal distance to the main sphere
                .setApertureRadius(2.0)
                .build();

        camera.renderImage().writeToImage("DOFSceneComparisonASS");
    }

    @Test
    void testFinalAdvancedScene() {
        Scene scene = new Scene("Final Advanced Scene")
                .setAmbientLight(new AmbientLight(new Color(15, 15, 15)));

        Material metallic = new Material().setKD(0.4).setKS(0.6).setNShininess(100).setKR(0.3);
        Material glass = new Material().setKD(0.1).setKS(0.5).setNShininess(300).setKT(0.8);
        Material matte = new Material().setKD(0.9).setKS(0.1).setNShininess(20);
        Material mirror = new Material().setKR(1.0).setKS(0.8).setNShininess(300);

        scene.geometries.add(
                new Sphere(new Point(0, 0, -100), 15)
                        .setEmission(new Color(60, 60, 90))
                        .setMaterial(metallic)
        );

        scene.geometries.add(
                new Cylinder(5,
                        new Ray(new Point(50, -20, -80), new Vector(0, 1, 0)), 80)
                        .setEmission(new Color(100, 100, 120))
                        .setMaterial(glass)
        );

        scene.lights.add(
                new PointLight(new Color(0, 700, 700), new Point(50, -7, -80))
                        .setKL(0.0005).setKQ(0.0005)
        );

        scene.lights.add(
                new PointLight(new Color(700, 0, 700), new Point(50, 20, -80))
                        .setKL(0.0005).setKQ(0.0005)
        );

        scene.lights.add(
                new PointLight(new Color(700, 700, 0), new Point(50, 40, -80))
                        .setKL(0.0005).setKQ(0.0005)
        );



        scene.geometries.add(
                new Sphere(new Point(-50, 10, -90), 10)
                        .setEmission(new Color(50, 70, 100))
                        .setMaterial(glass)
        );
        scene.lights.add(
                new SpotLight(new Color(500, 0, 0), new Point(-50, 10, -90), new Vector(-1, 0, 0))
                        .setKL(0.0003).setKQ(0.0003)
        );

        scene.geometries.add(
                new Triangle(
                        new Point(-20, -20, -70),
                        new Point(-5, -5, -70),
                        new Point(-30, -5, -70))
                        .setEmission(new Color(90, 40, 40))
                        .setMaterial(matte)
        );

        scene.geometries.add(
                new Polygon(
                        new Point(20, -20, -150),
                        new Point(25, -18, -150),
                        new Point(27, -13, -150),
                        new Point(22, -10, -150),
                        new Point(17, -15, -150)
                ).setEmission(new Color(20, 70, 80))
                        .setMaterial(metallic)
        );

        scene.geometries.add(
                new Sphere(new Point(0, -10, -130), 7)
                        .setEmission(new Color(40, 40, 40))
                        .setMaterial(mirror)
        );

        scene.geometries.add(
                new Plane(new Point(0, -20, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(20, 20, 20))
                        .setMaterial(new Material().setKR(0.4).setKS(0.5).setNShininess(100))
        );

        scene.geometries.add(
                new Plane(new Point(0, 0, -200), new Vector(0, 0, 1))
                        .setEmission(new Color(10, 10, 15))
                        .setMaterial(new Material().setKD(0.7))
        );


        scene.lights.add(new DirectionalLight(new Color(300, 300, 400), new Vector(0, -1, -1)));

        Camera camera = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 30, 50))
                .setDirection(new Vector(0, -0.25, -1))
                .setVpDistance(150)
                .setVpSize(200, 200)
                .setResolution(1000, 1000)
                //.setAaSamples(81)
                //.setMultithreading(8)
                .setDebugPrint(1)
                .setDofSamples(10)
                .setFocalDistance(new Point(0, 0, -100).distance(new Point(0, 30, 50)))
                .setApertureRadius(5)
                .setASSdepthDOF(4)
                //.setASSdepth(2)
                .build();

        camera.renderImage().writeToImage("FinalAdvancedSceneDOFASS");
    }

}
