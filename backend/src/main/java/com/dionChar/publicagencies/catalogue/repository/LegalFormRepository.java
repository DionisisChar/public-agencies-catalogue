package com.dionChar.publicagencies.catalogue.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dionChar.publicagencies.catalogue.model.LegalForm;

@Repository
public interface LegalFormRepository extends JpaRepository<LegalForm, Long> {

	Optional<LegalForm> findByName(String name);

}
