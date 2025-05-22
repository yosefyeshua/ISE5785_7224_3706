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

    protected Color emission = Color.BLACK;
    private Material material = new Material();

    public Color getEmission() { return emission; }
    public Geometry setEmission(Color emission) { this.emission = emission; return this;}

    public Material getMaterial() { return material; }
    public Geometry setMaterial(Material material) { this.material = material; return this;}
    
    /**
     * Computes and returns the normal vector at a given point on the geometric object.
     * The normal vector is typically a unit vector perpendicular to the surface at the given point.
     *
     * @param point the point on the geometry at which to compute the normal
     * @return the normal vector at the specified point
     */
    public abstract Vector getNormal(Point point);
}
