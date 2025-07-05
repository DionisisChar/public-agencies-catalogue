package com.dionChar.publicagencies.catalogue.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dionChar.publicagencies.catalogue.dto.admin.create.CreateOrganizationRequestDTO;
import com.dionChar.publicagencies.catalogue.dto.admin.edit.OrganizationEditResponseDTO;
import com.dionChar.publicagencies.catalogue.dto.admin.update.LocalOrganizationUpdateRequestDTO;
import com.dionChar.publicagencies.catalogue.dto.admin.update.PublicOrganizationUpdateRequestDTO;
import com.dionChar.publicagencies.catalogue.dto.api.google.GooglePlaceDetailsResponseDTO;
import com.dionChar.publicagencies.catalogue.model.LegalForm;
import com.dionChar.publicagencies.catalogue.model.LocalAuthority;
import com.dionChar.publicagencies.catalogue.model.LocalOrganization;
import com.dionChar.publicagencies.catalogue.model.Ministry;
import com.dionChar.publicagencies.catalogue.model.Organization;
import com.dionChar.publicagencies.catalogue.model.PublicOrganization;
import com.dionChar.publicagencies.catalogue.model.enums.PublicSectorStatus;
import com.dionChar.publicagencies.catalogue.repository.LegalFormRepository;
import com.dionChar.publicagencies.catalogue.repository.LocalAuthorityRepository;
import com.dionChar.publicagencies.catalogue.repository.LocalOrganizationRepository;
import com.dionChar.publicagencies.catalogue.repository.MinistryRepository;
import com.dionChar.publicagencies.catalogue.repository.OrganizationRepository;
import com.dionChar.publicagencies.catalogue.repository.PublicOrganizationRepository;
import com.dionChar.publicagencies.catalogue.service.external.google.GooglePlaceDetailsService;
import com.dionChar.publicagencies.catalogue.util.UserInputNormalizer;

@Service
public class OrganizationServiceImpl implements OrganizationService {

	private final PublicOrganizationRepository publicOrganizationRepository;
	private final LocalOrganizationRepository localOrganizationRepository;
	private final OrganizationRepository organizationRepository;
	private final LegalFormRepository legalFormRepository;
	private final MinistryRepository ministryRepository;
	private final LocalAuthorityRepository localAuthorityRepository;

	private final LegalFormService legalFormService;
	private final MinistryService ministryService;
	private final LocalAuthorityService localAuthorityService;
	private final GooglePlaceDetailsService googlePlaceDetailsService;

	public OrganizationServiceImpl(PublicOrganizationRepository publicOrganizationRepository,
			LocalOrganizationRepository localOrganizationRepository, OrganizationRepository organizationRepository, LegalFormRepository legalFormRepository,
			MinistryRepository ministryRepository, LocalAuthorityRepository localAuthorityRepository,
			LegalFormService legalFormService, MinistryService ministryService,
			LocalAuthorityService localAuthorityService, GooglePlaceDetailsService googlePlaceDetailsService) {
		this.publicOrganizationRepository = publicOrganizationRepository;
		this.localOrganizationRepository = localOrganizationRepository;
		this.organizationRepository = organizationRepository;
		this.legalFormRepository = legalFormRepository;
		this.ministryRepository = ministryRepository;
		this.localAuthorityRepository = localAuthorityRepository;
		this.legalFormService = legalFormService;
		this.ministryService = ministryService;
		this.localAuthorityService = localAuthorityService;
		this.googlePlaceDetailsService = googlePlaceDetailsService;
	}

