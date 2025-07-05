package com.dionChar.publicagencies.catalogue.util;

import java.text.Normalizer;

import info.debatty.java.stringsimilarity.JaroWinkler;

/**
 * Χρήσιμες συναρτήσεις για σύγκριση και κανονικοποίηση ονομάτων οργανισμών,
 * ιδίως κατά την αντιστοίχιση με δεδομένα από εξωτερικά APIs όπως το Diavgeia.
 */

/**
 * Utility κλάση για τη σύγκριση ονομάτων οργανισμών μεταξύ της βάσης δεδομένων
 * της εφαρμογής (μητρώο υπηρεσιών) και του Diavgeia API, με χρήση fuzzy string
 * matching (Jaro-Winkler).
 *
 * <p>
 * Η βασική μέθοδος {@link #isSimilarOrganizationName(String, String)}
 * επιστρέφει {@code true} αν δύο ονόματα υπηρεσιών ταιριάζουν απόλυτα μετά από
 * διαδικασία κανονικοποίησης (normalization), η οποία περιλαμβάνει:
 * <ul>
 * <li>Αφαίρεση παρενθέσεων και περιεχομένου τους</li>
 * <li>Αφαίρεση εισαγωγικών και ειδικών χαρακτήρων</li>
 * <li>Αντικατάσταση κοινών συντομογραφιών (π.χ. Γ.Ν., Κ.Υ., Α.Ε.) με πλήρεις
 * λέξεις</li>
 * <li>Καθαρισμό πολλαπλών κενών</li>
 * </ul>
 *
 * <p>
 * Η μέθοδος χρησιμοποιεί τη βιβλιοθήκη {@code JaroWinkler} από το πακέτο
 * {@code info.debatty}.
 *
 * <p>
 * Χρησιμοποιείται κυρίως από το service {@code DiavgeiaOrgFinderServiceImpl}
 * όταν δεν εντοπίζεται exact match στο αρχείο {@code organizations.json} του
 * Diavgeia και επιχειρείται fuzzy αντιστοίχιση.
 *
 * <p>
 * Η κλάση είναι τελική και διαθέτει μόνο static μεθόδους.
 *
 * @author dionChar
 */
public class OrganizationNameUtils {

