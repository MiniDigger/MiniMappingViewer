package me.minidigger.mappingviewer.minimappingviewer.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import com.fasterxml.jackson.databind.JsonNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import me.minidigger.mappingviewer.minimappingviewer.config.CacheConfig;

@Service
public class YarnService {

    public static final String VERSIONS_URL = "https://maven.fabricmc.net/net/fabricmc/yarn/versions.json";
    public static final String TINY_URL = "https://maven.fabricmc.net/net/fabricmc/yarn/<ver>%2Bbuild.<build>/yarn-<ver>%2Bbuild.<build>-tiny.gz";

    private static final Logger log = LoggerFactory.getLogger(YarnService.class);

    private final RestTemplate rest;

    private final LoadingCache<String, Map<String, List<Integer>>> versionManifest = CacheBuilder.newBuilder()
            .maximumSize(1)
            .expireAfterWrite(CacheConfig.CACHE_TIME)
            .build(new CacheLoader<>() {
                @Override
                public Map<String, List<Integer>> load(String dummy) {
                    ResponseEntity<JsonNode> manifest = rest.getForEntity(VERSIONS_URL, JsonNode.class);
                    if (!manifest.getStatusCode().is2xxSuccessful() || manifest.getBody() == null) {
                        log.warn("Error while fetching version manifest!");
                        return new HashMap<>();
                    } else {
                        Map<String, List<Integer>> map = new HashMap<>();
                        manifest.getBody().fieldNames().forEachRemaining(n -> {
                            List<Integer> builds = new ArrayList<>();
                            manifest.getBody().get(n).elements().forEachRemaining(i -> builds.add(i.intValue()));
                            map.put(n, builds);
                        });
                        return map;
                    }
                }
            });

    private final LoadingCache<String, Optional<String>> versions = CacheBuilder.newBuilder()
            .maximumSize(100).expireAfterWrite(CacheConfig.CACHE_TIME).build(new CacheLoader<>() {
                @Override
                public Optional<String> load(String version) {
                    List<Integer> builds = versionManifest.getUnchecked("dummy").get(version);
                    if (builds == null || builds.isEmpty()) {
                        log.warn("Unknown version {}", version);
                        return Optional.empty();
                    }
                    int build = builds.get(builds.size() - 1);
                    String url = TINY_URL.replace("<ver>", version).replace("<build>", build + "");

                    try {
                        return Optional.ofNullable(rest.execute(new URI(url), HttpMethod.GET, null, clientHttpResponse -> {
                            GZIPInputStream stream = new GZIPInputStream(clientHttpResponse.getBody());
                            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                            return reader.lines().collect(Collectors.joining("\n"));
                        }));
                    } catch (HttpClientErrorException | URISyntaxException ex) {
                        log.warn("Error while downloading tiny mappings using url {}", url, ex);
                        return Optional.empty();
                    }
                }
            });

    @Autowired
    public YarnService(RestTemplate rest) {
        this.rest = rest;
    }

    public Optional<String> getMappings(String version) {
        return versions.getUnchecked(version);
    }
}
