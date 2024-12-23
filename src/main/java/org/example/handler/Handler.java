package org.example.handler;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

public abstract class Handler {

    protected String method;
    protected String path;

    public abstract void handle(String method, String path, BufferedOutputStream responseStream) throws IOException, URISyntaxException;

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }
}