	/**
	 * Κανονικοποιεί το όνομα του οργανισμού αφαιρώντας παρενθέσεις, ειδικούς
	 * χαρακτήρες, και αντικαθιστώντας κοινές συντομογραφίες.
	 */
	public static String normalizeOrganizationName(String name) {

		// if (name == null) return "";

		// Αφαίρεση παρενθέσεων με το περιεχόμενό τους
		name = name.replaceAll("\\(.*?\\)", "");

		// Αφαίρεση εισαγωγικών και ειδικών χαρακτήρων
		name = name.replaceAll("[\"“”«»‘’']", "");

		// Κανονικοποίηση όλων των "περίεργων" τελειών σε κανονικές
		// name = name.replaceAll("[·•｡]", ".");

		// Αντικατάσταση συντομογραφιών με πλήρεις λέξεις (word boundaries,
		// case-sensitive)

		// Αντικατάσταση με "ΓΕΝΙΚΟ ΝΟΣΟΚΟΜΕΙΟ"
		name = name.replaceAll("(?<=\\s|^)Γ\\.Ν\\.(?=\\s|$)", "ΓΕΝΙΚΟ ΝΟΣΟΚΟΜΕΙΟ");
		name = name.replaceAll("(?<=\\s|^)Γ\\.Ν(?=\\s|$)", "ΓΕΝΙΚΟ ΝΟΣΟΚΟΜΕΙΟ");
		name = name.replaceAll("(?<=\\s|^)ΓΝ\\.(?=\\s|$)", "ΓΕΝΙΚΟ ΝΟΣΟΚΟΜΕΙΟ");
		name = name.replaceAll("(?<=\\s|^)ΓΝ(?=\\s|$)", "ΓΕΝΙΚΟ ΝΟΣΟΚΟΜΕΙΟ");

		// Αντικατάσταση με "ΚΕΝΤΡΟ ΥΓΕΙΑΣ"
		name = name.replaceAll("(?<=\\s|^)Κ\\.Υ\\.(?=\\s|$)", "ΚΕΝΤΡΟ ΥΓΕΙΑΣ");
		name = name.replaceAll("(?<=\\s|^)Κ\\.Υ(?=\\s|$)", "ΚΕΝΤΡΟ ΥΓΕΙΑΣ");
		name = name.replaceAll("(?<=\\s|^)ΚΥ\\.(?=\\s|$)", "ΚΕΝΤΡΟ ΥΓΕΙΑΣ");
		name = name.replaceAll("(?<=\\s|^)ΚΥ(?=\\s|$)", "ΚΕΝΤΡΟ ΥΓΕΙΑΣ");

		// Αντικατάσταση Α.Ε. / Α.Ε / ΑΕ με "ΑΝΩΝΥΜΗ ΕΤΑΙΡΙΑ" — μόνο όταν είναι αυτόνομη
		// λέξη
		name = name.replaceAll("(?<=\\s|^)Α\\.Ε\\.(?=\\s|$)", "ΑΝΩΝΥΜΗ ΕΤΑΙΡΙΑ");
		name = name.replaceAll("(?<=\\s|^)Α\\.Ε(?=\\s|$)", "ΑΝΩΝΥΜΗ ΕΤΑΙΡΙΑ");
		name = name.replaceAll("(?<=\\s|^)ΑΕ(?=\\s|$)", "ΑΝΩΝΥΜΗ ΕΤΑΙΡΙΑ");

		// Αντικατάσταση με "ΟΡΓΑΝΙΣΜΟΣ ΤΟΠΙΚΗΣ ΑΥΤΟΔΙΟΙΚΗΣΗΣ"
		name = name.replaceAll("(?<=\\s|^)Ο\\.Τ\\.Α\\.(?=\\s|$)", "ΟΡΓΑΝΙΣΜΟΣ ΤΟΠΙΚΗΣ ΑΥΤΟΔΙΟΙΚΗΣΗΣ");
		name = name.replaceAll("(?<=\\s|^)Ο\\.Τ\\.Α(?=\\s|$)", "ΟΡΓΑΝΙΣΜΟΣ ΤΟΠΙΚΗΣ ΑΥΤΟΔΙΟΙΚΗΣΗΣ");
		name = name.replaceAll("(?<=\\s|^)ΟΤΑ(?=\\s|$)", "ΟΡΓΑΝΙΣΜΟΣ ΤΟΠΙΚΗΣ ΑΥΤΟΔΙΟΙΚΗΣΗΣ");

		name = name.replaceAll("(?<=\\s|^)ΓΕΝ\\.(?=\\s|$)", "ΓΕΝΙΚΟ");
		name = name.replaceAll("(?<=\\s|^)ΝΟΣΟΚ\\.(?=\\s|$)", "ΝΟΣΟΚΟΜΕΙΟ");
		name = name.replaceAll("(?<=\\s|^)ΝΟΣ\\.(?=\\s|$)", "ΝΟΣΟΚΟΜΕΙΟ");

		// Καθαρισμός κενών
		name = name.replaceAll("\\s+", " ").trim();

		return name;
	}

	/**
	 * Αφαιρεί μόνο τους τόνους (όχι διαλυτικά ή άλλα σημεία) από ελληνικούς
	 * χαρακτήρες.
	 */
	public static String removeAccentsOnly(String input) {
		if (input == null)
			return null;

		// Unicode Normalization: διασπά χαρακτήρες με τόνους (NFD)
		String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);

		// Αφαίρεση ΜΟΝΟ του χαρακτήρα "τόνος" (U+0301)
		return normalized.replaceAll("\\u0301", "");
	}

	/**
	 * Επιστρέφει true αν τα δύο ονόματα είναι "ίδια" μετά από normalization και
	 * fuzzy σύγκριση.
	 */
	public static boolean isSimilarOrganizationName(String organizationName, String orgDiavgeia) {
		JaroWinkler jaroWinkler = new JaroWinkler();
		String normalizedOrganizationName = normalizeOrganizationName(organizationName);
		String normalizedOrgDiavgeia = normalizeOrganizationName(orgDiavgeia);
		System.out.println("Κανονικοποίηση Μητρώου - Διαύγεια: [" + normalizedOrganizationName + "] vs ["
				+ normalizedOrgDiavgeia + "]");

		double similarity = jaroWinkler.similarity(normalizedOrganizationName, normalizedOrgDiavgeia);
		System.out.println(similarity);
		// Πλήρης ταύτιση απαιτείται προς το παρόν
		return similarity == 1;
	}
}
