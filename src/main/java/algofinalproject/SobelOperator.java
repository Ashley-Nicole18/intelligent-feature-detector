package algofinalproject;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class SobelOperator {
    //sobel kernels: measure intensity changes across pixels
    //SOBEL_X - detects horizontal intensity changes (corresponds to vertical edges)
    private static final int[][] SOBEL_X = {
        {-1, 0, 1},
        {-2, 0, 2},
        {-1, 0, 1}
    };
    //SOBEL_Y - detects vertical intensity changes (horizontal edges)
    private static final int[][] SOBEL_Y = {
        {-1, -2, -1},
        {0, 0, 0},
        {1, 2, 1}
    };
    //apply sobel inside a region
    public static void apply(PixelReader reader, PixelWriter writer, QuadtreeNode node) {
        int x = node.getX();
        int y = node.getY();
        int width = node.getWidth();
        int height = node.getHeight();
        // precompute grayscale buffer - creates 2d arr to store grayscale values
        int[][] grayBuffer = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Color c = reader.getColor(x + i, y + j);
                grayBuffer[i][j] = (int)((c.getRed() + c.getGreen() + c.getBlue()) / 3.0 * 255);
            }
        }
        // apply sobel convolution
        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                int gx = 0, gy = 0;

                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        int pixel = grayBuffer[i + dx][j + dy]; 
                        gx += pixel * SOBEL_X[dy + 1][dx + 1];
                        gy += pixel * SOBEL_Y[dy + 1][dx + 1];
                    }
                }
                // highlight strong edges where intensity changes sharply
                int magnitude = (int)Math.min(255, Math.sqrt(gx * gx + gy * gy));
                writer.setColor(x + i, y + j, Color.grayRgb(magnitude));
            }
        }
    }
}