package com.dionChar.publicagencies.catalogue.service.importExcel;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.dionChar.publicagencies.catalogue.dto.api.google.GooglePlaceDetailsResponseDTO;
import com.dionChar.publicagencies.catalogue.dto.importExcel.LocalOrganizationExcelRowDTO;
import com.dionChar.publicagencies.catalogue.dto.importExcel.PublicOrganizationExcelRowDTO;
import com.dionChar.publicagencies.catalogue.model.LegalForm;
import com.dionChar.publicagencies.catalogue.model.LocalAuthority;
import com.dionChar.publicagencies.catalogue.model.LocalOrganization;
import com.dionChar.publicagencies.catalogue.model.Ministry;
import com.dionChar.publicagencies.catalogue.model.PublicOrganization;
import com.dionChar.publicagencies.catalogue.model.enums.PublicSectorStatus;
import com.dionChar.publicagencies.catalogue.repository.LocalOrganizationRepository;
import com.dionChar.publicagencies.catalogue.repository.PublicOrganizationRepository;
import com.dionChar.publicagencies.catalogue.service.LegalFormService;
import com.dionChar.publicagencies.catalogue.service.LocalAuthorityService;
import com.dionChar.publicagencies.catalogue.service.MinistryService;
import com.dionChar.publicagencies.catalogue.service.external.google.GooglePlaceDetailsService;
import com.dionChar.publicagencies.catalogue.util.OrganizationNameUtils;

/**
 * Processor υπεύθυνος για την επεξεργασία των DTO γραμμών Excel και τη
 * μετατροπή τους σε Public ή Local Organization entities με πλήρη επιχειρησιακή
 * λογική (lookup + save).
 */
@Component
public class OrganizationImportProcessor {

	private final MinistryService ministryService;
	private final LegalFormService legalFormService;
	private final LocalAuthorityService localAuthorityService;
	private final GooglePlaceDetailsService googlePlaceDetailsService;
	private final PublicOrganizationRepository publicOrganizationRepository;
	private final LocalOrganizationRepository localOrganizationRepository;

	public OrganizationImportProcessor(MinistryService ministryService, LegalFormService legalFormService,
			LocalAuthorityService localAuthorityService, GooglePlaceDetailsService placeDetailsService,
			PublicOrganizationRepository publicOrganizationRepository,
			LocalOrganizationRepository localOrganizationRepository) {

		this.ministryService = ministryService;
		this.legalFormService = legalFormService;
		this.localAuthorityService = localAuthorityService;
		this.googlePlaceDetailsService = placeDetailsService;
		this.publicOrganizationRepository = publicOrganizationRepository;
		this.localOrganizationRepository = localOrganizationRepository;
	}

	/**
	 * Επεξεργάζεται ένα DTO τύπου PublicOrganization και αποθηκεύει το αντίστοιχο
	 * entity.
	 *
	 * @param dto Γραμμή από το Excel φύλλο "ΦΟΡΕΙΣ"
	 * @return Το αποθηκευμένο PublicOrganization
	 */

	public PublicOrganization processPublic(PublicOrganizationExcelRowDTO dto) {
		// 1. Εύρεση ή δημιουργία υπουργείου
		Ministry ministry = ministryService.findOrCreateByName(dto.getSupervisingMinistryName());

		// 2. Εύρεση ή δημιουργία νομικής μορφής (για PUBLIC)
		LegalForm legalForm = legalFormService.findOrCreate(dto.getLegalFormName());

		// 3. Έλεγχος appliesTo
		/*
		 * AppliesTo appliesTo = legalForm.getAppliesTo(); if (appliesTo !=
		 * AppliesTo.PUBLIC && appliesTo != AppliesTo.BOTH) { throw new
		 * IllegalArgumentException("Η νομική μορφή '" + legalForm.getName() +
		 * "' δεν ισχύει για Δημόσιο Οργανισμό."); }
		 */

		// 4. Μετατροπή ENUM ('ΕΝΤΟΣ' ή 'ΕΚΤΟΣ')
		PublicSectorStatus status = parsePublicSectorStatus(dto.getPublicSectorStatus());

		// 5. Δημιουργία entity
		String name = OrganizationNameUtils.removeAccentsOnly(dto.getName());
		PublicOrganization entity = new PublicOrganization(name, legalForm, status, ministry,
				dto.getPolicySector(), dto.getAdditionalInfo());

		// 7. Εμπλουτισμός με στοιχεία από Google Places
		Optional<GooglePlaceDetailsResponseDTO> OptionalPlaceDetails = googlePlaceDetailsService
				.getPlaceDetails(entity.getName());
		if (OptionalPlaceDetails.isPresent()) {
			GooglePlaceDetailsResponseDTO placeDetails = OptionalPlaceDetails.get();
			entity.setAddress(placeDetails.getFormattedAddress());
			entity.setPhoneNumber(placeDetails.getInternationalPhoneNumber());
			entity.setWebsite(placeDetails.getWebsiteUri());
			entity.setLatitude(placeDetails.getLatitude());
			entity.setLongitude(placeDetails.getLongitude());
		}

		// 6. Αποθήκευση
		return publicOrganizationRepository.save(entity);
	}

