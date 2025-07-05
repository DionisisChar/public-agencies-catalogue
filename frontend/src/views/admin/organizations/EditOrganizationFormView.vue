<template>
  <div class="create-org-form">
    <h2>Ενημέρωση Οργανισμού / Υπηρεσίας</h2>

    <form @submit.prevent="handleSubmit">
      <!-- Όνομα -->
      <div class="form-row">
        <label>Όνομα Οργανισμού/Υπηρεσίας *</label>
        <input v-model="form.name" type="text" required maxlength="1000" />
      </div>

      <!-- Νομική Μορφή -->
      <div class="form-row">
        <label>Νομική Μορφή *</label>
        <pre>🚨 form.legalFormId: {{ form.legalFormId }}</pre>
        <select
          v-model="form.legalFormId"
          :disabled="form.legalFormOtherActive"
        >
          <option disabled value="">Επιλέξτε...</option>
          <option
            v-for="option in legalFormOptions"
            :key="option.value"
            :value="option.value"
          >
            {{ option.label }}
          </option>
          <option value="other">Άλλο...</option>
        </select>
        <div v-if="form.legalFormId === 'other'">
          <input
            v-model="form.legalFormName"
            type="text"
            placeholder="Νέα νομική μορφή"
          />
        </div>
      </div>

      <!-- Υπουργείο -->
      <div class="form-row">
        <label>Εποπτεύον Υπουργείο</label>
        <pre>
        🚨 isLocalAuthorityInputDisabled: {{
            isLocalAuthorityInputDisabled
          }}</pre
        >
        <pre>🚨 form.ministryId: {{ form.ministryId }}</pre>
        <pre>🚨 form.localAuthorityIds: {{ form.localAuthorityIds }}</pre>
        <select
          v-model="form.ministryId"
          :disabled="form.localAuthorityIds.length > 0"
        >
          <option value="">Επιλέξτε...</option>
          <option
            v-for="option in ministryOptions"
            :key="option.value"
            :value="option.value"
          >
            {{ option.label }}
          </option>
        </select>
      </div>

      <!-- Εποπτεύων ΟΤΑ -->
      <div class="form-row" style="position: relative">
        <label>Εποπτεύων ΟΤΑ</label>
        <input
          v-model="localAuthorityQuery"
          :disabled="isLocalAuthorityInputDisabled"
          type="text"
          placeholder="Αναζήτηση ΟΤΑ..."
          autocomplete="off"
          @focus="handleOtaFocus"
          @input="handleOtaInput"
          style="margin-bottom: 0.4rem"
        />

        <!-- Custom dropdown -->
        <div
          v-if="
            showOtaDropdown && localAuthorityOptions.length && !form.ministryId
          "
          class="ota-dropdown"
        >
          <div
            v-for="option in localAuthorityOptions"
            :key="option.value"
            class="ota-option"
            :class="{ selected: form.localAuthorityIds.includes(option.value) }"
            style="padding: 0.4rem 0.8rem; cursor: pointer"
            @mousedown.prevent="toggleLocalAuthority(option.value)"
          >
            {{ option.label }}
            <span v-if="form.localAuthorityIds.includes(option.value)">✔️</span>
          </div>
        </div>
        <div
          v-if="form.localAuthorityIds.length"
          class="selected-badges"
          style="margin-top: 1.2rem"
        >
          <small>Επιλέχθηκαν: {{ form.localAuthorityIds.length }} ΟΤΑ</small>
          <div class="badges-row">
            <span v-for="id in form.localAuthorityIds" :key="id" class="badge">
              {{ getLocalAuthorityLabel(id) }}
              <button
                type="button"
                class="remove-btn"
                @click="removeLocalAuthority(id)"
              >
                ×
              </button>
            </span>
          </div>
        </div>
      </div>

      <!-- Υπαγωγή στο Δημόσιο Τομέα -->
      <div class="form-row">
        <label>Υπαγωγή στο Δημόσιο Τομέα *</label>
        <pre>🚨 form.publicSectorStatus: {{ form.publicSectorStatus }}</pre>

        <select v-model="form.publicSectorStatus" required>
          <option disabled value="">Επιλέξτε...</option>
          <option
            v-for="option in publicSectorStatusOptions"
            :key="option"
            :value="option"
          >
            {{ getGreekLabel(option) }}
          </option>
        </select>
      </div>

      <!-- Τομείς Πολιτικής -->
      <div class="form-row">
        <label>Τομείς Πολιτικής</label>
        <input
          v-model="form.policySector"
          type="text"
          :disabled="form.localAuthorityIds.length > 0"
          placeholder="π.χ. ΟΙΚΟΝΟΜΙΑΣ, ΠΟΛΙΤΙΣΜΟΥ"
        />
      </div>

      <!-- Διεύθυνση -->
      <div class="form-row">
        <label>Διεύθυνση</label>
        <input v-model="form.address" type="text" maxlength="255" />
      </div>

      <!-- Τηλέφωνο -->
      <div class="form-row">
        <label>Τηλέφωνο</label>
        <input v-model="form.phoneNumber" type="text" maxlength="100" />
      </div>

      <!-- Ιστοσελίδα -->
      <div class="form-row">
        <label>Ιστοσελίδα</label>
        <input v-model="form.website" type="url" maxlength="255" />
      </div>

      <!-- Λοιπές Πληροφορίες -->
      <div class="form-row">
        <label>Λοιπές Πληροφορίες</label>
        <textarea
          v-model="form.additionalInfo"
          maxlength="1000"
          rows="2"
          @input="autoResize($event)"
          ref="additionalInfoTextarea"
          placeholder="Γράψτε εδώ αν υπάρχουν επιπλέον πληροφορίες"
        ></textarea>
      </div>

      <!-- Submit -->
      <div class="form-row buttons-row">
        <button type="submit" :disabled="!canSubmit">Αποθήκευση</button>
        <button type="button" class="cancel-btn" @click="cancelEdit">Άκυρο</button>
      </div>
      
    </form>
    <pre style="color: red">
