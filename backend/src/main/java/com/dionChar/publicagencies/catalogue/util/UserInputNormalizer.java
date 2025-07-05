package com.dionChar.publicagencies.catalogue.util;


/**
 * Utility class για την κανονικοποίηση κειμενικών πεδίων που προέρχονται από
 * input του χρήστη (φόρμες δημιουργίας/ενημέρωσης οργανισμών).
 *
 * <p>
 * Σκοπός είναι η μετατροπή κενών ή whitespace-only strings σε {@code null},
 * ώστε:
 * <ul>
 *   <li>να αποφευχθεί η αποθήκευση άχρηστων {@code ""} στη βάση δεδομένων</li>
 *   <li>να είναι πιο εύκολη η λογική εμπλουτισμού (π.χ. από Google Place Details)</li>
 *   <li>να υπάρχει σταθερή αναπαράσταση της έννοιας «δεν υπάρχει τιμή» ως {@code null}</li>
 * </ul>
 *
 * <p>
 * Χρησιμοποιείται κυρίως στο service layer, για πεδία όπως διεύθυνση,
 * τηλέφωνο, ιστοσελίδα, πρόσθετες πληροφορίες κ.λπ.
 *
 * <p><b>Παράδειγμα:</b><br>
 * {@code setAddress(UserInputNormalizer.normalize(dto.getAddress()));}
 *
 * <p>
 * Η κλάση είναι τελική και διαθέτει μόνο static μεθόδους.
 * 
 * @author dionChar
 */

public final class UserInputNormalizer {
	
	private UserInputNormalizer() {
        // private constructor για να αποτραπεί η δημιουργία instance
    }

    /**
     * Επιστρέφει {@code null} αν η είσοδος είναι {@code null}, {@code ""} ή περιέχει μόνο κενά.
     * Αλλιώς επιστρέφει trimmed string.
     *
     * @param input Η αρχική τιμή
     * @return Κανονικοποιημένη τιμή (null ή trimmed string)
     */
    public static String normalize(String input) {
        return (input == null || input.isBlank()) ? null : input.trim();
    }


}
