package com.dionChar.publicagencies.catalogue.dto.admin.create;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateOrganizationRequestDTO {

	@NotBlank
	@Size(max = 1000)
	private String name;

	// Legal Form: μπορεί να έρθει είτε ως id είτε ως name (free text)
	// Δεν βάζω validation εδώ γιατί:
	// Αν έρθει legalFormId, αλλά το legalFormName είναι null, τότε θα σου σκάσει
	// validation error χωρίς λόγο.
	private Long legalFormId;
	private String legalFormName;

	// Ministry: είτε επιλέγεται από dropdown (id) είτε γράφεται (name)
	private Long ministryId;
	private String ministryName;

	// ΟΤΑ: είτε πολλαπλά ids είτε πολλαπλά ονόματα
	private List<Long> localAuthorityIds;
	private List<String> localAuthorityNames;

	// Public Sector Status: υποχρεωτικό μόνο αν πρόκειται για PublicOrganization
	private String publicSectorStatus;

	// policySector: μόνο για PublicOrganization
	private String policySector;

	// Προαιρετικά πεδία
	private String address;
	private String phoneNumber;
	private String website;
	private String additionalInfo;

	// Getters και Setters

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getLegalFormId() {
		return legalFormId;
	}

	public void setLegalFormId(Long legalFormId) {
		this.legalFormId = legalFormId;
	}

	public String getLegalFormName() {
		return legalFormName;
	}

	public void setLegalFormName(String legalFormName) {
		this.legalFormName = legalFormName;
	}

	public Long getMinistryId() {
		return ministryId;
	}

	public void setMinistryId(Long ministryId) {
		this.ministryId = ministryId;
	}

	public String getMinistryName() {
		return ministryName;
	}

	public void setMinistryName(String ministryName) {
		this.ministryName = ministryName;
	}

	public List<Long> getLocalAuthorityIds() {
		return localAuthorityIds;
	}

	public void setLocalAuthorityIds(List<Long> localAuthorityIds) {
		this.localAuthorityIds = localAuthorityIds;
	}

	public List<String> getLocalAuthorityNames() {
		return localAuthorityNames;
	}

	public void setLocalAuthorityNames(List<String> localAuthorityNames) {
		this.localAuthorityNames = localAuthorityNames;
	}

	public String getPublicSectorStatus() {
		return publicSectorStatus;
	}

	public void setPublicSectorStatus(String publicSectorStatus) {
		this.publicSectorStatus = publicSectorStatus;
	}

	public String getPolicySector() {
		return policySector;
	}

	public void setPolicySector(String policySector) {
		this.policySector = policySector;
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
}
