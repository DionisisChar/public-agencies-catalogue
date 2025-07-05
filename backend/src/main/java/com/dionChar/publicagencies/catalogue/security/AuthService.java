package com.dionChar.publicagencies.catalogue.security;

import com.dionChar.publicagencies.catalogue.dto.auth.LoginRequestDTO;
import com.dionChar.publicagencies.catalogue.dto.auth.LoginResponseDTO;

/**
 * Το interface ορίζει μία μέθοδο login, η οποία:
 * 
 * Δέχεται ένα LoginRequestDTO (με email + password),
 * 
 * Επιστρέφει ένα LoginResponseDTO (με τα στοιχεία του χρήστη).
 * 
 * 
 */
public interface AuthService {

	LoginResponseDTO login(LoginRequestDTO request);
}
