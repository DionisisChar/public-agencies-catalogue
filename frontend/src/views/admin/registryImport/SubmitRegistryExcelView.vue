<template>
  <div class="submit-registry-container">
    <h1 class="page-title">
      Υποβολή Επικαιροποιημένου Μητρώου Υπηρεσιών και Φορέων της Ελληνικής
      Διοίκησης
    </h1>

    <section class="info-box">
      <h2>
        <AlertCircle style="width: 1.1em; vertical-align: middle" /> Σημαντικές
        Πληροφορίες
      </h2>
      <br />
      <p>
        Εδώ μπορείτε να ανεβάσετε το επικαιροποιημένο αρχείο Excel όπως αυτό
        αναρτάται από το Υπουργείο Εσωτερικών στην ιστοσελίδα του
        <a href="https://www.ypes.gr/services/mitroo" target="_blank"
          >Μητρώου Δημόσιων Υπηρεσιών</a
        >.
      </p>
      <p>
        Με την υποβολή του αρχείου, το σύστημα θα επιχειρήσει να καταχωρήσει
        όλες τις νέες εγγραφές που περιλαμβάνονται σε αυτό. Δεν υποστηρίζεται
        αυτόματη ενημέρωση υπαρχόντων οργανισμών.
      </p>
      <p class="warning">
        <AlertTriangle style="width: 1.1em; vertical-align: middle" />
        Συνιστάται να έχει προηγηθεί πλήρης διαγραφή παλαιότερων δεδομένων,
        διαφορετικά ενδέχεται να προκύψουν ασυνέπειες.
      </p>
    </section>

    <!-- Αναμονή διεργασιών -->
    <div v-if="isUploading" class="upload-spinner">
      <span class="spinner-icon">⏳</span>
      <span class="spinner-text"
        >Επεξεργασία αρχείου... Παρακαλώ περιμένετε</span
      >
    </div>

    <!-- 🟦 Upload Box -->
    <section class="upload-box">
      <h2 class="upload-title">Υποβολή Αρχείου Excel</h2>
      <div class="upload-area" @drop.prevent="handleDrop" @dragover.prevent>
        <input
          id="file-upload"
          type="file"
          @change="handleFileUpload"
          accept=".xls,.xlsx"
          style="display: none"
        />

        <label for="file-upload" class="upload-dropzone">
          <div class="upload-label">
            <UploadCloud
              style="width: 1.4em; margin-right: 0.5em; vertical-align: middle"
            />
            Επιλέξτε ή σύρετε ένα αρχείο Excel εδώ
          </div>
        </label>

        <div v-if="selectedFile" class="file-info">
          <div class="file-label-and-details">
            <div class="file-label">Επιλεγμένο αρχείο:</div>
            <div class="file-details">
              <span class="file-name" :title="selectedFile.name">
                {{ truncateFileName(selectedFile.name) }}
              </span>
              <span class="file-size"
                >({{ formatFileSize(selectedFile.size) }})</span
              >
            </div>
          </div>
          <div class="file-actions">
            <button @click="previewFile" title="Προβολή" class="icon-button">
              <Eye style="width: 1.1em" />
            </button>
            <button @click="downloadFile" title="Λήψη" class="icon-button">
              <Download style="width: 1.1em" />
            </button>
            <button @click="removeFile" title="Αφαίρεση" class="icon-button">
              <FileX style="width: 1.1em" />
            </button>
          </div>
        </div>
      </div>

      <div class="upload-warning">
        <h3>
          <AlertTriangle style="width: 1.1em; vertical-align: middle" /> Οδηγίες
          και Συστάσεις
        </h3>
        <p>
          Η παρούσα λειτουργία προορίζεται για οριστική υποβολή του
          επικαιροποιημένου αρχείου, το οποίο θα εμπλουτίσει τη βάση δεδομένων
          του συστήματος με νέες εγγραφές.
        </p>
        <p>
          Δεν υποστηρίζεται αυτόματη ενημέρωση υπαρχόντων στοιχείων. Εάν η βάση
          περιέχει ήδη εγγραφές με ίδια ονόματα οργανισμών, οι νέες εγγραφές θα
          απορριφθούν λόγω περιορισμών μοναδικότητας (Unique Constraint).
        </p>
        <p>
          Εάν επιθυμείτε πλήρη επικαιροποίηση της βάσης, συνιστάται ισχυρά να
          έχει προηγηθεί είτε:
        </p>
        <ul>
          <li>διαγραφή όλων των υφιστάμενων εγγραφών ή</li>
          <li>εισαγωγή του αρχείου σε καθαρή βάση δεδομένων.</li>
        </ul>
        <p>
          ℹ️ Η μη τήρηση των παραπάνω ενδέχεται να οδηγήσει σε ασυνέπειες και
          παρωχημένα δεδομένα, καθώς δεν θα ενημερωθούν τα στοιχεία των
          υπαρχουσών υπηρεσιών.
        </p>
      </div>

      <div class="final-submit">
        <button @click="confirmSubmission" :disabled="!selectedFile">
          <FileCheck
            style="width: 1.1em; vertical-align: middle; margin-right: 0.4em"
          />
          Οριστική Υποβολή
        </button>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref } from "vue";
