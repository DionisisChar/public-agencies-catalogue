package com.dionChar.publicagencies.catalogue.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dionChar.publicagencies.catalogue.model.LocalAuthority;

@Repository
public interface LocalAuthorityRepository extends JpaRepository<LocalAuthority, Long> {

	Optional<LocalAuthority> findByName(String name);

	// ΣΟΣ Δεν χρησιμοποιούμε Optional σε collections.
	List<LocalAuthority> findByNameContainingIgnoreCase(String namePart);

	// ✅ Για admin διαχείριση με pagination
	Page<LocalAuthority> findByNameContainingIgnoreCase(String namePart, Pageable pageable);
}
