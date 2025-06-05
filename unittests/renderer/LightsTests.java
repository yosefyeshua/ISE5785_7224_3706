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
    * First scene for some of tests
    */
   private final Scene scene1 = new Scene("Test scene");
   /**
    * Second scene for some of tests
    */
   private final Scene scene2 = new Scene("Test scene")
           .setAmbientLight(new AmbientLight(new Color(38, 38, 38)));

   /**
    * First camera builder for some of tests
    */
   private final Camera.Builder camera1 = Camera.getBuilder()                                          //
           .setRayTracer(scene1, RayTracerType.SIMPLE)                                                                      //
           .setLocation(new Point(0, 0, 1000))                                                                              //
           .setDirection(Point.ZERO, Vector.AXIS_Y)                                                                         //
           .setVpSize(150, 150).setVpDistance(1000);

   /**
    * Second camera builder for some of tests
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

   /**
    * Produce a picture of a tube and trigle and sphere lighted by a multi light
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

   /**
    * Render a chessboard on a glass table with realistic lighting and fluorescent lamps above.
    */
   @Test
   void chessboardOnGlassTableWithFluorescent() {
       Scene scene = new Scene("Chessboard Glass Table")
               .setAmbientLight(new AmbientLight(new Color(30, 30, 30)));

       // Chessboard parameters
       int size = 8;
       double squareSize = 30;
       double boardZ = -100;
       Color beige = new Color(245, 245, 220);
       Color brown = new Color(139, 69, 19);
       Material mat = new Material().setKD(0.7).setKS(0.3).setNShininess(100);

       // Build chessboard
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

       // Glass table (large transparent rectangle under the chessboard)
       double tableWidth = size * squareSize + 40;
       double tableHeight = size * squareSize + 40;
       double tableZ = boardZ;
       Color glassColor = new Color(180, 220, 255).reduce(2);
       Material glassMat = new Material()
               .setKD(0.1)
               .setKS(0.9)
               .setNShininess(300)
               .setKT(0.8); // 80% transparent

       Point t1 = new Point(-tableWidth / 2, -tableHeight / 2, tableZ);
       Point t2 = new Point(tableWidth / 2, -tableHeight / 2, tableZ);
       Point t3 = new Point(tableWidth / 2, tableHeight / 2, tableZ);
       Point t4 = new Point(-tableWidth / 2, tableHeight / 2, tableZ);
       scene.geometries.add(
           new Polygon(t1, t2, t3, t4)
               .setEmission(glassColor)
               .setMaterial(glassMat)
       );

       // Table legs (transparent cylinders)
       double legRadius = 5;
       double legHeight = 80;
       double legZ0 = tableZ - legHeight;
       Material floorMat = new Material()
               .setKD(0.2)      // Diffuse
               .setKS(0.9)      // High specular for shine
               .setNShininess(300) // Very shiny
               .setKR(0.7);     // Reflection (0.0-1.0)

       scene.geometries.add(
               new Plane(new Point(0, 0, legZ0), new Vector(0, 0, 1))
                       .setEmission(Color.RED.scale(2))
                       .setMaterial(floorMat)
       );
       Material legMat = new Material().setKD(0.1).setKS(0.8).setNShininess(200).setKT(0.7);
       for (int dx : new int[]{-1, 1}) {
           for (int dy : new int[]{-1, 1}) {
               double x = dx * (tableWidth / 2 - 10);
               double y = dy * (tableHeight / 2 - 10);
               scene.geometries.add(
                   new Cylinder(legRadius,
                           new Ray(new Point(x, y, legZ0), new Vector(0, 0, 1)),
                           legHeight)
                       .setEmission(glassColor)
                       .setMaterial(legMat)
               );
           }
       }

       // Fluorescent lamps (two transparent cylinders above, with white point lights inside)
       double lampRadius = 4;
       double lampLength = tableWidth * 0.8;
       double lampZ = tableZ + 200; // Above the table
       double lampY1 = -tableHeight / 10; // Two heights for lamps
       double lampY2 = tableHeight / 10; // to create a more realistic look
       Material lampMat = new Material().setKD(0.05).setKS(0.9).setNShininess(200).setKT(0.6);

       for (double lampY : new double[]{lampY1, lampY2}) {
           Point lampStart = new Point(-lampLength / 2, lampY, lampZ);
           Vector lampDir = new Vector(1, 0, 0);
           scene.geometries.add(
               new Cylinder(lampRadius, new Ray(lampStart, lampDir), lampLength)
                   .setEmission(new Color(220, 220, 255))
                   .setMaterial(lampMat)
           );
           // Add white point lights along the lamp
           for (int i = 0; i < 5; i++) {
               double frac = (i + 0.5) / 5.0;
               Point lightPos = lampStart.add(lampDir.scale(lampLength * frac));
               scene.lights.add(
                   new PointLight(new Color(200, 200, 200), lightPos)
                       .setKL(0.0005).setKQ(0.0001)
               );
           }
       }

       // Camera setup
       Camera camera = Camera.getBuilder()
               .setRayTracer(scene, RayTracerType.SIMPLE)
               .setLocation(new Point(0, -600, 200))
               .setDirection(new Point(0, 0, boardZ), new Vector(0, 1, 0))
               .setVpSize(400, 400)
               .setVpDistance(700)
               .setResolution(800, 800)
               .build();

       camera.renderImage()
               .writeToImage("chessboardOnGlassTableWithFluorescent");
   }


    @Test
    void chessboardOnGlassTableWithFluorescent2() {
        Scene scene = new Scene("Chessboard Glass Table")
                .setAmbientLight(new AmbientLight(new Color(30, 30, 30)));

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

        // שולחן זכוכית
        double tableWidth = size * squareSize + 40;
        double tableHeight = size * squareSize + 40;
        double tableZ = boardZ;
        Color glassColor = new Color(180, 220, 255).reduce(2);
        Material glassMat = new Material()
                .setKD(0.1).setKS(0.9).setNShininess(300).setKT(0.8);

        Point t1 = new Point(-tableWidth / 2, -tableHeight / 2, tableZ);
        Point t2 = new Point(tableWidth / 2, -tableHeight / 2, tableZ);
        Point t3 = new Point(tableWidth / 2, tableHeight / 2, tableZ);
        Point t4 = new Point(-tableWidth / 2, tableHeight / 2, tableZ);
        scene.geometries.add(
                new Polygon(t1, t2, t3, t4)
                        .setEmission(glassColor)
                        .setMaterial(glassMat)
        );

        // רגליים פחות שקופות
        double legRadius = 5;
        double legHeight = 80;
        double legZ0 = tableZ - legHeight;
        Material legMat = new Material().setKD(0.1).setKS(0.6).setNShininess(100).setKT(0.3); // פחות KT

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

        // רצפה – פחות מבריקה, יותר כהה, קצת יותר נמוכה
        Material floorMat = new Material()
                .setKD(0.3).setKS(0.7).setNShininess(150).setKR(0.5); // פחות מבריק
        scene.geometries.add(
                new Plane(new Point(0, 0, legZ0 - 5), new Vector(0, 0, 1))
                        .setEmission(new Color(120, 0, 0))
                        .setMaterial(floorMat)
        );

        // פלורסנטים
        double lampRadius = 4;
        double lampLength = tableWidth * 0.8;
        double lampZ = tableZ + 200;
        double lampY1 = -tableHeight / 10;
        double lampY2 = tableHeight / 10;
        Material lampMat = new Material().setKD(0.05).setKS(0.9).setNShininess(200).setKT(0.6);

        for (double lampY : new double[]{lampY1, lampY2}) {
            Point lampStart = new Point(-lampLength / 2, lampY, lampZ);
            Vector lampDir = new Vector(1, 0, 0);
            scene.geometries.add(
                    new Cylinder(lampRadius, new Ray(lampStart, lampDir), lampLength)
                            .setEmission(new Color(220, 220, 255))
                            .setMaterial(lampMat)
            );
            for (int i = 0; i < 5; i++) {
                double frac = (i + 0.5) / 5.0;
                Point lightPos = lampStart.add(lampDir.scale(lampLength * frac));
                scene.lights.add(
                        new PointLight(new Color(200, 200, 200), lightPos)
                                .setKL(0.0005).setKQ(0.0001)
                );
            }
        }

        // שני כיסאות חומים פשוטים בצדדים
        Color chairColor = new Color(100, 50, 20);
        Material chairMat = new Material().setKD(0.6).setKS(0.2).setNShininess(30);
        double seatWidth = 25, seatDepth = 25, seatHeight = 10;
        double chairZ = legZ0 + 10;
        double chairY = tableHeight / 2 + 20;

        for (int dx : new int[]{-1, 1}) {
            double x0 = dx * (tableWidth / 2 - 20);
            Point c1 = new Point(x0, chairY, chairZ);
            Point c2 = new Point(x0 + seatWidth, chairY, chairZ);
            Point c3 = new Point(x0 + seatWidth, chairY + seatDepth, chairZ);
            Point c4 = new Point(x0, chairY + seatDepth, chairZ);
            scene.geometries.add(
                    new Polygon(c1, c2, c3, c4)
                            .setEmission(chairColor)
                            .setMaterial(chairMat)
            );
        }
        // Blue carpet just above the red floor
        double carpetZ = legZ0 - 4.5; // Slightly above the red floor
        double carpetWidth = tableWidth + 60;
        double carpetHeight = tableHeight + 60;
        Color carpetColor = new Color(30, 60, 180);
        Material carpetMat = new Material().setKD(0.7).setKS(0.1).setNShininess(20);

        Point carpet1 = new Point(-carpetWidth / 2, -carpetHeight / 2, carpetZ);
        Point carpet2 = new Point(carpetWidth / 2, -carpetHeight / 2, carpetZ);
        Point carpet3 = new Point(carpetWidth / 2, carpetHeight / 2, carpetZ);
        Point carpet4 = new Point(-carpetWidth / 2, carpetHeight / 2, carpetZ);

        scene.geometries.add(
            new Polygon(carpet1, carpet2, carpet3, carpet4)
                .setEmission(carpetColor)
                .setMaterial(carpetMat)
        );
        // מצלמה
        Camera camera = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, -600, 200))
                .setDirection(new Point(0, 0, boardZ), new Vector(0, 1, 0))
                .setVpSize(400, 400)
                .setVpDistance(700)
                .setResolution(800, 800)
                .build();

        camera.renderImage()
                .writeToImage("chessboardOnGlassTableWithFluorescent2");
    }
    @Test
    void chessboardOnGlassTableWithFluorescent3() {
        Scene scene = new Scene("Chessboard Glass Table")
                .setAmbientLight(new AmbientLight(new Color(30, 30, 30)));

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
        double tableZ = boardZ;
        Color glassColor = new Color(180, 220, 255).reduce(2);
        Material glassMat = new Material()
                .setKD(0.1).setKS(0.9).setNShininess(300).setKT(0.5); // half transparent

        Point t1 = new Point(-tableWidth / 2, -tableHeight / 2, tableZ);
        Point t2 = new Point(tableWidth / 2, -tableHeight / 2, tableZ);
        Point t3 = new Point(tableWidth / 2, tableHeight / 2, tableZ);
        Point t4 = new Point(-tableWidth / 2, tableHeight / 2, tableZ);
        scene.geometries.add(
                new Polygon(t1, t2, t3, t4)
                        .setEmission(glassColor)
                        .setMaterial(glassMat)
        );

        // Table legs
        double legRadius = 5;
        double legHeight = 80;
        double legZ0 = tableZ - legHeight;
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

        // Floor - shiny, reflective, much lower so it doesn't hide anything
        double floorZ = legZ0 - 50;
        Material floorMat = new Material()
                .setKD(0.3).setKS(0.7).setNShininess(150).setKR(0.4); // reflective
        scene.geometries.add(
                new Plane(new Point(0, 0, floorZ), new Vector(0, 0, 1))
                        .setEmission(new Color(120, 0, 0))
                        .setMaterial(floorMat)
        );

        // Blue carpet - just above the floor, not shiny, not specular, receives shadows
        double carpetZ = floorZ + 0.5;
        double carpetWidth = tableWidth + 60;
        double carpetHeight = tableHeight + 60;
        Color carpetColor = new Color(30, 60, 180);
        Material carpetMat = new Material().setKD(0.9).setKS(0.0).setNShininess(1); // matte, no specular

        Point carpet1 = new Point(-carpetWidth / 2, -carpetHeight / 2, carpetZ);
        Point carpet2 = new Point(carpetWidth / 2, -carpetHeight / 2, carpetZ);
        Point carpet3 = new Point(carpetWidth / 2, carpetHeight / 2, carpetZ);
        Point carpet4 = new Point(-carpetWidth / 2, carpetHeight / 2, carpetZ);

        scene.geometries.add(
            new Polygon(carpet1, carpet2, carpet3, carpet4)
                .setEmission(carpetColor)
                .setMaterial(carpetMat)
        );

        // Fluorescent lamps
        double lampRadius = 4;
        double lampLength = tableWidth * 0.8;
        double lampZ = tableZ + 200;
        double lampY1 = -tableHeight / 10;
        double lampY2 = tableHeight / 10;
        Material lampMat = new Material().setKD(0.05).setKS(0.9).setNShininess(200).setKT(0.6);

        for (double lampY : new double[]{lampY1, lampY2}) {
            Point lampStart = new Point(-lampLength / 2, lampY, lampZ);
            Vector lampDir = new Vector(1, 0, 0);
            scene.geometries.add(
                    new Cylinder(lampRadius, new Ray(lampStart, lampDir), lampLength)
                            .setEmission(new Color(220, 220, 255))
                            .setMaterial(lampMat)
            );
            for (int i = 0; i < 5; i++) {
                double frac = (i + 0.5) / 5.0;
                Point lightPos = lampStart.add(lampDir.scale(lampLength * frac));
                scene.lights.add(
                        new PointLight(new Color(200, 200, 200), lightPos)
                                .setKL(0.0005).setKQ(0.0001)
                );
            }
        }

        // Two brown chairs
        Color chairColor = new Color(100, 50, 20);
        Material chairMat = new Material().setKD(0.6).setKS(0.2).setNShininess(30);
        double seatWidth = 25, seatDepth = 25, seatHeight = 10;
        double chairZ = legZ0 + 10;
        double chairY = tableHeight / 2 + 20;

        for (int dx : new int[]{-1, 1}) {
            double x0 = dx * (tableWidth / 2 - 20);
            Point c1 = new Point(x0, chairY, chairZ);
            Point c2 = new Point(x0 + seatWidth, chairY, chairZ);
            Point c3 = new Point(x0 + seatWidth, chairY + seatDepth, chairZ);
            Point c4 = new Point(x0, chairY + seatDepth, chairZ);
            scene.geometries.add(
                    new Polygon(c1, c2, c3, c4)
                            .setEmission(chairColor)
                            .setMaterial(chairMat)
            );
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
                .writeToImage("chessboardOnGlassTableWithFluorescent3");
    }

    @Test
    void chessboardOnGlassTable_Final2() {
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
        double tableZ = boardZ;
        Color glassColor = new Color(180, 220, 255).reduce(2);
        Material glassMat = new Material()
                .setKD(0.1).setKS(0.9).setNShininess(300).setKT(0.7);

        Point t1 = new Point(-tableWidth / 2, -tableHeight / 2, tableZ);
        Point t2 = new Point(tableWidth / 2, -tableHeight / 2, tableZ);
        Point t3 = new Point(tableWidth / 2, tableHeight / 2, tableZ);
        Point t4 = new Point(-tableWidth / 2, tableHeight / 2, tableZ);
        scene.geometries.add(
            new Polygon(t1, t2, t3, t4)
                .setEmission(glassColor)
                .setMaterial(glassMat)
        );

        // Table legs
        double legRadius = 5;
        double legHeight = 80;
        double legZ0 = tableZ - legHeight;
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

        // Red shiny floor (large square)
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

        // big wall in the back (black, shiny) very distnsing from the camara and the nirmal is oppiste to the camera (plane
        Color wallColor = new Color(0, 0, 0);
        Material wallMat = new Material().setKD(0.1).setKS(0.2).setNShininess(300);
        scene.geometries.add(
            new Plane(
                new Point(0, 200, -300), // far away from the table
                new Vector(0, 0, -1)
            ).setEmission(wallColor).setMaterial(wallMat));



        // Blue carpet (smaller, above floor)
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

        // Two large chairs, one behind and one in front of the table, both facing the table
        double chairSeatWidth = 90, chairSeatDepth = 90, chairSeatHeight = 8;
        double chairLegHeight = 50;
        double chairLegRadius = 5;
        double chairZ = tableZ - chairLegHeight;
        double backrestHeight = 30;
        Color legColor = new Color(20, 20, 20);
        Color backrestColor = new Color(100, 50, 20);
        Material legMatChair = new Material().setKD(0.2).setKS(0.5).setNShininess(50);
        Material backrestMat = new Material().setKD(0.6).setKS(0.2).setNShininess(30);
        Material seatMat = new Material().setKD(0.5).setKS(0.2).setNShininess(20);

        // Chair positions: one behind, one in front
        double[] chairY = { tableHeight / 2 + 60, -tableHeight / 2 - chairSeatDepth - 60 };
        double[] chairAngle = { 0, 180 }; // 0: facing forward, 180: facing backward

        for (int i = 0; i < 2; i++) {
            double x0 = -chairSeatWidth / 2;
            double y0 = chairY[i];
            double angle = Math.toRadians(chairAngle[i]);
            // Center of table for rotation
            double cx = 0, cy = 0;
            // Rotate chair to face table
            double cosA = Math.cos(angle), sinA = Math.sin(angle);

            // Helper to rotate a point around the table center
            java.util.function.BiFunction<Double, Double, Point> rotate = (x, y) ->
                new Point(
                    cx + (x - cx) * cosA - (y - cy) * sinA,
                    cy + (x - cx) * sinA + (y - cy) * cosA,
                    tableZ
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
            // Legs (4)
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


        // Fluorescent lamps (lower, visible)
        double lampRadius = 4;
        double lampLength = tableWidth * 0.8;
        double lampZ = tableZ + 150;
        double lampY1 = -tableHeight / 10;
        double lampY2 = tableHeight / 10;
        Material lampMat = new Material().setKD(0.05).setKS(0.9).setNShininess(200).setKT(0.6);

        for (double lampY : new double[]{lampY1, lampY2}) {
            Point lampStart = new Point(-lampLength / 2, lampY, lampZ);
            Vector lampDir = new Vector(1, 0, 0);
            scene.geometries.add(
                new Cylinder(lampRadius, new Ray(lampStart, lampDir), lampLength)
                    .setEmission(new Color(220, 220, 255))
                    .setMaterial(lampMat)
            );
            for (int i = 0; i < 5; i++) {
                double frac = (i + 0.5) / 5.0;
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
        double tableZ = boardZ;
        Color glassColor = new Color(180, 220, 255).reduce(2);
        Material glassMat = new Material()
                .setKD(0.1).setKS(0.9).setNShininess(300).setKT(0.7);

        Point t1 = new Point(-tableWidth / 2, -tableHeight / 2, tableZ);
        Point t2 = new Point(tableWidth / 2, -tableHeight / 2, tableZ);
        Point t3 = new Point(tableWidth / 2, tableHeight / 2, tableZ);
        Point t4 = new Point(-tableWidth / 2, tableHeight / 2, tableZ);
        scene.geometries.add(
                new Polygon(t1, t2, t3, t4)
                        .setEmission(glassColor)
                        .setMaterial(glassMat)
        );

        // Table legs
        double legRadius = 5;
        double legHeight = 80;
        double legZ0 = tableZ - legHeight;
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
        double chairSeatWidth = 70, chairSeatDepth = 70, chairSeatHeight = 8;
        double chairLegHeight = 50;
        double chairLegRadius = 5;
        double chairZ = tableZ - chairLegHeight;
        double backrestHeight = 30;
        Color legColor = new Color(20, 20, 20);
        Color backrestColor = new Color(100, 50, 20);
        Material legMatChair = new Material().setKD(0.2).setKS(0.5).setNShininess(50);
        Material backrestMat = new Material().setKD(0.6).setKS(0.2).setNShininess(30);
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
                            tableZ
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
        double lampZ = tableZ + 150;
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
            for (int i = 0; i < 5; i++) {
                double frac = (i + 0.5) / 5.0;
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
                .writeToImage("chessboardOnGlassTable_Final2");
    }

}