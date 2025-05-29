package primitives;

/**
 * The {@code Material} class defines the optical properties of a surface,
 * used in shading and lighting calculations such as ambient, diffuse, and specular reflection.
 */
public class Material {

    /**
     * Ambient reflection coefficient.
     */
    public Double3 kA = Double3.ONE;

    /**
     * Diffuse reflection coefficient.
     */
    public Double3 kD = Double3.ZERO;

    /**
     * Specular reflection coefficient.
     */
    public Double3 kS = Double3.ZERO;

    /**
     * Shininess factor for specular reflection.
     */
    public int nShininess = 0;

    /**
     * Constructs a default material with ambient = 1, diffuse = 0, specular = 0, shininess = 0.
     */
    public Material() {}

    /**
     * Constructs a material with the specified ambient intensity.
     *
     * @param intensity the ambient reflection coefficient
     */
    public Material(Double3 intensity) {
        this.kA = intensity;
    }

    /**
     * Constructs a material with the specified ambient intensity.
     *
     * @param intensity the ambient reflection coefficient
     */
    public Material(double intensity) {
        this.kA = new Double3(intensity);
    }

    /**
     * Sets the diffuse reflection coefficient.
     *
     * @param kD the {@link Double3} representing the diffuse coefficient
     * @return this {@code Material} object for method chaining
     */
    public Material setKD(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Sets the diffuse reflection coefficient.
     *
     * @param kD the double value to set for all channels of the diffuse coefficient
     * @return this {@code Material} object for method chaining
     */
    public Material setKD(double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    /**
     * Sets the specular reflection coefficient.
     *
     * @param kS the {@link Double3} representing the specular coefficient
     * @return this {@code Material} object for method chaining
     */
    public Material setKS(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Sets the specular reflection coefficient.
     *
     * @param kS the double value to set for all channels of the specular coefficient
     * @return this {@code Material} object for method chaining
     */
    public Material setKS(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * Sets the shininess factor for specular highlights.
     *
     * @param nShininess the shininess factor
     * @return this {@code Material} object for method chaining
     */
    public Material setNShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }
}
