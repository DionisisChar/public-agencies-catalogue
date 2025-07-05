package com.dionChar.publicagencies.catalogue.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dionChar.publicagencies.catalogue.dto.common.OptionDTO;
import com.dionChar.publicagencies.catalogue.model.LocalAuthority;

public interface LocalAuthorityService {
	
	
	List<LocalAuthority> findAll(); // Για dropdown
	
	/**
     * Επιστρέφει υπάρχοντα ΟΤΑ (Δήμο / Περιφέρεια) με βάση το όνομα ή τον δημιουργεί αν δεν υπάρχει.
     *
     * @param name Όνομα του ΟΤΑ
     * @return Το entity LocalAuthority (υφιστάμενο ή νέο)
     */
    LocalAuthority findOrCreateByName(String name);
    
    List<LocalAuthority> searchByName(String query);
    
    Page<OptionDTO> searchByNamePaged(String query, Pageable pageable);
    
    public void updateLocalAuthority(Long id,String newName);
    
    public void deleteLocalAuthority(Long id);
    

}
