package com.dionChar.publicagencies.catalogue.service;

import java.util.List;

import com.dionChar.publicagencies.catalogue.model.LegalForm;

public interface LegalFormService {

	
	List<LegalForm> findAll(); //Για Dropdown
	
	/**
	 * Επιστρέφει υπάρχουσα νομική μορφή με appliesTo = PUBLIC ή BOTH. Αν δεν
	 * υπάρχει, τη δημιουργεί ως PUBLIC.
	 *
	 * @param name Ονομασία νομικής μορφής (π.χ. "ΝΠΔΔ")
	 * @return Το entity LegalForm από τη βάση
	 */
	//LegalForm findOrCreateForPublic(String name);
	
	LegalForm findOrCreate(String name);
	

}
