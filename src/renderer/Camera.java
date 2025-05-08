package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.MissingResourceException;

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
     * Constructs a ray from the camera through a specific pixel on the view plane.
     *
     * @param nX number of columns (horizontal resolution)
     * @param nY number of rows (vertical resolution)
     * @param j  pixel column index (0-based from left)
     * @param i  pixel row index (0-based from top)
     * @return a {@link Ray} from the camera through the specified pixel
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        Point pij = pIJ;
        double rY = height / nY;
        double rX = width / nX;
        double xJ = (j - (nX - 1) / 2.0) * rX;
        double yI = -(i - (nY - 1) / 2.0) * rY;

        if (!Util.isZero(xJ))
            pij = pij.add(vRight.scale(xJ));
        if (!Util.isZero(yI))
            pij = pij.add(vUp.scale(yI));
        return new Ray(p0, pij.subtract(p0));
    }

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
     * Builder class for constructing {@link Camera} instances using a fluent API.
     */
    public static class Builder {
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
            // Currently unused — can be added later
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

            camera.pIJ = camera.p0.add(camera.vTo.scale(camera.distance));

            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
