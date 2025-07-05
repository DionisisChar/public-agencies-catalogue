package com.dionChar.publicagencies.catalogue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dionChar.publicagencies.catalogue.model.LocalOrganization;

@Repository
public interface LocalOrganizationRepository extends JpaRepository<LocalOrganization, Long> {

	// Θα επεκταθεί αν χρειαστεί φίλτρα π.χ. based on LocalAuthority
}