🔎 showConfirmationModal: {{ showConfirmationModal }}</pre
    >

    <!-- Modal Επιβεβαίωσης -->
    <div v-if="showConfirmationModal" class="modal">
      <div class="modal-content">
        <h3>📝 Επιβεβαίωση Στοιχείων</h3>
        <p><strong>Όνομα:</strong> {{ form.name }}</p>
        <p>
          <strong>Νομική Μορφή:</strong>
          {{ selectedLegalFormLabel }}
        </p>
        <p><strong>Υπουργείο:</strong> {{ selectedMinistryLabel }}</p>
        <p>
          <strong>ΟΤΑ:</strong>
          <span v-if="form.localAuthorityIds.length === 0">-</span>
          <span v-else>{{ selectedLocalAuthoritiesLabels }}</span>
        </p>
        <p><strong>Δημόσιος Τομέας:</strong> {{ form.publicSectorStatus }}</p>
        <p><strong>Τομέας Πολιτικής:</strong> {{ form.policySector || "-" }}</p>
        <p><strong>Διεύθυνση:</strong> {{ form.address || "-" }}</p>
        <p><strong>Τηλέφωνο:</strong> {{ form.phoneNumber || "-" }}</p>
        <p><strong>Ιστοσελίδα:</strong> {{ form.website || "-" }}</p>
        <p>
          <strong>Λοιπές Πληροφορίες:</strong> {{ form.additionalInfo || "-" }}
        </p>

        <div class="modal-buttons">
          <button @click="submitFinal()" class="confirm-btn">
            ✅ Επιβεβαίωση
          </button>
          <button @click="showConfirmationModal = false" class="cancel-btn">
            ❌ Ακύρωση
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, computed, watch, ref, onMounted, onUnmounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import {
  fetchMinistries,
  fetchLegalForms,
  fetchPublicSectorStatus,
  searchLocalAuthorities,
} from "../../../services/dropdown/DropdownService";
import {
  fetchOrganizationEditData,
  updateLocalOrganization,
  updatePublicOrganization,
} from "../../../services/admin/organizations/OrganizationAdminService";

