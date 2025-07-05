package com.dionChar.publicagencies.catalogue.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Παρέχει το Bean του RestTemplate για χρήση σε εξωτερικά APIs.
 */

@Configuration
public class AppConfig {

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
