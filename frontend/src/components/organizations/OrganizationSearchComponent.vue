<template>
  <div class="organization-search-container">
    <!-- Κουμπί εναλλαγής φίλτρων (θα το υλοποιήσουμε αργότερα) -->
    <div class="filters-toggle">
      <button @click="filtersVisible = !filtersVisible">🎛️ Φίλτρα</button>
    </div>

    <!-- Περιοχή φίλτρων (placeholder για τώρα) -->
    <div v-if="filtersVisible" class="filters-section">
      <div class="filter-row">
        <label for="name-input">Ονομασία:</label>
        <input
          id="name-input"
          type="text"
          v-model="criteria.name"
          placeholder="Πληκτρολογήστε όνομα οργανισμού ή υπηρεσίας"
        />
        <label for="legal-form-select">Νομική Μορφή:</label>
        <select id="legal-form-select" v-model="criteria.legalFormId">
          <option :value="null">-- Όλες --</option>
          <option v-for="form in legalForms" :key="form.id" :value="form.id">
            {{ form.label }}
          </option>
        </select>
        <label for="public-sector-status-select">Δημόσιος Τομέας:</label>
        <select
          id="public-sector-status-select"
          v-model="criteria.publicSectorStatus"
        >
          <option :value="null">-- Όλοι --</option>
          <option
            v-for="status in publicSectorStatuses"
            :key="status"
            :value="status"
          >
            {{ status }}
          </option>
        </select>
        <button @click="handleSearch">Αναζήτηση</button>
      </div>
      <!-- Νέα σειρά -->
      <div class="filter-row">
        <!-- 🔹 Πεδίο αναζήτησης -->
        <label for="ministry-select">Εποπτεύον Υπουργείο:</label>
        <div
          class="dropdown-wrapper"
          style="position: relative; display: inline-block"
        >
          <input
            id="ministry-select"
            type="text"
            v-model="ministryQuery"
            placeholder="Αναζήτηση Υπουργείου..."
            @focus="showMinistryDropdown = true"
            @blur="showMinistryDropdown = false"
          />

          <!-- 🔹 Λίστα dropdown επιλογών -->
          <ul
            v-if="showMinistryDropdown && ministryOptions.length"
            class="dropdown-list"
          >
            <li
              v-for="option in ministryOptions"
              :key="option.id"
              @mousedown.prevent="toggleMinistrySelection(option)"
              :class="{ selected: criteria.ministryIds.includes(option.id) }"
            >
              {{ option.label }}
            </li>
          </ul>
        </div>
        <div
          v-if="criteria.ministryIds.length"
          class="selected-badges"
          style="margin-top: 0.5rem"
        >
          <small
            >Επιλέχθηκαν: {{ criteria.ministryIds.length }} Υπουργεία</small
          >
          <div class="badges-row">
            <span v-for="id in criteria.ministryIds" :key="id" class="badge">
              {{ getMinistryLabel(id) }}
              <button
                type="button"
                class="remove-btn"
                @click="removeMinistry(id)"
              >
                ×
              </button>
            </span>
          </div>
        </div>

        <!-- 🔹 Αναζήτηση ΟΤΑ -->
        <label for="ota-select">Φορέας ΟΤΑ:</label>
        <div class="dropdown-wrapper" style="position: relative">
          <input
            id="ota-select"
            type="text"
            v-model="localAuthorityQuery"
            placeholder="Αναζήτηση ΟΤΑ..."
            @focus="showLocalAuthorityDropdown = true"
            @blur="showLocalAuthorityDropdown = false"
          />

          <ul
            v-if="showLocalAuthorityDropdown && localAuthorityOptions.length"
            class="dropdown-list"
          >
            <li
              v-for="option in localAuthorityOptions"
              :key="option.id"
              @mousedown.prevent="toggleLocalAuthoritySelection(option)"
              :class="{
                selected: criteria.localAuthorityIds.includes(option.id),
              }"
            >
              {{ option.label }}
            </li>
          </ul>
        </div>

        <div
          v-if="criteria.localAuthorityIds.length"
          class="selected-badges"
          style="margin-top: 0.5rem"
        >
          <small
            >Επιλέχθηκαν: {{ criteria.localAuthorityIds.length }} ΟΤΑ</small
          >
          <div class="badges-row">
            <span
              v-for="option in selectedLocalAuthorities"
              :key="option.id"
              class="badge"
            >
              {{ option.label }}
              <button
                type="button"
                class="remove-btn"
                @click="removeLocalAuthority(option.id)"
              >
                ×
              </button>
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- Κύριο περιεχόμενο: Πίνακας και Χάρτης -->
    <div class="main-content">
      <!-- 📄 Πίνακας Οργανισμών -->
      <div class="table-section">
        <table class="org-table">
          <thead>
            <tr>
              <th>Ονομασία Οργανισμού/Υπηρεσίας</th>
              <th>Εποπτεύον Υπουργείο</th>
              <th>Εποπτεύων ΟΤΑ</th>
              <th>Νομική Μορφή - Είδος Υπηρεσίας</th>
              <th>Τομείς Πολιτικής</th>
              <th>Υπαγωγή στο Δημόσιο Τομέα</th>
              <th>Διεύθυνση</th>
              <th>Τηλέφωνο</th>
              <th>Website</th>
              <th>Λοιπές Πληροφορίες</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="org in organizations"
              :key="org.id"
              :data-org-id="org.id"
              @click="handleRowClick(org)"
              @mouseenter="$emit('rowHover', org.id)"
              @mouseleave="$emit('rowHover', null)"
              :class="[
                'clickable-row',
                props.highlightedOrganizationId === org.id ? 'highlighted' : '',
              ]"
            >
              <td>{{ org.name }}</td>
              <td>{{ org.supervisingMinistryName || "-" }}</td>
              <td>
                {{ org.supervisingLocalAuthorityNames?.join(", ") || "-" }}
              </td>
              <td>{{ org.legalFormName }}</td>
              <td>{{ org.policySector || "-" }}</td>
              <td>{{ org.publicSectorStatus }}</td>
              <td>{{ org.address || "-" }}</td>
              <td>{{ org.phoneNumber || "-" }}</td>
              <td>
                <a
                  v-if="org.website"
                  :href="org.website"
                  target="_blank"
                  class="website-link"
                >
                  {{ org.website }}
                </a>
                <span v-else>-</span>
              </td>
              <td class="tooltip-cell">
                <span class="clamp-text">{{ org.additionalInfo || "-" }}</span>
                <span class="tooltip-text">{{ org.additionalInfo }}</span>
              </td>
            </tr>
          </tbody>
        </table>
        <div class="pagination-controls">
          <button :disabled="currentPage === 0" @click="goToPreviousPage">
            ⬅️ Προηγούμενη
          </button>
          <span>Σελίδα {{ currentPage + 1 }} από {{ totalPages }}</span>
          <button
            :disabled="currentPage >= totalPages - 1"
            @click="goToNextPage"
          >
            Επόμενη ➡️
          </button>
        </div>
      </div>
    </div>
  </div>

  <!-- 🔧 Modal Ενεργειών Admin -->
  <div
    v-if="props.isAdmin && showAdminOptionsModal"
    class="modal-overlay"
    @click.self="showAdminOptionsModal = false"
  >
    <div class="modal">
      <h3>🏛️ {{ selectedOrganization?.name }}</h3>
      <div class="modal-actions">
        <button @click="goToEditOrganization">✏️ Επεξεργασία</button>
        <button @click="handleDeleteOrganization">🗑️ Διαγραφή</button>
      </div>
      <button class="modal-close" @click="showAdminOptionsModal = false">
        ✖ Κλείσιμο
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from "vue";
import { searchOrganizations } from "../../services/organizations/OrganizationSearchService";
import { deleteOrganization } from "../../services/admin/organizations/OrganizationAdminService";
import {
  fetchLegalForms,
  fetchPublicSectorStatus,
  fetchMinistries,
  searchLocalAuthorities,
} from "../../services/dropdown/DropdownService";
import debounce from "lodash.debounce";
import { useRouter } from "vue-router";
const router = useRouter();

