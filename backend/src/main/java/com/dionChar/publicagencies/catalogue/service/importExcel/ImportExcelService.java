package com.dionChar.publicagencies.catalogue.service.importExcel;

import com.dionChar.publicagencies.catalogue.dto.importExcel.LocalOrganizationExcelRowDTO;
import com.dionChar.publicagencies.catalogue.dto.importExcel.PublicOrganizationExcelRowDTO;
import com.dionChar.publicagencies.catalogue.model.LocalOrganization;
import com.dionChar.publicagencies.catalogue.model.PublicOrganization;
import com.dionChar.publicagencies.catalogue.repository.LocalOrganizationRepository;
import com.dionChar.publicagencies.catalogue.repository.PublicOrganizationRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImportExcelService {

	// Να προσθέσω και για LocalOrg
	private final Validator validator;
	private final OrganizationImportProcessor organizationProcessor;
	private final PublicOrganizationRepository publicOrgRepo;
	private final LocalOrganizationRepository localOrgRepo;

	// Constructor να προσθέσω και για LocalOrg
	public ImportExcelService(Validator validator, OrganizationImportProcessor organizationProcessor,
			PublicOrganizationRepository publicOrgRepo, LocalOrganizationRepository localOrgRepo) {
		this.validator = validator;
		this.organizationProcessor = organizationProcessor;
		this.publicOrgRepo = publicOrgRepo;
		this.localOrgRepo = localOrgRepo;
	}

	/**
	 * Διαβάζει το Excel από HTTP upload (MultipartFile) και εισάγει τους
	 * οργανισμούς.
	 *
	 * @param file το αρχείο Excel που ανέβηκε από τον admin
	 */
	public void importAllFromExcel(MultipartFile file) {
		try (InputStream input = file.getInputStream(); Workbook workbook = WorkbookFactory.create(input)) {
			importPublicOrganizationsFromSheet(workbook.getSheet("ΦΟΡΕΙΣ"));
			importLocalOrganizationsFromSheet(workbook.getSheet("ΦΟΡΕΙΣ ΟΤΑ"));
		} catch (Exception e) {
			System.err.println("❌ Σφάλμα κατά την ανάγνωση του αρχείου: " + e.getMessage());
			throw new RuntimeException("Αποτυχία στην επεξεργασία του Excel", e);
		}
	}

	/**
	 * Διαβάζει το Excel από τοπικό path και εισάγει τις εγγραφές PublicOrganization
	 * από το φύλλο "ΦΟΡΕΙΣ".
	 *
	 * @param filePath πλήρης τοπική διαδρομή του αρχείου .xlsx
	 */

	public void importAllFromExcel(String path) {
		try (InputStream input = new FileInputStream(path); Workbook workbook = WorkbookFactory.create(input)) {

			importPublicOrganizationsFromSheet(workbook.getSheet("ΦΟΡΕΙΣ"));
			importLocalOrganizationsFromSheet(workbook.getSheet("ΦΟΡΕΙΣ ΟΤΑ"));

		} catch (Exception e) {
			System.err.println("❌ Σφάλμα κατά την ανάγνωση του αρχείου: " + e.getMessage());
		}
	}

	public void importPublicOrganizationsFromSheet(Sheet sheet) {
		if (sheet == null) {
			System.err.println("❌ Το φύλλο 'ΦΟΡΕΙΣ' δεν βρέθηκε στο αρχείο.");
			return;
		}

		int rowIndex = 0;
		for (Row row : sheet) {
			if (rowIndex++ == 0)
				continue; // παράκαμψη επικεφαλίδας

			PublicOrganizationExcelRowDTO dto = mapRowToPublicDTO(row);

			Set<ConstraintViolation<PublicOrganizationExcelRowDTO>> violations = validator.validate(dto);
			if (!violations.isEmpty()) {
				System.err.println("⚠️ Σφάλματα validation στη γραμμή " + rowIndex + ": " + violations);
				continue;
			}

			try {
				PublicOrganization entity = organizationProcessor.processPublic(dto);
				publicOrgRepo.save(entity);
				System.out.println("✅ Αποθηκεύτηκε: " + entity.getName());
			} catch (Exception e) {
				System.err.println("❌ Σφάλμα εισαγωγής στη γραμμή " + rowIndex + ": " + e.getMessage());
			}
		}
	}

	// ✅ Νέα overloaded μέθοδος με όριο !!! SOS TO BE DELETED
	public void importPublicOrganizationsFromSheet(Sheet sheet, int maxRows) {
		if (sheet == null) {
			System.err.println("❌ Το φύλλο 'ΦΟΡΕΙΣ' δεν βρέθηκε στο αρχείο.");
			return;
		}

		int rowIndex = 0;
		for (Row row : sheet) {
			if (rowIndex++ == 0)
				continue; // παράκαμψη επικεφαλίδας
			if (rowIndex > maxRows)
				break;
			PublicOrganizationExcelRowDTO dto = mapRowToPublicDTO(row);

			Set<ConstraintViolation<PublicOrganizationExcelRowDTO>> violations = validator.validate(dto);
			if (!violations.isEmpty()) {
				System.err.println("⚠️ Σφάλματα validation στη γραμμή " + rowIndex + ": " + violations);
				continue;
			}

			try {
				PublicOrganization entity = organizationProcessor.processPublic(dto);
				publicOrgRepo.save(entity);
				System.out.println("✅ Αποθηκεύτηκε: " + entity.getName());
			} catch (Exception e) {
				System.err.println("❌ Σφάλμα εισαγωγής στη γραμμή " + rowIndex + ": " + e.getMessage());
			}
		}
	}

	public void importLocalOrganizationsFromSheet(Sheet sheet) {
		if (sheet == null) {
			System.err.println("❌ Το φύλλο 'ΟΤΑ' δεν βρέθηκε στο αρχείο Excel.");
			return;
		}

		int rowIndex = 0;
		for (Row row : sheet) {
			if (rowIndex++ == 0)
				continue; // Παράκαμψη επικεφαλίδας

			LocalOrganizationExcelRowDTO dto = mapRowToLocalDTO(row);

			Set<ConstraintViolation<LocalOrganizationExcelRowDTO>> violations = validator.validate(dto);
			if (!violations.isEmpty()) {
				System.err.println("⚠️ Σφάλματα validation στη γραμμή " + rowIndex + ": " + violations);
				continue;
			}

			try {
				LocalOrganization entity = organizationProcessor.processLocal(dto);
				localOrgRepo.save(entity);
				System.out.println("✅ Αποθηκεύτηκε ΟΤΑ: " + entity.getName());
			} catch (Exception e) {
				System.err.println("❌ Σφάλμα εισαγωγής στη γραμμή " + rowIndex + ": " + e.getMessage());
			}
		}
	}

	// ✅ Νέα overloaded μέθοδος με όριο για testing μόνο !!! SOS TO BE DELETED
	public void importLocalOrganizationsFromSheet(Sheet sheet, int maxRows) {
		if (sheet == null) {
			System.err.println("❌ Το φύλλο 'ΟΤΑ' δεν βρέθηκε στο αρχείο Excel.");
			return;
		}

		int rowIndex = 0;
		for (Row row : sheet) {
			if (rowIndex++ == 0)
				continue; // Παράκαμψη επικεφαλίδας
			if (rowIndex > maxRows)
				break;
			LocalOrganizationExcelRowDTO dto = mapRowToLocalDTO(row);

			Set<ConstraintViolation<LocalOrganizationExcelRowDTO>> violations = validator.validate(dto);
			if (!violations.isEmpty()) {
				System.err.println(
						"⚠️ Σφάλματα validation στη γραμμή " + rowIndex + ": " + violations + "  " + dto.getName());
				continue;
			}

			try {
				LocalOrganization entity = organizationProcessor.processLocal(dto);
				localOrgRepo.save(entity);
				System.out.println("✅ Αποθηκεύτηκε ΟΤΑ: " + entity.getName());
			} catch (Exception e) {
				System.err.println("❌ Σφάλμα εισαγωγής στη γραμμή " + rowIndex + ": " + e.getMessage());
			}
		}
	}

	private PublicOrganizationExcelRowDTO mapRowToPublicDTO(Row row) {
		return new PublicOrganizationExcelRowDTO(getCellValue(row, 0), getCellValue(row, 1), getCellValue(row, 2),
				getCellValue(row, 3), getCellValue(row, 4), getCellValue(row, 5));
	}

	private String getCellValue(Row row, int index) {
		Cell cell = row.getCell(index, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
		return switch (cell.getCellType()) {
		case STRING -> cell.getStringCellValue().trim();
		case NUMERIC -> String.valueOf(cell.getNumericCellValue()).trim();
		case BOOLEAN -> String.valueOf(cell.getBooleanCellValue()).trim();
		case FORMULA -> cell.getCellFormula().trim();
		default -> "";
		};
	}

	private LocalOrganizationExcelRowDTO mapRowToLocalDTO(Row row) {
		return new LocalOrganizationExcelRowDTO(getCellValue(row, 0), // ΟΝΟΜΑΣΙΑ ΦΟΡΕΑ
				getCellValue(row, 1), // ΟΤΑ
				getCellValue(row, 2), // ΝΟΜΙΚΗ ΜΟΡΦΗ
				getCellValue(row, 3), // ΕΝΤΟΣ/ΕΚΤΟΣ
				getCellValue(row, 4) // ΛΟΙΠΕΣ ΠΛΗΡΟΦΟΡΙΕΣ
		);
	}

}
