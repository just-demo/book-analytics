package edu.self.utils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.UncheckedIOException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FileUtils {
    public static String readResourceFile(String fileName) {
        try {
            return IOUtils.toString(FileUtils.class.getClassLoader().getResourceAsStream(fileName), UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