import {
  FileUp,
  FileX,
  FileCheck,
  Download,
  Eye,
  AlertCircle,
  AlertTriangle,
  UploadCloud,
} from "lucide-vue-next";
import { submitRegistryExcelFile } from "../../../services/admin/registryImport/importExcelService";

const selectedFile = ref(null);
const isUploading = ref(false);

function handleFileUpload(event) {
  const file = event.target.files[0];
  if (file && file.size <= 5 * 1024 * 1024) {
    selectedFile.value = file;
  } else {
    alert("Το αρχείο είναι πολύ μεγάλο. Μέγιστο επιτρεπτό μέγεθος: 5MB");
  }
}

function removeFile() {
  selectedFile.value = null;
}

function downloadFile() {
  const url = URL.createObjectURL(selectedFile.value);
  const link = document.createElement("a");
  link.href = url;
  link.download = selectedFile.value.name;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  URL.revokeObjectURL(url);
}

function previewFile() {
  const url = URL.createObjectURL(selectedFile.value);
  window.open(url, "_blank");
}

function truncateFileName(name) {
  return name.length > 40 ? name.substring(0, 37) + "..." : name;
}

function formatFileSize(size) {
  return (size / 1024).toFixed(1) + " KB";
}

async function confirmSubmission() {
  if (!selectedFile.value) return;
  const confirmed = confirm(
    `❗ Πρόκειται να υποβάλετε το αρχείο "${selectedFile.value.name}" για εμπλουτισμό της βάσης δεδομένων. Είστε βέβαιοι;`
  );
  if (!confirmed) return;

    isUploading.value = true;

    try{
        const response = await submitRegistryExcelFile(selectedFile.value);
        alert(response.data); // ή φτιάχνουμε ένα custom success popup
    } catch (error) {
        alert("❌ Σφάλμα κατά την υποβολή: " + error.response?.data || error.message);
    } finally {
        isUploading.value = false;
    }
}

function handleDrop(event) {
  const files = event.dataTransfer.files;
  if (files.length > 0) {
    handleFileUpload({ target: { files } });
  }
}
</script>

<style scoped>
.upload-title {
  text-align: center;
  font-size: 1.4rem;
  margin-bottom: 1.2rem;
}
.submit-registry-container {
  padding: 2rem;
}

.page-title {
  font-size: 1.7rem;
  font-weight: bold;
  margin-bottom: 1.8rem;
}

