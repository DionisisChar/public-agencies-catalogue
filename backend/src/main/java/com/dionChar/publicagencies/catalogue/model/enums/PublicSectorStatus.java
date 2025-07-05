package com.dionChar.publicagencies.catalogue.model.enums;

/**
 * Αν κάπου έχω θέματα ίσως να το ξαναγυυρίσω σε ΕΚΤΟΣ, ΕΝΤΟΣ χωρίς label και constructor
 */

public enum PublicSectorStatus {
	
	ENTOS("ΕΝΤΟΣ"),
	EKTOS("ΕΚΤΟΣ");
	
	private final String label;
	
	PublicSectorStatus(String label){
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
	
	
}
