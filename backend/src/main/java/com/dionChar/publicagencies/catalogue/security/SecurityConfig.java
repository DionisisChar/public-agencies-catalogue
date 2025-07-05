package com.dionChar.publicagencies.catalogue.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.dionChar.publicagencies.catalogue.security.filter.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final JwtAuthenticationFilter jwtAuthFilter;
	
	public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
		this.jwtAuthFilter = jwtAuthFilter;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						// 🔓 Δημόσια endpoints
						.requestMatchers("/api/auth/login",
								"/api/organizations/**",
				                "/api/dropdowns/**",
				                "/api/diavgeia/**").permitAll()
						// 🔒 Endpoints μόνο για ADMIN
					    .requestMatchers("/api/admin/**").hasRole("ADMIN")
						// 🔒 Admin endpoints απαιτούν authentication
						.anyRequest().authenticated()
						)
				.exceptionHandling(e -> e
						.authenticationEntryPoint((req, res, ex) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
						)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}

}
