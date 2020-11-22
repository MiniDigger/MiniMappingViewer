package me.minidigger.mappingviewer.minimappingviewer.service;

import net.fabricmc.lorenztiny.TinyMappingsReader;
import net.fabricmc.mapping.tree.TinyMappingFactory;

import org.cadixdev.atlas.Atlas;
import org.cadixdev.bombe.asm.jar.JarEntryRemappingTransformer;
import org.cadixdev.lorenz.MappingSet;
import org.cadixdev.lorenz.asm.LorenzRemapper;
import org.cadixdev.lorenz.io.MappingFormats;
import org.cadixdev.lorenz.io.MappingsReader;
import org.cadixdev.lorenz.merge.MappingSetMerger;
import org.cadixdev.lorenz.merge.MergeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import me.minidigger.mappingviewer.minimappingviewer.util.FileUtil;
import me.minidigger.mappingviewer.minimappingviewer.util.SpigotPackageMergerHandler;

@Service
public class MappingService {

    private static final Logger log = LoggerFactory.getLogger(MappingService.class);

    private final MojangService mojangService;
    private final SpigotService spigotService;
    private final YarnService yarnService;

    @Autowired
    public MappingService(MojangService mojangService, SpigotService spigotService, YarnService yarnService) {
        this.mojangService = mojangService;
        this.spigotService = spigotService;
        this.yarnService = yarnService;
    }

    public List<String> readMojang(String version, boolean server) {
        Path mappingFolder = FileUtil.getMappingFolder(version, server);
        if (mappingFolder == null) {
            return Collections.emptyList();
        }
        Path path = mappingFolder.resolve("notchToMojang.tsrg");
        if (!Files.exists(path)) {
            if (!createMojang(version, server)) {
                return Collections.emptyList();
            }
        }
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    public boolean createMojang(String version, boolean server) {
        Optional<String> mappings;
        if (server) {
            mappings = mojangService.getServerMappings(version);
        } else {
            mappings = mojangService.getClientMappings(version);
        }

        if (mappings.isEmpty()) {
            log.warn("No mojang mappings found for {}", version);
            return false;
        }

        try (MappingsReader reader = MappingFormats.byId("proguard").createReader(new ByteArrayInputStream(mappings.get().getBytes(StandardCharsets.UTF_8)))) {
            Path mappingFolder = FileUtil.getMappingFolder(version, server);
            if (mappingFolder == null) {
                return false;
            }

            MappingSet mojangToNotch = reader.read();

//            inheritanceFix(mappingFolder, version, mojangToNotch);

            MappingSet notchToMojang = mojangToNotch.reverse();

            MappingFormats.TSRG.write(mojangToNotch, mappingFolder.resolve("mojangToNotch.tsrg"));
            MappingFormats.TSRG.write(notchToMojang, mappingFolder.resolve("notchToMojang.tsrg"));

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> readSpigot(String version) {
        Path mappingFolder = FileUtil.getMappingFolder(version, true);
        if (mappingFolder == null) {
            return Collections.emptyList();
        }
        Path path = mappingFolder.resolve("notchToSpigot.tsrg");
        if (!Files.exists(path)) {
            if (!createSpigot(version)) {
                return Collections.emptyList();
            }
        }
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    public boolean createSpigot(String version) {
        Optional<String> classes = spigotService.getClassMappings(version);
        Optional<String> member = spigotService.getMemberMappings(version);
        if (classes.isEmpty() || member.isEmpty()) {
            log.warn("No spigot mappings found for {}", version);
            return false;
        }

        try (MappingsReader classReader = MappingFormats.CSRG.createReader(new ByteArrayInputStream(classes.get().getBytes(StandardCharsets.UTF_8)));
             MappingsReader memberReader = MappingFormats.CSRG.createReader(new ByteArrayInputStream(member.get().getBytes(StandardCharsets.UTF_8)))) {

            MappingSet mergedSet = MappingSetMerger.create(classReader.read(), memberReader.read()).merge();

            // spigot isn't complete, so we need to fill it
            Path mappingFolder = FileUtil.getMappingFolder(version, true);
            if (mappingFolder == null) {
                return false;
            }

            Path file = mappingFolder.resolve("notchToMojang.tsrg");
            if (!Files.exists(file)) {
                log.warn("MojMap didn't exist get, creating");
                readMojang(version, true);
            }
            MappingSet notchToMojang = MappingFormats.TSRG.read(file);
            MappingSet notchToSpigot = MappingSetMerger.create(mergedSet, notchToMojang, MergeConfig.builder().withMergeHandler(new SpigotPackageMergerHandler()).build()).merge();

            inheritanceFix(mappingFolder, version, notchToSpigot);

            MappingSet spigotToNotch = notchToSpigot.reverse();

            MappingFormats.TSRG.write(notchToSpigot, mappingFolder.resolve("notchToSpigot.tsrg"));
            MappingFormats.TSRG.write(spigotToNotch, mappingFolder.resolve("spigotToNotch.tsrg"));

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> readYarn(String version) {
        Path mappingFolder = FileUtil.getMappingFolder(version, true);
        if (mappingFolder == null) {
            return Collections.emptyList();
        }
        Path path = mappingFolder.resolve("notchToYarn.tsrg");
        if (!Files.exists(path)) {
            if (!createYarn(version)) {
                return Collections.emptyList();
            }
        }
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    public boolean createYarn(String version) {
        Optional<String> mappings = yarnService.getMappings(version);
        if (mappings.isEmpty()) {
            log.warn("No yarn mappings found for {}", version);
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new StringReader(mappings.get()));
             TinyMappingsReader tinyMappingsReader = new TinyMappingsReader(TinyMappingFactory.loadWithDetection(reader), "official", "named")){
            Path mappingFolder = FileUtil.getMappingFolder(version, true);
            if (mappingFolder == null) {
                return false;
            }

            MappingSet notchToYarn = tinyMappingsReader.read(MappingSet.create());

            inheritanceFix(mappingFolder, version, notchToYarn);

            MappingSet yarnToNotch = notchToYarn.reverse();

            MappingFormats.TSRG.write(notchToYarn, mappingFolder.resolve("notchToYarn.tsrg"));
            MappingFormats.TSRG.write(yarnToNotch, mappingFolder.resolve("yarnToNotch.tsrg"));

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void inheritanceFix(Path mappingFolder, String version, MappingSet mappings) {
        try (Atlas atlas = new Atlas()) {
            Path vanillaJar = mappingFolder.resolve("server.jar");
            if (!Files.exists(vanillaJar)) {
                log.warn("vanilla server jar didn't exist yet, downloading");
                mojangService.downloadServerJar(version, vanillaJar);
            }
            Path tempFile = Files.createTempFile("paperweight", "jar");
            atlas.install((ctx) -> new JarEntryRemappingTransformer(new LorenzRemapper(mappings, ctx.inheritanceProvider())));
            atlas.run(vanillaJar, tempFile);
        } catch (IOException ex) {
            log.warn("Error while running atlas", ex);
        }
    }
}
