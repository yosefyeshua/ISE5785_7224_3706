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

    @Test
    void SECImageTest() {
        ImageWriter IW = new ImageWriter(800, 500);
        int k=0;
        int l=0;
        for (int i = 0; i < IW.nX(); ++i)
        {
            for (int j = 0; j < IW.nY(); ++j)
            {
                if (i % 50 >= 25 && j % 50>=25)
                    IW.writePixel(i, j, new Color(255-i%255, 255- j%255, l));
                else
                    IW.writePixel(i, j, new Color(j%255, i%255, k));

                k +=1;
                l+=2;
                if (k>=255)
                    k=0;
                if (l>=255)
                    l=0;
            }
        }
        IW.writeToImage("test22");
    }

}