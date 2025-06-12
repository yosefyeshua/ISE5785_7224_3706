package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;
import scene.Scene;

import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.stream.*;


/**
 * The {@code Camera} class represents a virtual camera in 3D space.
 * <p>
 * It defines the camera's position and orientation, and provides functionality
 * for constructing rays through pixels on a view plane for rendering purposes.
 * This class uses a builder pattern to configure the camera fluently and safely.
 */
public class Camera implements Cloneable {

    /**
     * Camera position
     */
    private Point p0;

    /**
     * Forward direction vector
     */
    private Vector vTo;

    /**
     * Up direction vector
     */
    private Vector vUp;

    /**
     * Right direction vector (computed)
     */
    private Vector vRight;

    /**
     * View plane width
     */
    private double width;

    /**
     * View plane height
     */
    private double height;

    /**
     * Distance from camera to view plane
     */
    private double distance;

    /**
     * Center of the view plane
     */
    private Point pIJ;

    /**
     * The {@link ImageWriter} used to write pixel data.
     */
    private ImageWriter imageWriter;

    /**
     * The ray tracer that determines the color for each ray.
     */
    private RayTracerBase rayTracer;

    /**
     * The horizontal resolution of the image (number of pixels along X-axis).
     */
    private int nX = 1;

    /**
     * The vertical resolution of the image (number of pixels along Y-axis).
     */
    private int nY = 1;

    // DEPTH OF FIELD
    /**
     * The distance from the camera to the focal plane for depth of field.
     */
    private double focalDistance = 0;
    /**
     * The radius of the aperture for depth of field.
     */
    private double apertureRadius = 0;
    /**
     * The number of samples for depth of field.
     */
    private int dofSamples = 0;

    /**
     * The number of anti-aliasing samples per pixel.
     */
    private int aaSamples = 1;

    /**
    * Flag for adaptive super sampling
    */
    private boolean adaptiveSuperSampling = false;

    //MT
    /** Amount of threads to use fore rendering image by the camera */
    private int threadsCount = 0;
    /**
     * Amount of threads to spare for Java VM threads:<br>
     * Spare threads if trying to use all the cores
     */
    private static final int SPARE_THREADS = 2;
    /**
     * Debug print interval in seconds (for progress percentage)<br>
     * if it is zero - there is no progress output
     */
    private double printInterval = 0;
    /**
     * Pixel manager for supporting:
     * <ul>
     * <li>multi-threading</li>
     * <li>debug print of progress percentage in Console window/tab</li>
     * </ul>
     */
    private PixelManager pixelManager;


    /**
     * Returns the width of the view plane.
     *
     * @return the view plane width
     */
    public double getWidth() {
        return width;
    }

    /**
     * Returns the height of the view plane.
     *
     * @return the view plane height
     */

    public double getHeight() {
        return height;
    }


    /**
     * Returns a new {@link Builder} instance for creating a {@code Camera} using a fluent API.
     *
     * @return a new {@link Camera.Builder}
     */
    public static Builder getBuilder() {
        return new Builder();
    }


    /**
     * Render image using multi-threading by parallel streaming
     * @return the camera object itself
     */
    private Camera renderImageStream() {
        IntStream.range(0, nY).parallel()
                .forEach(i -> IntStream.range(0, nX).parallel()
                        .forEach(j -> castRay(j, i)));
        return this;
    }
    /**
     * Render image without multi-threading
     * @return the camera object itself
     */
    private Camera renderImageNoThreads() {
        for (int i = 0; i < nY; ++i)
            for (int j = 0; j < nX; ++j)
                castRay(j, i);
        return this;
    }
    /**
     * Render image using multi-threading by creating and running raw threads
     * @return the camera object itself
     */
    private Camera renderImageRawThreads() {
        var threads = new LinkedList<Thread>();
        while (threadsCount-- > 0)
            threads.add(new Thread(() -> {
                PixelManager.Pixel pixel;
                while ((pixel = pixelManager.nextPixel()) != null)
                    castRay(pixel.col(), pixel.row());
            }));
        for (var thread : threads) thread.start();
        try {
            for (var thread : threads) thread.join();
        } catch (InterruptedException ignored) {}
        return this;
    }

