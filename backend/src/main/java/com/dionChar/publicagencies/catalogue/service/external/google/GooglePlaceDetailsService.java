package com.dionChar.publicagencies.catalogue.service.external.google;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dionChar.publicagencies.catalogue.dto.api.google.GooglePlaceDetailsResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Service
public class GooglePlaceDetailsService {

	private final GooglePlaceLookupService googlePlaceLookupService;
	private final RestTemplate restTemplate;

	@Value("${google.api.placesnew.url}")
	String apiBaseUrl;

	@Value("${google.api.placesnew.key}")
	String apiKey;

	public GooglePlaceDetailsService(GooglePlaceLookupService googlePlaceLookupService, RestTemplate restTemplate) {
		this.googlePlaceLookupService = googlePlaceLookupService;
		this.restTemplate = restTemplate;
	}

	public Optional<GooglePlaceDetailsResponseDTO> getPlaceDetails(String organizationName){
		try {
		PlaceDetailsResponse response = callPlaceDetails(organizationName);
		
		String address = response.formattedAddress;
		String phoneNumber = response.internationalPhoneNumber;
		String website = response.websiteUri;
		Double lat = response.location !=null ? response.location.latitude : null ;
		Double lng = response.location !=null ? response.location.longitude : null ;
		
		// Αν όλα είναι null επιστρέφω empty
		if( address == null && phoneNumber == null && website == null && lat == null && lng == null) {
			return Optional.empty();
			}
		
		return Optional.of(new GooglePlaceDetailsResponseDTO(address,phoneNumber,website,lat,lng));
		} catch (Exception e) {
			System.out.println("GooglePlaceDetails error in getPlaceDetails for: " + organizationName);
			return Optional.empty();
		}
	}

	private PlaceDetailsResponse callPlaceDetails(String organizationName) {
		Optional<String> optionalPlaceId = googlePlaceLookupService.getPlaceId(organizationName);
		if (optionalPlaceId.isEmpty()) {
			throw new RuntimeException("No place id found for organization name: " + organizationName);
		}

		String placeId = optionalPlaceId.get();
		String languageCode = "?languageCode=el"; // γλώσσα επιστρεφόμενων αποτελεσμάτων

		// Google Places(new) Place Details api
		String url = apiBaseUrl + "places/" + placeId + languageCode ;

		// Headers
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Goog-Api-Key", apiKey);
		headers.set("X-Goog-FieldMask", "formattedAddress,internationalPhoneNumber,websiteUri,location");


		// Request entity
		HttpEntity<Void> entity = new HttpEntity<>(headers);

		// Εκτέλεση GET
		ResponseEntity<PlaceDetailsResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity,
				PlaceDetailsResponse.class);

		// Response body
		PlaceDetailsResponse body = response.getBody();

		if (body == null) {
			throw new RuntimeException("No details found for organization: " + organizationName);
		}

		return body;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class PlaceDetailsResponse {
		public String formattedAddress;
		public String internationalPhoneNumber;
		public String websiteUri;
		public Location location;
	}

	private static class Location {
		public double latitude;
		public double longitude;
	}

}
