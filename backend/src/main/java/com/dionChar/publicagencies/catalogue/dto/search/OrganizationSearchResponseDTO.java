package com.dionChar.publicagencies.catalogue.dto.search;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

//@JsonInclude(JsonInclude.Include.NON_NULL) // Χρειάζομαι τα null γιατί η πληροφορία που 
											 // μεταδίδω είναι μικρή
											 // και κάνει τη δουλειά του front end πιο εύκολη (ξέρει τι έρχεται)
public class OrganizationSearchResponseDTO {

	private Long id;

	private String name;

	private String supervisingMinistryName;

	private List<String> supervisingLocalAuthorityNames;

	private String legalFormName;

	private String publicSectorStatus; // "ΕΝΤΟΣ" ή "ΕΚΤΟΣ"
	
	private String policySector;

	private String address;

	private String phoneNumber;

	private String website;

	private String additionalInfo;

	private Double latitude;
	private Double longitude;

	private String organizationType;

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

	public String getSupervisingMinistryName() {
		return supervisingMinistryName;
	}

	public void setSupervisingMinistryName(String supervisingMinistryName) {
		this.supervisingMinistryName = supervisingMinistryName;
	}

	public List<String> getSupervisingLocalAuthorityNames() {
		return supervisingLocalAuthorityNames;
	}

	public void setSupervisingLocalAuthorityNames(List<String> supervisingLocalAuthorityNames) {
		this.supervisingLocalAuthorityNames = supervisingLocalAuthorityNames;
	}

	public String getLegalFormName() {
		return legalFormName;
	}

	public void setLegalFormName(String legalFormName) {
		this.legalFormName = legalFormName;
	}

	public String getPublicSectorStatus() {
		return publicSectorStatus;
	}

	public void setPublicSectorStatus(String publicSectorStatus) {
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

	public String getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(String organizationType) {
		this.organizationType = organizationType;
	}

	public String getPolicySector() {
		return policySector;
	}

	public void setPolicySector(String policySector) {
		this.policySector = policySector;
	}

}
