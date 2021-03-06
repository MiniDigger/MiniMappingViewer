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
import me.minidigger.mappingviewer.minimappingviewer.model.MappingProvider;
import me.minidigger.mappingviewer.minimappingviewer.service.MergingService;

@Controller
@RequestMapping("/api/merge/")
public class MergeController {

    private final MergingService service;

    @Autowired
    public MergeController(MergingService service) {
        this.service = service;
    }

    @ResponseBody
    @GetMapping(value = "/{version}/server/{type1}/{type2}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String serverMerge(@PathVariable String version,
                              @PathVariable MappingProvider type1,
                              @PathVariable MappingProvider type2,
                              @RequestParam(value = "delimiter", defaultValue = "NEWLINE") Delimiter delimiter) {
        return String.join(delimiter.getDelimiter(), service.merge(version, type1, type2, true));
    }

    @ResponseBody
    @GetMapping(value = "/{version}/client/{type1}/{type2}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String clientMerge(@PathVariable String version,
                              @PathVariable MappingProvider type1,
                              @PathVariable MappingProvider type2,
                              @RequestParam(value = "delimiter", defaultValue = "NEWLINE") Delimiter delimiter) {
        return String.join(delimiter.getDelimiter(), service.merge(version, type1, type2, false));
    }
}
