package com.dionChar.publicagencies.catalogue.model;

import com.dionChar.publicagencies.catalogue.model.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "email", nullable = false, unique = true, length = 255)
	private String email;

	@Column(name = "password", nullable = false, length = 255)
	private String password;

	@Column(name = "fullName", length = 255)
	private String fullName;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false, length = 50)
	private Role role;
	
	@Column(name = "enabled", nullable = false)
	private boolean enabled = true;
	
	protected User() {}
	
	public User(String email, String password, String fullName, Role role, boolean enabled) {
		this.email = email;
		this.password = password;
		this.fullName = fullName;
		this.role = role;
		this.enabled = enabled;
	}
	
	public Long getId() {
		return id;
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

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
