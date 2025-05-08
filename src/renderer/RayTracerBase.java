package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

public abstract class RayTracerBase {
    protected final Scene scene;

    RayTracerBase(Scene scene) { this.scene = scene; }

    public abstract Color traceRay(Ray ray);
}