	@Override
	@Transactional
	public void createOrganization(CreateOrganizationRequestDTO dto) {

		// 🔹 1. Έλεγχος τι είδους οργανισμός δημιουργείται
		// TODO: να προσθέσω έλεγχο .isblank στο name toy ministry και να αλλάξω
		// κατάλληλα και το OrganizationCreateView.vue στη γραμμή 313 και 328 από null σε ""
		boolean hasMinistry = dto.getMinistryId() != null || dto.getMinistryName() != null;
		boolean hasLocalAuthority = (dto.getLocalAuthorityIds() != null && !dto.getLocalAuthorityIds().isEmpty())
				|| (dto.getLocalAuthorityNames() != null && !dto.getLocalAuthorityNames().isEmpty());
		System.out.println("Πέρασα το 1");
		// Giati me == kai oxi me =
		if (hasMinistry == hasLocalAuthority) {
			// Δεν πρέπει να υπάρχουν και τα δύο ή κανένα
			throw new IllegalArgumentException("Πρέπει να δοθεί είτε Υπουργείο είτε ΟΤΑ, αλλά όχι και τα δύο.");
		}
		System.out.println("Πέρασα το 1");
		// 🔹 2. LegalForm: είτε από id είτε με findOrCreate μέσω name
		LegalForm legalForm;
		if (dto.getLegalFormId() != null) {
			legalForm = legalFormRepository.findById(dto.getLegalFormId()).orElseThrow(
					() -> new IllegalArgumentException("Δεν βρέθηκε η νομική μορφή με id " + dto.getLegalFormId()));
		} else if (dto.getLegalFormName() != null) {
			legalForm = legalFormService.findOrCreate(dto.getLegalFormName());
		} else {
			throw new IllegalArgumentException("Πρέπει να οριστεί νομική μορφή.");
		}
		System.out.println("Πέρασα το 2");
		// 🔹 3. PublicSectorStatus: έρχεται ως string από dropdown → enum
		PublicSectorStatus publicSectorStatus;
		try {

			publicSectorStatus = PublicSectorStatus.valueOf(dto.getPublicSectorStatus().toUpperCase());

		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException("Μη έγκυρη τιμή για το πεδίο υπαγωγής στο Δημόσιο.");
		}
		System.out.println("Πέρασα το 3");
		// 🔹 4. Δημιουργία Public ή Local οργανισμού ανάλογα
		if (hasMinistry) {
			System.out.println("Μπήκα στο createPublic");
			createPublicOrganization(dto, legalForm, publicSectorStatus);
		} else {
			System.out.println("Μπήκα στο createLocal");
			createLocalOrganization(dto, legalForm, publicSectorStatus);

		}
	}

	// Γιατί χρησιμοποιώ απευθείας το Repository και όχι κάποιο service για getById
	// πχ.
	// 🔸 Δημιουργία PublicOrganization
	private void createPublicOrganization(CreateOrganizationRequestDTO dto, LegalForm legalForm,
			PublicSectorStatus publicSectorStatus) {
		Ministry ministry;

		if (dto.getMinistryId() != null) {
			ministry = ministryRepository.findById(dto.getMinistryId())
					.orElseThrow(() -> new IllegalArgumentException("Το υπουργείο δεν βρέθηκε."));
		} else {
			ministry = ministryService.findOrCreateByName(dto.getMinistryName());
		}
		System.out.println("Πάω για δημιουργία Public");

		String policySector = UserInputNormalizer.normalize(dto.getPolicySector());
		String address = UserInputNormalizer.normalize(dto.getAddress());
		String phoneNumber = UserInputNormalizer.normalize(dto.getPhoneNumber());
		String website = UserInputNormalizer.normalize(dto.getWebsite());
		String additionalInfo = UserInputNormalizer.normalize(dto.getAdditionalInfo());

		PublicOrganization publicOrg = new PublicOrganization(dto.getName().trim().toUpperCase(), legalForm,
				publicSectorStatus, ministry, policySector, address, phoneNumber,
				website, additionalInfo);
		
		// Ενημέρωση πεδίων απο Google Places Api αν ο χρήστης τα έχει αφήσει κενά (null)
		enrichWithGoogleDetails(publicOrg);
		
		publicOrganizationRepository.save(publicOrg);
		System.out.println("Τελείωσα δημιουργία Public");

	}

	// 🔸 Δημιουργία LocalOrganization
	private void createLocalOrganization(CreateOrganizationRequestDTO dto, LegalForm legalForm,
			PublicSectorStatus publicSectorStatus) {
		System.out.println("Είμαι στη δημιουργία Local");

		Set<LocalAuthority> localAuthorities = new HashSet<>();
		// Από επιλογή
		if (dto.getLocalAuthorityIds() != null) {
			for (Long id : dto.getLocalAuthorityIds()) {
				LocalAuthority localAuthority = localAuthorityRepository.findById(id)
						.orElseThrow(() -> new IllegalArgumentException("Δεν βρέθηκε ΟΤΑ με id " + id));
				localAuthorities.add(localAuthority);
			}
		}
		System.out.println("Πάω για δημιουργία Local");

		// Από εισαγωγή ονόματος OTA
		if (dto.getLocalAuthorityNames() != null) {
			for (String name : dto.getLocalAuthorityNames()) {
				LocalAuthority localAuthority = localAuthorityService.findOrCreateByName(name);
				localAuthorities.add(localAuthority);
			}
		}

		// Πρέπει να υπάρχει τουλάχιστον ένα
		if (localAuthorities.isEmpty()) {
			throw new IllegalArgumentException("Πρέπει να οριστεί τουλάχιστον ένας ΟΤΑ.");
		}

		// Προσοχή στη σειρά που τα βάζω...Αν δω κάτι ανάποδα μάλλον εδώ είναι το
		// πρόβλημα
		String address = UserInputNormalizer.normalize(dto.getAddress());
		String phoneNumber = UserInputNormalizer.normalize(dto.getPhoneNumber());
		String website = UserInputNormalizer.normalize(dto.getWebsite());
		String additionalInfo = UserInputNormalizer.normalize(dto.getAdditionalInfo());
		LocalOrganization localOrg = new LocalOrganization(dto.getName(), legalForm, publicSectorStatus,
				localAuthorities, address, phoneNumber, website, additionalInfo);
		
		// Ενημέρωση πεδίων απο Google Places Api αν ο χρήστης τα έχει αφήσει κενά (null)
		enrichWithGoogleDetails(localOrg);

		localOrganizationRepository.save(localOrg);
		System.out.println("Τελείωσα τη δημιουργία Local");

	}

