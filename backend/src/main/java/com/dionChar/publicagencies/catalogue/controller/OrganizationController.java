package com.dionChar.publicagencies.catalogue.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dionChar.publicagencies.catalogue.dto.search.OrganizationMapSearchRequestDTO;
import com.dionChar.publicagencies.catalogue.dto.search.OrganizationSearchCriteriaDTO;
import com.dionChar.publicagencies.catalogue.dto.search.OrganizationSearchResponseDTO;
import com.dionChar.publicagencies.catalogue.service.search.OrganizationSearchService;

import jakarta.validation.Valid;

/**
 * Το βασικό μου Search endpoint για εύρεση ενός οργανισμού(Local/Public)
 * με δυναμικά κριτήρια.
 * Επιστροφή σε σελίδες (pagination).
 * 
 * Δέχεται για παράμετρο τα κριτήρια σε Json Body της μορφής:
 * 
 * name: " String "
 * publicSectorStatus: "ENTOS / EKTOS"
 * legalFormId: Long
 * ministryIds: Long[]
 * localAuthorityIds: Long[]
 * page: int
 * size: int
 */

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

	private final OrganizationSearchService organizationSearchService;

	public OrganizationController(OrganizationSearchService organizationSearchService) {
		this.organizationSearchService = organizationSearchService;
	}

	// Δέχεται κριτήρια και επιστρέφει Πληροφορίες Οργανισμου! (Όλες)
	@PostMapping("/search")
	public Page<OrganizationSearchResponseDTO> search(
			@RequestBody @Valid OrganizationSearchCriteriaDTO criteria) {
		return organizationSearchService.search(criteria);
	}
	
	@PostMapping("/search/map")
	public ResponseEntity<List<OrganizationSearchResponseDTO>> searchForMap(@RequestBody OrganizationMapSearchRequestDTO criteria){
		List<OrganizationSearchResponseDTO> results = organizationSearchService.searchForMap(criteria);
		return ResponseEntity.ok(results);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<OrganizationSearchResponseDTO> getOrganizationDetails(@PathVariable Long id){
		OrganizationSearchResponseDTO dto = organizationSearchService.getOrganizationDetailsById(id);
		return ResponseEntity.ok(dto);
	}
}
