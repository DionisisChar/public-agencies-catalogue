package com.dionChar.publicagencies.catalogue.dto.admin.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LocalAuthorityUpdateRequestDTO {
	

	@NotNull(message = "Το όνομα δεν μπορεί να είναι null.")
	@NotBlank(message = "Το όνομα δεν μπορεί να είναι κενό")
	@Size(min = 3, max = 255, message = "Το όνομα πρέπει να έχει από 3 έως 255 χαρακτήρες.")
	private String name;

	public LocalAuthorityUpdateRequestDTO() {}
	
	public LocalAuthorityUpdateRequestDTO(String name) {
		this.name =name;
	}
	
	public void setName(String name) {
		this.name =name;
	}
	
	public String getName() {
		return name;
	}
}
