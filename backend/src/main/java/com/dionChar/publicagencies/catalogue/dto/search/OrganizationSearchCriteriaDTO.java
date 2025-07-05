package com.dionChar.publicagencies.catalogue.dto.search;

import java.util.List;

import com.dionChar.publicagencies.catalogue.model.enums.PublicSectorStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@JsonIgnoreProperties(ignoreUnknown = false)
public class OrganizationSearchCriteriaDTO {

	private String name;

    private Long legalFormId;

    private PublicSectorStatus publicSectorStatus;

    private List<Long> ministryIds;

    private List<Long> localAuthorityIds;

    @Min(0)
    private int page;

    @Max(100)
    private int size = 20; // Default τιμή

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

	public PublicSectorStatus getPublicSectorStatus() {
		return publicSectorStatus;
	}

	public void setPublicSectorStatus(PublicSectorStatus publicSectorStatus) {
		this.publicSectorStatus = publicSectorStatus;
	}

	public List<Long> getMinistryIds() {
		return ministryIds;
	}

	public void setMinistryIds(List<Long> ministryIds) {
		this.ministryIds = ministryIds;
	}

	public List<Long> getLocalAuthorityIds() {
		return localAuthorityIds;
	}

	public void setLocalAuthorityIds(List<Long> localAuthorityIds) {
		this.localAuthorityIds = localAuthorityIds;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
    
    
}
