package primitives;

/**
 * Wrapper class for java.jwt.Color The constructors operate with any
 * non-negative RGB values. The colors are maintained without upper limit of
 * 255. Some additional operations are added that are useful for manipulating
 * light's colors
 * @author Dan Zilberstein
 */
public class Color {
   /**
    * The internal fields maintain RGB components as double numbers from 0 to
    * whatever...
    */
   public final Double3     rgb;

   /* setup of colors */
   /** Black color = (0,0,0) */
   public static final Color BLACK = new Color();
   /** White color = (255,255,255) */
   public static final Color WHITE = new Color(255, 255, 255);
   /** Gray color = (127,127,127) */
   public static final Color GRAY  = new Color(127, 127, 127);
   /** Dark Gray color = (64,64,64) */
   public static final Color DARK_GRAY = new Color(64, 64, 64);
   /** Light Gray color = (192,192,192) */
   public static final Color LIGHT_GRAY = new Color(192, 192, 192);
   /** Pink color = (255,192,203) */
   public static final Color PINK = new Color(255, 192, 203);
   /** Orange color = (255,165,0) */
   public static final Color ORANGE = new Color(255, 165, 0);
   /** Brown color = (165,42,42) */
   public static final Color BROWN = new Color(165, 42, 42);
   /** Light Blue color = (173,216,230) */
   public static final Color LIGHT_BLUE = new Color(173, 216, 230);
   /** Red color = (255,0,0) */
   public static final Color RED = new Color(255, 0, 0);
   /** Green color = (0,255,0) */
   public static final Color GREEN = new Color(0, 255, 0);
   /** Blue color = (0,0,255) */
   public static final Color BLUE = new Color(0, 0, 255);
   /** Dark Blue color = (0,0,139) */
   public static final Color DARK_BLUE = new Color(0, 0, 139);
   /** Light Green color = (144,238,144) */
   public static final Color LIGHT_GREEN = new Color(144, 238, 144);
   /** Light Pink color = (255,182,193) */
   public static final Color LIGHT_PINK = new Color(255, 182, 193);
   /** Light Yellow color = (255,255,224) */
   public static final Color LIGHT_YELLOW = new Color(255, 255, 224);
   /** Cyan color = (0,255,255) */
   public static final Color CYAN = new Color(0, 255, 255);
   /** Magenta color = (255,0,255) */
   public static final Color MAGENTA = new Color(255, 0, 255);
   /** Yellow color = (255,255,0) */
   public static final Color YELLOW = new Color(255, 255, 0);


   /** Default constructor - to generate Black Color (privately) */
   private Color() { rgb = Double3.ZERO; }

   /**
    * Constructor to generate a color according to RGB components Each component
    * in
    * range 0..255 (for printed white color) or more [for lights]
    * @param r Red component
    * @param g Green component
    * @param b Blue component
    */
   public Color(double r, double g, double b) {
      if (r < 0 || g < 0 || b < 0) throw new IllegalArgumentException("Negative color component is illegal");
      rgb = new Double3(r, g, b);
   }

   /**
    * Constructor to generate a color according to RGB components Each component
    * in
    * range 0..255 (for printed white color) or more [for lights]
    * @param rgb triad of Red/Green/Blue components
    */
   private Color(Double3 rgb) {
      if (rgb.d1() < 0 || rgb.d2() < 0 || rgb.d3() < 0)
         throw new IllegalArgumentException("Negative color component is illegal");
      this.rgb = rgb;
   }

   /**
    * Constructor on base of java.awt.Color object
    * @param other java.awt.Color's source object
    */
   public Color(java.awt.Color other) { rgb = new Double3(other.getRed(), other.getGreen(), other.getBlue()); }

   /**
    * Color getter - returns the color after converting it into java.awt.Color
    * object During the conversion any component bigger than 255 is set to 255
    * @return java.awt.Color object based on this Color RGB components
    */
   public java.awt.Color getColor() {
      int ir = (int) rgb.d1();
      int ig = (int) rgb.d2();
      int ib = (int) rgb.d3();
      return new java.awt.Color(ir > 255 ? 255 : ir, ig > 255 ? 255 : ig, ib > 255 ? 255 : ib);
   }

   /**
    * Operation of adding this and one or more other colors (by component)
    * @param  colors one or more other colors to add
    * @return        new Color object which is a result of the operation
    */
   public Color add(Color... colors) {
      double rr = rgb.d1();
      double rg = rgb.d2();
      double rb = rgb.d3();
      for (Color c : colors) {
         rr += c.rgb.d1();
         rg += c.rgb.d2();
         rb += c.rgb.d3();
      }
      return new Color(rr, rg, rb);
   }

   /**
    * Scale the color by a scalar triad per rgb
    * @param  k scale factor per rgb
    * @return   new Color object which is the result of the operation
    */
   public Color scale(Double3 k) {
      if (k.d1() < 0.0 || k.d2() < 0.0 || k.d3() < 0.0)
         throw new IllegalArgumentException("Can't scale a color by a negative number");
      return new Color(rgb.product(k));
   }

   /**
    * Scale the color by a scalar
    * @param  k scale factor
    * @return   new Color object which is the result of the operation
    */
   public Color scale(double k) {
      if (k < 0.0) throw new IllegalArgumentException("Can't scale a color by a negative number");
      return new Color(rgb.scale(k));
   }

   /**
    * Scale the color by (1 / reduction factor)
    * @param  k reduction factor
    * @return   new Color object which is the result of the operation
    */
   public Color reduce(int k) {
      if (k < 1) throw new IllegalArgumentException("Can't scale a color by a by a number lower than 1");
      return new Color(rgb.reduce(k));
   }

   @Override
   public String toString() { return "rgb:" + rgb; }
}
