package me.minidigger.mappingviewer.minimappingviewer.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import me.minidigger.mappingviewer.minimappingviewer.config.CacheConfig;
import me.minidigger.mappingviewer.minimappingviewer.model.spigot.Info;
import me.minidigger.mappingviewer.minimappingviewer.model.spigot.Version;

@Service
public class SpigotService {

    public static final String VERSIONS_URL = "https://hub.spigotmc.org/versions/";
    public static final String INFO_URL = "https://hub.spigotmc.org/stash/projects/SPIGOT/repos/builddata/raw/info.json?at=";
    public static final String MAPPINGS_URL = "https://hub.spigotmc.org/stash/projects/SPIGOT/repos/builddata/raw/mappings/";

    private static final Logger log = LoggerFactory.getLogger(SpigotService.class);

    private final RestTemplate rest;
    private final RestTemplate jsonRest;

    private LoadingCache<String, Optional<String>> refs = CacheBuilder.newBuilder()
            .maximumSize(100).expireAfterWrite(CacheConfig.CACHE_TIME).build(new CacheLoader<>() {
                @Override
                public Optional<String> load(String version) {
                    ResponseEntity<Version> versionEntity = rest.getForEntity(VERSIONS_URL + version + ".json", Version.class);
                    if (!versionEntity.getStatusCode().is2xxSuccessful() || versionEntity.getBody() == null) {
                        log.warn("Could not load ref for {}", version);
                        return Optional.empty();
                    }

                    return Optional.ofNullable(versionEntity.getBody().getRefs().getBuildData());
                }
            });

    private LoadingCache<String, Optional<Info>> infos = CacheBuilder.newBuilder()
            .maximumSize(100).expireAfterWrite(CacheConfig.CACHE_TIME).build(new CacheLoader<>() {
                @Override
                public Optional<Info> load(String version) {
                    Optional<String> ref = refs.getUnchecked(version);
                    if (ref.isEmpty()) {
                        log.warn("No ref for {}", version);
                        return Optional.empty();
                    }

                    ResponseEntity<Info> info = jsonRest.getForEntity(INFO_URL + ref.get(), Info.class);
                    if (!info.getStatusCode().is2xxSuccessful()) {
                        log.warn("Could not load info for {}", version);
                        return Optional.empty();
                    }

                    return Optional.ofNullable(info.getBody());
                }
            });

    private LoadingCache<String, Optional<String>> members = CacheBuilder.newBuilder()
            .maximumSize(100).expireAfterWrite(CacheConfig.CACHE_TIME).build(new CacheLoader<>() {
                @Override
                public Optional<String> load(String version) {
                    Optional<String> ref = refs.getUnchecked(version);
                    if (ref.isEmpty()) {
                        log.warn("No ref for {}", version);
                        return Optional.empty();
                    }
                    Optional<Info> info = infos.getUnchecked(version);
                    if (info.isEmpty()) {
                        log.warn("No info for {}", version);
                        return Optional.empty();
                    }

                    ResponseEntity<String> mappings = rest.getForEntity(MAPPINGS_URL + info.get().getMemberMappings() + "?at=" + ref.get(), String.class);
                    if (!mappings.getStatusCode().is2xxSuccessful()) {
                        log.warn("Could not load member mappings for {}", version);
                        return Optional.empty();
                    }

                    return Optional.ofNullable(mappings.getBody());
                }
            });

    private LoadingCache<String, Optional<String>> classes = CacheBuilder.newBuilder()
            .maximumSize(100).expireAfterWrite(CacheConfig.CACHE_TIME).build(new CacheLoader<>() {
                @Override
                public Optional<String> load(String version) {
                    Optional<String> ref = refs.getUnchecked(version);
                    if (ref.isEmpty()) {
                        log.warn("No ref for {}", version);
                        return Optional.empty();
                    }
                    Optional<Info> info = infos.getUnchecked(version);
                    if (info.isEmpty()) {
                        log.warn("No info for {}", version);
                        return Optional.empty();
                    }

                    ResponseEntity<String> mappings = rest.getForEntity(MAPPINGS_URL + info.get().getClassMappings() + "?at=" + ref.get(), String.class);
                    if (!mappings.getStatusCode().is2xxSuccessful()) {
                        log.warn("Could not load class mappings for {}", version);
                        return Optional.empty();
                    }

                    return Optional.ofNullable(mappings.getBody());
                }
            });

    @Autowired
    public SpigotService(RestTemplate restTemplate, @Qualifier("forceJsonRestTemplate") RestTemplate forceJsonRestTemplate) {
        this.rest = restTemplate;
        this.jsonRest = forceJsonRestTemplate;
    }

    public Optional<String> getClassMappings(String version) {
        return classes.getUnchecked(version);
    }

    public Optional<String> getMemberMappings(String version) {
        return members.getUnchecked(version);
    }
}
