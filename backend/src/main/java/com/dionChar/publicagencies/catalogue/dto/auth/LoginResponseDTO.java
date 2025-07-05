package com.dionChar.publicagencies.catalogue.dto.auth;

public class LoginResponseDTO {

	private String email;
	private String fullName;
	private String role;

	public LoginResponseDTO(String email, String fullName, String role) {
		this.email = email;
		this.fullName = fullName;
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public String getFullName() {
		return fullName;
	}

	public String getRole() {
		return role;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
