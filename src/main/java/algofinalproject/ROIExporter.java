package algofinalproject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;

public class ROIExporter {
    
    // JSON export
    // Format: { "imageName": "...", "complexity": "...", "rois": [ {...}, ... ] }
    public static void toJSON(
        List<QuadtreeNode> rois,
        String imageName,
        String complexityLabel,
        double complexityScore,
        File outpuFile
    ) {
        // build string piece by piece
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("    \"imageName\": \"").append(imageName).append("\",\n");
        sb.append("    \"complexityLabel\": \"").append(complexityLabel).append("\",\n");
        sb.append("    \"complexityScore\": ").append(String.format("%.4f", complexityScore)).append(",\n"); 
        sb.append("    \"totalROIs\": ").append(rois.size()).append(",\n");
        sb.append("    \"rois\": [\n");

        for (int i = 0; i < rois.size(); i++) {
            QuadtreeNode roi = rois.get(i);
            sb.append("    {\n");
            sb.append("      \"id\": ").append(i).append(",\n");
            sb.append("      \"x\": ").append(roi.getX()).append(",\n");
            sb.append("      \"y\": ").append(roi.getY()).append(",\n");
            sb.append("      \"width\": ").append(roi.getWidth()).append(",\n");
            sb.append("      \"height\": ").append(roi.getHeight()).append(",\n");
            sb.append("      \"variance\": ").append(String.format("%.6f", roi.getVariance())).append(",\n");
            sb.append("      \"depth\": ").append(roi.getDepth()).append("\n");
            sb.append("    }");

            if (i < rois.size() - 1) sb.append(",");
            sb.append("\n");
        }

        sb.append("    ]\n");
        sb.append("}\n");

        writeToFile(outpuFile, sb.toString());
    }


    // CSV Export
    public static void toCSV(
        List<QuadtreeNode> rois,
        String imageName,
        String complexityLabel,
        File outputFile
    ) {
        StringBuilder sb = new StringBuilder();

        // Header row 
        sb.append("id, imageName, complexityLabel, x, y, width, height, area, variance, depth\n");

        for (int i = 0; i < rois.size(); i++) {
            QuadtreeNode roi = rois.get(i);
            int area = roi.getWidth() * roi.getHeight();
            sb.append(i).append(",");
            sb.append(imageName).append(",");
            sb.append(complexityLabel).append(",");
            sb.append(roi.getX()).append(",");
            sb.append(roi.getY()).append(",");
            sb.append(roi.getWidth()).append(",");
            sb.append(roi.getHeight()).append(",");
            sb.append(area).append(",");
            sb.append(String.format("%.6f", roi.getVariance())).append(",");
            sb.append(roi.getDepth()).append("\n");
        }

        writeToFile(outputFile, sb.toString());
    }

    // Crops each ROI from the original image and save it as an individual PNG
    public static void toCroppedPNGs(
        List<QuadtreeNode> rois,
        Image source,
        File outputFolder
    ) {
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }

        PixelReader reader = source.getPixelReader();
        int saved = 0;

        for (int i = 0; i < rois.size(); i++) {
            QuadtreeNode roi = rois.get(i);

            int x = roi.getX();
            int y = roi.getY();
            int w = roi.getWidth();
            int h = roi.getHeight();

            // Skip regions too small to be useful 
            if (w < 4 || h < 4) continue;

            WritableImage crop = new WritableImage(w, h);
            PixelWriter writer = crop.getPixelWriter();

            // Copy each pixel from the source image into the crop
            for (int px = 0; px < w; px++) {
                for (int py = 0; py < h; py++) {
                    writer.setColor(px, py, reader.getColor(x + px, y + py));
                }
            }

            // Build a filename with namimg convention
            String filename = String.format("roi_%04d_x%d_y%d_%dx%d.png",
            i, x, y, w, h);

            File outFile = new File(outputFolder, filename);

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(crop, null), "png", outFile);
                saved++;
            } catch (IOException e) {
                System.out.println("❌ Failed to save crop " + i + ": " + e.getMessage());
            }
        }
        System.out.println("✅ Saved " + saved + " cropped regions to: "
            + outputFolder.getAbsolutePath());
    }

    private static void writeToFile(File file, String content) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
            System.out.println("✅ Exported to: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("❌ Export failed: " + e.getMessage());
        }
    }
}