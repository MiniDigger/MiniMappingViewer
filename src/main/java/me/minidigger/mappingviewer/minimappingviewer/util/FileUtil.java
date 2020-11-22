package me.minidigger.mappingviewer.minimappingviewer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {

    private static final Logger log = LoggerFactory.getLogger(FileUtil.class);
    private static final Path cacheFolder = Path.of("");

    public static Path getMappingFolder(String version, boolean server) {
        try {
            Path versionFolder = cacheFolder.resolve(version);
            if (!Files.isDirectory(versionFolder)) {
                Files.createDirectory(versionFolder);
            }
            Path mappingFolder = versionFolder.resolve(server ? "server" : "client");
            if (!Files.isDirectory(mappingFolder)) {
                Files.createDirectory(mappingFolder);
            }
            return mappingFolder;
        } catch (IOException ex) {
            log.warn("Error while preparing mapping folder", ex);
            return null;
        }
    }
}
