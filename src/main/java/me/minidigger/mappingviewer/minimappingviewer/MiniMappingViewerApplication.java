package me.minidigger.mappingviewer.minimappingviewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

@SpringBootApplication
public class MiniMappingViewerApplication {
    public static final Duration CACHE_TIME = Duration.of(1, ChronoUnit.DAYS);

    public static void main(String[] args) {
        SpringApplication.run(MiniMappingViewerApplication.class, args);
    }

    @Bean
    public RestTemplate forceJsonRestTemplate() {
        RestTemplate restTemplate = create();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON));
        restTemplate.getMessageConverters().add(0, converter);
        return restTemplate;
    }

    @Bean
    @Primary
    public RestTemplate restTemplate() {
        return create();
    }

    private RestTemplate create() {
       return new RestTemplateBuilder().defaultHeader("user-agent","MiniMappingViewer v2").build();
    }
}