// Για να πάρω id από επιλεγμένο Οργανισμό (Σελίδα OrganizationEditView - OrganizationSearchComponent)
const route = useRoute();
const router = useRouter();
const organizationId = route.params.id;

// Reactive options για τα dropdowns
const legalFormOptions = ref([]);
const ministryOptions = ref([]);
const publicSectorStatusOptions = ref([]);

const localAuthorityQuery = ref(""); // για το input του search
const allLocalAuthorities = ref([]); // για τη λίστα όλων των ΟΤΑ
const showOtaDropdown = ref(false);

// Αν το true → εμφανίζεται το modal επιβεβαίωσης
const showConfirmationModal = ref(false);
// Χρησιμοποιείται για disable στο κουμπί κατά την υποβολή
const isSubmitting = ref(false);

// Φέρνουμε τις επιλογές για Υπουργεία, Νομικές Μορφές και Δημόσιο Τομέα όταν ανοίγει η φόρμα
onMounted(async () => {
  // Legal Forms
  try {
    const response = await fetchLegalForms();
    legalFormOptions.value = response.data.map((option) => ({
      value: option.id,
      label: option.label,
      id: option.id,
    }));
  } catch (error) {
    legalFormOptions.value = [];
  }

  // Ministries
  try {
    const response = await fetchMinistries();
    ministryOptions.value = response.data.map((option) => ({
      value: option.id,
      label: option.label,
      id: option.id,
    }));
  } catch (error) {
    ministryOptions.value = [];
  }

  // Public Sector Status
  try {
    const response = await fetchPublicSectorStatus();
    publicSectorStatusOptions.value = response.data;
  } catch (error) {
    publicSectorStatusOptions.value = ["ΕΝΤΟΣ", "ΕΚΤΟΣ"];
  }

  // Όταν φορτώσει η σελίδα, φέρνουμε όλους τους OTA (query="")

  try {
    const response = await searchLocalAuthorities(""); // το query είναι κενό!
    allLocalAuthorities.value = response.data.map((option) => ({
      value: option.id,
      label: option.label,
      id: option.id,
    }));
    console.log("✅ OTA fetched:", allLocalAuthorities.value); // DEBUG
  } catch (error) {
    allLocalAuthorities.id = [];
  }

  // Οργανισμός προσυμπληρωμένα στοιχεία από επιλογή στο Search
  try {
    const data = await fetchOrganizationEditData(organizationId);

    console.log("📨 Δεδομένα από backend:", data);

    form.name = data.name;
    form.legalFormId = data.legalFormId;
    form.ministryId = data.ministryId || null;
    form.localAuthorityIds = data.localAuthorityIds || [];
    form.publicSectorStatus = greekLabelToEnumValue(data.publicSectorStatus);
    form.policySector = data.policySector;
    form.address = data.address;
    form.phoneNumber = data.phoneNumber;
    form.website = data.website;
    form.additionalInfo = data.additionalInfo;
    form.organizationType = data.organizationType;
  } catch (error) {
    console.error("❌ Σφάλμα κατά τη φόρτωση δεδομένων οργανισμού:", error);
  }
});

// Υπολογίζει τα options που εμφανίζονται ανάλογα με το query του χρήστη
const localAuthorityOptions = computed(() => {
  if (!localAuthorityQuery.value) return allLocalAuthorities.value;
  const queryUpper = localAuthorityQuery.value.toUpperCase();
  return allLocalAuthorities.value.filter(
    (option) =>
      option.label.toUpperCase().includes(queryUpper) ||
      (option.value + "").includes(queryUpper)
  );
});

// Βασικό state της φόρμας (form fields)
const form = reactive({
  name: "",
  legalFormId: null,
  legalFormName: "",
  ministryId: null,
  ministryName: null,
  localAuthorityIds: [],
  publicSectorStatus: "",
  policySector: "",
  address: "",
  phoneNumber: "",
  website: "",
  additionalInfo: "",
});