    /** This function renders image's pixel color map from the scene
     * included in the ray tracer object
     * @return the camera object itself
     */
    public Camera renderImage() {
        pixelManager = new PixelManager(nY, nX, printInterval);
        return switch (threadsCount) {
            case 0 -> renderImageNoThreads();
            case -1 -> renderImageStream();
            default -> renderImageRawThreads();
        };
    }


    /**
     * Draws a grid over the rendered image.
     *
     * @param interval spacing of the grid lines
     * @param color color of the grid
     * @return the camera instance for chaining
     */
    public Camera printGrid(int interval, Color color) {
        for (int i = 0; i < nX; i++) {
            for (int j = 0; j < nY; j++) {
                if (i % interval == 0 || j % interval == 0) {
                    imageWriter.writePixel(j, i, color);
                }
            }
        }
        return this;
    }

    /**
     * Saves the image to a file.
     *
     * @param filename name of the file
     */
    public Camera writeToImage(String filename) {
        imageWriter.writeToImage(filename);
        return this;
    }


    /**
     * Constructs multiple rays from the aperture toward the focal point.
     *
     * @param ray The primary ray through the view plane pixel.
     * @return List of rays from random aperture positions toward the focal point.
     */
    private List<Ray> constructDofRays(Ray ray) {
        List<Ray> rays = new LinkedList<>();
            double t = focalDistance / vTo.dotProduct(ray.getDirection());
        Point focalPoint = ray.getPoint(t);

        for (int i = 0; i < dofSamples; i++) {
                Point aperturePoint = p0.getRandomRadialPoint(vRight,vUp,apertureRadius);
            Vector dir = focalPoint.subtract(aperturePoint).normalize();
            rays.add(new Ray(aperturePoint, dir));
        }
        return rays;
    }


    /**
     * Calculates the color for a ray with depth of field effect.
     *
     * @param ray The primary ray through the view plane pixel.
     * @return The averaged color from multiple rays through the aperture.
     */
    private Color calcDOFcolor(Ray ray) {
        List<Ray> rays = constructDofRays(ray);
        Color dofColor = Color.BLACK;
        for (Ray r : rays) {
            dofColor = dofColor.add(rayTracer.traceRay(r));
        }
        return dofColor.scale(1.0 / rays.size());
    }


    /**
     * Constructs a ray from the camera through a specific pixel on the view plane.
     *
     * @param nX number of columns (horizontal resolution)
     * @param nY number of rows (vertical resolution)
     * @param j  pixel column index (0-based from left)
     * @param i  pixel row index (0-based from top)
     * @return a {@link Ray} from the camera through the specified pixel
     */
    public Ray constructRay(int nX, int nY, int j, int i){
        return constructRay(nX, nY, j, i, 0,0);
    }

    /**
     * Constructs a ray from the camera through a specific pixel on the view plane.
     *
     * @param nX number of columns (horizontal resolution)
     * @param nY number of rows (vertical resolution)
     * @param j  pixel column index (0-based from left)
     * @param i  pixel row index (0-based from top)
     * @param xOff offset in the X direction for anti-aliasing and adaptive super sampling
     * @param yOff offset in the Y direction for anti-aliasing and adaptive super sampling
     * @return a {@link Ray} from the camera through the specified pixel
     */
    public Ray constructRay(int nX, int nY, int j, int i, double xOff, double yOff){
        Point pij = pIJ;
        double rY = height / nY;
        double rX = width / nX;
        double xJ = (j + xOff - (nX - 1) / 2.0) * rX;
        double yI = -(i + yOff - (nY - 1) / 2.0) * rY;
        xJ = xJ + Util.random(-0.5, 0.5) * rX;
        yI = yI + Util.random(-0.5, 0.5) * rY;
        if (!Util.isZero(xJ))
            pij = pij.add(vRight.scale(xJ));
        if (!Util.isZero(yI))
            pij = pij.add(vUp.scale(yI));
        return new Ray(p0, pij.subtract(p0));
    }


