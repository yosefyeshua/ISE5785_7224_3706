package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

/**
 * {@code Geometry} is an abstract class representing a geometric shape or object.
 * It serves as the foundation for defining geometric entities and enforces
 * the implementation of a method to compute normal vectors at given points.
 */
public abstract class Geometry extends Intersectable {

    /**
     * The emission color of the geometry.
     * Used to simulate light-emitting surfaces like lamps or glowing objects.
     */
    protected Color emission = Color.BLACK;
    /**
     * The material properties of the geometry, which affect how it interacts with light.
     */
    private Material material = new Material();

    /**
     * Returns the emission color of the geometry.
     *
     * @return the emission color
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Sets the emission color of the geometry.
     *
     * @param emission the new emission color
     * @return this geometry instance for method chaining
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }


    /**
     * Returns the material properties of the geometry.
     *
     * @return the material
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the material properties of the geometry.
     *
     * @param material the new material to assign
     * @return this geometry instance for method chaining
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }


    /**
     * Computes and returns the normal vector at a given point on the geometric object.
     * The normal vector is typically a unit vector perpendicular to the surface at the given point.
     *
     * @param point the point on the geometry at which to compute the normal
     * @return the normal vector at the specified point
     */
    public abstract Vector getNormal(Point point);
}
