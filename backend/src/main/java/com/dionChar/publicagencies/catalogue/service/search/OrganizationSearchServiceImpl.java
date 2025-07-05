package com.dionChar.publicagencies.catalogue.service.search;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dionChar.publicagencies.catalogue.dto.search.OrganizationMapSearchRequestDTO;
import com.dionChar.publicagencies.catalogue.dto.search.OrganizationSearchCriteriaDTO;
import com.dionChar.publicagencies.catalogue.dto.search.OrganizationSearchResponseDTO;
import com.dionChar.publicagencies.catalogue.model.LocalAuthority;
import com.dionChar.publicagencies.catalogue.model.LocalOrganization;
import com.dionChar.publicagencies.catalogue.model.Organization;
import com.dionChar.publicagencies.catalogue.model.PublicOrganization;
import com.dionChar.publicagencies.catalogue.repository.OrganizationRepository;
import com.dionChar.publicagencies.catalogue.repository.specification.OrganizationSpecification;

import jakarta.persistence.EntityNotFoundException;

/**
 * Το Βασικό μου Search με κριτήρια από χρήστη (OrganizationSearchCriteriaDTO)
 * Θα το χρησιμοποιήσω και για update/delete από ADMIN (Ψάχνει/Βρήσκει Εκτελεί)
 */

@Service
public class OrganizationSearchServiceImpl implements OrganizationSearchService {

	private final OrganizationRepository organizationRepository;

	public OrganizationSearchServiceImpl(OrganizationRepository organizationRepository) {
		this.organizationRepository = organizationRepository;
	}

	@Override
	public Page<OrganizationSearchResponseDTO> search(OrganizationSearchCriteriaDTO criteria) {
		Specification<Organization> spec = OrganizationSpecification.buildSearchSpec(criteria);
		Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize());

		return organizationRepository.findAll(spec, pageable).map(this::mapToSearchResponseDTO); // Χρησιμοποιώ
																									// private method
																									// για mapping
	}
	
	@Override
	public List<OrganizationSearchResponseDTO> searchForMap(OrganizationMapSearchRequestDTO criteria) {
		Specification<Organization> spec = OrganizationSpecification.buildMapSearchSpec(criteria);
		return organizationRepository.findAll(spec).stream().map(this::mapToSearchResponseDTO).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public OrganizationSearchResponseDTO getOrganizationDetailsById(Long id) {
		 Organization org = organizationRepository.findById(id)
			        .orElseThrow(() -> new EntityNotFoundException("Ο οργανισμός δεν βρέθηκε με id: " + id));

			    return mapToSearchResponseDTO(org);
	}
	
	
	// Να βγάλω το mapper σε Ξεχωριστή κλάση για reusability
	private OrganizationSearchResponseDTO mapToSearchResponseDTO(Organization org) {
		OrganizationSearchResponseDTO dto = new OrganizationSearchResponseDTO();

		dto.setId(org.getId());
		dto.setName(org.getName());
		dto.setLegalFormName(org.getLegalForm().getName());
		dto.setPublicSectorStatus(org.getPublicSectorStatus().getLabel());
		dto.setAdditionalInfo(org.getAdditionalInfo());
		dto.setAddress(org.getAddress());
		dto.setWebsite(org.getWebsite());
		dto.setPhoneNumber(org.getPhoneNumber());
		dto.setLatitude(org.getLatitude());
		dto.setLongitude(org.getLongitude());

		// Aν είναι PublicOrg
		if (org instanceof PublicOrganization publicOrg) {
			dto.setOrganizationType("PUBLIC");
			dto.setSupervisingMinistryName(publicOrg.getSupervisingMinistry().getName());
			dto.setPolicySector(publicOrg.getPolicySector());
		}

		// Αν είνια Local
		// Μάλλον να το αλλάξω και να παω με stream() 
		if (org instanceof LocalOrganization localOrg) {
			dto.setOrganizationType("LOCAL");
			List<String> names = new ArrayList<>();

			for (LocalAuthority authority : localOrg.getSupervisingLocalAuthorities()) {
				names.add(authority.getName());
			}
			dto.setSupervisingLocalAuthorityNames(names);

			/*
			 * 
			 * List<String> names = localOrg.getSupervisingLocalAuthorities() .stream()
			 * .map(LocalAuthority::getName) .toList();
			 * dto.setSupervisingLocalAuthorityNames(names);
			 */
		}
		return dto;

	}

}
