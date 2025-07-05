package com.dionChar.publicagencies.catalogue.dto.api.google;

public class GooglePlaceDetailsResponseDTO {

	private String formattedAddress;
	private String internationalPhoneNumber;
	private String websiteUri;
	private Double latitude;
	private Double longitude;

	public GooglePlaceDetailsResponseDTO() {
	}

	public GooglePlaceDetailsResponseDTO(String formattedAddress, String internationalPhoneNumber, String websiteUri,
			Double latitude, Double longitude) {
		this.formattedAddress = formattedAddress;
		this.internationalPhoneNumber = internationalPhoneNumber;
		this.websiteUri = websiteUri;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getFormattedAddress() {
		return formattedAddress;
	}

	public void setFormattedAddress(String formattedAddress) {
		this.formattedAddress = formattedAddress;
	}

	public String getInternationalPhoneNumber() {
		return internationalPhoneNumber;
	}

	public void setInternationalPhoneNumber(String internationalPhoneNumber) {
		this.internationalPhoneNumber = internationalPhoneNumber;
	}

	public String getWebsiteUri() {
		return websiteUri;
	}

	public void setWebsiteUri(String websiteUri) {
		this.websiteUri = websiteUri;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
}
