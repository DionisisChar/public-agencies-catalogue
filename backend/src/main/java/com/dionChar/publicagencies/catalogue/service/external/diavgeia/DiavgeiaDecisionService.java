package com.dionChar.publicagencies.catalogue.service.external.diavgeia;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.dionChar.publicagencies.catalogue.dto.api.diavgeia.DiavgeiaDecisionDetailsResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * !!! Το σωστό Service όπως θα χρησιμοποιηθεί στην εφαρμογή μου !!!
 * 
 * Η κλάση {@link DiavgeiaDecisionService} είναι υπεύθυνη για την αλληλεπίδραση
 * με το API του Διαύγειας, προκειμένου να ανακτήσει τις τελευταίες αποφάσεις
 * για μία δεδομένη δημόσια υπηρεσία.
 * 
 * Η κλάση χρησιμοποιεί την υπηρεσία {@link DiavgeiaOrganizationFinderService}
 * για να βρει το `orgUid` μιας δημόσιας υπηρεσίας με βάση το όνομά της. Στη
 * συνέχεια, καλεί το API του Διαύγειας με το `orgUid` για να αναζητήσει τις
 * αποφάσεις της υπηρεσίας αυτής. Οι αποφάσεις επιστρέφονται ως αντικείμενα
 * {@link DiavgeiaDecisionDetailsResponseDTO}, τα οποία περιλαμβάνουν βασικές
 * πληροφορίες για κάθε απόφαση, όπως το ADA, το θέμα, την ημερομηνία έκδοσης
 * και το URL του εγγράφου.
 * 
 * Η κλάση παρέχει τις εξής βασικές μεθόδους:
 * <ul>
 * <li>{@link DiavgeiaDecisionService#getDecisions(String)}: Ανακτά τις
 * τελευταίες αποφάσεις για μία υπηρεσία με βάση το όνομά της. Αν δεν υπάρχουν
 * αποφάσεις, επιστρέφει μία κενή λίστα.</li>
 * <li>{@link DiavgeiaDecisionService#findLatestDecisions(String)}: Βρίσκει τις
 * τελευταίες αποφάσεις για μία δημόσια υπηρεσία, αναζητώντας το `orgUid` και
 * καλώντας το API του Διαύγειας.</li>
 * <li>{@link DiavgeiaDecisionService#callDiavgeiaSearch(String, int)}: Εκτελεί
 * το αίτημα αναζήτησης στο API του Διαύγειας και επιστρέφει τις αποφάσεις της
 * υπηρεσίας.</li>
 * </ul>
 * 
 * Η κλάση χρησιμοποιεί την {@link RestTemplate} για την εκτέλεση αιτημάτων HTTP
 * στο API του Διαύγειας. Τα αποτελέσματα από το API επεξεργάζονται και
 * μετατρέπονται σε DTOs για περαιτέρω χρήση στην εφαρμογή.
 * 
 * Αν το API δεν επιστρέψει έγκυρες αποφάσεις ή αν δεν βρεθεί το `orgUid`, η
 * κλάση επιστρέφει {@link Optional.empty()}.
 * 
 * Σημείωση: Η κλάση αναμένει ότι η διεύθυνση URL του API του Διαύγειας είναι
 * ρυθμισμένη μέσω του {@link Value} annotation στο πεδίο `apiUrl`.
 */

@Service
public class DiavgeiaDecisionService {

	private final RestTemplate restTemplate;
	private final DiavgeiaOrganizationFinderService orgFinderService;

	@Value("${diavgeia.api.url}")
	String apiUrl;

	public DiavgeiaDecisionService(RestTemplate restTemplate, DiavgeiaOrganizationFinderService orgFinderService) {
		this.restTemplate = restTemplate;
		this.orgFinderService = orgFinderService;
	}

	public List<DiavgeiaDecisionDetailsResponseDTO> getDecisions(String organizationName) {
		Optional<List<DiavgeiaDecisionDetailsResponseDTO>> decisions = findLatestDecisions(organizationName);
		System.out.println(decisions + " Στο service");
		return decisions.orElse(Collections.emptyList());
	}

	// TI γίνεται άν το orgUid είναι κενό ή null?? ΔΕΣ ΤΟ ΛΙΓΟ ΑΥΤΟ
	// Είδα ότι υπάρχει κάτι που επιστρέφεται οπότε πρέπει κάπως να διαχειριστώ αυτή
	// την περίπτωση
	private Optional<List<DiavgeiaDecisionDetailsResponseDTO>> findLatestDecisions(String organizationName) {
		Optional<String> orgUidOptional = orgFinderService.getOrgUid(organizationName);
		if (orgUidOptional.isEmpty()) {
			System.out.println("Δεν υπάρχει αντίστοιχο όνομα");
			return Optional.empty(); // Αν δεν βρεθεί το orgUid, επιστρέφουμε Optional.empty
		}
		String orgUid = orgUidOptional.get();
		System.out.println(orgUid);
		SearchResponse searchResponse = callDiavgeiaSearch(orgUid, 5);

		// Ελέγχουμε αν υπάρχουν αποφάσεις
		// Μήπως να το αλλάξω σε Empty list ??
		if (searchResponse.decisions == null || searchResponse.decisions.isEmpty()) {
			System.out.println("Δεν βρεθηκαν αποφάσεις και έχω κενό Optional ");
			return Optional.empty();
		}

		// Μετατρέπουμε τις αποφάσεις σε DTOs
		List<DiavgeiaDecisionDetailsResponseDTO> decisions = searchResponse.decisions.stream()
				.map(decision -> new DiavgeiaDecisionDetailsResponseDTO(decision.ada, decision.subject,
						decision.issueDate, decision.documentUrl, decision.documentType))
				.toList();

		// Επιστρέφουμε Optional.empty() αν η λίστα είναι άδεια, αλλιώς επιστρέφουμε τις
		// αποφάσεις
		return decisions.isEmpty() ? Optional.empty() : Optional.of(decisions);

	}

	// !! ΣΟΣ !! ΔΕΣ ΣΧΟΛΙΑ ΠΙΟ ΚΑΤΩ
	private SearchResponse callDiavgeiaSearch(String orgUid, int size) {
		String url = String.format("%s/search.json?org=%s&size=%d", apiUrl, orgUid, size);
		ResponseEntity<SearchResponse> response = restTemplate.exchange(url, HttpMethod.GET, null,
				SearchResponse.class);
		SearchResponse body = response.getBody();

		// !!!!! ΣΟΣ !!!!!
		// εδώ πετάει σφάλμα όταν δεν βρίσκει αποφάσεις πρέπει να το διαχειριστώ
		// καλύτερα γιατί σταματάει η εφαρμογή μου
		// Ισως να μην χρειάζεται να κάνω καθόλου έλεγχο
		if (body == null || body.decisions == null || body.decisions.isEmpty()) {
			System.out.println("Δεν βρέθηκαν αποφάσεις για το uid:" + orgUid);
			return new SearchResponse();
		}
		return body;
	}

	@JsonIgnoreProperties("ignoreUnknown = true")
	private static class SearchResponse {
		public List<Decision> decisions;
	}

	private static class Decision {
		public String ada;
		public String subject;
		public Long issueDate;
		public String documentType;
		public String documentUrl;
	}
}
