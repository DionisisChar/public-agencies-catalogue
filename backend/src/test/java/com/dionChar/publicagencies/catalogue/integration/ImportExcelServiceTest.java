package com.dionChar.publicagencies.catalogue.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.FileInputStream;
import java.io.InputStream;

import com.dionChar.publicagencies.catalogue.service.importExcel.ImportExcelService;

@SpringBootTest
@DisplayName("Έλεγχος Excel Import με οριοθετημένες εγγραφές")
class ImportExcelServiceTest {

	@Autowired
    private ImportExcelService importExcelService;

    private static final String EXCEL_PATH = "C:\\Users\\Διονύσης-PC\\Desktop\\Ptyxiakh\\Αρχείο Excel\\eggr14047-20240731mhtr(1).xlsx"; // 🔁 άλλαξε path

    private static final int MAX_ROWS = 301; //για 20 εγγραφές 

	
	@Test
	@DisplayName("Εισαγωγή 20 πρώτων εγγραφών από το Excel")
	public void testImportLimitedRowsFromExcel() {
		try (InputStream input = new FileInputStream(EXCEL_PATH);
	             Workbook workbook = WorkbookFactory.create(input)) {

	            Sheet publicSheet = workbook.getSheet("ΦΟΡΕΙΣ");
	            if (publicSheet != null) {
	                System.out.println("\n=== Εισαγωγή PublicOrganizations (max 20 rows) ===");
	                //importExcelService.importPublicOrganizationsFromSheet(publicSheet, MAX_ROWS);
	                importExcelService.importPublicOrganizationsFromSheet(publicSheet,20); //άλλαξετο χωρις ιντ
	            } else {
	                System.err.println("❌ Το φύλλο 'ΦΟΡΕΙΣ' δεν βρέθηκε.");
	            }

	            Sheet localSheet = workbook.getSheet("ΦΟΡΕΙΣ ΟΤΑ");
	            if (localSheet != null) {
	                System.out.println("\n=== Εισαγωγή LocalOrganizations (max 20 rows) ===");
	                //importExcelService.importLocalOrganizationsFromSheet(localSheet, MAX_ROWS);
	                importExcelService.importLocalOrganizationsFromSheet(localSheet,20); // αλλαξέτο χωρίς ιντ
	            } else {
	                System.err.println("❌ Το φύλλο 'ΟΤΑ' δεν βρέθηκε.");
	            }

	        } catch (Exception e) {
	            System.err.println("❌ Σφάλμα κατά το import: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }
	

}
