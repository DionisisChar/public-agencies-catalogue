> 📄 This project is also available in English: [README.en.md](README.en.md)

# Κατάλογος Δημοσίων Οργανισμών και Υπηρεσιών

## Πληροφοριακό Σύστημα e-Government

Αυτή η εφαρμογή υλοποιήθηκε στο πλαίσιο της διπλωματικής διατριβής:

**Υλοποίηση Διαδικτυακής Εφαρμογής Καταλόγου Δημοσίων Οργανισμών και Υπηρεσιών**

🎓 Ελληνικό Ανοικτό Πανεπιστήμιο  
Σχολή Θετικών Επιστημών και Τεχνολογίας  
Πρόγραμμα Μεταπτυχιακών Σπουδών: *Μεταπτυχιακή Εξειδίκευση στα Πληροφοριακά Συστήματα*  
**Επιβλέπων καθηγητής**: Γεώργιος Μαυρομμάτης  
**Συγγραφέας**: Διονύσιος Χαραλαμπόπουλος  
📧 Επικοινωνία: dion.charalampopoulos@gmail.com  

---

## ✨ Περιγραφή

Η παρούσα εργασία αφορά τη σχεδίαση και υλοποίηση μιας διαδικτυακής εφαρμογής **e-Government**, η οποία καθιστά διαθέσιμο online το **Μητρώο Δημοσίων Υπηρεσιών και Οργανισμών**.

Η εφαρμογή:
- Ενημερώνεται από τα επίσημα δεδομένα του Υπουργείου Εσωτερικών  
  🔗 [Μητρώο Δημοσίων Υπηρεσιών](https://www.ypes.gr/category/dioikitiki-anasygkrotisi/mitroo-dimosion-ypiresion/)
- Επιτρέπει φιλική αναζήτηση υπηρεσιών και οργανισμών με φίλτρα
- Παρέχει συνδυαστική πληροφορία μέσω διαλειτουργικότητας με APIs (Δι@ύγεια, Google's Places API(New))
- Περιλαμβάνει περιβάλλον διαχείρισης (admin panel) για την προσθήκη, τροποποίηση και μαζική εισαγωγή φορέων

---

## 🚀 Λειτουργίες

- 🔍 Αναζήτηση οργανισμών με φίλτρα (όνομα, Υπουργείο, ΟΤΑ, νομική μορφή, δημόσιος/ιδιωτικός τομέας)
- 🗺️ Προβολή οργανισμών στον χάρτη (OpenStreetMap)
- 📄 Ενσωμάτωση με Δι@ύγεια API για εμφάνιση πρόσφατων αποφάσεων οργανισμών
- 🧩 Αυτόματη συμπλήρωση στοιχείων (διεύθυνση, τηλέφωνο, ιστότοπος, συντεταγμένες) μέσω Google Places API (New)
- 👤 Διαχειριστικό περιβάλλον (CRUD για Υπουργεία, ΟΤΑ και Οργανισμούς)
- 📤 Μαζική ενημέρωση & καθαρισμός της βάσης φορέων με επίσημα δεδομένα του ΥΠΕΣ

---

## ⚙️ Τεχνολογίες

- **Backend**: Java 17, Spring Boot, Spring Security, Spring Data JPA, Hibernate, JWT
- **Frontend**: Vue.js (Vite, Composition API), Axios, OpenStreetMap, Node.js & npm (για την εγκατάσταση και εκτέλεση του frontend)
- **Database**: MariaDB
- **API Ενσωμάτωση**:
  - [Δι@ύγεια API](https://diavgeia.gov.gr)
  - [Google Places API (New) (SearchText + Place Details)](https://developers.google.com/maps/documentation/places/web-service/op-overview))

- **Αρχιτεκτονική**:
  - RESTful API
  - MVC Pattern
  - Layered Architecture
  - Separation of Concerns

---

## 🔐 Σημειώσεις Ασφαλείας

- Η αυθεντικοποίηση χρήστη βασίζεται σε **JWT Token**, αποθηκευμένο σε **HttpOnly Cookie**
- Η πρόσβαση στα **admin endpoints** προστατεύεται μέσω ρόλων (`ROLE_ADMIN`)
- Δεν περιλαμβάνονται πραγματικά credentials, API keys ή κωδικοί στο παρόν δημόσιο αποθετήριο

---

## 🧪 Εκτέλεση σε Περιβάλλον Ανάπτυξης

Για να εκτελέσετε την εφαρμογή τοπικά:

1. **Backend (Spring Boot):**  
   Με χρήση STS/Eclipse/IntelliJ, εκτελέστε την `main` κλάση του Spring Boot project (π.χ. `PublicAgenciesCatalogueApplication.java`).

2. **Frontend (Vue.js):**  
   ```bash
   npm install
   npm run dev
   
--- 

## 📄 Άδεια Χρήσης

Το έργο **δεν διαθέτει άδεια χρήσης** (license).  
Η δημοσίευσή του γίνεται μόνο για εκπαιδευτικούς και ακαδημαϊκούς σκοπούς.  
Απαγορεύεται η αναπαραγωγή ή τροποποίηση του κώδικα χωρίς ρητή άδεια από τον συγγραφέα.

---

## 📧 Επικοινωνία

Για οποιαδήποτε πληροφορία ή διευκρίνιση μπορείτε να επικοινωνήσετε:

**Διονύσιος Χαραλαμπόπουλος**  
📧 dion.charalampopoulos@gmail.com  
