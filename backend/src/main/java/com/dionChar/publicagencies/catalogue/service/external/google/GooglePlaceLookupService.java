package com.dionChar.publicagencies.catalogue.service.external.google;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dionChar.publicagencies.catalogue.util.OrganizationNameUtils;
import com.dionChar.publicagencies.catalogue.util.UserInputNormalizer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Service
public class GooglePlaceLookupService {

	private final RestTemplate restTemplate;

	@Value("${google.api.placesnew.url}")
	String apiBaseUrl;

	@Value("${google.api.placesnew.key}")
	String apiKey;

	public GooglePlaceLookupService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	// Μέθοδος που επιστρέφει το place.id από την απάντηση του textSearch(new)
	// (επιστρέφει Optional)
	public Optional<String> getPlaceId(String organizationName) {
		try {
			String normalizedName = organizationName.replaceAll("\\(.*?\\)", "").replace("\"", "");
			GooglePlacesTextSearchResponse response = callTextSearch(normalizedName);
			String placeId = response.places.get(0).id;
			return Optional.ofNullable(placeId);
		} catch (Exception e) {
			System.out.println(
					"GooglePlaceLookupService - Error in getPlaceId for: " + organizationName + " → " + e.getMessage());
			return Optional.empty();
		}

	}

	// Επιστρέφει ενα LocationDTO με τις συντεταγμένες τις υπηρεσίας ή κενό από την
	// απάντηση του Find Place
	/*
	 * public Optional<LocationDTO> getCoordinates(String serviceName) { try {
	 * GooglePlacesTextSearchResponse response = callFindPlace(serviceName); double
	 * lat = response.candidates.get(0).geometry.location.lat; double lng =
	 * response.candidates.get(0).geometry.location.lng; return
	 * Optional.ofNullable(new LocationDTO(lat, lng)); } catch (Exception e) {
	 * System.out.println("Error: " + e.getMessage()); //return Optional.of(new
	 * LocationDTO(1.1,1.1)); //Κάπου υπάρχει πρόβλημα και μπαίνει εδώ, να το σβήσω
	 * μετα return Optional.empty(); }
	 * 
	 * }
	 */

	// Βοηθητική μέθοδος για να κάνω χρηση του RestTemplate με κλήση στο
	// textSearch(new)
	// και να παίρνω το σωμα της Απάντησης
	private GooglePlacesTextSearchResponse callTextSearch(String organizationName) {
		String url = apiBaseUrl + "places:searchText";

		// JSON body
		String jsonBody = "{ \"textQuery\": \"" + organizationName + "\" }";

		// Headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-Goog-Api-Key", apiKey);
		headers.set("X-Goog-FieldMask", "places.id"); // Απαραίτητο FieldMask (τουλάχιστον 1)

		// Request entity
		HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

		// Εκτέλεση POST
		ResponseEntity<GooglePlacesTextSearchResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity,
				GooglePlacesTextSearchResponse.class);

		/*
		 * ResponseEntity<GooglePlacesTextSearchResponse> response =
		 * restTemplate.exchange(url, HttpMethod.GET, null,
		 * GooglePlacesTextSearchResponse.class);
		 */

		GooglePlacesTextSearchResponse body = response.getBody();

		if (body == null || body.places == null || body.places.isEmpty()) {
			throw new RuntimeException("No valid place found for service: " + organizationName);
		}
		return body;
	}

	@JsonIgnoreProperties("ignoreUnknown = true")
	private static class GooglePlacesTextSearchResponse {
		public List<Place> places;
	}

	private static class Place {
		public String id;
	}

}
