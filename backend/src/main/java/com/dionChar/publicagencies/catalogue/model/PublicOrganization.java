package com.dionChar.publicagencies.catalogue.model;

import com.dionChar.publicagencies.catalogue.model.enums.PublicSectorStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "public_organization")
@PrimaryKeyJoinColumn(name = "id") // Shared PK με Organization
public class PublicOrganization extends Organization {

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "supervising_ministry_id", nullable = false)
	@NotNull
	private Ministry supervisingMinistry;

	@Column(name = "policy_sector", length = 255)
	private String policySector;

	public PublicOrganization() {
	}

	// Constructor μόνο για Excel
	public PublicOrganization(String name, LegalForm legalForm, PublicSectorStatus publicSectorStatus,
			Ministry supervisingMinistry, String policySector,String additionalInfo) {
		super(name, legalForm, publicSectorStatus,additionalInfo);
		this.supervisingMinistry = supervisingMinistry;
		this.policySector = policySector;
	}

	// Πλήρης constructor (Google enrichment - πλήρης εγγραφή)
	public PublicOrganization(String name, LegalForm legalForm, PublicSectorStatus publicSectorStatus,
			Ministry supervisingMinistry, String policySector, String address, String phoneNumber, String website,
			String additionalInfo) {
		super(name, legalForm, publicSectorStatus, address, phoneNumber, website, additionalInfo);
		this.supervisingMinistry = supervisingMinistry;
		this.policySector = policySector;
	}

	public Ministry getSupervisingMinistry() {
		return supervisingMinistry;
	}

	public void setSupervisingMinistry(Ministry supervisingMinistry) {
		this.supervisingMinistry = supervisingMinistry;
	}

	public String getPolicySector() {
		return policySector;
	}

	public void setPolicySector(String policySector) {
		this.policySector = policySector;
	}

}
