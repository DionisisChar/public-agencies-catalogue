package com.dionChar.publicagencies.catalogue.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dionChar.publicagencies.catalogue.dto.admin.create.CreateOrganizationRequestDTO;
import com.dionChar.publicagencies.catalogue.dto.admin.edit.OrganizationEditResponseDTO;
import com.dionChar.publicagencies.catalogue.dto.admin.update.LocalOrganizationUpdateRequestDTO;
import com.dionChar.publicagencies.catalogue.dto.admin.update.PublicOrganizationUpdateRequestDTO;
import com.dionChar.publicagencies.catalogue.service.OrganizationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/organizations")
public class AdminOrganizationController {

	private final OrganizationService organizationService;

	public AdminOrganizationController(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@PostMapping
	public ResponseEntity<Void> createOrganization(@RequestBody @Valid CreateOrganizationRequestDTO dto) {
		organizationService.createOrganization(dto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOrganization(@PathVariable Long id) {
		organizationService.deleteOrganization(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/public/{id}")
	public ResponseEntity<Void> updatePublicOrganization(@PathVariable Long id,
			@RequestBody @Valid PublicOrganizationUpdateRequestDTO dto) {
		organizationService.updatePublicOrganization(id, dto);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/local/{id}")
	public ResponseEntity<Void> updateLocalOrganization(@PathVariable Long id,
			@RequestBody @Valid LocalOrganizationUpdateRequestDTO dto){
		organizationService.updateLocalOrganization(id, dto);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}/edit")
	public ResponseEntity<OrganizationEditResponseDTO> getOrganizationEditData(@PathVariable Long id) {
	    OrganizationEditResponseDTO dto = organizationService.getOrganizationEditData(id);
	    return ResponseEntity.ok(dto);
	}
}
