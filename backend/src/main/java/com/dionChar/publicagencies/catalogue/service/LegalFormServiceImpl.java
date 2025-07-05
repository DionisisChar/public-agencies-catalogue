package com.dionChar.publicagencies.catalogue.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dionChar.publicagencies.catalogue.model.LegalForm;
import com.dionChar.publicagencies.catalogue.repository.LegalFormRepository;

@Service
public class LegalFormServiceImpl implements LegalFormService {

	private final LegalFormRepository legalFormRepository;

	public LegalFormServiceImpl(LegalFormRepository legalFormRepository) {
		this.legalFormRepository = legalFormRepository;
	}

	@Override
	public List<LegalForm> findAll() {
		return legalFormRepository.findAll();
	}

	/*@Override
	public LegalForm findOrCreateForPublic(String name) {
		// Αν είναι κενό ή null → "Δεν προσδιορίστηκε"
		String finalName = (name == null || name.trim().isEmpty()) ? "Δεν προσδιορίστηκε" : name.trim().toUpperCase();

		return legalFormRepository.findByName(finalName).orElseGet(() -> {
			LegalForm legalForm = new LegalForm(finalName);
			return legalFormRepository.save(legalForm);
		});
	}*/

	// Αφού έσβησα το appliesTo πλέον οι δυο μέθοδοι findOrCreateForLocal και
	// findOrCreateForPublic
	// είναι ακριβώς οι ίδιες. Οπότε χρειάζεται ενοποίηση.
	@Override
	public LegalForm findOrCreate(String name) {

		// Αν είναι κενό ή null → "Δεν προσδιορίστηκε"
		String finalName = (name == null || name.trim().isEmpty()) ? "Δεν προσδιορίστηκε" : name.trim().toUpperCase();

		return legalFormRepository.findByName(finalName).orElseGet(() -> {
			LegalForm legalForm = new LegalForm(finalName);
			return legalFormRepository.save(legalForm);
		});
	}

}
