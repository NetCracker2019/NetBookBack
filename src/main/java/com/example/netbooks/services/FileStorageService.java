package com.example.netbooks.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {
    private final Path rootLocation = Paths.get("files");

    public void saveFile(MultipartFile file, String name) throws IOException {
        try {
            Files.copy(file.getInputStream(), this.rootLocation.resolve(name));
        } catch (Exception e) {
            throw e;
        }

    }

    public void deleteFile(String filename) {
        FileSystemUtils.deleteRecursively(rootLocation.resolve(filename).toFile());
    }

    public Resource loadFile(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("fail load file " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("fail load file " + filename);
        }
    }
}
