package scene;

import geometries.Geometries;
import geometries.Geometry;
import lighting.AmbientLight;
import primitives.Color;

public class Scene {
    public String name;
    public Color background = Color.BLACK;
    public AmbientLight ambientLight = AmbientLight.NONE;
    public Geometries geometries = new Geometries();

    Scene(String name) { this.name = name; }

    Scene setBackground(Color background) { this.background = background; return this;}
    Scene setAmbientLight(AmbientLight ambientLight) { this.ambientLight = ambientLight; return this; }
    Scene setGeometries(Geometries geometries) { this.geometries = geometries; return this; }
}
