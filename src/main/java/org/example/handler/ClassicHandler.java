package org.example.handler;



import org.example.ServerUtils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

/**
 * Обработчик для "/classic.html"
 */
public class ClassicHandler extends Handler {

    public ClassicHandler() {
        this.method = "GET";
        this.path = "/classic.html";
    }

    @Override
    public void handle(String method, String path, BufferedOutputStream out) throws IOException {
        final var filePath = Path.of(".", "public", path);
        final var mimeType = Files.probeContentType(filePath);

        // special case for classic
        if (path.equals(this.path)) {
            final var template = Files.readString(filePath);
            final var content = template.replace("{time}", LocalDateTime.now().toString()).getBytes();
            out.write((ServerUtils.createOkResponse(mimeType, content.length)).getBytes());
            out.write(content);
            out.flush();
        }
    }
}
