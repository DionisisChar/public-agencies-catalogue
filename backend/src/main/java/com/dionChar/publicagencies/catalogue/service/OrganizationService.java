package com.dionChar.publicagencies.catalogue.service;

import com.dionChar.publicagencies.catalogue.dto.admin.create.CreateOrganizationRequestDTO;
import com.dionChar.publicagencies.catalogue.dto.admin.edit.OrganizationEditResponseDTO;
import com.dionChar.publicagencies.catalogue.dto.admin.update.LocalOrganizationUpdateRequestDTO;
import com.dionChar.publicagencies.catalogue.dto.admin.update.PublicOrganizationUpdateRequestDTO;

public interface OrganizationService {

	/**
	 * Βασική μέθοδος για δημιουργεία PublicOrganization ή LocalOrganization. Η
	 * λογική διαχωρισμού έγγειται στο ότι αν έχει Ministry είναι PublicOrg
	 * αλλιώς αν έχει LocalAuthorities είναι LocalOrg.
	 */
	public void createOrganization(CreateOrganizationRequestDTO dto);

	public void deleteOrganization(Long id);
	
	OrganizationEditResponseDTO getOrganizationEditData(Long id);
	
	public void updatePublicOrganization(Long id, PublicOrganizationUpdateRequestDTO dto);
	
	public void updateLocalOrganization(Long id, LocalOrganizationUpdateRequestDTO dto);

}