.info-box {
  background: linear-gradient(180deg, #f7faff 0%, #eef4fb 100%);
  border-left: 6px solid #2265ba;
  border: 1px solid #c2d7f2;
  box-shadow: 0 0 8px rgba(34, 101, 186, 0.08);
  padding: 1.6rem 1.8rem;
  border-radius: 12px;
  margin-bottom: 2rem;
  font-size: 1.05rem;
  line-height: 1.6;
  color: #10294b;
}

.upload-warning {
  background: linear-gradient(180deg, #fffdf6 0%, #fff9e6 100%);
  border-left: 6px solid #ff9900;
  border: 1px solid #ffd58a;
  box-shadow: 0 0 8px rgba(255, 153, 0, 0.08);
  padding: 1.6rem 1.8rem;
  border-radius: 12px;
  margin-bottom: 2rem;
  font-size: 0.92rem;
  line-height: 1.6;
  color: #4a2e00;
  margin-top: 30px;
}

/*.info-box,
.upload-warning {
  background: linear-gradient(180deg, #fffefc 0%, #fffdf6 100%);
  border: 1px solid #ffce73;
  border-left: 6px solid #ff9900;
  box-shadow: 0 0 8px rgba(255, 204, 102, 0.1);
  padding: 1.6rem 1.8rem;
  border-radius: 12px;
  margin-bottom: 2rem;
  font-size: 1.08rem;
  line-height: 1.6;
  color: #442e00;
} */

/*.upload-warning {
    font-size: 0.9rem;
}*/

.upload-warning h3 {
  margin-top: 0;
  margin-bottom: 1rem;
  color: #bb4e00;
  font-size: 1.1rem;
}

.upload-warning ul {
  margin: 0.4rem 0 1rem 1.2rem;
  padding-left: 1.2rem;
  list-style: disc;
}

.upload-warning li {
  margin-bottom: 0.5rem;
}

.warning {
  color: #b20000;
  font-weight: 600;
  margin-top: 1rem;
}

.upload-box {
  border: 2px dashed #a8b3c2;
  padding: 1.5rem;
  border-radius: 12px;
  background-color: #fafbfc;
}

.upload-area {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.upload-dropzone {
  border: 2px dashed #002350;
  border-radius: 12px;
  padding: 2rem;
  text-align: center;
  cursor: pointer;
  color: #555;
  font-size: 1.05rem;
  margin-bottom: 1.5rem;
  transition: border-color 0.3s;
  width: 100%;
  max-width: 640px;
}

.upload-button {
  display: inline-flex;
  align-items: center;
  background-color: #2265ba;
  color: white;
  padding: 0.6rem 1.2rem;
  font-weight: 600;
  font-size: 1.05rem;
  border-radius: 8px;
  cursor: pointer;
  margin-bottom: 1rem;
  transition: background 0.2s;
}
.upload-button:hover {
  background-color: #1c4e95;
}

.file-info {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  margin-top: 1rem;
  padding: 1rem 1.2rem;
  border: 1px solid #cbd4e1;
  background-color: #f9fbff;
  border-radius: 10px;
  font-size: 1.02rem;
  width: 100%;
  max-width: 640px;
}

.file-label-and-details {
  display: flex;
  align-items: baseline;
  gap: 0.6rem;
  flex-wrap: wrap;
}

.file-label {
  font-weight: 600;
  color: #1a1a1a;
}

.file-name {
  font-weight: 500;
  color: #003366;
}

.file-size {
  color: #666;
  font-size: 0.95rem;
  margin-left: 0.3rem;
}

.file-icon {
  font-size: 1.2rem;
}

.file-actions {
  display: flex;
  align-items: center;
  gap: 0.6rem;
}

.file-actions button {
  margin-left: 0.4rem;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1.2rem;
}

.final-submit {
  margin-top: 1.8rem;
  text-align: center;
}

.final-submit button {
  background-color: #002350;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 0.7rem 2rem;
  font-size: 1.05rem;
  cursor: pointer;
  transition: background 0.2s;
}

.final-submit button:disabled {
  background-color: #a5b1c6;
  cursor: not-allowed;
}

.final-submit button:hover {
  background-color: #2265ba;
}

.icon-button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 0.25rem;
  color: #444;
  transition: color 0.2s;
}
.icon-button:hover {
  color: #2265ba;
}

.upload-dropzone {
  border: 2px dashed #aaa;
  border-radius: 12px;
  padding: 2rem;
  text-align: center;
  cursor: pointer;
  color: #555;
  font-size: 1.05rem;
  margin-bottom: 1.5rem;
  transition: border-color 0.3s;
}

.upload-dropzone:hover {
  border-color: #2265ba;
  background-color: #f9fbff;
}

/* Animation για κλεψύδρα */
.upload-spinner {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.8rem;
  font-size: 1.05rem;
  margin-top: 1.5rem;
  color: #2265ba;
  animation: pulse 1.5s infinite;
}

.spinner-icon {
  animation: spin 1.2s linear infinite;
  font-size: 1.4rem;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

@keyframes pulse {
  0% {
    opacity: 0.7;
  }
  50% {
    opacity: 1;
  }
  100% {
    opacity: 0.7;
  }
}
</style>
