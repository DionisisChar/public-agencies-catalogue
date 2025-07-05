package com.dionChar.publicagencies.catalogue.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dionChar.publicagencies.catalogue.dto.admin.update.MinistryUpdateRequestDTO;
import com.dionChar.publicagencies.catalogue.service.MinistryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/ministries")
public class AdminMinistryController {

	private final MinistryService ministryService;

	public AdminMinistryController(MinistryService ministryService) {
		this.ministryService = ministryService;
	}

	/**
	 * Ενημερώνει το όνομα ενός υπουργείου. Μόνο για admin χρήση.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Void> updateMinistryName(@PathVariable Long id,
			@RequestBody @Valid MinistryUpdateRequestDTO ministryDTO) {
		ministryService.updateMinistry(id, ministryDTO.getName());
		return ResponseEntity.noContent().build(); // 204 No content (Η ενέργεια πέτυχε αλλά no body)

	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteMinistry(@PathVariable Long id){
		ministryService.deleteMinistry(id);
		return ResponseEntity.noContent().build();
	}
}
