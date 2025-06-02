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

}