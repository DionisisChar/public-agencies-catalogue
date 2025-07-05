package com.dionChar.publicagencies.catalogue.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.dionChar.publicagencies.catalogue.dto.common.OptionDTO;
import com.dionChar.publicagencies.catalogue.model.LegalForm;
import com.dionChar.publicagencies.catalogue.model.LocalAuthority;
import com.dionChar.publicagencies.catalogue.model.Ministry;

/**
 * Κοινή utility μέθοδος (mapper) που μετατρέπει τις Οντότητες σε OptionDTO.
 */

public class DropdownMapper {

	private DropdownMapper() {
        // utility class – no instantiation
    }

    public static OptionDTO fromLegalForm(LegalForm legalForm) {
        return new OptionDTO(legalForm.getId(), legalForm.getName());
    }

    public static OptionDTO fromMinistry(Ministry ministry) {
        return new OptionDTO(ministry.getId(), ministry.getName());
    }

    public static OptionDTO fromLocalAuthority(LocalAuthority localAuthority) {
        return new OptionDTO(localAuthority.getId(), localAuthority.getName());
    }

    public static List<OptionDTO> fromLegalForms(List<LegalForm> legalForms) {
        return legalForms.stream().map(DropdownMapper::fromLegalForm).collect(Collectors.toList());
    }

    public static List<OptionDTO> fromMinistries(List<Ministry> ministries) {
        return ministries.stream().map(DropdownMapper::fromMinistry).collect(Collectors.toList());
    }

    public static List<OptionDTO> fromLocalAuthorities(List<LocalAuthority> localAuthorities) {
        return localAuthorities.stream().map(DropdownMapper::fromLocalAuthority).collect(Collectors.toList());
    }
}
