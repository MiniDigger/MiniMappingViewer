package me.minidigger.mappingviewer.minimappingviewer.service;

import org.cadixdev.lorenz.MappingSet;
import org.cadixdev.lorenz.io.MappingFormats;
import org.cadixdev.lorenz.merge.MappingSetMerger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import me.minidigger.mappingviewer.minimappingviewer.model.MappingProvider;
import me.minidigger.mappingviewer.minimappingviewer.util.FileUtil;

@Service
public class MergingService {

    private static final Logger log = LoggerFactory.getLogger(MergingService.class);

    public List<String> merge(String version, MappingProvider type1, MappingProvider type2, boolean server) {
        Path mappingFolder = FileUtil.getMappingFolder(version, server);
        if (mappingFolder == null) {
            return Collections.emptyList();
        }

        String outputName = type1.getFirst() + "To" + type2.getSecond() + ".tsrg";
        Path outputPath = mappingFolder.resolve(outputName);
        if (Files.exists(outputPath)) {
            try {
                return Files.readAllLines(outputPath);
            } catch (IOException e) {
                e.printStackTrace();
                return Collections.emptyList();
            }
        }

        String notchToType1Name = "notchTo" + type1.getSecond() + ".tsrg";
        String notchToType2Name = "notchTo" + type2.getSecond() + ".tsrg";

        Path notchToType1Path = mappingFolder.resolve(notchToType1Name);
        Path notchToType2Path = mappingFolder.resolve(notchToType2Name);

        if (!Files.exists(notchToType1Path) || !Files.exists(notchToType2Path)) {
            log.warn("Can't merge " + outputName + " because some input didn't exist");
            return Collections.emptyList();
        }

        try {
            MappingSet notchToType1 = MappingFormats.TSRG.read(notchToType1Path);
            MappingSet notchToType2 = MappingFormats.TSRG.read(notchToType2Path);

            MappingSet output = MappingSetMerger.create(notchToType1.reverse(), notchToType2).merge();
            MappingFormats.TSRG.write(output, outputPath);

            // also write reverse, while we at it
            MappingSet output2 = output.reverse();
            String outputName2 = type2.getFirst() + "To" + type1.getSecond() + ".tsrg";
            Path outputPath2 = mappingFolder.resolve(outputName2);
            MappingFormats.TSRG.write(output2, outputPath2);

            return Files.readAllLines(outputPath);
        } catch (IOException ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
}
