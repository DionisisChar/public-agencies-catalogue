package com.dionChar.publicagencies.catalogue.security.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dionChar.publicagencies.catalogue.model.User;
import com.dionChar.publicagencies.catalogue.security.JwtService;
import com.dionChar.publicagencies.catalogue.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserService userService;

	public JwtAuthenticationFilter(JwtService jwtService, UserService userService) {
		this.jwtService = jwtService;
		this.userService = userService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// 1. Παίρνω το JWT από cookie
		String token = extractJwtFromCookies(request);

		if (token == null || token.isBlank()) {
			filterChain.doFilter(request, response);
			return;
		}

		// 2. Παίρνω το email από το token
		String email = jwtService.extractUserName(token);

		// 3. Ελέγχω αν είναι έγκυρο και χρήστης ενεργός
		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			Optional<User> userOpt = userService.findByEmail(email);

			if (userOpt.isPresent() && jwtService.isTokenValid(token, userOpt.get().getEmail())) {
				User user = userOpt.get();

				// Για να παίρνει δυναμικά τους ρόλους ανάλογα με τον κάθε χρήστη
				String roleName = user.getRole().name();
				SimpleGrantedAuthority authority = new SimpleGrantedAuthority(("ROLE_" + roleName));

				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						user, null,
						List.of(authority));

				SecurityContextHolder.getContext().setAuthentication(authToken);
				System.out.println("🎭 Συνδεδεμένος ρόλος: " + authority.getAuthority());
			}
			System.out.println("➡️ Νέο request: " + request.getMethod() + " " + request.getRequestURI());
		}

		filterChain.doFilter(request, response);
	}

	private String extractJwtFromCookies(HttpServletRequest request) {
		if (request.getCookies() == null) {
			return null;
		}

		return Arrays.stream(request.getCookies()).filter(c -> c.getName().equals("jwt")).map(Cookie::getValue)
				.findFirst().orElse(null);

	}

}
