package com.dionChar.publicagencies.catalogue.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dionChar.publicagencies.catalogue.service.importExcel.ImportExcelService;

@RestController
@RequestMapping("/api/admin/import")
public class AdminImportExcelController {
	private final ImportExcelService importExcelService;

	public AdminImportExcelController(ImportExcelService importExcelService) {
		this.importExcelService = importExcelService;
	}

	@PostMapping("/excel")
	public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {
		try {
			System.out.println("📥 Έλαβε αρχείο: " + file.getOriginalFilename() + " (" + file.getSize() + " bytes)");
			importExcelService.importAllFromExcel(file);
			System.out.println("✅ Επιτυχής εισαγωγή από " + file.getOriginalFilename());
			return ResponseEntity.ok("✅ Το αρχείο επεξεργάστηκε επιτυχώς");
		} catch (Exception e) {
			System.err.println("❌ Σφάλμα κατά την επεξεργασία του αρχείου: " + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError()
					.body("❌ Σφάλμα κατά την επεξεργασία του αρχείου: " + e.getMessage());
		}
	}

}
