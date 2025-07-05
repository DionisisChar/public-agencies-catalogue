package com.dionChar.publicagencies.catalogue.dto.admin.edit;

import java.util.List;

/**
 * Δεν είναι γενικό getById, αλλά αποκλειστικά για edit use-case.
 */

public class OrganizationEditResponseDTO {

	
	private Long id;
    private String name;
    private Long legalFormId;
    private String publicSectorStatus;
    private String policySector;
    private String address;
    private String phoneNumber;
    private String website;
    private String additionalInfo;
    private String organizationType;

    // PUBLIC
    private Long ministryId;

    // LOCAL
    private List<Long> localAuthorityIds;

    // Getters – Setters

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

    public Long getLegalFormId() {
        return legalFormId;
    }

    public void setLegalFormId(Long legalFormId) {
        this.legalFormId = legalFormId;
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

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public Long getMinistryId() {
        return ministryId;
    }

    public void setMinistryId(Long ministryId) {
        this.ministryId = ministryId;
    }

    public List<Long> getLocalAuthorityIds() {
        return localAuthorityIds;
    }

    public void setLocalAuthorityIds(List<Long> localAuthorityIds) {
        this.localAuthorityIds = localAuthorityIds;
    }
}
