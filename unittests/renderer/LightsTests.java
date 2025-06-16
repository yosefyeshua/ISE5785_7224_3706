package renderer;

import static java.awt.Color.BLUE;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

/**
 * Test rendering a basic image
 * @author Dan Zilberstein
 */
class LightsTests {
   /**
    * Default constructor to satisfy JavaDoc generator
    */
   LightsTests() { /* to satisfy JavaDoc generator */ }

   /**
    * First scene for some of the tests
    */
   private final Scene scene1 = new Scene("Test scene");
   /**
    * Second scene for some of the tests
    */
   private final Scene scene2 = new Scene("Test scene")
           .setAmbientLight(new AmbientLight(new Color(38, 38, 38)));

   /**
    * First camera builder for some of the tests
    */
   private final Camera.Builder camera1 = Camera.getBuilder()                                          //
           .setRayTracer(scene1, RayTracerType.SIMPLE)                                                                      //
           .setLocation(new Point(0, 0, 1000))                                                                              //
           .setDirection(Point.ZERO, Vector.AXIS_Y)                                                                         //
           .setVpSize(150, 150).setVpDistance(1000);

   /**
    * Second camera builder for some of the tests
    */
   private final Camera.Builder camera2 = Camera.getBuilder()                                          //
           .setRayTracer(scene2, RayTracerType.SIMPLE)                                                                      //
           .setLocation(new Point(0, 0, 1000))                                                                              //
           .setDirection(Point.ZERO, Vector.AXIS_Y)                                                                         //
           .setVpSize(200, 200).setVpDistance(1000);

   /**
    * Shininess value for most of the geometries in the tests
    */
   private static final int SHININESS = 301;
   /**
    * Diffusion attenuation factor for some of the geometries in the tests
    */
   private static final double KD = 0.5;
   /**
    * Diffusion attenuation factor for some of the geometries in the tests
    */
   private static final Double3 KD3 = new Double3(0.2, 0.6, 0.4);

   /**
    * Specular attenuation factor for some of the geometries in the tests
    */
   private static final double KS = 0.5;
   /**
    * Specular attenuation factor for some of the geometries in the tests
    */
   private static final Double3 KS3 = new Double3(0.2, 0.4, 0.3);

   /**
    * Material for some of the geometries in the tests
    */
   private final Material material = new Material().setKD(KD3).setKS(KS3).setNShininess(SHININESS);
   /**
    * Light color for tests with triangles
    */
   private final Color trianglesLightColor = new Color(800, 500, 250);
   /**
    * Light color for tests with sphere
    */
   private final Color sphereLightColor = new Color(800, 500, 0);
   /**
    * Color of the sphere
    */
   private final Color sphereColor = new Color(BLUE).reduce(2);

   /**
    * Center of the sphere
    */
   private final Point sphereCenter = new Point(0, 0, -50);
   /**
    * Radius of the sphere
    */
   private static final double SPHERE_RADIUS = 50d;

   /**
    * The triangles' vertices for the tests with triangles
    */
   private final Point[] vertices =
           {
                   // the shared left-bottom:
                   new Point(-110, -110, -150),
                   // the shared right-top:
                   new Point(95, 100, -150),
                   // the right-bottom
                   new Point(110, -110, -150),
                   // the left-top
                   new Point(-75, 78, 100)
           };
   /**
    * Position of the light in tests with sphere
    */
   private final Point sphereLightPosition = new Point(-50, -50, 25);
   /**
    * Light direction (directional and spot) in tests with sphere
    */
   private final Vector sphereLightDirection = new Vector(1, 1, -0.5);
   /**
    * Position of the light in tests with triangles
    */
   private final Point trianglesLightPosition = new Point(30, 10, -100);
   /**
    * Light direction (directional and spot) in tests with triangles
    */
   private final Vector trianglesLightDirection = new Vector(-2, -2, -2);

   /**
    * The sphere in appropriate tests
    */
   private final Geometry sphere = new Sphere(sphereCenter, SPHERE_RADIUS)
           .setEmission(sphereColor).setMaterial(new Material().setKD(KD).setKS(KS).setNShininess(SHININESS));
   /**
    * The first triangle in appropriate tests
    */
   private final Geometry triangle1 = new Triangle(vertices[0], vertices[1], vertices[2])
           .setMaterial(material);
   /**
    * The first triangle in appropriate tests
    */
   private final Geometry triangle2 = new Triangle(vertices[0], vertices[1], vertices[3])
           .setMaterial(material);

   /**
    * Produce a picture of a sphere lighted by a directional light
    */
   @Test
   void sphereDirectional() {
      scene1.geometries.add(sphere);
      scene1.lights.add(new DirectionalLight(sphereLightColor, sphereLightDirection));

      camera1 //
              .setResolution(500, 500) //
              .build() //
              .renderImage() //
              .writeToImage("lightSphereDirectional");
   }

   /**
    * Produce a picture of a sphere lighted by a point light
    */
   @Test
   void spherePoint() {
      scene1.geometries.add(sphere);
      scene1.lights.add(new PointLight(sphereLightColor, sphereLightPosition) //
              .setKL(0.001).setKQ(0.0002));

      camera1 //
              .setResolution(500, 500) //
              .build() //
              .renderImage() //
              .writeToImage("lightSpherePoint");
   }

   /**
    * Produce a picture of a sphere lighted by a spotlight
    */
   @Test
   void sphereSpot() {
      scene1.geometries.add(sphere);
      scene1.lights.add(new SpotLight(sphereLightColor, sphereLightPosition, sphereLightDirection) //
              .setKL(0.001).setKQ(0.0001));

      camera1 //
              .setResolution(500, 500) //
              .build() //
              .renderImage() //
              .writeToImage("lightSphereSpot");
   }

   /**
    * Produce a picture of two triangles lighted by a directional light
    */
   @Test
   void trianglesDirectional() {
      scene2.geometries.add(triangle1, triangle2);
      scene2.lights.add(new DirectionalLight(trianglesLightColor, trianglesLightDirection));

      camera2.setResolution(500, 500) //
              .build() //
              .renderImage() //
              .writeToImage("lightTrianglesDirectional");
   }

   /**
    * Produce a picture of two triangles lighted by a point light
    */
   @Test
   void trianglesPoint() {
      scene2.geometries.add(triangle1, triangle2);
      scene2.lights.add(new PointLight(trianglesLightColor, trianglesLightPosition) //
              .setKL(0.001).setKQ(0.0002));

      camera2.setResolution(500, 500) //
              .build() //
              .renderImage() //
              .writeToImage("lightTrianglesPoint");
   }

   /**
    * Produce a picture of two triangles lighted by a spotlight
    */
   @Test
   void trianglesSpot() {
      scene2.geometries.add(triangle1, triangle2);
      scene2.lights.add(new SpotLight(trianglesLightColor, trianglesLightPosition, trianglesLightDirection) //
              .setKL(0.001).setKQ(0.0001));

      camera2.setResolution(500, 500) //
              .build() //
              .renderImage() //
              .writeToImage("lightTrianglesSpot");
   }