function resetForm() {
  form.name = "";
  form.legalFormId = null;
  form.legalFormName = "";
  form.ministryId = null;
  form.ministryName = null;
  form.localAuthorityIds = [];
  form.localAuthorityNames = [];
  form.publicSectorStatus = "";
  form.policySector = "";
  form.address = "";
  form.phoneNumber = "";
  form.website = "";
  form.additionalInfo = "";
}

// Βοηθητικά για business logic
const canSubmit = computed(() => {
  // Πρέπει να δώσει ΕΝΑ  από ministry ή local authority, όχι και τα δύο, όχι κανένα
  const hasMinistry = !!form.ministryId;
  const hasLocalAuthority = form.localAuthorityIds.length > 0;
  const exactlyOneSelected = hasMinistry !== hasLocalAuthority;
  return (
    form.name.trim().length > 0 &&
    form.publicSectorStatus &&
    form.legalFormId &&
    exactlyOneSelected
  );
});

// Αν ο χρήστης διαλέξει "Άλλο" στη νομική μορφή, κρατάμε το πεδίο legalFormName
watch(
  () => form.legalFormId,
  (newVal) => {
    if (newVal !== "other") {
      form.legalFormName = "";
    }
  }
);

// Αν αλλάξει Υπουργείο, καθαρίζει τους ΟΤΑ (αντίστροφα)
watch(
  () => form.ministryId,
  (val) => {
    if (val) {
      form.localAuthorityIds = [];
      showOtaDropdown.value = false;
      localAuthorityQuery.value = "";
    }
  }
);
watch(
  () => form.localAuthorityIds,
  (val) => {
    if (val.length > 0) {
      form.ministryId = "";
      form.policySector = "";
    }
  }
);

// Βρίσκει το label της νομικής μορφής (ή το 'άλλο' αν έχει πληκτρολογηθεί)
const selectedLegalFormLabel = computed(() => {
  if (form.legalFormId === "other") return form.legalFormName;
  const match = legalFormOptions.value.find(
    (o) => o.value === form.legalFormId
  );
  return match ? match.label : "";
});

// Βρίσκει το label του υπουργείου (αν υπάρχει)
const selectedMinistryLabel = computed(() => {
  const match = ministryOptions.value.find((o) => o.value === form.ministryId);
  return match ? match.label : "";
});

// Βρίσκει όλα τα labels των ΟΤΑ (σε μία γραμμή, χωρισμένα με κόμμα)
const selectedLocalAuthoritiesLabels = computed(() => {
  return form.localAuthorityIds
    .map((id) => {
      const found = allLocalAuthorities.value.find((o) => o.value === id);
      return found ? found.label : id;
    })
    .join(", ");
});

// Όταν ο χρήστης πατά "Αποθήκευση", ανοίγει το modal για επιβεβαίωση
function handleSubmit() {
  showConfirmationModal.value = true;
}

