package com.dionChar.publicagencies.catalogue.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dionChar.publicagencies.catalogue.model.Ministry;
import com.dionChar.publicagencies.catalogue.repository.MinistryRepository;

@Service
public class MinistryServiceImpl implements MinistryService {

	private final MinistryRepository ministryRepository;

	public MinistryServiceImpl(MinistryRepository ministryRepository) {
		this.ministryRepository = ministryRepository;
	}

	@Override
	public List<Ministry> findAll() {
		return ministryRepository.findAll();
	}

	// Να προσθέσω το trim όπως και στα άλλα firdOrCreateService για να αποφεύγω
	// μεγάλα κενά κλπ.
	/*
	 * String cleanedName = name.trim(); return
	 * ministryRepository.findByName(cleanedName) .orElseGet(() ->
	 * ministryRepository.save(new Ministry(cleanedName)));
	 */
	@Override
	public Ministry findOrCreateByName(String name) {
		return ministryRepository.findByName(name).orElseGet(() -> {
			Ministry ministry = new Ministry(name.trim().toUpperCase());
			return ministryRepository.save(ministry);
		});
	}

	// Είναι απλή crud λειτουργία οπότε θα αφήσω τον Controller
	// να διαχειριστεί το map από DTO σε Entity
	@Override
	public void updateMinistry(Long id, String newName) {
		Ministry ministry = ministryRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Υπουργείο με id " + id + " δεν βρέθηκε"));
		ministry.setName(newName.trim().toUpperCase());
		ministryRepository.save(ministry);
	}

	@Override
	public void deleteMinistry(Long id) {
		Ministry ministry = ministryRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Δεν βρέθηκε Υπουργείο με id: " + id));
		ministryRepository.delete(ministry);
	}

}
