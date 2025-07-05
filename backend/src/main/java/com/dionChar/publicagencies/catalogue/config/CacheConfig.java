package com.dionChar.publicagencies.catalogue.config;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

import org.springframework.cache.caffeine.CaffeineCacheManager;

@Configuration
public class CacheConfig {

	@Bean
	CacheManager cacheManager() {
		CaffeineCacheManager cacheManager = new CaffeineCacheManager("diavgeiaOrganizations");
		cacheManager.setCaffeine(
				Caffeine.newBuilder().
				expireAfterWrite(12, TimeUnit.HOURS)
				.maximumSize(1));
		return cacheManager;
	}

}
