package br.edu.fiec.MapeamentoDeSaude.shared.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequestMapping("/images")
public class ImageController {

    public static final String UPLOAD_DIR = "uploads"; // Diretório para salvar as imagens
    public static final String THUMBNAIL_DIR = "thumbnails";
    public static final int THUMBNAIL_SIZE = 150; // Tamanho do thumbnail (150x150 pixels)

    private void createDirectoryIfNotExists(String directoryName) {
        File directory = new File(directoryName);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                System.err.println("Failed to create directory: " + directoryName);
            }
        }
    }

    public ImageController() {
        // Cria os diretórios de upload e thumbnail se não existirem
        createDirectoryIfNotExists(UPLOAD_DIR);
        createDirectoryIfNotExists(THUMBNAIL_DIR);
    }

    // Esta rota é pública (controlada pelo SecurityConfig)
    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename, @RequestParam(value = "thumbnail", defaultValue = "false") boolean thumbnail) {
        try {
            String directory = thumbnail ? THUMBNAIL_DIR : UPLOAD_DIR;
            Path filePath = Paths.get(directory, thumbnail ? "thumb_" + filename : filename);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream"; // Tipo de conteúdo padrão
                }
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (IOException ioException) {
            log.error("getImage - Error reading file", ioException);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            log.error("getImage - Generic error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

        }
    }
}