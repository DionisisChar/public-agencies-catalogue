package com.dionChar.publicagencies.catalogue.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequestDTO {

	@NotBlank(message = "Η διεύθυνση email είναι υποχρεωτική.")
	@Email(message = "Μη έγκυρη μορφή email.")
	private String email;

	@NotBlank(message = "Ο κωδικός πρόσβασης είναι υποχρεωτικός.")
	@Size(min = 6, max = 100, message = "Ο κωδικός πρέπει να έχει τουλάχιστον 6 χαρακτήρες.")
	private String password;

	public LoginRequestDTO() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
