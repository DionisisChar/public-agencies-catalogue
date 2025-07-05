package com.dionChar.publicagencies.catalogue.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	// Secret key (256-bit) από properties
	private final SecretKey secretKey;

	public JwtService(@Value("${app.jwt.secret}") String secret) {
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	// ⏱ Διάρκεια ισχύος token (π.χ. 1 ώρα)
	private static final long EXPIRATION_TIME_MS = 60 * 60 * 1000;

	
	/**
     * Δημιουργεί ένα JWT token με subject το email.
     */
	public String generateToken(String subjectEmail) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + EXPIRATION_TIME_MS);

		return Jwts.builder()
				.setSubject(subjectEmail)
				.setIssuedAt(now)
				.setExpiration(expiry)
				.signWith(secretKey)
				.compact();
	}

	 /**
     * Εξάγει το email (subject) από το JWT token.
     */
	public String extractUserName(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build().parseClaimsJws(token)
				.getBody()
				.getSubject();
	}


	/**
     * Ελέγχει αν το token είναι έγκυρο:
     * - Το email που κουβαλάει είναι το αναμενόμενο.
     * - Δεν έχει λήξει.
     */
	public boolean isTokenValid(String token, String expectedEmail) {
		try {
			
			Claims claims = Jwts.parserBuilder()
					.setSigningKey(secretKey)
					.build()
					.parseClaimsJws(token)
					.getBody();
							
			String actualEmail = claims.getSubject();
			Date expiration = claims.getExpiration();
			
			return actualEmail.equals(expectedEmail) && expiration.after(new Date());
		
		} catch (Exception e) {
			return false;
		}
	}

}
