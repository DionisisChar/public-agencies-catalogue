package com.dionChar.publicagencies.catalogue.model;

import com.dionChar.publicagencies.catalogue.model.enums.PublicSectorStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "organization")
public abstract class Organization {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false, unique = true, length = 1000) // Ίσως θέλει μεγαλύτερο length !!!!
	@NotBlank
	@Size(max = 1000)
	private String name;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "legal_form_id", nullable = false)
	@NotNull
	private LegalForm legalForm;

	@Enumerated(EnumType.STRING)
	@Column(name = "public_sector_status", nullable = false, length = 15)
	@NotNull
	private PublicSectorStatus publicSectorStatus;

	@Column(name = "address", length = 255)
	private String address;

	@Column(name = "phone_number", length = 100)
	private String phoneNumber;

	@Column(name = "website", length = 255)
	private String website;

	@Column(name = "additional_info", columnDefinition = "TEXT")
	private String additionalInfo;
	
	@Column(name = "latitude")
	private Double latitude;
	
	@Column(name= "longitude")
	private Double longitude;

	protected Organization() {
	}

	// Constructor με μόνο τα πεδία που προκύπτουν από Excel
	protected Organization(String name, LegalForm legalForm, PublicSectorStatus publicSectorStatus,String additionalInfo) {
		this.name = name;
		this.legalForm = legalForm;
		this.publicSectorStatus = publicSectorStatus;
		this.additionalInfo = additionalInfo;
	}

	// Πλήρης constructor (χρήσιμος για enrichment από Google API και εισαγωγή πλήρης εγγραφής)
	protected Organization(String name, LegalForm legalForm, PublicSectorStatus publicSectorStatus, String address,
			String phoneNumber, String website, String additionalInfo) {
		this.name = name;
		this.legalForm = legalForm;
		this.publicSectorStatus = publicSectorStatus;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.website = website;
		this.additionalInfo = additionalInfo;
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

	public LegalForm getLegalForm() {
		return legalForm;
	}

	public void setLegalForm(LegalForm legalForm) {
		this.legalForm = legalForm;
	}

	public PublicSectorStatus getPublicSectorStatus() {
		return publicSectorStatus;
	}

	public void setPublicSectorStatus(PublicSectorStatus publicSectorStatus) {
		this.publicSectorStatus = publicSectorStatus;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

}
