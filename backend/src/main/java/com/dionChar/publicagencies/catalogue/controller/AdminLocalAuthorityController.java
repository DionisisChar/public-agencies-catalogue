package com.dionChar.publicagencies.catalogue.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dionChar.publicagencies.catalogue.dto.admin.update.LocalAuthorityUpdateRequestDTO;
import com.dionChar.publicagencies.catalogue.dto.common.OptionDTO;
import com.dionChar.publicagencies.catalogue.service.LocalAuthorityService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/local-authorities")
public class AdminLocalAuthorityController {

	private final LocalAuthorityService localAuthorityService;

	public AdminLocalAuthorityController(LocalAuthorityService localAuthorityService) {
		this.localAuthorityService = localAuthorityService;
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateLocalAuthority(@PathVariable Long id,
			@Valid @RequestBody LocalAuthorityUpdateRequestDTO localAuthorityDTO) {

		localAuthorityService.updateLocalAuthority(id, localAuthorityDTO.getName());
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteLocalAuthority(@PathVariable Long id){
		localAuthorityService.deleteLocalAuthority(id);
		return ResponseEntity.noContent().build();
	}
	
	// 🔍 Αναζήτηση ΟΤΑ με query και pagination σε admin διαχείριση
	// Ίσως να πρέπει να τη βγάλω εκτός admin γιατί μπορεί να έχει και 
	// γενική χρήση
    @GetMapping("/search")
    public Page<OptionDTO> searchLocalAuthorities(
            @RequestParam(required = false) String query,
            Pageable pageable) {
        return localAuthorityService.searchByNamePaged(query, pageable);
    }
}