    /**
     * Constructs a ray from the camera through a specific pixel on the view plane,
     * using adaptive super sampling for antialiasing.
     *
     * @param i pixel row index (0-based from top)
     * @param j pixel column index (0-based from left)
     * @param depth current recursion depth
     * @param minX minimum X coordinate for adaptive sampling
     * @param maxX maximum X coordinate for adaptive sampling
     * @param minY minimum Y coordinate for adaptive sampling
     * @param maxY maximum Y coordinate for adaptive sampling
     * @return a {@link Color} representing the averaged color of the sampled rays
     */
    private Color adaptiveSuperSampling(int i, int j, int depth, double minX, double maxX, double minY, double maxY) {
        Color c0,c1,c2,c3,c4;
        Ray r0,r1,r2,r3,r4;
        double midX = (minX + maxX) / 2;
        double midY = (minY + maxY) / 2;
        r0 = constructRay(nX, nY, i, j, midX, midY);
        c0 = rayTracer.traceRay(r0);
        if(dofSamples > 0 && apertureRadius > 0 && focalDistance > 0)
            c0 = calcDOFcolor(r0);
        if (depth >= 2) {
            return c0;
        }

        r1 = constructRay(nX, nY, i, j, minX, minY);
        r2 = constructRay(nX, nY, i, j, maxX, minY);
        r3 = constructRay(nX, nY, i, j, minX, maxY);
        r4 = constructRay(nX, nY, i, j, maxX, maxY);

        c1 = rayTracer.traceRay(r1);
        c2 = rayTracer.traceRay(r2);
        c3 = rayTracer.traceRay(r3);
        c4 = rayTracer.traceRay(r4);

        if (c1.equals(c0) && c2.equals(c0) && c3.equals(c0) && c4.equals(c0))
            return c0;
        else {
            if(!c1.equals(c0))
                c1 = adaptiveSuperSampling(i, j, depth + 1, minX, midX, minY, midY);
            if(!c2.equals(c0))
                c2 = adaptiveSuperSampling(i, j, depth + 1, midX, maxX, minY, midY);
            if(!c3.equals(c0))
                c3 = adaptiveSuperSampling(i, j, depth + 1, minX, midX, midY, maxY);
            if(!c4.equals(c0))
                c4 = adaptiveSuperSampling(i, j, depth + 1, midX, maxX, midY, maxY);
        }

        return c0.add(c1,c2, c3, c4).reduce(5);
    }


    /**
     * Shoots a ray through a pixel, optionally applying depth of field.
     *
     * @param column Current column.
     * @param row Current row.
     */
    private void castRay(int column, int row) {
        Color color = Color.BLACK;
        if( adaptiveSuperSampling ) {
            color = adaptiveSuperSampling(row, column, 0, -0.5, 0.5, -0.5, 0.5);
        }
        else {
            Ray ray = constructRay(this.nX, this.nY, row, column);
            for (int s = 0; s < aaSamples; s++) {
                // Use jittered ray construction for AA
                if (aaSamples > 1)
                    ray = constructRay(this.nX, this.nY, row, column, Util.random(-0.5, 0.5), Util.random(-0.5, 0.5));

                if (apertureRadius > 0 && focalDistance > 0 && dofSamples > 1) {
                    color = color.add(calcDOFcolor(ray));
                } else {
                    color = color.add(rayTracer.traceRay(ray));
                }
            }
            color = color.reduce(aaSamples);
        }
        imageWriter.writePixel(row, column, color);
        pixelManager.pixelDone();
    }



