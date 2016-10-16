package com.ajurczyk.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for file conversions.
 *
 * @author aleksander.jurczyk@seedlabs.io
 */
public final class FileUtils {

    private FileUtils() {

    }

    /**
     * Read file and put into byte[].
     *
     * @param filePath source file path
     * @return byte[] with file content
     * @throws IOException I/O error occurred reading from the file, or file doesn't exist
     */
    public static byte[] readAsByteArray(String filePath) throws IOException {
        final Path path = Paths.get(filePath);
        return Files.readAllBytes(path);
    }

    /**
     * Create a file and write byte[] inside.
     *
     * @param data     byte[] to be written into file
     * @param filePath output file path
     * @param append   append to existing file
     * @throws IOException error while closing file or while writing to file
     */
    public static void save(byte[] data, String filePath, boolean append) throws IOException {
        final FileOutputStream fos = new FileOutputStream(filePath, append);
        try {
            fos.write(data);
            fos.close();
        } finally {
            fos.close();
        }
    }

    /**
     * Create a file and write String inside.
     *
     * @param data     String to be written into file
     * @param filePath output file path
     * @param append   append to existing file
     * @throws IOException error while closing file or while writing to file
     */
    public static void save(String data, String filePath, boolean append) throws IOException {
        save(data.getBytes(StandardCharsets.US_ASCII), filePath, append);
    }

    /**
     * Check set of paths if files exists.
     *
     * @param paths String array of paths to check
     * @throws IOException thrown when file doesn't exist
     */
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    public static void verifyPaths(String... paths) throws FileNotFoundException {
        for (final String path : paths) {
            final File file = new File(path);
            if (!file.isFile() || !file.exists()) {
                throw new FileNotFoundException("File: " + path + " doesn't exist.");
            }
        }
    }

    /**
     * Removes file if exists.
     *
     * @param path Path to file
     */
    public static void removeFile(String path) {
        final File file = new File(path);
        if (file.exists()) {
            file.delete();

        }
    }
}
