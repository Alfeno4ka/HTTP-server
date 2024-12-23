package org.example.handler;



import org.example.ServerUtils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileHandler extends Handler {

    private final List<String> validPaths = List.of(
            "/index.html",
            "/spring.svg",
            "/spring.png",
            "/resources.html",
            "/styles.css",
            "/app.js",
            "/links.html",
            "/forms.html",
            "/events.html",
            "/events.js");

    public FileHandler() {
        this.method = "GET";
        this.path = "^\\/[a-zA-Z]+\\.[a-zA-Z]{1,4}$"; //Любой путь без квери-параметров
    }

    @Override
    public void handle(String method, String path, BufferedOutputStream out) throws IOException {
        if (!isValidPath(path)) {
            out.write((ServerUtils.createNotFoundResponse()).getBytes());
            out.flush();
            return;
        }

        Path filePath = Path.of(".", "public", path);
        String mimeType = Files.probeContentType(filePath);

        out.write((ServerUtils.createOkResponse(mimeType, Files.size(filePath))).getBytes());
        Files.copy(filePath, out);
        out.flush();
    }

    private boolean isValidPath(String path) {
        return validPaths.stream().anyMatch(path::contains);
    }
}
