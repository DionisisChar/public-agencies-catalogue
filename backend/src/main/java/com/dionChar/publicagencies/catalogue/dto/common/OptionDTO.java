package com.dionChar.publicagencies.catalogue.dto.common;


/**
 * Κοινό DTO υπεύθυνο για τα dropDown μενου (id + label).
 * 
 */

public class OptionDTO {

	private Long id;
    private String label;

    public OptionDTO(Long id, String label) {
        this.id = id;
        this.label = label;
    }

    public Long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }
}
