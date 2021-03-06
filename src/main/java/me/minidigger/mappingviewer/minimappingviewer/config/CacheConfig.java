package me.minidigger.mappingviewer.minimappingviewer.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
@EnableCaching
public class CacheConfig {
  public static final Duration CACHE_TIME = Duration.of(1, ChronoUnit.DAYS);
}
