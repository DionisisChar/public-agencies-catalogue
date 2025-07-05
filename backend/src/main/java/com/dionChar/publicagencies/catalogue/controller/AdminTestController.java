package com.dionChar.publicagencies.catalogue.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/test")
public class AdminTestController {
	
	@GetMapping
    public ResponseEntity<String> testAdminAccess() {
        return ResponseEntity.ok("✅ Έχεις πρόσβαση ως ADMIN!");
    }

}
