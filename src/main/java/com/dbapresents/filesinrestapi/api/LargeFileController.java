package com.dbapresents.filesinrestapi.api;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("largefiles")
public class LargeFileController {

    private static final String LARGE_FILE_PATH = "c:/temp/largefile.txt";

    public LargeFileController() throws IOException {
        createLargeFileOnDisk();
    }

    private void createLargeFileOnDisk() throws IOException {
        File file = new File(LARGE_FILE_PATH);
        try (FileWriter writer = new FileWriter(file)) {
            for (int i = 0; i < 10000000; i++) {
                writer.write(i + " qwertyuiopasdfghjlzxcvbnm\n");
            }
        }
    }

    @RequestMapping("/bytearray")
    public ResponseEntity<ByteArrayResource> getByteArray() throws URISyntaxException, IOException {
        URI uri = new URI("file:///" + LARGE_FILE_PATH);
        File file = new File(uri);
        Path path = Paths.get(uri);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=receivedFile.txt");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(Files.readAllBytes(path)));
    }

    @RequestMapping("/streamresource")
    public ResponseEntity<Resource> getStreamResource() throws URISyntaxException, IOException {
        URI uri = new URI("file:///" + LARGE_FILE_PATH);
        File file = new File(uri);
        Resource resource = new InputStreamResource(new FileInputStream(file));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=receivedFile.txt");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
