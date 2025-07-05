package com.dionChar.publicagencies.catalogue.controller.external.test;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dionChar.publicagencies.catalogue.service.external.google.GooglePlaceLookupService;

@RestController
public class TestGooglePlaceIdController {

	private final GooglePlaceLookupService googlePlaceLookupService;

	public TestGooglePlaceIdController(GooglePlaceLookupService googlePlaceLookupService) {
		this.googlePlaceLookupService = googlePlaceLookupService;
	}

	@GetMapping("/api/test/google-place-id")
	public ResponseEntity<String> getPlaceId(@RequestParam("name") String name) {
		return googlePlaceLookupService.getPlaceId(name)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.noContent().build());
	}
}
