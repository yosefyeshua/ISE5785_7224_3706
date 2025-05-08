package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import static org.junit.jupiter.api.Assertions.*;

class ImageWriterTest {

    @Test
    void firstImageTest() {
        ImageWriter IW = new ImageWriter(800, 500);
        Color yellow = new Color(255, 255, 0);
        Color red = new Color(255, 0, 0);

        for (int i = 0; i < IW.nX(); ++i)
        {
            for (int j = 0; j < IW.nY(); ++j)
            {
                if (i % 50 != 0 && j % 50 != 0)
                    IW.writePixel(i, j, yellow);
                else
                    IW.writePixel(i, j, red);
            }
        }
        IW.writeToImage("test");
    }
}