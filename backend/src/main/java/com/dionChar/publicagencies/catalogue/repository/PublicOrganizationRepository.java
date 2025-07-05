package com.dionChar.publicagencies.catalogue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dionChar.publicagencies.catalogue.model.PublicOrganization;

@Repository
public interface PublicOrganizationRepository extends JpaRepository<PublicOrganization, Long> {
	
	// Προσθέτω custom methods αργότερα π.χ. findByNameContainingIgnoreCase

}
