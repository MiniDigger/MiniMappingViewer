package me.minidigger.mappingviewer.minimappingviewer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import me.minidigger.mappingviewer.minimappingviewer.model.Delimiter;
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
    @GetMapping(value = "/MOJANG/{version}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getMojangMappings(@PathVariable String version,
                                          @RequestParam(value = "delimiter", defaultValue = "NEWLINE") Delimiter delimiter) {
        return String.join(delimiter.getDelimiter(), service.readMojang(version));
    }

    @ResponseBody
    @GetMapping(value = "/SPIGOT/{version}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getSpigotMappings(@PathVariable String version,
                                    @RequestParam(value = "delimiter", defaultValue = "NEWLINE") Delimiter delimiter) {
        return String.join(delimiter.getDelimiter(), service.readSpigot(version));
    }

    @ResponseBody
    @GetMapping(value = "/YARN/{version}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getYarnMappings(@PathVariable String version,
                                  @RequestParam(value = "delimiter", defaultValue = "NEWLINE") Delimiter delimiter) {
        return String.join(delimiter.getDelimiter(), service.readYarn(version));
    }
}
