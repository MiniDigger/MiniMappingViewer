package me.minidigger.mappingviewer.minimappingviewer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.minidigger.mappingviewer.minimappingviewer.service.MappingService;

@Controller
@RequestMapping("/api/mappings/")
public class MappingController {

    private final MappingService service;

    @Autowired
    public MappingController(MappingService service) {
        this.service = service;
    }

    @ResponseBody
    @GetMapping(value = "/mojang/{version}/client", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getMojangClientMappings(@PathVariable String version) {
        return String.join("\n", service.readMojang(version, false));
    }

    @ResponseBody
    @GetMapping(value = "/mojang/{version}/server", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getMojangServerMappings(@PathVariable String version) {
        return String.join("\n", service.readMojang(version, true));
    }

    @ResponseBody
    @GetMapping(value = "/spigot/{version}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getSpigotMappings(@PathVariable String version) {
        return String.join("\n", service.readSpigot(version));
    }

    @ResponseBody
    @GetMapping(value = "/yarn/{version}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getYarnMappings(@PathVariable String version) {
        return String.join("\n", service.readYarn(version));
    }
}
