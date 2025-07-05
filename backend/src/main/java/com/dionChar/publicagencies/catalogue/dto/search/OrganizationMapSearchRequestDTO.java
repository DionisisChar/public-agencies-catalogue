package com.dionChar.publicagencies.catalogue.dto.search;

import java.util.List;

import com.dionChar.publicagencies.catalogue.model.enums.PublicSectorStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = false)
public class OrganizationMapSearchRequestDTO {

	private String name;

	private Long legalFormId;

	private PublicSectorStatus publicSectorStatus;

	private List<Long> ministryIds;

	private List<Long> localAuthorityIds;

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

}