// Ορισμός μεταβλητών
const organizations = ref([]);
const legalForms = ref([]);
const publicSectorStatuses = ref([]);
const ministries = ref([]);
const loading = ref(false);
const error = ref(null);

const ministryQuery = ref(""); // για φιλτράρισμα input
const allMinistryOptions = ref([]); // τα options όταν φορτωθούν όλα
const showMinistryDropdown = ref(false);

const localAuthorityQuery = ref(""); // Τι πληκτρολογεί ο χρήστης
const showLocalAuthorityDropdown = ref(false); // Αν είναι ανοιχτό το dropdown
const localAuthorityOptions = ref([]); // Οι ΟΤΑ που προτείνει το backend
const selectedLocalAuthorities = ref([]);

const emit = defineEmits(["update:organizations", "rowClick", "rowHover"]); // Για να τα στείλω στον Γονέα OrganizationSearchView.vue

const criteria = ref({
  name: "",
  legalFormId: null,
  publicSectorStatus: null,
  ministryIds: [],
  localAuthorityIds: [],
  page: 0,
  size: 20,
});

const totalPages = ref(0);
const currentPage = ref(0);

const filtersVisible = ref(true);
const selectedOrganization = ref(null); // επιλογή γραμμής (οργανισμού)
const showAdminOptionsModal = ref(false); // Modal για ενημέρωση/διαγγραφη Οργανισμού