// Υποβολή στο backend μετά από επιβεβαίωση
async function submitFinal() {
  isSubmitting.value = true;

  try {
    // Δημιουργούμε το payload από τα πεδία της φόρμας
    const payload = {
      name: form.name,
      legalFormId: form.legalFormId,
      publicSectorStatus: form.publicSectorStatus,
      address: form.address,
      phoneNumber: form.phoneNumber,
      website: form.website,
      additionalInfo: form.additionalInfo,
    };

    // Αν είναι Public, προσθέτουμε τα επιπλέον πεδία
    if (form.organizationType === "PUBLIC") {
      payload.ministryId = form.ministryId;
      payload.policySector = form.policySector;
      console.log("📦 Payload που θα σταλεί:", payload);
      await updatePublicOrganization(organizationId, payload);
    }

    // Αν είναι Local, προσθέτουμε τα ΟΤΑ
    else if (form.organizationType === "LOCAL") {
      payload.localAuthorityIds = form.localAuthorityIds;
      console.log("📦 Payload που θα σταλεί:", payload);
      await updateLocalOrganization(organizationId, payload);
    }

    /* // Αν επιλέχθηκε "Άλλο" ως νομική μορφή, αφαιρούμε το id και βάζουμε το name
    if (form.legalFormId === "other") {
      payload.legalFormName = form.legalFormName;
      delete payload.legalFormId;
    } */

    // Κλείνουμε το modal και δείχνουμε επιτυχία
    showConfirmationModal.value = false;
    alert(`✅ Ο οργανισμός "${form.name}" ενημερώθηκε με επιτυχία.`);
    /*resetForm();
    console.log("📦 Payload μετά από εκαθάριση:", payload);*/
    // ✅ Redirect στη σελίδα αναζήτησης οργανισμών
    router.push("/admin/organizations/edit");
  } catch (error) {
    console.log("❌ Σφάλμα κατά την ενημέρωση (submitFinal):", error);
    console.log("📦 Payload μέσα στο σφάλμα:", payload);
    alert("❌ Αποτυχία ενημέρωσης οργανισμού.");
  } finally {
    isSubmitting.value = false;
  }
}

// Helpers για labels (ώστε να εμφανίζονται σωστά τα badges)
function getLocalAuthorityLabel(id) {
  const found = localAuthorityOptions.value.find((o) => o.value === id);
  return found ? found.label : id;
}

function removeLocalAuthority(id) {
  form.localAuthorityIds = form.localAuthorityIds.filter((v) => v !== id);
}

// Θα το χρειαστώ αν θελήσω να δείξω το label σε badge και όχι το id
function getMinistryLabel(id) {
  const found = ministryOptions.value.find((o) => o.value === id);
  return found ? found.label : id;
}

// Κλείνει το dropdown αν κάνεις κλικ εκτός
function handleClickOutside(event) {
  const dropdown = document.querySelector(".ota-dropdown");
  if (dropdown && !dropdown.contains(event.target)) {
    showOtaDropdown.value = false;
  }
}

const isLocalAuthorityInputDisabled = computed(() => !!form.ministryId);
onMounted(() => {
  document.addEventListener("click", handleClickOutside);
});
onUnmounted(() => {
  document.removeEventListener("click", handleClickOutside);
});

// Custom add/remove function (χωρίς Ctrl!)
function toggleLocalAuthority(id) {
  if (form.ministryId) return;
  if (form.localAuthorityIds.includes(id)) {
    form.localAuthorityIds = form.localAuthorityIds.filter((v) => v !== id);
  } else {
    form.localAuthorityIds.push(id);
  }
  // Reset το query για να μην εξαφανίζονται τα options που επέλεξες
  localAuthorityQuery.value = "";
  // Optionally, μπορείς να κρατάς το dropdown ανοιχτό ή να το κλείνεις
  // showOtaDropdown.value = false;
}

const additionalInfoTextarea = ref(null);

function autoResize(event) {
  const textarea = event.target;
  textarea.style.height = "auto";
  textarea.style.height = textarea.scrollHeight + "px";
}

function handleOtaFocus() {
  if (!form.ministryId) {
    showOtaDropdown.value = true;
  } else {
    showOtaDropdown.value = false;
  }
}

function handleOtaInput() {
  if (!isLocalAuthorityInputDisabled.value) {
    showOtaDropdown.value = true;
  }
}

function getGreekLabel(value) {
  switch (value) {
    case "ENTOS":
      return "ΕΝΤΟΣ";
    case "EKTOS":
      return "ΕΚΤΟΣ";
    default:
      return value;
  }
}

function greekLabelToEnumValue(label) {
  if (label === "ΕΝΤΟΣ") return "ENTOS";
  if (label === "ΕΚΤΟΣ") return "EKTOS";
  return null;
}

function cancelEdit() {
  router.push("/admin/organizations/edit"); // άλλαξέ το αν η διαδρομή είναι διαφορετική
}
</script>