    /**
     * Creates and returns a copy of this {@code Camera}.
     *
     * @return a shallow clone of this camera
     * @throws CloneNotSupportedException if cloning is not supported
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Builder class for {@link Camera} objects, using the builder pattern.
     */
    public static class Builder {
        /**
         * Internal camera being constructed
         */
        private final Camera camera = new Camera();

        /**
         * Sets the camera's position in space.
         *
         * @param p0 the position of the camera
         * @return this builder instance for chaining
         */
        public Builder setLocation(Point p0) {
            camera.p0 = p0;
            return this;
        }

        /**
         * Sets the camera's forward and up direction vectors.
         * The vectors must be orthogonal.
         *
         * @param vTo the forward ("to") direction
         * @param vUp the upward direction
         * @return this builder instance for chaining
         * @throws IllegalArgumentException if the vectors are not orthogonal
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            if (!Util.isZero(vTo.dotProduct(vUp)))
                throw new IllegalArgumentException("vTo and vUp must be orthogonal");
            camera.vTo = vTo.normalize();
            camera.vUp = vUp.normalize();
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            return this;
        }

        /**
         * Sets the direction of the camera based on a target point and up vector.
         *
         * @param target the point the camera is looking at
         * @param vUp    the upward direction
         * @return this builder instance for chaining
         */
        public Builder setDirection(Point target, Vector vUp) {
            camera.vTo = target.subtract(camera.p0).normalize();
            camera.vRight = camera.vTo.crossProduct(vUp).normalize();
            camera.vUp = camera.vRight.crossProduct(camera.vTo).normalize();
            return this;
        }

        /**
         * Sets the direction of the camera based on a target point.
         * Uses the default up vector of Y-axis.
         *
         * @param target the point the camera is looking at
         * @return this builder instance for chaining
         */
        public Builder setDirection(Point target) {
            camera.vUp = Vector.AXIS_Y;
            camera.vTo = target.subtract(camera.p0).normalize();
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            camera.vUp = camera.vRight.crossProduct(camera.vTo).normalize();
            return this;
        }

        /**
         * Sets the dimensions of the view plane.
         *
         * @param width  the width of the view plane
         * @param height the height of the view plane
         * @return this builder instance for chaining
         * @throws IllegalArgumentException if width or height is non-positive
         */
        public Builder setVpSize(double width, double height) {
            if (width <= 0 || height <= 0)
                throw new IllegalArgumentException("Width and height must be positive");
            camera.width = width;
            camera.height = height;
            return this;
        }

        /**
         * Sets the distance between the camera and the view plane.
         *
         * @param distance the distance to the view plane
         * @return this builder instance for chaining
         * @throws IllegalArgumentException if distance is non-positive
         */
        public Builder setVpDistance(double distance) {
            if (distance <= 0)
                throw new IllegalArgumentException("Distance must be positive");
            camera.distance = distance;
            return this;
        }

        /**
         * Sets the view plane resolution. (Not yet implemented)
         *
         * @param nX number of columns
         * @param nY number of rows
         * @return this builder instance for chaining
         */
        public Builder setResolution(int nX, int nY) {
            camera.nX = nX;
            camera.nY = nY;
            return this;
        }
        /**
         * Sets the focal distance for depth of field.
         *
         * @param focalDistance the distance to the focal plane
         * @return this builder instance for chaining
         */
        public Builder setFocalDistance(double focalDistance) {
            if (focalDistance <= 0)
                throw new IllegalArgumentException("Focal distance must be positive");
            camera.focalDistance = focalDistance;
            return this;
        }
        /**
         * Sets the aperture radius for depth of field.
         *
         * @param apertureRadius the radius of the aperture
         * @return this builder instance for chaining
         */
        public Builder setApertureRadius(double apertureRadius) {
            if (apertureRadius < 0)
                throw new IllegalArgumentException("Aperture radius must be non-negative");
            camera.apertureRadius = apertureRadius;
            return this;
        }
        /**
         * Sets the number of samples for depth of field.
         *
         * @param dofSamples the number of samples
         * @return this builder instance for chaining
         */
        public Builder setDofSamples(int dofSamples) {
            if (dofSamples < 0)
                throw new IllegalArgumentException("Number of depth of field samples must be non-negative");
            camera.dofSamples = dofSamples;
            return this;
        }


