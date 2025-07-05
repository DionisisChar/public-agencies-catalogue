package com.dionChar.publicagencies.catalogue.security;

import org.springframework.stereotype.Service;

import com.dionChar.publicagencies.catalogue.dto.auth.LoginRequestDTO;
import com.dionChar.publicagencies.catalogue.dto.auth.LoginResponseDTO;
import com.dionChar.publicagencies.catalogue.model.User;
import com.dionChar.publicagencies.catalogue.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AuthServiceImpl implements AuthService {

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;

	public AuthServiceImpl(UserService userService,PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public LoginResponseDTO login(LoginRequestDTO request) {
		User user = userService.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("Δεν βρέθηκε χρήστης με αυτό το email."));
		
		if(!user.isEnabled()) {
			throw new IllegalStateException("Ο λογαριασμός του χρήστη είναι απενεργοποιημένος.");
		}
		
		if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new IllegalArgumentException("Μη έγκυρα διαπιστευτήρια.");
		}
		
		// Δεν το χρειάζομαι δε θα το βάλω σε localStorage αλλά μέσω Cookie μέσα σε headers
		//String token = jwtService.generateToken(user.getEmail());
		
		return new LoginResponseDTO (user.getEmail(),user.getFullName(),user.getRole().name());
	}

}
