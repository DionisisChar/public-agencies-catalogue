package com.dionChar.publicagencies.catalogue.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dionChar.publicagencies.catalogue.dto.common.OptionDTO;
import com.dionChar.publicagencies.catalogue.mapper.DropdownMapper;
import com.dionChar.publicagencies.catalogue.model.enums.PublicSectorStatus;
import com.dionChar.publicagencies.catalogue.service.LegalFormService;
import com.dionChar.publicagencies.catalogue.service.LocalAuthorityService;
import com.dionChar.publicagencies.catalogue.service.MinistryService;


/**
 * Controller που κάνει expose endpoints υπεύθυνα για την προβολή
 * dropdown επιλογών με Ονόματα Ministry, LocalAuthority, LegalForm
 * και 
 * publicSectorStatus ΕΝΤΟΣ / ΕΚΤΟΣ
 * 
 * Μπορεί να χρησιμοποιηθεί όπου χρειάζομαι dropdown
 */

@RestController
@RequestMapping("/api/dropdowns")
public class DropdownController {

	private final MinistryService ministryService;
    private final LocalAuthorityService localAuthorityService;
    private final LegalFormService legalFormService;

    public DropdownController(
            MinistryService ministryService,
            LocalAuthorityService localAuthorityService,
            LegalFormService legalFormService
    ) {
        this.ministryService = ministryService;
        this.localAuthorityService = localAuthorityService;
        this.legalFormService = legalFormService;
    }

    @GetMapping("/ministries")
    public List<OptionDTO> getMinistries() {
        return DropdownMapper.fromMinistries(ministryService.findAll());
    }

    // Φέρνει δυναμικά τις LocalAuthorities σύμφωνα με αυτό που πληκτρολογεί ο χρήστης
    // Καλύτερη εμπειρία UI καθώς η λίστα LocalAuthorities είναι πολύ μεγάλη.
    @GetMapping("/local-authorities/search")
    public List<OptionDTO> getLocalAuthorities(@RequestParam String query) {
        return DropdownMapper.fromLocalAuthorities(localAuthorityService.searchByName(query));
    }

    @GetMapping("/legal-forms")
    public List<OptionDTO> getLegalForms() {
        return DropdownMapper.fromLegalForms(legalFormService.findAll());
    }

    @GetMapping("/public-sector-status")
    public List<String> getPublicSectorStatusOptions() {
        return Arrays.stream(PublicSectorStatus.values())
                     .map(Enum::name)
                     .toList();
    }
	
}
