package me.minidigger.mappingviewer.minimappingviewer.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import me.minidigger.mappingviewer.minimappingviewer.MiniMappingViewerApplication;
import me.minidigger.mappingviewer.minimappingviewer.model.mojang.Version;
import me.minidigger.mappingviewer.minimappingviewer.model.mojang.VersionManifest;

@Service
public class MojangService {

    public static final String MANIFEST_URL = "https://launchermeta.mojang.com/mc/game/version_manifest.json";

    private static final Logger log = LoggerFactory.getLogger(MojangService.class);

    private final RestTemplate rest;

    private final LoadingCache<String, Optional<VersionManifest>> versionManifest = CacheBuilder.newBuilder()
            .maximumSize(1)
            .expireAfterWrite(MiniMappingViewerApplication.CACHE_TIME)
            .build(new CacheLoader<>() {
                @Override
                public Optional<VersionManifest> load(String dummy) {
                    ResponseEntity<VersionManifest> manifest = rest.getForEntity(MANIFEST_URL, VersionManifest.class);
                    if (!manifest.getStatusCode().is2xxSuccessful()) {
                        log.warn("Error while fetching version manifest!");
                        return Optional.empty();
                    } else {
                        return Optional.ofNullable(manifest.getBody());
                    }
                }
            });


    private final LoadingCache<String, Optional<Version>> versions = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(MiniMappingViewerApplication.CACHE_TIME).build(new CacheLoader<>() {
                @Override
                public Optional<Version> load(String id) {
                    Optional<VersionManifest> manifestOptional = versionManifest.getUnchecked("dum");
                    if (manifestOptional.isEmpty()) {
                        log.warn("No version manifest found!");
                        return Optional.empty();
                    }
                    Optional<VersionManifest.Version> manifest = manifestOptional.get().getVersions().stream().filter(version -> version.getId().equals(id)).findFirst();
                    if (manifest.isEmpty()) {
                        log.warn("Version {} not found in manifest!", id);
                        return Optional.empty();
                    }

                    ResponseEntity<Version> version = rest.getForEntity(manifest.get().getUrl(), Version.class);
                    if (!version.getStatusCode().is2xxSuccessful()) {
                        log.warn("Error while fetching version {}!", manifest.get());
                        return Optional.empty();
                    } else {
                        return Optional.ofNullable(version.getBody());
                    }
                }
            });
    private final LoadingCache<String, Optional<String>> clientMappings = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(MiniMappingViewerApplication.CACHE_TIME).build(new CacheLoader<>() {
                @Override
                public Optional<String> load(String id) {
                    Optional<Version> version = versions.getUnchecked(id);
                    if (version.isEmpty()) {
                        log.warn("Version {} not found", id);
                        return Optional.empty();
                    }
                    Version.Download download = version.get().getDownloads().getClient_mappings();
                    if (download == null) {
                        log.warn("No client mappings for {}", id);
                        return Optional.empty();
                    }

                    ResponseEntity<String> mapping = rest.getForEntity(download.getUrl(), String.class);
                    if (!mapping.getStatusCode().is2xxSuccessful()) {
                        log.warn("Error while fetching client mappings for {}!", id);
                        return Optional.empty();
                    } else {
                        return Optional.ofNullable(mapping.getBody());
                    }
                }
            });

    private final LoadingCache<String, Optional<String>> serverMappings = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(MiniMappingViewerApplication.CACHE_TIME).build(new CacheLoader<>() {
                @Override
                public Optional<String> load(String id) {
                    Optional<Version> version = versions.getUnchecked(id);
                    if (version.isEmpty()) {
                        log.warn("Version {} not found", id);
                        return Optional.empty();
                    }
                    Version.Download download = version.get().getDownloads().getServer_mappings();
                    if (download == null) {
                        log.warn("No server mappings for {}", id);
                        return Optional.empty();
                    }

                    ResponseEntity<String> mapping = rest.getForEntity(download.getUrl(), String.class);
                    if (!mapping.getStatusCode().is2xxSuccessful()) {
                        log.warn("Error while fetching server mappings for {}!", id);
                        return Optional.empty();
                    } else {
                        return Optional.ofNullable(mapping.getBody());
                    }
                }
            });

    private final LoadingCache<String, Optional<String>> vanillaServerJar = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(MiniMappingViewerApplication.CACHE_TIME).build(new CacheLoader<>() {
                @Override
                public Optional<String> load(String id) {
                    Optional<Version> version = versions.getUnchecked(id);
                    if (version.isEmpty()) {
                        log.warn("Version {} not found", id);
                        return Optional.empty();
                    }
                    Version.Download download = version.get().getDownloads().getServer();
                    if (download == null) {
                        log.warn("No server jar for {}", id);
                        return Optional.empty();
                    } else {

                        return Optional.ofNullable(download.getUrl());
                    }
                }
            });

    @Autowired
    public MojangService(RestTemplate rest) {
        this.rest = rest;
    }

    public Stream<VersionManifest.Version> getVersions() {
        return versionManifest.getUnchecked("dum")
          .map(value -> value.getVersions().stream().filter(this::supportsMappings))
          .orElseGet(Stream::empty);
    }

    private boolean supportsMappings(VersionManifest.Version version) {
        return ((version.getId().startsWith("1.1") && version.getId().compareTo("1.15") >= 0) ||
               (version.getId().contains("w") && version.getId().compareTo("19w36a") >= 0) ||
               version.getId().equals("1.14.4")) && !version.getId().startsWith("3D Shareware");
    }

    public Optional<String> getServerMappings(String version) {
        return serverMappings.getUnchecked(version);
    }

    public Optional<String> getClientMappings(String version) {
        return clientMappings.getUnchecked(version);
    }

    public Optional<String> getVanillaServerJar(String version) {
        return vanillaServerJar.getUnchecked(version);
    }

    public boolean downloadServerJar(String version, Path path) {
        Optional<String> url = vanillaServerJar.getUnchecked(version);
        if (url.isEmpty()) {
            log.warn("server jar url for {} not found", version);
            return false;
        } else {
            return rest.execute(url.get(), HttpMethod.GET, null, clientHttpResponse -> {
                StreamUtils.copy(clientHttpResponse.getBody(), Files.newOutputStream(path));
                return true;
            }) != null;
        }
    }
}
