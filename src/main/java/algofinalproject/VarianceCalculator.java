package algofinalproject;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class VarianceCalculator {
    public static double compute(PixelReader reader, int x, int y, int width, int height) {
        int pixelCount = width * height;
        double sum = 0;

        // compute mean brightness
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                Color color = reader.getColor(i, j);
                double brightness = 
                    (color.getRed()
                    + color.getGreen()
                    + color.getBlue()) / 3.0;
                sum += brightness;
            }
        }
        double mean = sum / pixelCount;

        // compute variance
        double varianceSum = 0;
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                Color color = reader.getColor(i, j);
                double brightness = 
                    (color.getRed()
                    + color.getGreen()
                    + color.getBlue()) / 3.0;
                varianceSum += Math.pow(brightness - mean, 2);
            }
        }
        return varianceSum / pixelCount;
    }
}
