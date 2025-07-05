package com.dionChar.publicagencies.catalogue.dto.importExcel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO αναπαράστασης μίας γραμμής από το φύλλο Excel "ΦΟΡΕΙΣ". Περιέχει όλα τα
 * δεδομένα που απαιτούνται για τη δημιουργία PublicOrganization.
 *
 * Τα πεδία αντιστοιχούν άμεσα στις στήλες του Excel. Ο έλεγχος ορθότητας
 * (validation) εφαρμόζεται με Bean Validation annotations.
 */

public class PublicOrganizationExcelRowDTO {

	@NotBlank
	@Size(max = 1000)
	private String name;

	@NotBlank
	@Size(max = 255)
	private String supervisingMinistryName;

	@Size(max = 255)
	@NotNull
	private String legalFormName;

	@Size(max = 255)
	private String policySector;

	@NotBlank
	@NotNull
	private String publicSectorStatus; // Θα μετατραπεί σε ENUM στο service

	@Size(max = 1000)
	private String additionalInfo;

	// === Constructors ===

	public PublicOrganizationExcelRowDTO() {
	}

	/**
	 * Constructor για τη δημιουργία DTO από μία γραμμή του Excel. Χρησιμοποιείται
	 * όταν γίνεται parsing των κελιών του φύλλου "ΦΟΡΕΙΣ".
	 *
	 * @param name                    Ονομασία του οργανισμού (στήλη 1)
	 * @param supervisingMinistryName Εποπτεύον Υπουργείο (στήλη 2)
	 * @param legalFormName           Νομική μορφή (στήλη 3)
	 * @param policySector            Τομέας πολιτικής (προαιρετικό) (στήλη 4)
	 * @param publicSectorStatus      'ENTOS' ή 'EKTOS' (στήλη 5)
	 * @param additionalInfo          Πρόσθετες πληροφορίες (προαιρετικό) (στήλη 6)
	 */
	public PublicOrganizationExcelRowDTO(String name, String supervisingMinistryName, String legalFormName,
			String policySector, String publicSectorStatus, String additionalInfo) {
		this.name = name;
		this.supervisingMinistryName = supervisingMinistryName;
		this.legalFormName = legalFormName;
		this.policySector = policySector;
		this.publicSectorStatus = publicSectorStatus;
		this.additionalInfo = additionalInfo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSupervisingMinistryName() {
		return supervisingMinistryName;
	}

	public void setSupervisingMinistryName(String supervisingMinistryName) {
		this.supervisingMinistryName = supervisingMinistryName;
	}

	public String getLegalFormName() {
		return legalFormName;
	}

	public void setLegalFormName(String legalFormName) {
		this.legalFormName = legalFormName;
	}

	public String getPolicySector() {
		return policySector;
	}

	public void setPolicySector(String policySector) {
		this.policySector = policySector;
	}

	public String getPublicSectorStatus() {
		return publicSectorStatus;
	}

	public void setPublicSectorStatus(String publicSectorStatus) {
		this.publicSectorStatus = publicSectorStatus;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

}
