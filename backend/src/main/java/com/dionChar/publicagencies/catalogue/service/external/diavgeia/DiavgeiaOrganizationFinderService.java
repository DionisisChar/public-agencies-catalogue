package com.dionChar.publicagencies.catalogue.service.external.diavgeia;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dionChar.publicagencies.catalogue.util.OrganizationNameUtils;

/**
 * !!!!! Είναι το ΣΩΣΤΟ SERVICE όπως θα το χρησιμοποιήσω στην εφαρμογή μου
 * !!!!!!
 * 
 * 
 * 
 * Service για την αναζήτηση του UID ενός οργανισμού στο Diavgeia API
 * (organizations.json, βασισμένο στο όνομα της υπηρεσίας (label).
 * 
 * Χρησιμοποιεί το RestTemplate για να κάνει HTTP κλήσεις στο Diavgeia API και
 * επιστρέφει τον μοναδικό αναγνωριστικό (UID) ενός οργανισμού, αν βρεθεί, με
 * βάση το όνομα της υπηρεσίας (service name).
 * 
 * Κύριες μέθοδοι: - getOrgUid(String serviceName): Επιστρέφει το UID του
 * οργανισμού για το δοθέν serviceName. Επιστρέφει Optional<String>. -
 * findOrgUidByLabel(String serviceName, List<> organizations): Αναζητά το UID
 * σε μια λίστα οργανισμών. - callDiavgeiaOrganizations(): Κάνει HTTP GET αίτηση
 * στο Diavgeia API και επιστρέφει τη λίστα οργανισμών.
 * 
 * Αν δεν βρεθεί οργανισμός με το ζητούμενο όνομα, επιστρέφεται
 * Optional.empty(). Αν υπάρξει σφάλμα κατά την κλήση του API, θα ριχτεί
 * RuntimeException.
 */

@Service
public class DiavgeiaOrganizationFinderService {

    private final DiavgeiaOrganizationsProviderService providerService;

	/*@Value("${diavgeia.api.url}")
	String apiUrl;*/

	public DiavgeiaOrganizationFinderService(DiavgeiaOrganizationsProviderService providerService) {
        this.providerService = providerService;
	}

	public Optional<String> getOrgUid(String organizationName) {
		OrganizationResponse organizationResponse = providerService.callDiavgeiaOrganizations();
		// Παλιά Λογική (Δουλευει καλά)
		// return findOrgUidByLabel(serviceName, organizationResponse.organizations);

		// Νεα Λογική με FuzzyOrganizationMatcher προς Δοκιμή !! ΑΝ ΔΕ ΔΟΥΛΕΥΕΙ ΤΗΝ
		// ΑΦΑΙΡΩ !!
		return findOrgUidByLabel2(organizationName, organizationResponse.organizations);

	}

	// Να ξαναδώ λίγο αυτή τη μέθοδο !! Ίσως υπάρχει κάποιο λάθος
	// Μήπως πρέπει να κάνω εδώ αλλαγή ή στην επόμενη που την καλεί??
	// SOS Να κρατήσω και αυτή τη μέθοδο όπως είναι γιατί δουλευει
	// σε περίπτωση που αυτή με το fuzzyMatcher αποτύχει.
	private Optional<String> findOrgUidByLabel(String organizationName, List<DiavgeiaOrganization> organizations) {
		return organizations.stream().filter(org -> org.label != null && org.label.equalsIgnoreCase(organizationName))
				.map(org -> org.uid).findFirst();
	}

	private Optional<String> findOrgUidByLabel2(String organizationName, List<DiavgeiaOrganization> organizations) {

		// 1. Exact match (ίδιο με την παλιά λογική)
		Optional<String> exactMatch = organizations.stream()
				.filter(org -> org.label != null && org.label.equalsIgnoreCase(organizationName)).map(org -> org.uid)
				.findFirst();
		if (exactMatch.isPresent())
			return exactMatch;

		// 2. Fuzzy match (αφού δεν βρέθηκε exact)
		for (DiavgeiaOrganization org : organizations) {
			if (org.label != null && OrganizationNameUtils.isSimilarOrganizationName(organizationName, org.label)) {
				return Optional.of(org.uid);
			}
		}
		// 3. Δεν βρέθηκε τίποτα
		return Optional.empty();
	}

	/*@Cacheable("diavgeiaOrganizations")
	public OrganizationResponse callDiavgeiaOrganizations() {
		String url = String.format("%s/organizations.json", apiUrl);

		ResponseEntity<OrganizationResponse> response = restTemplate.exchange(url, HttpMethod.GET, null,
				OrganizationResponse.class);

		OrganizationResponse body = response.getBody();

		if (body == null || body.organizations == null || body.organizations.isEmpty()) {
			throw new RuntimeException("No organizations list found");
		}
		return body;
	}*/

	 static class OrganizationResponse {
		// να το αλλάξω σε List<DiavgeiaOrganization>
		public List<DiavgeiaOrganization> organizations;
	}

	// Να αλλάξω το όνομα της κλάσης σε DiavgeiaOrganization
	 static class DiavgeiaOrganization {
		public String uid;
		public String label;
	}

}
