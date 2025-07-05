package com.dionChar.publicagencies.catalogue.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "legal_form")
public class LegalForm {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false, unique = true, length = 255)
	@NotBlank
	@Size(max = 255)
	private String name;

	/*@Enumerated(EnumType.STRING)
	@Column(name = "applies_to", nullable = false, length = 15)
	@NotNull
	private AppliesTo appliesTo;*/

	// Απαιτείται by JPA
	public LegalForm() {}

	/**
	 * Constructor για δημιουργία νομικής μορφής με όνομα και appliesTo scope.
	 *
	 * @param name      Όνομα νομικής μορφής (π.χ. "ΝΠΔΔ", "Α.Ε.", ή "Δεν
	 *                  προσδιορίστηκε")
	 * @param appliesTo Scope εφαρμογής: PUBLIC, LOCAL ή BOTH
	 */
	public LegalForm(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*public AppliesTo getAppliesTo() {
		return appliesTo;
	}

	public void setAppliesTo(AppliesTo appliesTo) {
		this.appliesTo = appliesTo;
	}*/

}
