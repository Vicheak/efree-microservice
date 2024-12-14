package com.efree.fileservice.api.banner;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BannerUtil {

    public static void validateImageDimension(MultipartFile file, int expectedWidth, int expectedHeight, String imageType) throws IOException {
        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image == null) {
            throw new IOException("Failed to read the image for " + imageType);
        }

        int width = image.getWidth();
        int height = image.getHeight();

        if (width != expectedWidth || height != expectedHeight) {
            throw new IOException(imageType + " dimensions are invalid. Expected : " +
                    expectedWidth + "x" + expectedHeight + ", Found: " + width + "x" + height);
        }
    }

    public static String getBannerFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastDotIndex + 1);
    }

}
