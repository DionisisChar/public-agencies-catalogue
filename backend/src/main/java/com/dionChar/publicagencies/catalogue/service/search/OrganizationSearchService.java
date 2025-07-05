package com.dionChar.publicagencies.catalogue.service.search;

import java.util.List;

import org.springframework.data.domain.Page;

import com.dionChar.publicagencies.catalogue.dto.search.OrganizationMapSearchRequestDTO;
import com.dionChar.publicagencies.catalogue.dto.search.OrganizationSearchCriteriaDTO;
import com.dionChar.publicagencies.catalogue.dto.search.OrganizationSearchResponseDTO;

public interface OrganizationSearchService {
	
	Page<OrganizationSearchResponseDTO> search(OrganizationSearchCriteriaDTO criteria);
	
	List<OrganizationSearchResponseDTO> searchForMap(OrganizationMapSearchRequestDTO criteria);
	
	OrganizationSearchResponseDTO getOrganizationDetailsById(Long id);

}
