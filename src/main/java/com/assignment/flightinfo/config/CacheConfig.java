package com.assignment.flightinfo.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
public class CacheConfig {
	
@Value("${cache.maximum.size}")
private Integer maxCacheSize ;

@Value("${cache.initial.size}")
private Integer initialCacheSize ;

 @Bean
 public CacheManager cacheManager() {
  CaffeineCacheManager cacheManager = new CaffeineCacheManager("flightinfo");
  cacheManager.setCaffeine(caffeineCacheBuilder());
  return cacheManager;
 }

 Caffeine < Object, Object > caffeineCacheBuilder() {
  return Caffeine.newBuilder()
   .initialCapacity(100)
   .maximumSize(initialCacheSize)
   .expireAfterAccess(24, TimeUnit.HOURS)
   .weakKeys()
   .recordStats();
 }
}
