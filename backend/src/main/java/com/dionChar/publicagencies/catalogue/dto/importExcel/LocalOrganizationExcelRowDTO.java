package com.dionChar.publicagencies.catalogue.dto.importExcel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO αναπαράστασης μίας γραμμής από το φύλλο Excel "ΦΟΡΕΙΣ ΟΤΑ" για
 * LocalOrganization. Περιέχει τις στήλες που απαιτούνται για την εισαγωγή ενός
 * τοπικού οργανισμού (ΟΤΑ).
 */
public class LocalOrganizationExcelRowDTO {

	@NotBlank
	@Size(max = 1000)
	@NotNull
	private String name;

	@NotBlank
	@Size(max = 255)
	private String localAuthorityName; // Από το Excel: ΟΤΑ

	@NotBlank
	@Size(max = 255)
	private String legalFormName;

	@NotBlank
	private String publicSectorStatus;

	@Size(max = 1000)
	private String additionalInfo;

	public LocalOrganizationExcelRowDTO() {
	}

	/**
	 * Constructor για χρήση κατά την ανάγνωση από το Excel.
	 *
	 * @param name               Ονομασία του οργανισμού
	 * @param localAuthorityName ΟΤΑ (Δήμος / Περιφέρεια)
	 * @param legalFormName      Νομική μορφή
	 * @param publicSectorStatus ΕΝΤΟΣ ή ΕΚΤΟΣ
	 * @param additionalInfo     Πρόσθετες πληροφορίες
	 */
	public LocalOrganizationExcelRowDTO(String name, String localAuthorityName, String legalFormName,
			String publicSectorStatus, String additionalInfo) {
		this.name = name;
		this.localAuthorityName = localAuthorityName;
		this.legalFormName = legalFormName;
		this.publicSectorStatus = publicSectorStatus;
		this.additionalInfo = additionalInfo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocalAuthorityName() {
		return localAuthorityName;
	}

	public void setLocalAuthorityName(String localAuthorityName) {
		this.localAuthorityName = localAuthorityName;
	}

	public String getLegalFormName() {
		return legalFormName;
	}

	public void setLegalFormName(String legalFormName) {
		this.legalFormName = legalFormName;
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
