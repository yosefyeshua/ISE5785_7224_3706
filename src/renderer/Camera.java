package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.MissingResourceException;

public class Camera implements Cloneable{
    private Point p0;
    private Vector vTo;
    private Vector vUp;
    private Vector vRight;

    private double width = 0d;
    private double height = 0d;
    private double distance = 0d;


    private Camera(){}


    public static Builder getBuilder(){
        return new Builder();
    }



    public Ray constructRay(int nX, int nY, int j, int i){return null;}

    public static class Builder{

        private final Camera camera = new Camera();

        public Builder setLocation(Point p0) {
            camera.p0 = p0;
            return this;
        }

        public Builder setDirection(Vector vTo, Vector vUp) {
            if (!Util.isZero(vTo.dotProduct(vUp)))
                throw new IllegalArgumentException("vTo and vUp must be orthogonal");
            camera.vTo = vTo.normalize();
            camera.vUp = vUp.normalize();
            return this;
        }


        public Builder setDirection(Point target, Vector vUp) {
            camera.vTo = target.subtract(camera.p0).normalize();
            camera.vRight = camera.vTo.crossProduct(vUp).normalize();
            camera.vUp = camera.vRight.crossProduct(camera.vTo).normalize();
            return this;
        }

        public Builder setDirection(Point target) {
            camera.vUp = Vector.AXIS_Y;
            camera.vTo = target.subtract(camera.p0).normalize();
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            return this;
        }

        public Builder setVpSize(double width, double height) {
            if (width<=0 || height<=0)
                throw new IllegalArgumentException("Width and height must be positive");
            camera.width = width;
            camera.height = height;
            return this;
        }

        public Builder setVpDistance(double distance) {
            if (distance<=0)
                throw new IllegalArgumentException("Distance must be positive");
            camera.distance = distance;
            return this;
        }


        public Builder setResolution(int nX, int nY) {
            return this;
        }



        public Camera build(){
            String problem = "Missing render data";
            String name = "Camera";
            if (camera.p0 == null)
                throw new MissingResourceException(problem,name,"camera location");
            if (camera.vTo == null)
                throw new MissingResourceException(problem,name,"camera direction");
            if (camera.vUp == null)
                throw new MissingResourceException(problem,name,"camera up");
            if (camera.width == 0 || camera.height == 0)
                throw new MissingResourceException(problem,name,"camera size");
            if (camera.distance == 0)
                throw new MissingResourceException(problem,name,"camera distance");
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
