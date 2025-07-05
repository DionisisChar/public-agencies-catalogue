package com.dionChar.publicagencies.catalogue.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dionChar.publicagencies.catalogue.model.Ministry;



@Repository
public interface MinistryRepository extends JpaRepository<Ministry, Long> {

	Optional<Ministry> findByName(String name);
}
