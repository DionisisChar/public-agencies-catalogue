package com.dionChar.publicagencies.catalogue.model;

import java.util.Set;

import com.dionChar.publicagencies.catalogue.model.enums.PublicSectorStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "local_organization")
@PrimaryKeyJoinColumn(name = "id") // Shared PK με Organization
public class LocalOrganization extends Organization {

	//@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)// Αυτό άλλαξέ το 
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "local_organization_local_authority", // Join Table κάνει τη συσχέτιση
			joinColumns = @JoinColumn(name = "local_organization_id"), inverseJoinColumns = @JoinColumn(name = "local_authority_id"))
	@NotNull
	private Set<LocalAuthority> supervisingLocalAuthorities;

	// Default constructor (για JPA)
	public LocalOrganization() {
	}

	// Constructor για Excel
	public LocalOrganization(String name, LegalForm legalForm, PublicSectorStatus publicSectorStatus,
			Set<LocalAuthority> supervisingLocalAuthorities,String additionalInfo) {
		super(name, legalForm, publicSectorStatus,additionalInfo);
		this.supervisingLocalAuthorities = supervisingLocalAuthorities;
	}

	// Πλήρης constructor
	public LocalOrganization(String name, LegalForm legalForm, PublicSectorStatus publicSectorStatus,
			Set<LocalAuthority> supervisingLocalAuthorities, String address, String phoneNumber, String website,
			String additionalInfo) {
		super(name, legalForm, publicSectorStatus, address, phoneNumber, website, additionalInfo);
		this.supervisingLocalAuthorities = supervisingLocalAuthorities;
	}

	public Set<LocalAuthority> getSupervisingLocalAuthorities() {
		return supervisingLocalAuthorities;
	}

	public void setSupervisingLocalAuthorities(Set<LocalAuthority> supervisingLocalAuthorities) {
		this.supervisingLocalAuthorities = supervisingLocalAuthorities;
	}

}
