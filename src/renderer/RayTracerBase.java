package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * Abstract base class for ray tracing strategies.
 * <p>
 * Subclasses must implement the {@link #traceRay(Ray)} method to define how
 * rays are traced and how colors are calculated.
 */
public abstract class RayTracerBase {
    /**
     * The scene containing geometries and lighting information used for ray tracing.
     */
    protected final Scene scene;

    /**
     * Constructs a ray tracer for the given scene.
     *
     * @param scene the {@link Scene} to trace rays through
     */
    RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Traces a ray through the scene and returns the resulting color.
     *
     * @param ray the {@link Ray} to trace
     * @return the {@link Color} resulting from tracing the ray
     */
    public abstract Color traceRay(Ray ray);
}
