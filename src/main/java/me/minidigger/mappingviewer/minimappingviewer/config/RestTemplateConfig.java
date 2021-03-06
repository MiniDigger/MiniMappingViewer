package me.minidigger.mappingviewer.minimappingviewer.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Configuration
public class RestTemplateConfig {

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
