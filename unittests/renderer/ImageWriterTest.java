package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import static org.junit.jupiter.api.Assertions.*;

class ImageWriterTest {

    @Test
    void firstImageTest() {
        ImageWriter IW = new ImageWriter(800, 500);

        for (int i = 0; i < IW.nX(); ++i)
        {
            for (int j = 0; j < IW.nY(); ++j)
            {
                if (i % 50 != 0 && j % 50 != 0)
                    IW.writePixel(i, j, Color.YELLOW);
                else
                    IW.writePixel(i, j, Color.RED);
            }
        }
        IW.writeToImage("test");
    }
}