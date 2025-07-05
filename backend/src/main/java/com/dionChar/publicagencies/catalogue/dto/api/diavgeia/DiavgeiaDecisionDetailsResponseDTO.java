package com.dionChar.publicagencies.catalogue.dto.api.diavgeia;

/**
 * To Σωστό DTO για πληροφορίες πράξεων από Διαύγεια Api
 * 
 * !! ΣΟΣ !! Να προσθέσω validation (μάλλον χειροκίνητα και όχι με Spring
 * Annotations) Να προσθέσω μετατροπές δεδομένων όπου χρειάζεται Δες το αρχείο
 * word " Διαχείριση null IssueDate και Μετατροπή "
 */

public class DiavgeiaDecisionDetailsResponseDTO {

	private String ada;
	private String subject;
	private Long issueDate;
	private String documentUrl;
	private String documentType;

	public DiavgeiaDecisionDetailsResponseDTO() {
	}

	public DiavgeiaDecisionDetailsResponseDTO(String ada, String subject, Long issueDate, String documentUrl,
			String documentType) {
		this.ada = ada;
		this.subject = subject;
		this.issueDate = issueDate;
		this.documentUrl = documentUrl;
		this.documentType = documentType;
	}

	public String getAda() {
		return ada;
	}

	public void setAda(String ada) {
		this.ada = ada;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Long getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Long issueDate) {
		this.issueDate = issueDate;
	}

	public String getDocumentUrl() {
		return documentUrl;
	}

	public void setDocumentUrl(String documentUrl) {
		this.documentUrl = documentUrl;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
}