<style scoped>
.create-org-form {
  max-width: 650px;
  margin: 0 auto;
  padding: 2.5rem 2rem;
  background: #f7fbff;
  border-radius: 20px;
  box-shadow: 0 0 10px #0001;
}
h2 {
  margin-bottom: 2rem;
  color: #225b99;
}
.form-row {
  margin-bottom: 1.2rem;
  display: flex;
  flex-direction: column;
}
label {
  font-weight: 600;
  margin-bottom: 0.25rem;
}
input,
select {
  padding: 0.45rem 0.75rem;
  border-radius: 8px;
  border: 1px solid #bbb;
  font-size: 1rem;
}
input:disabled,
select:disabled {
  background: #eef0f3;
  color: #888;
}
button[type="submit"] {
  background: #2265ba;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 0.7rem 2rem;
  font-size: 1.08rem;
  cursor: pointer;
  transition: background 0.17s;
}
button[type="submit"]:disabled {
  background: #a5b1c6;
  cursor: not-allowed;
}

.buttons-row {
  flex-direction: row;
  justify-content: flex-start;
  gap: 0.8rem; /* Απόσταση μεταξύ κουμπιών */
}

.cancel-btn {
  background: #f0f0f0;
  color: #333;
  border: 1px solid #bbb;
  border-radius: 8px;
  padding: 0.7rem 2rem;
  font-size: 1.08rem;
  cursor: pointer;
  transition: background 0.17s;
}

.cancel-btn:hover {
  background: #e0e0e0;
}

small {
  color: #777;
}

.selected-badges {
  margin-top: 0.3rem;
}
.badges-row {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
  margin-top: 0.3rem;
}
.badge {
  background: #e5f1fd;
  border: 1px solid #b5d1ea;
  color: #185989;
  border-radius: 16px;
  padding: 0.23rem 0.85rem 0.23rem 0.6rem;
  font-size: 0.98rem;
  display: flex;
  align-items: center;
  gap: 0.2rem;
}
.remove-btn {
  background: transparent;
  color: #185989;
  font-size: 1.08em;
  border: none;
  cursor: pointer;
  margin-left: 2px;
  line-height: 1;
  padding: 0 2px;
  border-radius: 8px;
  transition: background 0.15s;
}
.remove-btn:hover {
  background: #ecde12;
  font-size: larger;
}

textarea {
  min-height: 2.4rem;
  max-height: 300px;
  resize: none;
  font-size: 1rem;
  padding: 0.45rem 0.75rem;
  border-radius: 8px;
  border: 1px solid #bbb;
  transition: border-color 0.17s;
}

textarea:focus {
  border-color: #2265ba;
}

.ota-dropdown {
  position: absolute;
  top: 100%; /* ✅ Αυτό τη βάζει κάτω από το input */
  width: 95%;
  max-height: 180px;
  overflow-y: auto;
  border: 1px solid #bbb;
  border-radius: 8px;
  background: #fff;
  z-index: 20;
  box-shadow: 0 6px 20px #0002;
}

.ota-option.selected {
  background: #e5f1fd;
  color: #185989;
  font-weight: bold;
}
.ota-option:hover {
  background: #f3faff;
}
.ota-dropdown {
  box-shadow: 0 6px 20px #0002;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}
.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 20px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
  width: 90%;
  max-width: 600px;
}
.modal-content h3 {
  margin-bottom: 1rem;
  color: #225b99;
}
.modal-content p {
  margin: 0.4rem 0;
  font-size: 1rem;
}
.modal-buttons {
  display: flex;
  justify-content: center;
  gap: 1.5rem;
  margin-top: 1.8rem;
}
.confirm-btn,
.cancel-btn {
  padding: 0.6rem 1.5rem;
  font-size: 1rem;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  transition: background 0.2s;
}
.confirm-btn {
  background: #1d9748;
  color: white;
}
.cancel-btn {
  background: #bbb;
  color: white;
}
.confirm-btn:hover {
  background: #157a3a;
}
.cancel-btn:hover {
  background: #999;
}
</style>