// ΣΟΣ για έλεγχο admin ή publicUser
const props = defineProps({
  isAdmin: Boolean,
  highlightedOrganizationId: Number,
});

// Διαχείριση κλίκ. Άλλη ροή για admin και publicUser
function handleRowClick(org) {
  if (props.isAdmin) {
    handleAdminClick(org);
  } else {
    handlePublicClick(org);
  }
}

function handleAdminClick(org) {
  selectedOrganization.value = org;
  showAdminOptionsModal.value = true;
}

// !!!!!!!!!! SOS SOS SOS EDW  EDW GIA PUBLIC USER !!!!!!!!!!!!
function handlePublicClick(org) {
  // προς το παρόν placeholder
  console.log("🔍 Δημόσια προβολή για οργανισμό:", org.id);
  selectedOrganization.value = org;
  emit("rowClick", org);
}

// Redirect
function goToEditOrganization() {
  const id = selectedOrganization.value?.id;
  if (!id) return;
  showAdminOptionsModal.value = false;
  router.push(`/admin/organizations/edit-form/${id}`);
}

// Διαγραφή
async function handleDeleteOrganization() {
  const org = selectedOrganization.value;
  if (!org || !org.id) return;

  const confirmed = confirm(
    `❗ Πρόκειται να διαγράψετε τον οργανισμό "${org.name}" είστε σίγουροι ότι θέλετε να συνεχίσετε;`
  );
  console.log("🗑️ Διαγραφή οργανισμού:", org.id);
  if (!confirmed) return;
  try {
    await deleteOrganization(org.id);
    alert(`✅ Ο οργανισμός "${org.name}" διαγράφηκε επιτυχώς.`);
    showAdminOptionsModal.value = false;
    fetchOrganizations(); // Επαναφόρτωση
  } catch (err) {
    console.error("❌ Σφάλμα διαγραφής:", err);
    alert("❌ Αποτυχία διαγραφής. Δοκιμάστε ξανά.");
  }
}

//-----//

// δυναμικά για localAuthorities
watch(criteria, (newVal) => {
  console.log("✅ Κριτήρια:", newVal.localAuthorityIds);
});

watch(selectedLocalAuthorities, (newVal) => {
  console.log("📌 Επιλεγμένοι ΟΤΑ (labels):", newVal);
});

// Μέθοδος για αναζήτηση Οργανισμών
async function fetchOrganizations() {
  loading.value = true;
  error.value = null;

  try {
    const result = await searchOrganizations(criteria.value);
    organizations.value = result.content;
    totalPages.value = result.totalPages;
    currentPage.value = result.number;
    // στέλνω τα αποτελέσματα στον γονέα (που είναι το SearchView)
    emit("update:organizations", result.content);
    console.log("📤 Εκπέμπεται:", result.content);
  } catch (err) {
    console.error("❌ Σφάλμα στο fetch:", err);
    error.value = "Αποτυχία φόρτωσης δεδομένων";
  } finally {
    loading.value = false;
  }
}

