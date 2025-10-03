package br.edu.fiec.MapeamentoDeSaude.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static br.edu.fiec.MapeamentoDeSaude.shared.controller.ImageController.*;

public class ImageUtils {


    public static String saveImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Invalid file type. Only images are allowed.");
        }

        try {
            String originalFileName = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFileName);

            // Novo nome do arquivo
            String newFileName = UUID.randomUUID().toString() + "." + fileExtension;
            Path filePath = Paths.get(UPLOAD_DIR, newFileName);

            Files.copy(file.getInputStream(), filePath);

            String thumbnailFileName = "thumb_" + newFileName;
            generateThumbnail(filePath.toFile(), thumbnailFileName);

            return newFileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload and process image: " + e.getMessage(), e); //wrap
        }
    }

    private static void generateThumbnail(File originalImageFile, String newFileName) throws IOException {
        BufferedImage originalImage = ImageIO.read(originalImageFile);
        File thumbnailFile = new File(THUMBNAIL_DIR, newFileName);
        Thumbnails.of(originalImage)
                .size(THUMBNAIL_SIZE, THUMBNAIL_SIZE)
                .keepAspectRatio(true)
                .toFile(thumbnailFile);
    }

    private static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }
}