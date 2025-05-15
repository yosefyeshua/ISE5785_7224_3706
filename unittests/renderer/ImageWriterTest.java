package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

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
    void secondImageTest() {
        ImageWriter IW = new ImageWriter(800, 500);
        int[][] rainbow = {
                {255, 0, 0},     // Red
                {255, 127, 0},   // Orange
                {255, 255, 0},   // Yellow
                {0, 255, 0},     // Green
                {0, 0, 255},     // Blue
                {75, 0, 130}     // Indigo
        };
        int segments = rainbow.length - 1;

        int centerX = IW.nX() / 2;
        int centerY = IW.nY() / 2;

// Max distance is to one of the corners from the center
        double maxDist = Math.sqrt(centerX * centerX + centerY * centerY);

        for (int i = 0; i < IW.nX(); ++i)
        {
            for (int j = 0; j < IW.nY(); ++j)
            {
                int dx = i - centerX;
                int dy = j - centerY;
                double dist = Math.sqrt(dx * dx + dy * dy);
                double t = dist / maxDist;

                // Clamp t to [0, 1]
                if (t > 1) t = 1;

                double scaledT = t * segments;
                int segment = (int)scaledT;
                if (segment >= segments) segment = segments - 1;

                double localT = scaledT - segment;

                int r1 = rainbow[segment][0];
                int g1 = rainbow[segment][1];
                int b1 = rainbow[segment][2];

                int r2 = rainbow[segment + 1][0];
                int g2 = rainbow[segment + 1][1];
                int b2 = rainbow[segment + 1][2];

                int red   = (int)(r1 + localT * (r2 - r1));
                int green = (int)(g1 + localT * (g2 - g1));
                int blue  = (int)(b1 + localT * (b2 - b1));

                IW.writePixel(i, j, new Color(red, green, blue));
            }
        }

        IW.writeToImage("test2");
    }
}