//--
// Αναζήτηση με φίλτρα
async function loadLegalForms() {
  try {
    const response = await fetchLegalForms();
    legalForms.value = response.data;
  } catch (err) {
    console.error("❌ Αποτυχία φόρτωσης νομικών μορφών", err);
  }
}

async function loadPublicSectorStatuses() {
  try {
    const response = await fetchPublicSectorStatus();
    publicSectorStatuses.value = response.data;
  } catch (err) {
    console.error("❌ Αποτυχία φόρτωσης status", err);
  }
}

async function loadMinistries() {
  try {
    const response = await fetchMinistries();
    ministries.value = response.data;
    allMinistryOptions.value = response.data;
  } catch (err) {
    console.error("❌ Αποτυχία φόρτωσης υπουργείων", err);
  }
}

watch(
  localAuthorityQuery,
  debounce(async (newQuery) => {
    if (!newQuery || newQuery.trim() === "") {
      localAuthorityOptions.value = [];
      return;
    }
    try {
      const response = await searchLocalAuthorities(newQuery);
      localAuthorityOptions.value = response.data;
    } catch (err) {
      console.error("❌ Σφάλμα κατά την αναζήτηση ΟΤΑ", err);
    }
  }, 300) //debounce 300ms
);

// φιλτραρισμένα υπουργεία από το input
const ministryOptions = computed(() => {
  if (!ministryQuery.value) return allMinistryOptions.value;

  const query = ministryQuery.value.toLowerCase();
  return allMinistryOptions.value.filter((m) =>
    m.label.toLowerCase().includes(query)
  );
});

onMounted(async () => {
  await fetchOrganizations();
  await loadLegalForms();
  await loadPublicSectorStatuses();
  await loadMinistries();
});

function handleSearch() {
  criteria.value.page = 0;
  fetchOrganizations();
}

// Λογική για Επόμενη/Προηγούμενη σελίδα
function goToPreviousPage() {
  if (currentPage.value > 0) {
    criteria.value.page -= 1;
    fetchOrganizations();
  }
}

function goToNextPage() {
  if (currentPage.value < totalPages.value - 1) {
    criteria.value.page += 1;
    fetchOrganizations();
  }
}

// Επιστρέφει το label ενός υπουργείου με βάση id
function getMinistryLabel(id) {
  const found = ministries.value.find((m) => m.id === id);
  return found ? found.label : "Άγνωστο";
}

// Προσθαφαίρεση επιλογής
function toggleMinistrySelection(option) {
  const index = criteria.value.ministryIds.indexOf(option.id);
  if (index === -1) {
    criteria.value.ministryIds.push(option.id);
  } else {
    criteria.value.ministryIds.splice(index, 1);
  }
}

function toggleLocalAuthoritySelection(option) {
  console.log("👉 Επιλογή ΟΤΑ:", option);

  if (!option || !option.id) {
    console.warn("⚠️ Option δεν έχει id ή είναι undefined:", option);
    return;
  }
  const index = criteria.value.localAuthorityIds.indexOf(option.id);
  if (index === -1) {
    criteria.value.localAuthorityIds.push(option.id);
    selectedLocalAuthorities.value.push(option);
  } else {
    criteria.value.localAuthorityIds.splice(index, 1);
    selectedLocalAuthorities.value = selectedLocalAuthorities.value.filter(
      (opt) => opt.id !== option.id
    );
  }
}

// Αφαίρεση μεμονωμένου badge
function removeMinistry(id) {
  const index = criteria.value.ministryIds.indexOf(id);
  if (index !== -1) {
    criteria.value.ministryIds.splice(index, 1);
  }
}

function removeLocalAuthority(id) {
  const index = criteria.value.localAuthorityIds.indexOf(id);
  if (index !== -1) {
    criteria.value.localAuthorityIds.splice(index, 1);
    selectedLocalAuthorities.value = selectedLocalAuthorities.value.filter(
      (opt) => opt.id !== id
    );
  }
}
</script>

<style scoped>
.organization-search-container {
  padding: 1.5rem;
}

.filters-toggle {
  margin-bottom: 0.5rem;
}

.main-content {
  display: flex;
  gap: 1.5rem;
}

.table-section {
  max-height: 600px; /* ή ό,τι θέλεις */
  max-width: 1280px;
  overflow-y: auto; /* ενεργοποιεί κάθετο scroll */
  border: 1px solid #ddd; /* προαιρετικά για να ξεχωρίζει */
}

