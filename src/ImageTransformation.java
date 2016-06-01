import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageTransformation {


    public BufferedImage getImageInScale(BufferedImage src, int w, int h) {
        int finalw = w;
        int finalh = h;
        double factor;
        if (src.getWidth() > src.getHeight()) {
            factor = ((double) src.getHeight() / (double) src.getWidth());
            finalh = (int) (finalw * factor);
        } else {
            factor = ((double) src.getWidth() / (double) src.getHeight());
            finalw = (int) (finalh * factor);
        }
        BufferedImage imgInScale = new BufferedImage(finalw, finalh, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = imgInScale.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.drawImage(src, 0, 0, finalw, finalh, null);
        g2.dispose();
        return imgInScale;
    }

    public BufferedImage getImageNotInScale(BufferedImage src, int w, int h) {
        int finalw = w;
        int finalh = h;

        BufferedImage imgNotScaled = new BufferedImage(finalw, finalh, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = imgNotScaled.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawImage(src, 0, 0, finalw, finalh, null);
        g2.dispose();
        return imgNotScaled;
    }

    public AffineTransformOp rotateImage(boolean flag, boolean resized, BufferedImage img) {
        double rotationRequired;
        double locationX;
        double locationY;
        if (flag) {
            rotationRequired = Math.toRadians(90);
            if (img.getWidth() > img.getHeight()) {
                locationX = img.getWidth() / 2;
                locationY = img.getWidth() / 2;
            } else {
                locationX = img.getHeight() / 2;
                locationY = img.getHeight() / 2;
            }
        } else {
            rotationRequired = Math.toRadians(-90);
            if (img.getWidth() > img.getHeight()) {
                locationX = img.getWidth() / 2;
                locationY = img.getWidth() / 2;
            } else {
                locationX = img.getHeight() / 2;
                locationY = img.getHeight() / 2;
            }
        }
        AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        resized = false;
        return op;
    }

    public String properties(String filePath) {
        File file = new File(filePath);
        String output = "";
        Metadata metadata = new Metadata();
        try {
            metadata = ImageMetadataReader.readMetadata(file);

            System.out.println(metadata);
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                System.out.format("%s = %s",
                        tag.getTagName(), tag.getDescription() + "\n");
                output = output + (tag.getTagName().toString() + " = " + tag.getDescription() + "\n".toString());
            }

        }

        return output;
    }
}
