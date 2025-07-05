package com.dionChar.publicagencies.catalogue.controller.external;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dionChar.publicagencies.catalogue.dto.api.diavgeia.DiavgeiaDecisionDetailsResponseDTO;
import com.dionChar.publicagencies.catalogue.service.external.diavgeia.DiavgeiaDecisionService;

/**
 * Controller για την ανάκτηση των τελευταίων αποφάσεων από το Διαύγεια για έναν
 * δημόσιο οργανισμό.
 */

@RestController
@RequestMapping("/api/diavgeia")
public class DiavgeiaDecisionController {

	private final DiavgeiaDecisionService diavgeiaDecisionService;

	public DiavgeiaDecisionController(DiavgeiaDecisionService diavgeiaDecisionService) {
		this.diavgeiaDecisionService = diavgeiaDecisionService;
	}

	/**
	 * Επιστρέφει τις 5 τελευταίες αποφάσεις για έναν οργανισμό με βάση το όνομα.
	 *
	 * @param name Το όνομα του οργανισμού όπως εμφανίζεται στο frontend
	 * @return Λίστα με αποφάσεις ή άδειο array
	 * 
	 *         ----------- !!! Να αλλάξω την υλοποίηση ώστε να δέχεται και -----
	 *         ----------- size(επιλογή χρήστη για αρ.Αποφάσεων) !!! ------
	 */
	@GetMapping("/decisions")
	public ResponseEntity<List<DiavgeiaDecisionDetailsResponseDTO>> getDecisionsByOrganizationName(
			@RequestParam("name") String name) {
		List<DiavgeiaDecisionDetailsResponseDTO> decisions = diavgeiaDecisionService.getDecisions(name);
		return ResponseEntity.ok(decisions);
	}

}
