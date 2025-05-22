package primitives;

public class Material {
    public Double3 kA = Double3.ONE;

    public Material() {};

    public Material(Double3 intensity) {
        this.kA = intensity;
    }

    public Material(double intensity) {
        this.kA = new Double3(intensity);
    }

}
