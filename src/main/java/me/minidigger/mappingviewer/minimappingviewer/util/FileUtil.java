package me.minidigger.mappingviewer.minimappingviewer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {

    private static final Logger log = LoggerFactory.getLogger(FileUtil.class);
    private static final Path cacheFolder = Path.of("");

    public static Path getMappingFolder(String version) {
        try {
            Path versionFolder = cacheFolder.resolve(version);
            if (!Files.isDirectory(versionFolder)) {
                Files.createDirectory(versionFolder);
            }
            return versionFolder;
        } catch (IOException ex) {
            log.warn("Error while preparing mapping folder", ex);
            return null;
        }
    }
}