	public LocalOrganization processLocal(LocalOrganizationExcelRowDTO dto) {

		// 1. Ανάλυση πεδίου ΟΤΑ
		Set<String> authorityNames = extractLocalAuthorityNames(dto.getLocalAuthorityName());

		if (authorityNames.isEmpty()) {
			throw new IllegalArgumentException("Δεν εντοπίστηκαν ΟΤΑ στη γραμμή: " + dto.getName());
		}

		// 2. Lookup ή δημιουργία όλων των LocalAuthority entities
		Set<LocalAuthority> authorities = authorityNames.stream().map(localAuthorityService::findOrCreateByName)
				.collect(Collectors.toSet());

		// 3. Εύρεση ή δημιουργία νομικής μορφής
		LegalForm legalForm = legalFormService.findOrCreate(dto.getLegalFormName()); // allakseto

		// 4. Έλεγχος appliesTo
		/*
		 * AppliesTo appliesTo = legalForm.getAppliesTo(); if (appliesTo !=
		 * AppliesTo.LOCAL && appliesTo != AppliesTo.BOTH) { throw new
		 * IllegalArgumentException("Η νομική μορφή '" + legalForm.getName() +
		 * "' δεν ισχύει για ΟΤΑ."); }
		 */

		// 5. Μετατροπή ENUM ('ΕΝΤΟΣ' ή 'ΕΚΤΟΣ')
		PublicSectorStatus status = parsePublicSectorStatus(dto.getPublicSectorStatus());

		// 6. Δημιουργία entity
		String name = OrganizationNameUtils.removeAccentsOnly(dto.getName());
		LocalOrganization entity = new LocalOrganization(name, legalForm, status, authorities,
				dto.getAdditionalInfo());

		// 7. Εμπλουτισμός με στοιχεία από Google Places
		Optional<GooglePlaceDetailsResponseDTO> OptionalPlaceDetails = googlePlaceDetailsService
				.getPlaceDetails(entity.getName());
		if (OptionalPlaceDetails.isPresent()) {
			GooglePlaceDetailsResponseDTO placeDetails = OptionalPlaceDetails.get();
			entity.setAddress(placeDetails.getFormattedAddress());
			entity.setPhoneNumber(placeDetails.getInternationalPhoneNumber());
			entity.setWebsite(placeDetails.getWebsiteUri());
			entity.setLatitude(placeDetails.getLatitude());
			entity.setLongitude(placeDetails.getLongitude());
		}

		// 7. Αποθήκευση
		return localOrganizationRepository.save(entity);
	}

	private PublicSectorStatus parsePublicSectorStatus(String input) {
		if (input == null) {
			throw new IllegalArgumentException("Το πεδίο 'Υπαγωγή στο Δημόσιο Τομέα' είναι κενό.");
		}
		return switch (input.trim().toUpperCase()) {
		case "ΕΝΤΟΣ" -> PublicSectorStatus.ENTOS;
		case "ΕΚΤΟΣ" -> PublicSectorStatus.EKTOS;
		default -> throw new IllegalArgumentException("Άγνωστη τιμή: " + input);
		};
	}

	private Set<String> extractLocalAuthorityNames(String input) {
		Set<String> result = new LinkedHashSet<>();

		if (input == null || input.trim().isEmpty()) {
			return result;
		}

		input = input.trim();

		if (input.toUpperCase().startsWith("ΔΗΜΟΙ:")) {
			String afterColon = input.substring(input.indexOf(":") + 1).trim();
			String[] parts = afterColon.split(",");
			for (String part : parts) {
				String authorityName = "ΔΗΜΟΣ " + part.trim();
				result.add(authorityName);
			}
		} else {
			result.add(input.trim());
		}

		return result;
	}
}
