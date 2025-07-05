package com.dionChar.publicagencies.catalogue.service;

import java.util.List;

import com.dionChar.publicagencies.catalogue.model.Ministry;

public interface MinistryService {
	
	
	List<Ministry> findAll();

	/**
	 * Επιστρέφει υπάρχον υπουργείο με βάση το όνομα ή το δημιουργεί αν δεν υπάρχει.
	 *
	 * @param name Όνομα υπουργείου (π.χ. "Υπουργείο Παιδείας")
	 * @return Το αντίστοιχο entity από τη βάση (είτε υπήρχε ήδη είτε μόλις
	 *         δημιουργήθηκε)
	 */
	Ministry findOrCreateByName(String name);
	
	void updateMinistry(Long id,String newName);

	void deleteMinistry(Long id);

}
