package com.dionChar.publicagencies.catalogue.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dionChar.publicagencies.catalogue.dto.common.OptionDTO;
import com.dionChar.publicagencies.catalogue.model.LocalAuthority;
import com.dionChar.publicagencies.catalogue.repository.LocalAuthorityRepository;

@Service
public class LocalAuthorityServiceImpl implements LocalAuthorityService {

	private final LocalAuthorityRepository localAuthorityRepository;

	public LocalAuthorityServiceImpl(LocalAuthorityRepository localAuthorityRepository) {
		this.localAuthorityRepository = localAuthorityRepository;
	}

	@Override
	public List<LocalAuthority> findAll() {
		return localAuthorityRepository.findAll();
	}

	@Override
	public LocalAuthority findOrCreateByName(String name) {
		return localAuthorityRepository.findByName(name)
				.orElseGet(() -> localAuthorityRepository.save(new LocalAuthority(name.trim().toUpperCase())));
	}

	@Override
	public List<LocalAuthority> searchByName(String query) {
		if (query == null || query.trim().isEmpty()) {
			return localAuthorityRepository.findAll();
		}
		return localAuthorityRepository.findByNameContainingIgnoreCase(query);
	}

	@Override
	public void updateLocalAuthority(Long id, String newName) {
		LocalAuthority localAuthority = localAuthorityRepository.findById(id).orElseThrow(
				() -> new IllegalArgumentException("Το εποπτεύον ΟΤΑ με id:" + " " + id + " δεν βρέθηκε."));
		localAuthority.setName(newName.trim().toUpperCase());
		localAuthorityRepository.save(localAuthority);

	}

	@Override
	public void deleteLocalAuthority(Long id) {
		LocalAuthority localAuthority = localAuthorityRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Δεν βρέθηκε εποπτεύον ΟΤΑ με id: " + id));
		localAuthorityRepository.delete(localAuthority);
	}

	@Override
	public Page<OptionDTO> searchByNamePaged(String query, Pageable pageable) {
		 Page<LocalAuthority> page;
	        if (query == null || query.trim().isEmpty()) {
	            page = localAuthorityRepository.findAll(pageable);
	        } else {
	            page = localAuthorityRepository.findByNameContainingIgnoreCase(query.trim(), pageable);
	        }

	        return page.map(la -> new OptionDTO(la.getId(), la.getName()));
	    }
	

}
