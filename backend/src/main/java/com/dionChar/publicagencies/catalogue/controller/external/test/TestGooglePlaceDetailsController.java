package com.dionChar.publicagencies.catalogue.controller.external.test;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dionChar.publicagencies.catalogue.dto.api.google.GooglePlaceDetailsResponseDTO;
import com.dionChar.publicagencies.catalogue.service.external.google.GooglePlaceDetailsService;

@RestController
@RequestMapping("/api/test/google-place-details")
public class TestGooglePlaceDetailsController {

	private final GooglePlaceDetailsService googlePlaceDetailsService;

	public TestGooglePlaceDetailsController(GooglePlaceDetailsService googlePlaceDetailsService) {
		this.googlePlaceDetailsService = googlePlaceDetailsService;
	}

	@GetMapping
	public ResponseEntity<GooglePlaceDetailsResponseDTO> getDetails(@RequestParam("name") String name) {
		Optional<GooglePlaceDetailsResponseDTO> detailsOpt = googlePlaceDetailsService.getPlaceDetails(name);
		return detailsOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
	}

}