.map-section {
  flex: 1;
  background-color: #f9f9f9;
  border: 1px solid #ccc;
  padding: 0.5rem;
}

.org-table {
  width: 100%;
  border-collapse: collapse;
}

.org-table thead th {
  position: sticky;
  top: 0;
  background-color: #f0f0f0;
  z-index: 1;
}

.org-table th,
.org-table td {
  border: 1px solid #ddd;
  padding: 0.6rem;
  text-align: left;
  vertical-align: top;
}

.org-table th {
  background-color: #f0f0f0;
  font-weight: 600;
}

.clickable-row {
  cursor: pointer;
}
.clickable-row:hover {
  background-color: #eaf2ff;
}

tr.highlighted {
  background-color: #fff8dc !important;
}

.tooltip-cell {
  position: relative;
}

.clamp-text {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2; /* ✅ standard fallback */
  -webkit-box-orient: vertical;
  overflow: hidden;
  cursor: pointer;
}

.tooltip-cell:hover .tooltip-text {
  display: block;
}

.tooltip-text {
  display: none;
  position: absolute;
  z-index: 10;
  max-width: 300px;
  background-color: #fff;
  border: 1px solid #ccc;
  padding: 0.4rem;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15);
  white-space: pre-wrap;
  top: 100%;
  left: 0;
}

.website-link {
  color: #2265ba;
  text-decoration: none;
  transition: all 0.2s ease;
  font-weight: 500;
}

.website-link:hover {
  color: #0b3ea1;
  text-decoration: underline;
  background-color: #eaf2ff;
  border-radius: 4px;
  padding: 0.1rem 0.2rem;
}

.filters-section {
  margin-bottom: 1rem;
  background-color: #f7f7f7;
  padding: 1rem;
  border-radius: 8px;
}

.filter-row {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.filter-row input {
  padding: 0.4rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  flex: 1;
}

.filter-row button {
  background-color: #2265ba;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  cursor: pointer;
}

.filter-row select {
  padding: 0.4rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.filter-row select[multiple] {
  height: 6rem;
}

.dropdown-list {
  position: absolute;
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 4px;
  max-height: 200px; /* περιορισμός ύψους */
  overflow-y: auto; /* scroll αν υπάρχουν πολλές επιλογές */
  width: 300px; /* πλάτος dropdown */
  font-size: 0.85rem; /* μικρότερη γραμματοσειρά */
  z-index: 1000; /* πάνω από άλλα στοιχεία */
  margin-top: 0.2rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.dropdown-list li {
  padding: 0.4rem;
  cursor: pointer;
}
.dropdown-list li:hover {
  background-color: #eee;
}
.dropdown-list li.selected {
  font-weight: 500;
  color: #2265ba;
}

.badges-row {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-top: 0.3rem;
}
.badge {
  background: #e0e0e0;
  padding: 0.3rem 0.6rem;
  border-radius: 12px;
  font-size: 0.85rem;
  display: flex;
  align-items: center;
}
.remove-btn {
  margin-left: 0.4rem;
  border: none;
  background: transparent;
  color: #333;
  font-size: 1rem;
  cursor: pointer;
}

.pagination-controls {
  margin-top: 1rem;
  display: flex;
  align-items: center;
  gap: 1rem;
}

.pagination-controls button {
  padding: 0.4rem 0.8rem;
  border: none;
  background-color: #2265ba;
  color: white;
  border-radius: 6px;
  cursor: pointer;
}

.pagination-controls button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.dropdown-wrapper {
  width: 300px;
}

.dropdown-wrapper input {
  width: 100%;
}

/* Αυτά είναι προσωρινά */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}

.modal {
  background: white;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
  width: 500px;
  text-align: center;
}

.modal-actions button {
  margin: 10px;
  padding: 8px 12px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.modal-actions button:first-child {
  background-color: #207ad4;
  color: white;
}

.modal-actions button:last-child {
  background-color: #d43f3f;
  color: white;
}

.modal-close {
  margin-top: 15px;
  background: transparent;
  border: none;
  color: #666;
  font-size: 14px;
  cursor: pointer;
}
</style>
