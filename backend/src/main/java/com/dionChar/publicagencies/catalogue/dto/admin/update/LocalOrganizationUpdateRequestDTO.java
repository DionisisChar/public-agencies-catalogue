package com.dionChar.publicagencies.catalogue.dto.admin.update;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LocalOrganizationUpdateRequestDTO {

	@NotBlank
	@Size(max = 1000)
	private String name;

	@NotNull
	private Long legalFormId;

	@NotNull
	private List<Long> localAuthorityIds;
	
	@NotBlank
	private String publicSectorStatus;
	
	 // Προαιρετικά πεδία
    private String address;
    private String phoneNumber;
    private String website;
    private String additionalInfo;

    // Getters & Setters

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

    public List<Long> getLocalAuthorityIds() {
        return localAuthorityIds;
    }

    public void setLocalAuthorityIds(List<Long> localAuthorityIds) {
        this.localAuthorityIds = localAuthorityIds;
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

}
