package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.MissingResourceException;

/**
 * Camera class represents a virtual camera in 3D space for rendering scenes.
 * It defines the position, orientation, view plane size, and resolution.
 */
public class Camera implements Cloneable {
    private Point p0;
    private Vector vTo;
    private Vector vUp;
    private Vector vRight;
    private double width;
    private double height;
    private double distance;

    /**
     * Constructs a ray through a specific pixel on the view plane.
     *
     * @param nX number of columns (width resolution)
     * @param nY number of rows (height resolution)
     * @param j  pixel column index
     * @param i  pixel row index
     * @return Ray from the camera through the pixel
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        Point pIJ = p0.add(vTo.scale(distance));
        double rY = height / nY;
        double rX = width / nX;
        double xJ = (j - (nX - 1) / 2.0) * rX;
        double yI = -(i - (nY - 1) / 2.0) * rY;
        if (xJ != 0)
            pIJ = pIJ.add(vRight.scale(xJ));
        if (yI != 0)
            pIJ = pIJ.add(vUp.scale(yI));
        return new Ray(p0, pIJ.subtract(p0));
    }

    /**
     * Returns the width of the view plane.
     * @return width
     */
    public double getWidth() {
        return width;
    }

    /**
     * Returns the height of the view plane.
     * @return height
     */
    public double getHeight() {
        return height;
    }

    /**
     * Returns a new Camera builder.
     * @return Camera.Builder instance
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Builder class for constructing Camera instances with a fluent API.
     */
    public static class Builder {
        private final Camera camera = new Camera();

        /**
         * Sets the camera location.
         * @param p0 the camera position
         * @return this builder
         */
        public Builder setLocation(Point p0) {
            camera.p0 = p0;
            return this;
        }

        /**
         * Sets the camera direction and up vector.
         * @param vTo direction vector
         * @param vUp up vector
         * @return this builder
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            if (!primitives.Util.isZero(vTo.dotProduct(vUp)))
                throw new IllegalArgumentException("vTo and vUp must be orthogonal");
            camera.vTo = vTo.normalize();
            camera.vUp = vUp.normalize();
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            return this;
        }

        /**
         * Sets the camera direction towards a target point and up vector.
         * @param target target point
         * @param vUp up vector
         * @return this builder
         */
        public Builder setDirection(Point target, Vector vUp) {
            camera.vTo = target.subtract(camera.p0).normalize();
            camera.vRight = camera.vTo.crossProduct(vUp).normalize();
            camera.vUp = camera.vRight.crossProduct(camera.vTo).normalize();
            return this;
        }

        /**
         * Sets the camera direction towards a target point (default up).
         * @param target target point
         * @return this builder
         */
        public Builder setDirection(Point target) {
            camera.vUp = Vector.AXIS_Y;
            camera.vTo = target.subtract(camera.p0).normalize();
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            camera.vUp = camera.vRight.crossProduct(camera.vTo).normalize();
            return this;
        }

        /**
         * Sets the view plane size.
         * @param width view plane width
         * @param height view plane height
         * @return this builder
         */
        public Builder setVpSize(double width, double height) {
            if (width <= 0 || height <= 0)
                throw new IllegalArgumentException("Width and height must be positive");
            camera.width = width;
            camera.height = height;
            return this;
        }

        /**
         * Sets the distance from the camera to the view plane.
         * @param distance distance to view plane
         * @return this builder
         */
        public Builder setVpDistance(double distance) {
            if (distance <= 0)
                throw new IllegalArgumentException("Distance must be positive");
            camera.distance = distance;
            return this;
        }

        /**
         * Sets the resolution of the view plane.
         * @param nX number of columns
         * @param nY number of rows
         * @return this builder
         */
        public Builder setResolution(int nX, int nY) {
            // Implementation can be added if needed
            return this;
        }

        /**
         * Builds and returns the Camera instance.
         * @return a new Camera object
         * @throws MissingResourceException if required parameters are missing
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

            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}