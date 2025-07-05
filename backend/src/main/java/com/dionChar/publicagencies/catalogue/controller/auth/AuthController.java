package com.dionChar.publicagencies.catalogue.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dionChar.publicagencies.catalogue.dto.auth.LoginRequestDTO;
import com.dionChar.publicagencies.catalogue.dto.auth.LoginResponseDTO;
import com.dionChar.publicagencies.catalogue.security.AuthService;
import com.dionChar.publicagencies.catalogue.security.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;
	private final JwtService jwtService;

	public AuthController(AuthService authService, JwtService jwtService) {
		this.authService = authService;
		this.jwtService = jwtService;
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request,
			HttpServletResponse response) {

		// 1. Επιχειρησιακή επιβεβαίωση credentials
		LoginResponseDTO userDto = authService.login(request);

		// 2. Δημιουργία JWT token
		String token = jwtService.generateToken(userDto.getEmail());

		// 3. Δημιουργία HttpOnly cookie
		Cookie cookie = new Cookie("jwt", token);
		cookie.setHttpOnly(true);
		cookie.setPath("/"); // προσβάσιμο σε όλη την εφαρμογή
		cookie.setMaxAge(60 * 60);
		
        // 4. Προσθήκη στο response
		response.addCookie(cookie);
		
        // 5. Επιστροφή user info χωρίς το token
		return ResponseEntity.ok(userDto);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletResponse response){
		Cookie cookie = new Cookie("jwt", ""); // change cookie to no token
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setMaxAge(0); // killing the cookie
		response.addCookie(cookie);
		return ResponseEntity.noContent().build();
	}

}
