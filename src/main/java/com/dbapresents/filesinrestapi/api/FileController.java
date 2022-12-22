package com.dbapresents.filesinrestapi.api;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("files")
public class FileController {

    @GetMapping("/csv")
    public byte[] getCsvFile() throws URISyntaxException, IOException {
        String csvFilePath = "static/sample.csv";
        URL url = getClass().getClassLoader().getResource(csvFilePath);
        if (url == null) {
            throw new IOException("File " + csvFilePath + " not found");
        }
        Path path = Paths.get(url.toURI());
        String fileContent = Files.readString(path);
        return fileContent.getBytes();
    }

    @RequestMapping("/byteimage")
    public ResponseEntity<ByteArrayResource> getByteImage() throws URISyntaxException, IOException {
        String imageFilePath = "static/image.png";
        URL url = getClass().getClassLoader().getResource(imageFilePath);
        if (url == null) {
            throw new IOException("File " + imageFilePath + " not found");
        }
        File file = new File(url.toURI());
        Path path = Paths.get(url.toURI());
        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(Files.readAllBytes(path)));
    }

    @RequestMapping("/imagefile")
    public ResponseEntity<ByteArrayResource> getImageFile() throws URISyntaxException, IOException {
        String imageFilePath = "static/image.png";
        URL url = getClass().getClassLoader().getResource(imageFilePath);
        if (url == null) {
            throw new IOException("File " + imageFilePath + " not found");
        }
        File file = new File(url.toURI());
        Path path = Paths.get(url.toURI());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=customname.png");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(Files.readAllBytes(path)));
    }

    @RequestMapping("/imageresource")
    public ResponseEntity<Resource> getResource() throws URISyntaxException, IOException {
        String imageFilePath = "static/image.png";
        URL url = getClass().getClassLoader().getResource(imageFilePath);
        if (url == null) {
            throw new IOException("File " + imageFilePath + " not found");
        }
        File file = new File(url.toURI());
        Resource resource = new InputStreamResource(new FileInputStream(file));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=customname.png");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