        /**
         * Sets the number of anti-aliasing samples per pixel.
         *
         * @param aaSamples the number of anti-aliasing samples
         * @return this builder instance for chaining
         * @throws IllegalArgumentException if aaSamples is negative
         */
        public Builder setAaSamples(int aaSamples) {
            if (aaSamples < 0)
                throw new IllegalArgumentException("Number of anti-aliasing samples must be non-negative");
            camera.aaSamples = aaSamples;
            return this;
        }

        /**
         * Set multi-threading <br>
         * Parameter value meaning:
         * <ul>
         * <li>-2 - number of threads is number of logical processors less 2</li>
         * <li>-1 - stream processing parallelization (implicit multi-threading) is used</li>
         * <li>0 - multi-threading is not activated</li>
         * <li>1 and more - literally number of threads</li>
         * </ul>
         * @param threads number of threads
         * @return builder object itself
         */
        public Builder setMultithreading(int threads) {
            if (threads < -3)
                throw new IllegalArgumentException("Multithreading parameter must be -2 or higher");
            if (threads == -2) {
                int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
                camera.threadsCount = cores <= 2 ? 1 : cores;
            } else
                camera.threadsCount = threads;
            return this;
        }
        /**
         * Set debug printing interval. If it's zero - there won't be printing at all
         * @param interval printing interval in %
         * @return builder object itself
         */
        public Builder setDebugPrint(double interval) {
            if (interval < 0) throw new IllegalArgumentException("interval parameter must be non-negative");
            camera.printInterval = interval;
            return this;
        }

        /**
         * Sets the adaptive super sampling flag.
         *
         * @param adaptiveSuperSampling true to enable adaptive super sampling, false to disable
         * @return this builder instance for chaining
         */
        public Builder setAdaptiveSuperSampling(boolean adaptiveSuperSampling) {
            camera.adaptiveSuperSampling = adaptiveSuperSampling;
            return this;
        }


        /**
         * Sets the {@link RayTracerBase} implementation for the camera.
         *
         * @param scene the scene to trace
         * @param type the ray tracer type to use
         * @return this builder instance for chaining
         */
        public Builder setRayTracer(Scene scene, RayTracerType type) {
            if (type == RayTracerType.SIMPLE)
                camera.rayTracer = new SimpleRayTracer(scene);
            else
                camera.rayTracer = null;
            return this;
        }

        /**
         * Builds and returns the configured {@link Camera} object.
         *
         * @return the built camera
         * @throws MissingResourceException if required fields are missing or invalid
         */
        public Camera build() {
            String problem = "Missing render data";
            String name = "Camera";
            if (camera.p0 == null)
                throw new MissingResourceException(problem, name, "camera location");
            if (camera.vTo == null)
                throw new MissingResourceException(problem, name, "camera direction");
            if (camera.vUp == null)
                throw new MissingResourceException(problem, name, "camera up");
            if (camera.width == 0 || camera.height == 0)
                throw new MissingResourceException(problem, name, "camera size");
            if (camera.distance == 0)
                throw new MissingResourceException(problem, name, "camera distance");

            if (camera.vRight == null)
                camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

            if (camera.nX <= 0 || camera.nY <= 0)
                throw new IllegalArgumentException("nX and nY must be positive");
            camera.imageWriter = new ImageWriter(camera.nX, camera.nY);

            if (camera.rayTracer == null)
                camera.rayTracer = new SimpleRayTracer(null);

            camera.pIJ = camera.p0.add(camera.vTo.scale(camera.distance));

            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