	@Override
	@Transactional
	public void deleteOrganization(Long id) {
		if (publicOrganizationRepository.existsById(id)) {
			publicOrganizationRepository.deleteById(id);
		} else if (localOrganizationRepository.existsById(id)) {
			localOrganizationRepository.deleteById(id);
		} else {
			throw new IllegalArgumentException("Δεν βρέθηκε Οργανισμός/Υπηρεσία με id: " + id);
		}

	}

	
	// Ίσως χρειαστεί να επεκτείνω τη λογική για να μπορεί να ενημερώνει αυτόματα από GooglePlaces
	// ή θα φτιάξψ ξεχωριστή μέθοδο για το λόγο αυτό.
	@Override
	@Transactional
	public void updatePublicOrganization(Long id, PublicOrganizationUpdateRequestDTO dto) {
		// 1. Βρες την οντότητα με βάση το id
		PublicOrganization publicOrg = publicOrganizationRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Δεν βρέθηκε PublicOrganization με id: " + id));

		// 2. Βρες τη νομική μορφή
		LegalForm legalForm = legalFormRepository.findById(dto.getLegalFormId()).orElseThrow(
				() -> new IllegalArgumentException("Δεν βρέθηκε η νομική μορφή με id " + dto.getLegalFormId()));
		// 3. Βρες το υπουργείο
		Ministry ministry = ministryRepository.findById(dto.getMinistryId())
				.orElseThrow(() -> new IllegalArgumentException("Δεν βρέθηκε υπουργείο με id " + dto.getMinistryId()));

		// 4. Μετατροπή PublicSectorStatus
		PublicSectorStatus status;
		try {
			status = PublicSectorStatus.valueOf(dto.getPublicSectorStatus().toUpperCase());
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException("Μη έγκυρη τιμή για το πεδίο υπαγωγής στο Δημόσιο.");
		}

		// 5. Ενημέρωση πεδίων
		publicOrg.setName(dto.getName().trim().toUpperCase());
		publicOrg.setLegalForm(legalForm);
		publicOrg.setSupervisingMinistry(ministry);
		publicOrg.setPublicSectorStatus(status);
		publicOrg.setPolicySector(UserInputNormalizer.normalize(dto.getPolicySector()));
		publicOrg.setAddress(UserInputNormalizer.normalize(dto.getAddress()));
		publicOrg.setPhoneNumber(UserInputNormalizer.normalize(dto.getPhoneNumber()));
		publicOrg.setWebsite(UserInputNormalizer.normalize(dto.getWebsite()));
		publicOrg.setAdditionalInfo(UserInputNormalizer.normalize(dto.getAdditionalInfo()));

		// 6. Ενημέρωση πεδίων απο Google Places Api αν ο χρήστης τα έχει αφήσει κενά (null)
				enrichWithGoogleDetails(publicOrg);
				
		// 7. Αποθήκευση
		publicOrganizationRepository.save(publicOrg);
	}