   @Test
   void trianglesCustomTest() {
      // Add the triangles
      scene2.geometries.add(triangle1, triangle2);

      // Red directional light from upper-left-back
      scene2.lights.add(new DirectionalLight(
              new Color(700, 0, 0),
              new Vector(-1, -1, -1)));

      // Green point light from bottom-right
      scene2.lights.add(new PointLight(
              new Color(0, 700, 0),
              new Point(60, -60, -60))
              .setKL(0.002).setKQ(0.0002));

      Point centroid = new Point(-30, 22.67, -66.67);
      Point lightPos = new Point(100, -100, -66.67);
      // Blue spotlight from front-top-right aiming at triangle center
      scene2.lights.add(new SpotLight(
              new Color(0, 0, 700),
              centroid.add(new Vector(0, 0, 30)),
              centroid.subtract(lightPos).normalize())
              .setKL(0.001).setKQ(0.0001));

      // Build and render
      camera2.setResolution(500, 500)
              .build()
              .renderImage()
              .writeToImage("TrianglesCustomTest");
   }
   /**
   * Produce a picture of a sphere lighted by a multi light
   */
   @Test
   void sphereMulti() {
       scene1.geometries.add(sphere);

       // Point Light
       scene1.lights.add(new PointLight(Color.YELLOW.scale(2), new Point(10,10 ,20)) //
               .setKL(0.000000001).setKQ(0.000000002));

       // Spot Light
      Point spotLightPosition = new Point(0,-100 , 0);
      scene1.lights.add(new SpotLight(Color.RED.scale(3), spotLightPosition,sphereCenter.subtract(spotLightPosition).add(new Vector(10,-10,30))) //
              .setKL(0.00005).setKQ(0.000007));

       // Directional Light
       scene1.lights.add(new DirectionalLight(Color.GREEN.scale(5), new Vector(-1, 0, 1)));

       camera1 //
               .setResolution(500, 500) //
               .build() //
               .renderImage() //
               .writeToImage("lightSphereMulti");
   }

   /**
    * Produce a picture of a tube lighted by a multi light
    */
   @Test
    void tubeMulti() {
         Point tubeCenter = new Point(0, 0, -50);
         double tubeRadius = 50d;
         Geometry tube = new Tube(tubeRadius,new Ray(tubeCenter, new Vector(-1,1,0.5)))
                .setEmission(sphereColor).setMaterial(new Material().setKD(KD).setKS(KS).setNShininess(SHININESS));
         scene1.geometries.add(tube);

         // Point Light
         scene1.lights.add(new PointLight(Color.YELLOW.scale(2), new Point(10,10 ,20)) //
                .setKL(0.0001).setKQ(0.002));

            // Spot Light
            Point spotLightPosition = new Point(0,-100 , 0);
            scene1.lights.add(new SpotLight(Color.RED.scale(3), spotLightPosition,tubeCenter.subtract(spotLightPosition).add(new Vector(10,-10,30))) //
                    .setKL(0.00005).setKQ(0.000007));
            // Directional Light
            scene1.lights.add(new DirectionalLight(Color.GREEN.scale(5), new Vector(-1, 0, 1)));



         camera1 //
                .setResolution(500, 500) //
                .build() //
                .renderImage() //
                .writeToImage("lightTubeMulti");
    }

    @Test
    void cylinderMulti() {
       Point cylinderCenter = new Point(0, 0, -50);
       double cylinderRadius = 50d;
       Geometry cylinder = new Cylinder(cylinderRadius,new Ray(cylinderCenter, new Vector(-1,1,0.5)), 50)
               .setEmission(sphereColor).setMaterial(new Material().setKD(KD).setKS(KS).setNShininess(SHININESS));
       scene1.geometries.add(cylinder);

       scene1.lights.add(new DirectionalLight(Color.GREEN.scale(5), new Vector(0, 0, -1)));




       camera1 //
               .setResolution(500, 500) //
               .build() //
               .renderImage() //
               .writeToImage("lightCylinderMulti");
    }

   @Test
   void Sun() {
      Geometry sun = new Sphere(30, Point.ZERO).setEmission(Color.RED.scale(5)).setMaterial(new Material().setKD(0.2).setKS(0.2).setNShininess(30).setKT(0.9));
      scene1.geometries.add(sun);

         PointLight plight = new PointLight(Color.YELLOW.scale(5), Point.ZERO);
         scene1.lights.add(plight);

      Point tubeCenter = new Point(-50, -50, -50);
      double tubeRadius = 20d;
      Geometry tube = new Cylinder(tubeRadius,new Ray(tubeCenter, new Vector(1,1,0)), 50)
              .setEmission(Color.BLUE).setMaterial(new Material().setKD(KD).setKS(KS).setNShininess(SHININESS));
      scene1.geometries.add(tube);



      camera1 //
              .setResolution(500, 500) //
              .build() //
              .renderImage() //
              .writeToImage("sun1");
   }

   @Test
   void solarTest() {
      // --- Setup Scene ---
      Scene scene1 = new Scene("Solar System");

// --- Define Materials ---
      Material basicMaterial = new Material().setKD(0.5).setKS(0.5).setNShininess(100);
      Material sunMaterial = new Material().setKD(0.8).setKS(0.8).setNShininess(300);

// --- Sun ---
      Geometry sun = new Sphere(new Point(0, 0, 0), 10)
              .setEmission(new Color(255, 204, 0)) // Bright yellow
              .setMaterial(sunMaterial);
      scene1.geometries.add(sun);

// --- Planets ---
      Color[] planetColors = {
              new Color(169, 169, 169), // Mercury
              new Color(255, 153, 51),  // Venus
              new Color(0, 102, 255),   // Earth
              new Color(255, 0, 0),     // Mars
              new Color(255, 255, 153)  // Jupiter
      };

      double[] orbitRadii = {15, 25, 35, 45, 60};
      double[] planetRadii = {1.5, 2.5, 2.5, 2.2, 5.0};

      for (int i = 0; i < planetColors.length; i++) {
         Point center = new Point(orbitRadii[i], 0, 0);
         Geometry planet = new Sphere(center, planetRadii[i])
                 .setEmission(planetColors[i])
                 .setMaterial(basicMaterial);
         scene1.geometries.add(planet);
      }

// --- Lighting ---
      scene1.lights.add(
              new PointLight(new Color(1000, 800, 600), new Point(0, 0, 0))
                      .setKL(0.0005)
                      .setKQ(0.0005)
      );
      scene1.setAmbientLight(new AmbientLight(new Color(50, 50, 50)));

// --- Camera ---
      Camera camera1 = Camera.getBuilder()
              .setRayTracer(scene1, RayTracerType.SIMPLE)
              .setLocation(new Point(0, -200, 100))
              .setDirection(new Point(0, 0, 0), new Vector(0, 0, 1))
              .setVpSize(150, 150)
              .setVpDistance(200)
              .setResolution(500, 500)
              .build();

      camera1.renderImage();
      camera1.writeToImage("solarSystem");


   }

    /**
    * Produce a picture of a tube and triangle and sphere lighted by a multi light
    */
   @Test
    void tubeTriangleSphereMulti() {
        Point tubeCenter = new Point(20, 0,-100 );
        double tubeRadius = 30d;
        Geometry tube = new Tube(tubeRadius,new Ray(tubeCenter, new Vector(-1,1,0.5)))
                 .setEmission(Color.ORANGE).setMaterial(new Material().setKD(KD).setKS(KS).setNShininess(SHININESS));
        scene1.geometries.add(tube);

        // Point Light
        scene1.lights.add(new PointLight(Color.YELLOW.scale(2), new Point(10,10 ,20)) //
                 .setKL(0.0001).setKQ(0.002));

            // Spot Light
            Point spotLightPosition = new Point(0,-100 , 0);
            scene1.lights.add(new SpotLight(Color.RED.scale(3), spotLightPosition,tubeCenter.subtract(spotLightPosition).add(new Vector(10,-10,30))) //
                  .setKL(0.00005).setKQ(0.000007));
            // Directional Light
            scene1.lights.add(new DirectionalLight(Color.GREEN.scale(5), new Vector(-1, 0, 1)));

        // Triangle
        Geometry triangle = new Triangle(vertices[0], vertices[1], vertices[2])
                  .setMaterial(material);
        scene1.geometries.add(triangle);

        // Sphere
        Geometry sphere = new Sphere(sphereCenter, SPHERE_RADIUS)
                  .setEmission(sphereColor).setMaterial(new Material().setKD(KD).setKS(KS).setNShininess(SHININESS));
        scene1.geometries.add(sphere);
        // Sphere light
        scene1.lights.add(new PointLight(Color.DARK_BLUE, sphereLightPosition) //
                  .setKL(0.001).setKQ(0.0002));


        camera1 //
                  .setResolution(500, 500) //
                  .build() //
                  .renderImage() //
                  .writeToImage("lightTubeTriangleSphereMulti");
    }


