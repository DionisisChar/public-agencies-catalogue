<template>
  <div class="map-only-layout">
    <h2>🗺️ Πλοήγηση Οργανισμών μέσω Χάρτη</h2>
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
        <button @click="handleSearch" class="search-button">Αναζήτηση</button>
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
    <!-- 🗺️ Χάρτης -->
    <div id="full-map" class="leaflet-map"></div>
  </div>
</template>

<script setup>
import { onMounted, ref, computed, watch } from "vue";
import L from "leaflet";
import debounce from "lodash.debounce";
import {
  fetchLegalForms,
  fetchPublicSectorStatus,
  fetchMinistries,
  searchLocalAuthorities,
} from "../../services/dropdown/DropdownService";
import { fetchOrganizationsForMap } from "../../services/organizations/OrganizationMapService";
import { useRouter } from "vue-router";
const router = useRouter();

const criteria = ref({
  name: "",
  legalFormId: null,
  publicSectorStatus: null,
  ministryIds: [],
  localAuthorityIds: [],
});
const organizations = ref([]);
let map = null;
let markersLayer = null;
const filtersVisible = ref(true);

// Dropdown δεδομένα
const legalForms = ref([]);
const publicSectorStatuses = ref([]);
const ministries = ref([]);
const allMinistryOptions = ref([]);
const localAuthorityOptions = ref([]);
const selectedLocalAuthorities = ref([]);

// Εσωτερικά για dropdown inputs
const ministryQuery = ref("");
const localAuthorityQuery = ref("");
const showMinistryDropdown = ref(false);
const showLocalAuthorityDropdown = ref(false);

onMounted(async () => {
  //  Δημιουργία Leaflet map
  map = L.map("full-map").setView([38.0, 23.7], 6); // Κέντρο Ελλάδα

  L.tileLayer(
    "https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png",
    {
      attribution: "&copy; OpenStreetMap contributors & CartoDB",
      subdomains: "abcd",
      maxZoom: 19,
    }
  ).addTo(map);

  markersLayer = L.layerGroup().addTo(map);

  //  Φόρτωση dropdowns
  await loadDropdowns();

  // Χρήση του reactive criteria ref
  const results = await fetchOrganizationsForMap(criteria.value);
  organizations.value = results;

  placePins(organizations.value);
});

// Φιλτράρισμα υπουργείων
const ministryOptions = computed(() => {
  if (!ministryQuery.value) return allMinistryOptions.value;
  const query = ministryQuery.value.toUpperCase();
  return allMinistryOptions.value.filter((m) =>
    m.label.toUpperCase().includes(query)
  );
});

// Debounced αναζήτηση ΟΤΑ
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
  }, 300)
);

async function loadDropdowns() {
  try {
    const [forms, statuses, ministriesResponse] = await Promise.all([
      fetchLegalForms(),
      fetchPublicSectorStatus(),
      fetchMinistries(),
    ]);
    legalForms.value = forms.data;
    publicSectorStatuses.value = statuses.data;
    allMinistryOptions.value = ministriesResponse.data;
    ministries.value = ministriesResponse.data;
  } catch (err) {
    console.error("❌ Αποτυχία φόρτωσης dropdown δεδομένων", err);
  }
}

async function handleSearch() {
  const results = await fetchOrganizationsForMap(criteria.value);
  organizations.value = results;
  placePins(results);
}

function placePins(orgList) {
  if (!map || !markersLayer) return;

  markersLayer.clearLayers();
  const bounds = [];

  orgList.forEach((org) => {
    if (org.latitude && org.longitude) {
      const marker = L.marker([org.latitude, org.longitude], {
        orgId: org.id,
      })
        .bindPopup(() => {
          const div = document.createElement("div");
          div.innerHTML = `
            <div class="popup-content">
              <strong>${org.name}</strong><br/>
              <small>
                ${
                  org.organizationType === "PUBLIC"
                    ? `Εποπτεύον Υπουργείο: ${org.supervisingMinistryName || "—"}`
                    : `Εποπτεύων ΟΤΑ: ${org.supervisingLocalAuthorityNames?.join(", ") || "—"}`
                }
              </small><br/>
              ${
                org.address ? `<div class="org-address">${org.address}</div>` : ""
              }
              ${
                org.phoneNumber
                  ? `<div class="org-phone">📞 ${org.phoneNumber}</div>`
                  : ""
              }
              <button class="details-button" id="go-to-org-${org.id}">
                ➤ Μετάβαση στον Οργανισμό
              </button>
            </div>
          `;
          // ✳️ Σύνδεση Vue Router στο κουμπί
          setTimeout(() => {
            const btn = div.querySelector(`#go-to-org-${org.id}`);
            btn?.addEventListener("click", () => {
              router.push(`/organizations/${org.id}`);
            });
          }, 0);
          return div;
        })
        .addTo(markersLayer);

      marker.on("click", () => {
        console.log("📍 Marker clicked:", org); //  Για debugging μόνο
      });
      bounds.push([org.latitude, org.longitude]);
    }
  });

  if (bounds.length > 0) {
    map.fitBounds(bounds, { padding: [20, 20] });
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
/** Styling για Φίλτρα */
.filters-toggle {
  margin-bottom: 0.5rem;
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
  font-size: 0.7rem;
  display: flex;
  align-items: center;
}
.remove-btn {
  margin-left: 0.4rem;
  border: none;
  background: transparent;
  color: #333;
  font-size: 0.8rem;
  line-height: 1;
  padding: 0;
  cursor: pointer;
  transform: scale(0.85);
}

.dropdown-wrapper {
  width: 300px;
}

.dropdown-wrapper input {
  width: 100%;
}

/* Styling για χάρτη */
.map-only-layout {
  max-width: 1440px;
  margin: 0 auto;
  padding: 2rem;
}

.leaflet-map {
  height: 75vh;
  width: 100%;
  border-radius: 12px;
  border: 1px solid #ccc;
  margin-top: 1rem;
}

.leaflet-popup-content {
  font-size: 0.9rem;
  line-height: 1.4;
}

.popup-content {
  max-width: 250px;
}

:deep(.popup-content strong) {
  font-size: 1.05rem;
  font-weight: 600;
  display: block;
  margin-bottom: 4px;
}

:deep(.popup-content small) {
  font-size: 0.95rem;
  color: #444;
}

:deep(.popup-content div) {
  margin-top: 5px;
}

:deep(.org-address) {
  font-size: 0.95rem;
  margin-top: 4px;
  color: #333;
}

:deep(.org-phone) {
  font-size: 0.95rem;
  margin-top: 4px;
  color: #2265ba;
  font-weight: 500;
}

:deep(.details-button) {
  margin-top: 10px;
  background-color: #2265ba;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: background-color 0.2s ease;
}

:deep(.details-button:hover) {
  background-color: #1a4e90;
}

.search-button {
  background-color: #2265ba;
  color: white;
  border: none;
  padding: 8px 12px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.9rem;
  margin-left: 1rem;
}
</style>