	@Override
	@Transactional
	public void updateLocalOrganization(Long id, LocalOrganizationUpdateRequestDTO dto) {
		// 1. Βρες την οντότητα ως LocalOrganization
		LocalOrganization localOrg = localOrganizationRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Δεν βρέθηκε LocalOrganization με id: " + id));
		
		// 2. Βρες τη νομική μορφή
	    LegalForm legalForm = legalFormRepository.findById(dto.getLegalFormId())
	            .orElseThrow(() -> new IllegalArgumentException("Δεν βρέθηκε η νομική μορφή με id " + dto.getLegalFormId()));
	    
	    // 3. Βρες τα LocalAuthorities με βάση τα ids
	    Set<LocalAuthority> authorities = new HashSet<>();
	    if (dto.getLocalAuthorityIds() != null) {
	        for (Long authorityId : dto.getLocalAuthorityIds()) {
	            LocalAuthority authority = localAuthorityRepository.findById(authorityId)
	                    .orElseThrow(() -> new IllegalArgumentException("Δεν βρέθηκε ΟΤΑ με id " + authorityId));
	            authorities.add(authority);
	        }
	    }
	    
	    // 4. Μετατροπή PublicSectorStatus
	    PublicSectorStatus status;
	    try {
	        status = PublicSectorStatus.valueOf(dto.getPublicSectorStatus().toUpperCase());
	    } catch (IllegalArgumentException ex) {
	        throw new IllegalArgumentException("Μη έγκυρη τιμή για το πεδίο υπαγωγής στο Δημόσιο.");
	    }
	    
	    // 5. Ενημέρωση πεδίων
	    localOrg.setName(dto.getName().trim().toUpperCase());
	    localOrg.setLegalForm(legalForm);
	    localOrg.setSupervisingLocalAuthorities(authorities);
	    localOrg.setPublicSectorStatus(status);
	    localOrg.setAddress(UserInputNormalizer.normalize(dto.getAddress()));
	    localOrg.setPhoneNumber(UserInputNormalizer.normalize(dto.getPhoneNumber()));
	    localOrg.setWebsite(UserInputNormalizer.normalize(dto.getWebsite()));
	    localOrg.setAdditionalInfo(UserInputNormalizer.normalize(dto.getAdditionalInfo()));
	    
	    // 6. Ενημέρωση πεδίων απο Google Places Api αν ο χρήστης τα έχει αφήσει κενά (null)
	 		enrichWithGoogleDetails(localOrg);

	    // 7. Αποθήκευση
	    localOrganizationRepository.save(localOrg);
	}
	
	// Αυτόματος εμπλουτισμός πεδίων με δεδομένα από Google PlaceDetails Api
	// Μόνο και όπου ο χρήστης δεν έχει δώσει δικά του δεδομένα
	private void enrichWithGoogleDetails(Organization entity) {
		Optional<GooglePlaceDetailsResponseDTO> optPlaceDetails = googlePlaceDetailsService.getPlaceDetails(entity.getName());
		if(optPlaceDetails.isPresent()) {
			GooglePlaceDetailsResponseDTO placeDetails = optPlaceDetails.get();
			
			if(entity.getAddress() == null)
				entity.setAddress(placeDetails.getFormattedAddress());
			
			if(entity.getPhoneNumber() == null)
				entity.setPhoneNumber(placeDetails.getInternationalPhoneNumber());
			
			if(entity.getWebsite() == null)
				entity.setWebsite(placeDetails.getWebsiteUri());
			
			// Τα παρακάτω δεδομένα σετάρονται πάντα και αυτόματα
			entity.setLatitude(placeDetails.getLatitude());
			entity.setLongitude(placeDetails.getLongitude());
			
		}
	}

	
	// Θα δουλέψει??
	@Override
	@Transactional(readOnly = true)
	public OrganizationEditResponseDTO getOrganizationEditData(Long id) {
		Organization org = organizationRepository.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("Δεν βρέθηκε οργανισμός με id: " + id));

	        OrganizationEditResponseDTO dto = new OrganizationEditResponseDTO();
	        
	        dto.setId(org.getId());
	        dto.setName(org.getName());
	        dto.setLegalFormId(org.getLegalForm().getId());
	        dto.setPublicSectorStatus(org.getPublicSectorStatus().getLabel());
	        dto.setPolicySector(org instanceof PublicOrganization pub ? pub.getPolicySector() : null);
	        dto.setAddress(org.getAddress());
	        dto.setPhoneNumber(org.getPhoneNumber());
	        dto.setWebsite(org.getWebsite());
	        dto.setAdditionalInfo(org.getAdditionalInfo());
	        
	        if (org instanceof PublicOrganization pubOrg) {
	            dto.setOrganizationType("PUBLIC");
	            dto.setMinistryId(pubOrg.getSupervisingMinistry().getId());
	        } else if (org instanceof LocalOrganization localOrg) {
	            dto.setOrganizationType("LOCAL");
	            List<Long> ids = localOrg.getSupervisingLocalAuthorities()
	                    .stream()
	                    .map(LocalAuthority::getId)
	                    .toList();
	            dto.setLocalAuthorityIds(ids);
	        } else {
	            throw new IllegalStateException("Μη αναγνωρίσιμος τύπος οργανισμού");
	        }

	        return dto;
	}

}