   /** Produce a picture of a sphere lighted by a narrow spotlight */
   @Test
   void sphereSpotSharp() {
      scene1.geometries.add(sphere);
      scene1.lights
         .add(new SpotLight(sphereLightColor, sphereLightPosition, new Vector(1, 1, -0.5)).setNarrowBeam(10) //
            .setKL(0.001).setKQ(0.00004));

      camera1.setResolution(500, 500) //
         .build() //
         .renderImage() //
         .writeToImage("lightSphereSpotSharp");
   }

   /** Produce a picture of two triangles lighted by a narrow spotlight */
   @Test
   void trianglesSpotSharp() {
      scene2.geometries.add(triangle1, triangle2);
      scene2.lights.add(new SpotLight(trianglesLightColor, trianglesLightPosition, trianglesLightDirection) //
              .setNarrowBeam(10).setKL(0.001).setKQ(0.00004));

      camera2.setResolution(500, 500) //
         .build() //
         .renderImage() //
         .writeToImage("lightTrianglesSpotSharp");
   }

    @Test
    void chessboardOnGlassTable_Final() {
        Scene scene = new Scene("Chessboard Glass Table")
                .setAmbientLight(new AmbientLight(new Color(30, 30, 30)));

        // Chessboard
        int size = 8;
        double squareSize = 30;
        double boardZ = -100;
        Color beige = new Color(245, 245, 220);
        Color brown = new Color(139, 69, 19);
        Material mat = new Material().setKD(0.7).setKS(0.3).setNShininess(100);

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Color color = ((row + col) % 2 == 0) ? beige : brown;
                double x0 = (col - size / 2.0) * squareSize;
                double y0 = (row - size / 2.0) * squareSize;
                Point p1 = new Point(x0, y0, boardZ + 1);
                Point p2 = new Point(x0 + squareSize, y0, boardZ + 1);
                Point p3 = new Point(x0 + squareSize, y0 + squareSize, boardZ + 1);
                Point p4 = new Point(x0, y0 + squareSize, boardZ + 1);
                scene.geometries.add(
                        new Polygon(p1, p2, p3, p4)
                                .setEmission(color)
                                .setMaterial(mat)
                );
            }
        }

        // Glass table
        double tableWidth = size * squareSize + 40;
        double tableHeight = size * squareSize + 40;
        Color glassColor = new Color(180, 220, 255).reduce(2);
        Material glassMat = new Material()
                .setKD(0.1).setKS(0.9).setNShininess(300).setKT(0.7);

        Point t1 = new Point(-tableWidth / 2, -tableHeight / 2, boardZ);
        Point t2 = new Point(tableWidth / 2, -tableHeight / 2, boardZ);
        Point t3 = new Point(tableWidth / 2, tableHeight / 2, boardZ);
        Point t4 = new Point(-tableWidth / 2, tableHeight / 2, boardZ);
        scene.geometries.add(
                new Polygon(t1, t2, t3, t4)
                        .setEmission(glassColor)
                        .setMaterial(glassMat)
        );

        // Table legs
        double legRadius = 5;
        double legHeight = 80;
        double legZ0 = boardZ - legHeight;
        Material legMat = new Material().setKD(0.1).setKS(0.6).setNShininess(100).setKT(0.3);
        for (int dx : new int[]{-1, 1}) {
            for (int dy : new int[]{-1, 1}) {
                double x = dx * (tableWidth / 2 - 10);
                double y = dy * (tableHeight / 2 - 10);
                scene.geometries.add(
                        new Cylinder(legRadius, new Ray(new Point(x, y, legZ0), new Vector(0, 0, 1)), legHeight)
                                .setEmission(glassColor)
                                .setMaterial(legMat)
                );
            }
        }

        // Red shiny floor
        double floorZ = legZ0 - 1;
        double floorWidth = tableWidth + 180;
        double floorHeight = tableHeight + 180;
        Color floorColor = new Color(120, 0, 0);
        Material floorMat = new Material()
                .setKD(0.3).setKS(0.7).setNShininess(150).setKR(0.4);
        scene.geometries.add(
                new Polygon(
                        new Point(-floorWidth / 2, -floorHeight / 2, floorZ),
                        new Point(floorWidth / 2, -floorHeight / 2, floorZ),
                        new Point(floorWidth / 2, floorHeight / 2, floorZ),
                        new Point(-floorWidth / 2, floorHeight / 2, floorZ)
                ).setEmission(floorColor).setMaterial(floorMat)
        );

        // Wall
        Color wallColor = new Color(0, 30, 50);
        Material wallMat = new Material().setKD(0.1).setKS(0.7).setNShininess(300);
        scene.geometries.add(
                new Plane(
                        new Point(0, 600, -200),
                        new Vector(0, -6, 20) // Normal pointing towards the camera
                ).setEmission(wallColor).setMaterial(wallMat)
        );

        // ✔ הוספת אור שמאיר את הקיר מאחור
        scene.lights.add(
                new PointLight(new Color(50, 50, 50), new Point(0, 180, 150))
                        .setKL(0.0005).setKQ(0.0002)
        );

        // Carpet
        double carpetZ = floorZ + 0.5;
        double carpetWidth = tableWidth + 40;
        double carpetHeight = tableHeight + 40;
        Color carpetColor = new Color(30, 60, 180);
        Material carpetMat = new Material().setKD(0.9).setKS(0.0).setNShininess(1);
        scene.geometries.add(
                new Polygon(
                        new Point(-carpetWidth / 2, -carpetHeight / 2, carpetZ),
                        new Point(carpetWidth / 2, -carpetHeight / 2, carpetZ),
                        new Point(carpetWidth / 2, carpetHeight / 2, carpetZ),
                        new Point(-carpetWidth / 2, carpetHeight / 2, carpetZ)
                ).setEmission(carpetColor).setMaterial(carpetMat)
        );

        // Chairs
        double chairSeatWidth = 70, chairSeatDepth = 70;
        double chairLegHeight = 50;
        double chairLegRadius = 5;
        double chairZ = boardZ - chairLegHeight;
        Color legColor = new Color(20, 20, 20);
        Color backrestColor = new Color(100, 50, 20);
        Material legMatChair = new Material().setKD(0.2).setKS(0.5).setNShininess(50);
        Material seatMat = new Material().setKD(0.5).setKS(0.2).setNShininess(20);

        double[] chairY = { tableHeight / 2 + 60, -tableHeight / 2 - chairSeatDepth - 60 };
        double[] chairAngle = { 0, 180 };

        for (int i = 0; i < 2; i++) {
            double x0 = -chairSeatWidth / 2;
            double y0 = chairY[i];
            double angle = Math.toRadians(chairAngle[i]);
            double cx = 0, cy = 0;
            double cosA = Math.cos(angle), sinA = Math.sin(angle);

            java.util.function.BiFunction<Double, Double, Point> rotate = (x, y) ->
                    new Point(
                            cx + (x - cx) * cosA - (y - cy) * sinA,
                            cy + (x - cx) * sinA + (y - cy) * cosA,
                            boardZ
                    );

            // Seat
            scene.geometries.add(
                    new Polygon(
                            rotate.apply(x0, y0),
                            rotate.apply(x0 + chairSeatWidth, y0),
                            rotate.apply(x0 + chairSeatWidth, y0 + chairSeatDepth),
                            rotate.apply(x0, y0 + chairSeatDepth)
                    ).setEmission(backrestColor).setMaterial(seatMat)
            );

            // Legs
            for (int lx = 0; lx <= 1; lx++) {
                for (int ly = 0; ly <= 1; ly++) {
                    double legX = x0 + lx * chairSeatWidth;
                    double legY = y0 + ly * chairSeatDepth;
                    Point legPos = rotate.apply(legX, legY);
                    scene.geometries.add(
                            new Cylinder(
                                    chairLegRadius,
                                    new Ray(new Point(legPos.getX(), legPos.getY(), chairZ), new Vector(0, 0, 1)),
                                    chairLegHeight
                            ).setEmission(legColor).setMaterial(legMatChair)
                    );
                }
            }

        }

        // Fluorescent lamps
        double lampRadius = 4;
        double lampLength = tableWidth * 0.8;
        double lampZ = boardZ + 150;
        double lampY1 = -tableHeight / 14;
        double lampY2 = tableHeight / 14;
        Material lampMat = new Material().setKD(0.05).setKS(0.9).setNShininess(200).setKT(0.6);

        for (double lampY : new double[]{lampY1, lampY2}) {
            Point lampStart = new Point(-lampLength / 2, lampY, lampZ);
            Vector lampDir = new Vector(1, 0, 0);
            scene.geometries.add(
                    new Cylinder(lampRadius, new Ray(lampStart, lampDir), lampLength)
                            .setEmission(new Color(220, 220, 255))
                            .setMaterial(lampMat)
            );
            for (int i = 0; i < 3; i++) {
                double frac = (i + 1) / 5.0;
                Point lightPos = lampStart.add(lampDir.scale(lampLength * frac));
                scene.lights.add(
                        new PointLight(new Color(200, 200, 200), lightPos)
                                .setKL(0.0005).setKQ(0.0001)
                );
            }
        }

        // Camera
        Camera camera = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, -600, 200))
                .setDirection(new Point(0, 0, boardZ), new Vector(0, 1, 0))
                .setVpSize(400, 400)
                .setVpDistance(700)
                .setResolution(800, 800)
                .build();

        camera.renderImage()
                .writeToImage("chessboardOnGlassTable_Final");
    }
    @Test
    void infiniteMirrorsWithPerson() {
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
                .setResolution(5000, 5000)
                .setMultithreading(4)
                .setDebugPrint(2)
                .build();

        camera.renderImage()
                .writeToImage("infiniteMirrorsWithPerson");
    }



    /**
     * Test for creating a prism effect using triangular geometry
     */
    @Test
    void prismTest() {
        Scene scene = new Scene("Prism Test")
                .setAmbientLight(new AmbientLight(new Color(20, 20, 20)));
        // Create a triangular prism using multiple triangles
        // This creates a triangular prism lying on its side

        // Front face of prism (triangle)
        scene.geometries.add(
                new Triangle(new Point(-50, -50, -100), new Point(50, -50, -100), new Point(0, 50, -100))
                        .setEmission(new Color(10, 10, 10))
                        .setMaterial(new Material().setKD(0.1).setKS(0.1).setNShininess(100).setKT(0.9).setKR(0.1))
        );

        // Back face of prism (triangle) - deeper in Z
        scene.geometries.add(
                new Triangle(new Point(-50, -50, -150), new Point(50, -50, -150), new Point(0, 50, -150))
                        .setEmission(new Color(10, 10, 10))
                        .setMaterial(new Material().setKD(0.1).setKS(0.1).setNShininess(100).setKT(0.9).setKR(0.1))
        );

        // Left side face
        scene.geometries.add(
                new Triangle(new Point(-50, -50, -100), new Point(0, 50, -100), new Point(-50, -50, -150))
                        .setEmission(new Color(10, 10, 10))
                        .setMaterial(new Material().setKD(0.1).setKS(0.1).setNShininess(100).setKT(0.9).setKR(0.1)),
                new Triangle(new Point(0, 50, -100), new Point(-50, -50, -150), new Point(0, 50, -150))
                        .setEmission(new Color(10, 10, 10))
                        .setMaterial(new Material().setKD(0.1).setKS(0.1).setNShininess(100).setKT(0.9).setKR(0.1))
        );

        // Right side face
        scene.geometries.add(
                new Triangle(new Point(50, -50, -100), new Point(50, -50, -150), new Point(0, 50, -100))
                        .setEmission(new Color(10, 10, 10))
                        .setMaterial(new Material().setKD(0.1).setKS(0.1).setNShininess(100).setKT(0.9).setKR(0.1)),
                new Triangle(new Point(0, 50, -100), new Point(50, -50, -150), new Point(0, 50, -150))
                        .setEmission(new Color(10, 10, 10))
                        .setMaterial(new Material().setKD(0.1).setKS(0.1).setNShininess(100).setKT(0.9).setKR(0.1))
        );

        // Bottom face
        scene.geometries.add(
                new Triangle(new Point(-50, -50, -100), new Point(-50, -50, -150), new Point(50, -50, -100))
                        .setEmission(new Color(10, 10, 10))
                        .setMaterial(new Material().setKD(0.1).setKS(0.1).setNShininess(100).setKT(0.9).setKR(0.1)),
                new Triangle(new Point(50, -50, -100), new Point(-50, -50, -150), new Point(50, -50, -150))
                        .setEmission(new Color(10, 10, 10))
                        .setMaterial(new Material().setKD(0.1).setKS(0.1).setNShininess(100).setKT(0.9).setKR(0.1))
        );

        // Add a white light source positioned to shine through the prism
        scene.lights.add(
                new SpotLight(new Color(800, 800, 800), new Point(-200, 0, -125), new Vector(1, 0, 0))
                        .setKL(0.0001).setKQ(0.0000001)
        );

        // Add some background elements to see the refracted light
        scene.geometries.add(
                // Background plane to catch the refracted light
                new Triangle(new Point(150, -200, -300), new Point(150, 200, -300), new Point(150, 200, 200))
                        .setEmission(new Color(20, 20, 20))
                        .setMaterial(new Material().setKD(0.8).setKS(0.2).setNShininess(20)),
                new Triangle(new Point(150, -200, -300), new Point(150, 200, 200), new Point(150, -200, 200))
                        .setEmission(new Color(20, 20, 20))
                        .setMaterial(new Material().setKD(0.8).setKS(0.2).setNShininess(20))
        );

        scene.setAmbientLight(new AmbientLight(new Color(15, 15, 15)));

        Camera camera = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 0, 500))
                .setDirection(Point.ZERO, Vector.AXIS_Y)
                .setVpDistance(500)
                .setVpSize(300, 300)
                .setResolution(800, 800)
                .build()
                .renderImage()
                .writeToImage("prismEffect");

        camera.renderImage()
                .writeToImage("prismEffect");
    }

    /**
     * Test for creating a prism effect with visible light beams using tiny spheres
     */
    @Test
    void prismWithVisibleBeams() {
        Scene scene = new Scene("Prism with Visible Beams")
                .setAmbientLight(new AmbientLight(new Color(20, 20, 20)));

        // Create a triangular prism (keeping original size but adjusting positions)
        // Front face
        scene.geometries.add(
                new Triangle(new Point(-30, -30, -100), new Point(30, -30, -100), new Point(0, 30, -100))
                        .setEmission(new Color(5, 5, 5))
                        .setMaterial(new Material().setKD(0.05).setKS(0.05).setNShininess(100).setKT(0.95))
        );

        // Back face
        scene.geometries.add(
                new Triangle(new Point(-30, -30, -120), new Point(30, -30, -120), new Point(0, 30, -120))
                        .setEmission(new Color(5, 5, 5))
                        .setMaterial(new Material().setKD(0.05).setKS(0.05).setNShininess(100).setKT(0.95))
        );

        // Side faces
        scene.geometries.add(
                // Left side
                new Triangle(new Point(-30, -30, -100), new Point(0, 30, -100), new Point(-30, -30, -120))
                        .setMaterial(new Material().setKD(0.05).setKS(0.05).setNShininess(100).setKT(0.95)),
                new Triangle(new Point(0, 30, -100), new Point(-30, -30, -120), new Point(0, 30, -120))
                        .setMaterial(new Material().setKD(0.05).setKS(0.05).setNShininess(100).setKT(0.95)),

                // Right side
                new Triangle(new Point(30, -30, -100), new Point(30, -30, -120), new Point(0, 30, -100))
                        .setMaterial(new Material().setKD(0.05).setKS(0.05).setNShininess(100).setKT(0.95)),
                new Triangle(new Point(0, 30, -100), new Point(30, -30, -120), new Point(0, 30, -120))
                        .setMaterial(new Material().setKD(0.05).setKS(0.05).setNShininess(100).setKT(0.95)),

                // Bottom
                new Triangle(new Point(-30, -30, -100), new Point(-30, -30, -120), new Point(30, -30, -100))
                        .setMaterial(new Material().setKD(0.05).setKS(0.05).setNShininess(100).setKT(0.95)),
                new Triangle(new Point(30, -30, -100), new Point(-30, -30, -120), new Point(30, -30, -120))
                        .setMaterial(new Material().setKD(0.05).setKS(0.05).setNShininess(100).setKT(0.95))
        );

        // Create incoming white light beam with larger spheres and closer spacing
        for (int i = 0; i < 90; i++) {
            double x = -120 + i * 1.5; // Shorter beam, from x=-120 to x=-30
            scene.geometries.add(
                    new Sphere(new Point(x, 0, -110), 1.2) // Increased sphere size
                            .setEmission(new Color(200, 200, 200))
                            .setMaterial(new Material().setKD(0.3).setKS(0.7).setNShininess(100).setKT(0.3))
            );
        }

        // Create dispersed light beams after the prism with larger spheres
        // Red beam (least refracted - smallest angle)
        for (int i = 0; i < 50; i++) {
            double x = 15 + i * 1.8; // Increased spacing
            double y = 0 + i * 0.08; // Slightly increased angle
            scene.geometries.add(
                    new Sphere(new Point(x, y, -110), 1.0) // Larger spheres
                            .setEmission(new Color(255, 0, 0))
                            .setMaterial(new Material().setKD(0.4).setKS(0.6).setNShininess(50).setKT(0.4))
            );
        }

        // Orange beam
        for (int i = 0; i < 50; i++) {
            double x = 15 + i * 1.8;
            double y = 0 + i * 0.12;
            scene.geometries.add(
                    new Sphere(new Point(x, y, -110), 1.0)
                            .setEmission(new Color(255, 165, 0))
                            .setMaterial(new Material().setKD(0.4).setKS(0.6).setNShininess(50).setKT(0.4))
            );
        }

        // Yellow beam
        for (int i = 0; i < 50; i++) {
            double x = 15 + i * 1.8;
            double y = 0 + i * 0.16;
            scene.geometries.add(
                    new Sphere(new Point(x, y, -110), 1.0)
                            .setEmission(new Color(255, 255, 0))
                            .setMaterial(new Material().setKD(0.4).setKS(0.6).setNShininess(50).setKT(0.4))
            );
        }

        // Green beam
        for (int i = 0; i < 50; i++) {
            double x = 15 + i * 1.8;
            double y = 0 + i * 0.20;
            scene.geometries.add(
                    new Sphere(new Point(x, y, -110), 1.0)
                            .setEmission(new Color(0, 255, 0))
                            .setMaterial(new Material().setKD(0.4).setKS(0.6).setNShininess(50).setKT(0.4))
            );
        }

        // Blue beam
        for (int i = 0; i < 50; i++) {
            double x = 15 + i * 1.8;
            double y = 0 + i * 0.24;
            scene.geometries.add(
                    new Sphere(new Point(x, y, -110), 1.0)
                            .setEmission(new Color(0, 0, 255))
                            .setMaterial(new Material().setKD(0.4).setKS(0.6).setNShininess(50).setKT(0.4))
            );
        }

        // Indigo beam
        for (int i = 0; i < 50; i++) {
            double x = 15 + i * 1.8;
            double y = 0 + i * 0.28;
            scene.geometries.add(
                    new Sphere(new Point(x, y, -110), 1.0)
                            .setEmission(new Color(75, 0, 130))
                            .setMaterial(new Material().setKD(0.4).setKS(0.6).setNShininess(50).setKT(0.4))
            );
        }

        // Violet beam (most refracted - largest angle)
        for (int i = 0; i < 50; i++) {
            double x = 15 + i * 1.8;
            double y = 0 + i * 0.32;
            scene.geometries.add(
                    new Sphere(new Point(x, y, -110), 1.0)
                            .setEmission(new Color(238, 130, 238))
                            .setMaterial(new Material().setKD(0.4).setKS(0.6).setNShininess(50).setKT(0.4))
            );
        }

        // Add a closer background screen to catch the dispersed light
        scene.geometries.add(
                new Triangle(new Point(140, -80, -150), new Point(140, 80, -150), new Point(140, 80, -70))
                        .setEmission(new Color(15, 15, 15))
                        .setMaterial(new Material().setKD(0.9).setKS(0.1).setNShininess(20)),
                new Triangle(new Point(140, -80, -150), new Point(140, 80, -70), new Point(140, -80, -70))
                        .setEmission(new Color(15, 15, 15))
                        .setMaterial(new Material().setKD(0.9).setKS(0.1).setNShininess(20))
        );

        // Improved lighting
        scene.lights.add(
                new SpotLight(new Color(500, 500, 500), new Point(-150, 0, -110), new Vector(1, 0, 0))
                        .setKL(0.0001).setKQ(0.0000001)
        );

        scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20)));

        // Adjusted camera for closer, better view
        Camera camera = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 0, 50)) // Much closer to the action
                .setDirection(Point.ZERO, Vector.AXIS_Y)
                .setVpDistance(150) // Reduced viewport distance
                .setVpSize(200, 200) // Slightly smaller viewport for better focus
                .setResolution(800, 800) // Higher resolution for better detail
                .build()
                .renderImage()
                .writeToImage("prismWithVisibleBeams8");
    }

    /**
     * Test for creating a more physically accurate prism effect with visible light beams using tiny spheres.
     * Dispersion starts at the entry face and increases at the exit face.
     */
    @Test
    void physicallyAccuratePrismDispersion() {
        Scene scene = new Scene("Physically Accurate Prism Dispersion")
                .setAmbientLight(new AmbientLight(new Color(20, 20, 20)));

        // Prism geometry (as before)
        // ... [same as your triangles and materials for prism faces] ...

        // Incoming white light beam (stops at the prism)
        for (int i = 0; i < 35; i++) {
            double x = -120 + i * 2.5; // Beam stops just before prism
            scene.geometries.add(
                    new Sphere(new Point(x, 0, -110), 1.2)
                            .setEmission(new Color(200, 200, 200))
                            .setMaterial(new Material().setKD(0.3).setKS(0.7).setNShininess(100).setKT(0.3))
            );
        }

        // Colored beams start at the entry face and travel through the prism at slightly different angles (simulate dispersion)
        Color[] colors = {new Color(255,0,0), new Color(255,165,0), new Color(255,255,0),
                new Color(0,255,0), new Color(0,0,255), new Color(75,0,130), new Color(238,130,238)};
        double[] entryAngles = {0.03, 0.05, 0.08, 0.11, 0.14, 0.17, 0.20}; // radians, red bends least, violet most

        // Path: from entry face (at x = -30, y = 0) through the prism to exit face (at x = 30)
        for (int c = 0; c < colors.length; c++) {
            Color color = colors[c];
            double angle = entryAngles[c];

            // Inside the prism: colored beams diverge
            for (int i = 0; i < 40; i++) {
                double x = -30 + i * 1.5; // inside prism
                double y = 0 + Math.tan(angle) * (x + 30); // diverge according to angle
                scene.geometries.add(
                        new Sphere(new Point(x, y, -110), 1.0)
                                .setEmission(color)
                                .setMaterial(new Material().setKD(0.4).setKS(0.6).setNShininess(50).setKT(0.4))
                );
            }

            // After the prism: greater divergence (simulate second refraction)
            double exitAngle = angle * 2; // exaggerate for visibility
            double lastX = 30;
            double lastY = Math.tan(angle) * (lastX + 30);
            for (int i = 1; i <= 40; i++) {
                double x = lastX + i * 1.8;
                double y = lastY + Math.tan(exitAngle) * i * 1.8;
                scene.geometries.add(
                        new Sphere(new Point(x, y, -110), 1.0)
                                .setEmission(color)
                                .setMaterial(new Material().setKD(0.4).setKS(0.6).setNShininess(50).setKT(0.4))
                );
            }
        }

        // Add a closer background screen to catch the dispersed light
        scene.geometries.add(
                new Triangle(new Point(140, -80, -150), new Point(140, 80, -150), new Point(140, 80, -70))
                        .setEmission(new Color(15, 15, 15))
                        .setMaterial(new Material().setKD(0.9).setKS(0.1).setNShininess(20)),
                new Triangle(new Point(140, -80, -150), new Point(140, 80, -70), new Point(140, -80, -70))
                        .setEmission(new Color(15, 15, 15))
                        .setMaterial(new Material().setKD(0.9).setKS(0.1).setNShininess(20))
        );

        // Improved lighting
        scene.lights.add(
                new SpotLight(new Color(500, 500, 500), new Point(-150, 0, -110), new Vector(1, 0, 0))
                        .setKL(0.0001).setKQ(0.0000001)
        );

        scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20)));

        // Adjusted camera for closer, better view
        Camera camera = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 0, 50)) // Much closer to the action
                .setDirection(Point.ZERO, Vector.AXIS_Y)
                .setVpDistance(150) // Reduced viewport distance
                .setVpSize(200, 200) // Slightly smaller viewport for better focus
                .setResolution(800, 800) // Higher resolution for better detail
                .build()
                .renderImage()
                .writeToImage("physicallyAccuratePrismDispersion");
    }

    /**
     * Test for creating a physically accurate and visually continuous prism rainbow effect.
     * - The prism is visible, with a slight tint and reflectivity.
     * - The rainbow beams are continuous: many closely-spaced, overlapping spheres, with smoothly interpolated colors.
     * - The color spectrum blends from red to violet (not just 7 separated colors).
     */
    @Test
    void prismWithContinuousBlendingRainbow() {
        Scene scene = new Scene("Prism with Continuous Blending Rainbow")
                .setAmbientLight(new AmbientLight(new Color(30, 30, 36))); // Slightly bluish ambient for prism visibility

        // 1. Prism Construction (Visible, slightly tinted)
        Material prismMat = new Material()
                .setKD(0.18).setKS(0.5).setNShininess(200)
                .setKT(0.7).setKR(0.08); // Slight reflectivity, less transparent
        Color prismTint = new Color(48, 64, 128); // Very faint blue

        // Front and Back faces
        scene.geometries.add(
                new Triangle(new Point(-30, -30, -100), new Point(30, -30, -100), new Point(0, 30, -100))
                        .setEmission(prismTint.scale(0.24))
                        .setMaterial(prismMat),
                new Triangle(new Point(-30, -30, -120), new Point(30, -30, -120), new Point(0, 30, -120))
                        .setEmission(prismTint.scale(0.24))
                        .setMaterial(prismMat)
        );
        // Sides & Bottom (6 triangles)
        scene.geometries.add(
                // Left
                new Triangle(new Point(-30, -30, -100), new Point(0, 30, -100), new Point(-30, -30, -120)).setMaterial(prismMat),
                new Triangle(new Point(0, 30, -100), new Point(-30, -30, -120), new Point(0, 30, -120)).setMaterial(prismMat),
                // Right
                new Triangle(new Point(30, -30, -100), new Point(30, -30, -120), new Point(0, 30, -100)).setMaterial(prismMat),
                new Triangle(new Point(0, 30, -100), new Point(30, -30, -120), new Point(0, 30, -120)).setMaterial(prismMat),
                // Bottom
                new Triangle(new Point(-30, -30, -100), new Point(-30, -30, -120), new Point(30, -30, -100)).setMaterial(prismMat),
                new Triangle(new Point(30, -30, -100), new Point(-30, -30, -120), new Point(30, -30, -120)).setMaterial(prismMat)
        );

        // 2. Incoming White Light Beam (Ends at prism front face)
        int whiteSteps = 36; double beamRadius = 1.1;
        for (int i = 0; i < whiteSteps; i++) {
            double x = -120 + i * 2.5; // x: -120 to -30
            scene.geometries.add(
                    new Sphere(new Point(x, 0, -110), beamRadius)
                            .setEmission(new Color(210, 210, 210))
                            .setMaterial(new Material().setKD(0.38).setKS(0.72).setNShininess(110).setKT(0.23))
            );
        }

        // 3. Continuous Rainbow: many beams, smooth color blending, overlapping spheres
        int rainbowBeams = 72; // More beams = smoother gradient
        double minDispersion = 0.035; // radians, red (least refracted)
        double maxDispersion = 0.19; // radians, violet (most refracted)
        int insidePrismSteps = 34; // From front to back face
        int outsidePrismSteps = 44; // After prism

        for (int k = 0; k < rainbowBeams; k++) {
            double t = k / (double)(rainbowBeams - 1);
            Color color = interpolateRainbowColor(t); // Smooth color (see below)
            double angle = minDispersion + t * (maxDispersion - minDispersion);

            // Inside prism: colored beams diverge
            for (int j = 0; j < insidePrismSteps; j++) {
                double x = -30 + j * 1.5; // x: -30 -> +30
                double y = Math.tan(angle) * (x + 30); // Dispersion
                scene.geometries.add(
                        new Sphere(new Point(x, y, -110), 0.87)
                                .setEmission(color)
                                .setMaterial(new Material().setKD(0.42).setKS(0.52).setNShininess(65).setKT(0.45))
                );
            }
            // At exit face (x=30)
            double lastX = 30;
            double lastY = Math.tan(angle) * (lastX + 30);
            double exitAngle = angle * 2; // Simulate wider dispersion after exit

            // After prism: beams diverge more
            for (int j = 0; j < outsidePrismSteps; j++) {
                double x = lastX + j * 1.6;
                double y = lastY + Math.tan(exitAngle) * j * 1.6;
                scene.geometries.add(
                        new Sphere(new Point(x, y, -110), 0.87)
                                .setEmission(color)
                                .setMaterial(new Material().setKD(0.42).setKS(0.52).setNShininess(55).setKT(0.45))
                );
            }
        }

        // 4. Background screen (to catch the rainbow)
        scene.geometries.add(
                new Triangle(new Point(140, -80, -150), new Point(140, 80, -150), new Point(140, 80, -70))
                        .setEmission(new Color(24, 24, 32))
                        .setMaterial(new Material().setKD(0.89).setKS(0.13).setNShininess(24)),
                new Triangle(new Point(140, -80, -150), new Point(140, 80, -70), new Point(140, -80, -70))
                        .setEmission(new Color(24, 24, 32))
                        .setMaterial(new Material().setKD(0.89).setKS(0.13).setNShininess(24))
        );

        // 5. Lighting: strong white spotlight + gentle ambient
        scene.lights.add(
                new SpotLight(new Color(900, 900, 900), new Point(-150, 0, -110), new Vector(1, 0, 0))
                        .setKL(0.00008).setKQ(0.00000008)
        );

        // 6. Camera
        Camera camera = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 0, 50))
                .setDirection(new Vector(0, 0, -1))
                .setVpDistance(150)
                .setVpSize(200, 200)
                .setResolution(900, 900)
                .build()
                .renderImage()
                .writeToImage("prismWithContinuousBlendingRainbow");
    }

    /**
     * Smoothly interpolates rainbow color for t in [0,1] using key rainbow stops,
     * using the primitives.Color class.
     */
    private Color interpolateRainbowColor(double t) {
        // Key stops: red, orange, yellow, green, blue, indigo, violet
        Color[] stops = {
                new Color(255, 0, 0),      // red
                new Color(255, 127, 0),    // orange
                new Color(255, 255, 0),    // yellow
                new Color(0, 255, 0),      // green
                new Color(0, 0, 255),      // blue
                new Color(75, 0, 130),     // indigo
                new Color(148, 0, 211)     // violet
        };
        double scaled = t * (stops.length - 1);
        int idx = (int) Math.floor(scaled);
        if (idx >= stops.length - 1) return stops[stops.length - 1];
        double localT = scaled - idx;

        // Interpolate each color channel individually
        double r = stops[idx].rgb.d1() * (1 - localT) + stops[idx + 1].rgb.d1() * localT;
        double g = stops[idx].rgb.d2() * (1 - localT) + stops[idx + 1].rgb.d2() * localT;
        double b = stops[idx].rgb.d3() * (1 - localT) + stops[idx + 1].rgb.d3() * localT;
        return new Color(r, g, b);
    }

    @Test
    void prismRainbowExitFace() {
        Scene scene = new Scene("Prism with Correct Exit Face Rainbow")
                .setAmbientLight(new AmbientLight(new Color(30, 30, 36)));

        // Prism vertices (z = -100 or -120, but beams are at z = -110)
        Point A = new Point(-30, -30, -100);
        Point B = new Point(30, -30, -100);
        Point C = new Point(0, 30, -100);
        Point D = new Point(-30, -30, -120);
        Point E = new Point(30, -30, -120);
        Point F = new Point(0, 30, -120);

        // Prism faces, projected at z = -110 for beams
        Material prismMat = new Material()
                .setKD(0.18).setKS(0.5).setNShininess(200)
                .setKT(0.7).setKR(0.08);
        Color prismTint = new Color	(173, 216, 230);

        // Prism geometry (for visualization only, at z=-100 and z=-120)
        scene.geometries.add(
                new Triangle(A, B, C).setEmission(prismTint.scale(0.24)).setMaterial(prismMat),
                new Triangle(D, E, F).setEmission(prismTint.scale(0.24)).setMaterial(prismMat),
                new Triangle(A, D, C).setMaterial(prismMat),
                new Triangle(C, D, F).setMaterial(prismMat),
                new Triangle(B, E, C).setMaterial(prismMat),
                new Triangle(C, E, F).setMaterial(prismMat),
                new Triangle(A, B, D).setMaterial(prismMat),
                new Triangle(B, D, E).setMaterial(prismMat)
        );

        // 1. White beam: from left to entry face at (x = -15, y = 0, z = -110)
        int whiteSteps = 72;
        for (int i = 0; i < whiteSteps; i++) {
            double x = -120 + i * ((-15 + 120.0) / (whiteSteps - 1)); // -120 to -15
            scene.geometries.add(
                    new Sphere(new Point(x, 0, -110), 1.1)
                            .setEmission(new Color(210, 210, 210))
                            .setMaterial(new Material().setKD(0.38).setKS(0.72).setNShininess(110).setKT(0.23))
            );
        }

        // Floor
        Geometry floor = new Plane(A.add(new Vector(0, -1, 0)), Vector.AXIS_Y)
                .setEmission(new Color(176, 189, 196))
                .setMaterial(new Material().setKD(0.9).setKS(0.1).setNShininess(10).setKR(0.15));


        scene.geometries.add(floor);
        //scene.geometries.add(new Polygon(new Point(-300, -30, -300), new Point(300, -30, -300), new Point(300, 0, -300), new Point(-300, 0, -300))
        //        .setEmission(new Color(206, 218, 233)));

        // 2. Rainbow beams: inside prism, from (x=-15, y=0, z=-110) to the exit face (from x=30,y=-30 to x=0,y=30, all at z=-110)
        int rainbowBeams = 72;
        double minDispersion = 0.035;
        double maxDispersion = 0.19;
        int insidePrismSteps = 40;
        int outsidePrismSteps = 44;

        /* Exit face (CFBE in z=-110 plane): from (x=30, y=-30) to (x=0, y=30) */
        Point exitStart = new Point(15, 0, -110);
        Point exitEnd = new Point(17, -4, -110);

        for (int k = 0; k < rainbowBeams; k++) {
            double t = k / (double)(rainbowBeams - 1);

            Color color = interpolateRainbowColor(t);
            double angle = minDispersion + t * (maxDispersion - minDispersion);

            double xExit = exitStart.getX() + t * (exitEnd.getX() - exitStart.getX());
            double yExit = exitStart.getY() + t * (exitEnd.getY() - exitStart.getY());

            // 2a. Inside prism: from (x=-15, y=0, z=-110) to (xExit, yExit, z=-110)
            for (int j = 0; j < insidePrismSteps; j++) {
                double s = j / (double)(insidePrismSteps - 1);
                double x = -15 + s * (xExit + 15);
                double y = 0 + s * (yExit - 0);
                double z = -110;

                // Dispersion grows slightly inside, fanning downward
                double y_disp = y - Math.tan(angle) * s * 8;
                scene.geometries.add(
                        new Sphere(new Point(x, y_disp, z), 0.87)
                                .setEmission(color)
                                .setMaterial(new Material().setKD(0.42).setKS(0.52).setNShininess(65).setKT(0.75))
                );
            }

            // 2b. After prism: start from (xExit, yExit, z=-110), diverge more (wider rainbow)
            double lastY = yExit - Math.tan(angle) * 8; // matches direction above
            double z = -110;
            double exitAngle = angle * 2.2; // Even wider after prism

            for (int j = 1; j <= outsidePrismSteps; j++) {
                double dx = j * 1.6;
                double x = xExit + dx;
                double y = lastY - Math.tan(exitAngle) * dx;
                scene.geometries.add(
                        new Sphere(new Point(x, y, z), 0.87)
                                .setEmission(color)
                                .setMaterial(new Material().setKD(0.42).setKS(0.52).setNShininess(55).setKT(0.75))
                );
            }
        }

        // 3. Background screen (to catch the rainbow)
        scene.geometries.add(
                new Triangle(new Point(140, -80, -180), new Point(140, 80, -180), new Point(140, 80, -70))
                        .setEmission(new Color(24, 24, 32))
                        .setMaterial(new Material().setKD(0.89).setKS(0.13).setNShininess(24)),
                new Triangle(new Point(140, -80, -180), new Point(140, 80, -70), new Point(140, -80, -70))
                        .setEmission(new Color(24, 24, 32))
                        .setMaterial(new Material().setKD(0.89).setKS(0.13).setNShininess(24))
        );

        // 4. Lighting: strong white spotlight + gentle ambient
        scene.lights.add(
                new SpotLight(new Color(900, 900, 900), new Point(-150, 0, -110), new Vector(1, 0, 0))
                        .setKL(0.00008).setKQ(0.00008)
        );

        // 5. Camera
        Camera camera = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 25, 50))
                .setDirection(new Vector(0, -0.02, -1))
                .setVpDistance(150)
                .setVpSize(170, 170)
                .setResolution(900, 900)
                .setAaSamples(5)
                .setDofSamples(5)
                .setApertureRadius(1)
                .setFocalDistance(new Point(0, 0, -100).distance(new Point(0, 25, 50)))
                .setMultithreading(10)
                .setDebugPrint(0.1)
                .build()
                .renderImage()
                .writeToImage("FinalPrismMultiThreadingDOF");
    }

    /**
     * Test for creating a physically accurate and visually continuous prism rainbow effect.
     * - The prism is visible, with a slight tint and reflectivity.
     * - The rainbow beams are continuous: many closely-spaced, overlapping spheres, with smoothly interpolated colors.
     * - The color spectrum blends from red to violet (not just 7 separated colors).
     */
    @Test
    void prismThatDoesntGiveMeNightmares() {
        Scene scene = new Scene("Prism That Doesn't Give Me Nightmares")
                .setAmbientLight(new AmbientLight(new Color(30, 30, 36))); // Slightly bluish ambient for prism visibility

        // 1. Prism Construction (Visible, slightly tinted)
        Material prismMat = new Material()
                .setKD(0.18).setKS(0.5).setNShininess(200)
                .setKT(0.7).setKR(0.08); // Slight reflectivity, less transparent
        Color prismTint = new Color(48, 64, 128); // Very faint blue

        // Front and Back faces
        scene.geometries.add(
                new Triangle(new Point(-30, -30, -100), new Point(30, -30, -100), new Point(0, 30, -100))
                        .setEmission(prismTint.scale(0.24))
                        .setMaterial(prismMat),
                new Triangle(new Point(-30, -30, -120), new Point(30, -30, -120), new Point(0, 30, -120))
                        .setEmission(prismTint.scale(0.24))
                        .setMaterial(prismMat)
        );
        // Sides & Bottom (6 triangles)
        scene.geometries.add(
                // Left
                new Triangle(new Point(-30, -30, -100), new Point(0, 30, -100), new Point(-30, -30, -120)).setMaterial(prismMat),
                new Triangle(new Point(0, 30, -100), new Point(-30, -30, -120), new Point(0, 30, -120)).setMaterial(prismMat),
                // Right
                new Triangle(new Point(30, -30, -100), new Point(30, -30, -120), new Point(0, 30, -100)).setMaterial(prismMat),
                new Triangle(new Point(0, 30, -100), new Point(30, -30, -120), new Point(0, 30, -120)).setMaterial(prismMat),
                // Bottom
                new Triangle(new Point(-30, -30, -100), new Point(-30, -30, -120), new Point(30, -30, -100)).setMaterial(prismMat),
                new Triangle(new Point(30, -30, -100), new Point(-30, -30, -120), new Point(30, -30, -120)).setMaterial(prismMat)
        );

        // 2. Incoming White Light Beam (Ends at prism front face)
        int whiteSteps = 60; double beamRadius = 1.1;
        for (int i = 0; i < whiteSteps; i++) {
            double x = -120 + i * 1.75; // x: -120 to -30
            scene.geometries.add(
                    new Sphere(new Point(x, 0, -110), beamRadius)
                            .setEmission(new Color(210, 210, 210))
                            .setMaterial(new Material().setKD(0.38).setKS(0.72).setNShininess(110).setKT(0.23))
            );
        }

        // 3. Continuous Rainbow: many beams, smooth color blending, overlapping spheres
        int rainbowBeams = 72; // More beams = smoother gradient
        double minDispersion = 0.035; // radians, red (least refracted)
        double maxDispersion = 0.19; // radians, violet (most refracted)
        int insidePrismSteps = 20; // From front to back face
        int outsidePrismSteps = 44; // After prism

        for (int k = 0; k < rainbowBeams; k++) {
            double t = k / (double)(rainbowBeams - 1);
            Color color = interpolateRainbowColor(t); // Smooth color (see below)
            double angle = minDispersion + t * (maxDispersion - minDispersion);

            // Inside prism: colored beams diverge
            for (int j = 0; j < insidePrismSteps; j++) {
                double x = -15 + j * 1.5; // x: -30 -> +30
                double y = Math.tan(angle) * (x + 15); // Dispersion
                scene.geometries.add(
                        new Sphere(new Point(x, y, -110), 0.87)
                                .setEmission(color)
                                .setMaterial(new Material().setKD(0.42).setKS(0.52).setNShininess(65).setKT(0.45))
                );
            }
            // At exit face (x=30)
            double lastX = 30;
            double lastY = Math.tan(angle) * (lastX + 30);
            double exitAngle = angle * 2; // Simulate wider dispersion after exit

            // After prism: beams diverge more
            for (int j = 0; j < outsidePrismSteps; j++) {
                double x = lastX + j * 1.6;
                double y = lastY + Math.tan(exitAngle) * j * 1.6;
                scene.geometries.add(
                        new Sphere(new Point(x, y, -110), 0.87)
                                .setEmission(color)
                                .setMaterial(new Material().setKD(0.42).setKS(0.52).setNShininess(55).setKT(0.45))
                );
            }
        }

        // 4. Background screen (to catch the rainbow)
        scene.geometries.add(
                new Triangle(new Point(140, -80, -150), new Point(140, 80, -150), new Point(140, 80, -70))
                        .setEmission(new Color(24, 24, 32))
                        .setMaterial(new Material().setKD(0.89).setKS(0.13).setNShininess(24)),
                new Triangle(new Point(140, -80, -150), new Point(140, 80, -70), new Point(140, -80, -70))
                        .setEmission(new Color(24, 24, 32))
                        .setMaterial(new Material().setKD(0.89).setKS(0.13).setNShininess(24))
        );

        // 5. Lighting: strong white spotlight + gentle ambient
        scene.lights.add(
                new SpotLight(new Color(900, 900, 900), new Point(-150, 0, -110), new Vector(1, 0, 0))
                        .setKL(0.00008).setKQ(0.00000008)
        );

        // 6. Camera
        Camera camera = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 0, 50))
                .setDirection(new Vector(0, 0, -1))
                .setVpDistance(150)
                .setVpSize(200, 200)
                .setResolution(900, 900)
                .build()
                .renderImage()
                .writeToImage("prismThatDoesntGiveMeNightmares");
    }
}