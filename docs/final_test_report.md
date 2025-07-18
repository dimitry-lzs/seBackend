
# Αναφορά Ελέγχου – Τελική Εκτελέσιμη Έκδοση

**Ομάδα:**
- Δημήτριος Λαζανάς Π22082
- Ιωάννα Ανδριανού Π22010
- Δανάη Χαρζάκα Π22194
- Αντώνιος Τσαλμπούρης Π22272

## 1. Σκοπός Αναφοράς
Η παρούσα αναφορά παρουσιάζει τα αποτελέσματα των ελέγχων που πραγματοποιήθηκαν στη **τελική εκτελέσιμη έκδοση** της εφαρμογής κρατήσεων ραντεβού μεταξύ ασθενών και ιατρών. Περιλαμβάνει τόσο τις βασικές λειτουργίες όσο και τα επιπλέον χαρακτηριστικά που υλοποιήθηκαν κατά τη φάση της κατασκευής.

## 2. Λειτουργικότητα της Τελικής Έκδοσης
Η τελική έκδοση περιλαμβάνει:

- Εγγραφή/Σύνδεση ιατρών και ασθενών
- Προσθήκη διαθεσιμότητας από γιατρούς
- Αναζήτηση ιατρών με φίλτρα ειδικότητας και τοποθεσίας
- Προβολή διαθεσιμότητας ιατρού
- Κράτηση & ακύρωση ραντεβού
- Προβολή ιστορικού ραντεβού
- Προβολή λεπτομεριών ραντεβού
- Καταγραφή διάγνωσης από τον ιατρό μετά το ραντεβού
- Αξιολόγηση ιατρού από τον ασθενή
- Προβολή βαθμολογίας ιατρών
- Δυνατότητα ελέγχου προφίλ και επεξεργασίας στοιχείων
- Responsive και φιλικό προς τον χρήστη περιβάλλον

## 3. Περιβάλλον Ελέγχου
- **OS:** Windows 11 / Ubuntu 22.04 / macOS 15
- **Backend:** Java + SQLite  
- **Frontend:** React  
- **Testing:** Χειροκίνητη δοκιμή (manual testing) σε περιβάλλον ανάπτυξης
- **Browser:** Chrome

<div style="page-break-after: always;"></div>

## 4. Πίνακας Ελέγχων (Test Cases)

| TC ID | Περιγραφή Ελέγχου | Είσοδοι | Αναμενόμενο Αποτέλεσμα | Πραγματικό Αποτέλεσμα | Επιτυχία |
|-------|-------------------|---------|--------------------------|------------------------|----------|
| TC01 | Εγγραφή ασθενούς | fullName, email, password, phone | Επιτυχής δημιουργία λογαριασμού | Όπως αναμενόταν | Ναι |
| TC02 | Εγγραφή γιατρού | fullName, email, password, phone, licenceID, officeLocation, bio | Επιτυχής δημιουργία λογαριασμού | Όπως αναμενόταν | Ναι |
| TC03 | Σύνδεση ασθενούς/γιατρού με σωστά στοιχεία | email, password | Επιτυχής σύνδεση | Όπως αναμενόταν | Ναι |
| TC04 | Σύνδεση ασθενούς/γιατρού με λάθος password | email, λάθος password | Εμφάνιση μηνύματος λάθους | Όπως αναμενόταν | Ναι |
| TC04 | Ορισμός διαθεσιμότητας | Ώρες & ημέρες | Καταχώρηση διαθεσιμότητας | Όπως αναμενόταν | Ναι |
| TC05 | Αναζήτηση ιατρού με ειδικότητα "Καρδιολόγος", "Αθήνα" | Specialty = "CARDIOLOGIST", officeLocation "ATHENS" | Λίστα με διαθέσιμους καρδιολόγους | Όπως αναμενόταν | Ναι |
| TC06 | Προβολή διαθεσιμότητας ιατρού | Επιλογή ιατρού | Λίστα με ελεύθερα slots | Όπως αναμενόταν | Ναι |
| TC07 | Κράτηση ραντεβού | Επιλογή ιατρού, ώρα, λόγος | Επιτυχής κράτηση | Όπως αναμενόταν | Ναι |
| TC08 | Προβολή ραντεβού | Είσοδος ασθενούς | Λίστα με ραντεβού | Όπως αναμενόταν | Ναι |
| TC09 | Ακύρωση ραντεβού | Επιλογή υπάρχοντος ραντεβού | Επιτυχής ακύρωση | Όπως αναμενόταν | Ναι |
| TC07 | Καταγραφή διάγνωσης | Εισαγωγή κειμένου μετά το ραντεβού | Αποθήκευση διάγνωσης | Όπως αναμενόταν | Ναι |
| TC08 | Αξιολόγηση ιατρού | Επιλογή αστεριών (1–5) + σχόλιο | Καταχώρηση αξιολόγησης | Όπως αναμενόταν | Ναι |
| TC09 | Προβολή ιστορικού ραντεβού | Είσοδος ασθενούς/γιατρού | Προβολή λεπτομεριών ανά ραντεβού | Όπως αναμενόταν | Ναι |
| TC10 | Προβολή μέσης αξιολόγησης γιατρού | Επιλογή του γιατρού | Εμφάνιση score | Όπως αναμενόταν | Ναι |
| TC10 | Προβολή όλων των αξιολογήσεων για το γιατρό | Επιλογή του γιατρού | Εμφάνιση αξιολογήσεων | Όπως αναμενόταν | Ναι |

## 6. Συμπεράσματα - Προτάσεις
Η τελική έκδοση είναι **λειτουργικά πλήρης**, φιλική προς τον χρήστη και ευσταθής κατά τις δοκιμές. Οι πρόσθετες λειτουργίες για τη προσθήκη διάγνωσης και της αξιολόγησης ενισχύουν σημαντικά τη χρηστικότητα του συστήματος. Προτείνεται:

- Δοκιμές για καλύτερη απεικόνιση σε κινητές συσκευές
- Ανάλυση δυνατοτήτων για ειδοποιήσεις (π.χ. reminder mail/sms)
