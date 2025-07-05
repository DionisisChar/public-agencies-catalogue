package com.dionChar.publicagencies.catalogue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PublicAgenciesCatalogueApplication {

	public static void main(String[] args) {
		SpringApplication.run(PublicAgenciesCatalogueApplication.class, args);
	}

}
