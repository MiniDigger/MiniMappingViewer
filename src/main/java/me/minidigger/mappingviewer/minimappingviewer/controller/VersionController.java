package me.minidigger.mappingviewer.minimappingviewer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Stream;

import me.minidigger.mappingviewer.minimappingviewer.model.MappingProvider;
import me.minidigger.mappingviewer.minimappingviewer.model.mojang.VersionManifest;
import me.minidigger.mappingviewer.minimappingviewer.service.MojangService;

@Controller
@RequestMapping("/api/versions/")
public class VersionController {

    private final MojangService mojangService;

    public VersionController(MojangService mojangService) {
      this.mojangService = mojangService;
    }

    @ResponseBody
    @GetMapping(value = "/{type}")
    public Stream<VersionManifest.Version> version(@PathVariable MappingProvider type) {
        if (!type.supportsSnapshots()) {
            return mojangService.getVersions().filter(v -> v.getType().equals("release"));
        } else {
            return mojangService.getVersions();
        }
    }
}
