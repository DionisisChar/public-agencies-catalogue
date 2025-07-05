<template>
  <div class="organization-search-layout">
    <!-- 🔍 Αναζήτηση και πίνακας + χάρτης δίπλα -->
    <div class="content-row">
      <div class="left-panel">
        <OrganizationSearchComponent
          :isAdmin="false"
          @update:organizations="handleOrganizationsUpdate"
          @row-click="handleRowClick"
          @rowHover="handleRowHover"
          :highlightedOrganizationId="highlightedOrganizationId"
        />
      </div>
      <div
        v-if="showFloatingPopup"
        class="floating-popup"
        :style="{
          top: floatingPopupTop + 'px',
          left: floatingPopupLeft + 'px',
        }"
      >
        <button @click="goToSelectedOrganization">
          ➤ Μετάβαση στον Οργανισμό
        </button>
      </div>
      <div class="right-map">
        <div id="org-map" class="leaflet-map"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, onUnmounted, nextTick } from "vue";
import OrganizationSearchComponent from "../../components/organizations/OrganizationSearchComponent.vue";
import L from "leaflet";
import { useRouter } from "vue-router";
const router = useRouter();

// Τα δεδομένα των οργανισμών (20 ανά σελίδα)
const organizations = ref([]);
const highlightedOrganizationId = ref(null);
const selectedOrganization = ref(null);
//  Αυτό θα κρατάει το Leaflet map instance
let map = null;
let markersLayer = null;

// Για popup όταν κάνω click σε γραμμή
const showFloatingPopup = ref(false);
const floatingPopupTop = ref(0);
const floatingPopupLeft = ref(0);

onMounted(() => {
  map = L.map("org-map").setView([38.0, 23.7], 6); // Κέντρο Ελλάδα

  L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    attribution: "© OpenStreetMap contributors",
  }).addTo(map);

  markersLayer = L.layerGroup().addTo(map);
});

watch(organizations, (newOrgs) => {
  if (!map || !markersLayer) return;

  markersLayer.clearLayers(); // 🔁 Καθαρίζουμε προηγούμενα pins

  const bounds = [];

  newOrgs.forEach((org) => {
    if (org.latitude && org.longitude) {
      const marker = L.marker([org.latitude, org.longitude], {
        orgId: org.id, // ✅ Αποθηκεύουμε ID του οργανισμού πάνω στο marker
      })
        .bindPopup(() => {
          const div = document.createElement("div");
          div.innerHTML = `
    <div class="popup-content">
      <strong>${org.name}</strong><br/>
      ${org.address ? `<div class="org-address">${org.address}</div>` : ""}
      <button class="details-button" id="go-to-org-${org.id}">
        ➤ Μετάβαση στον Οργανισμό
      </button>
    </div>
  `;
          setTimeout(() => {
            const btn = div.querySelector(`#go-to-org-${org.id}`);
            btn?.addEventListener("click", () => {
              router.push(`/organizations/${org.id}`);
            });
          }, 0);
          return div;
        })
        .on("click", () => {
          console.log("📌 Clicked on:", org.id);
          // Εδώ μετά θα βάλουμε router.push(`/details/${org.id}`)
        })
        .on("mouseover", () => {
          highlightRow(org.id);
        })
        .on("mouseout", () => {
          unhighlightRow(org.id);
        });

      marker.addTo(markersLayer);
      if (highlightedOrganizationId.value === org.id) {
        setTimeout(() => {
          const icon = marker._icon;
          if (icon) {
            icon.classList.add("highlighted-marker");
          }
        });
      }
      bounds.push([org.latitude, org.longitude]);
    }
  });

  if (bounds.length > 0) {
    map.fitBounds(bounds, { padding: [20, 20] });
  }
});

function highlightRow(orgId) {
  const row = document.querySelector(`tr[data-org-id="${orgId}"]`);
  if (row) row.classList.add("highlighted");
}

function unhighlightRow(orgId) {
  const row = document.querySelector(`tr[data-org-id="${orgId}"]`);
  if (row) row.classList.remove("highlighted");
}

// Όταν το component εκπέμπει τα αποτελέσματα αναζήτησης
function handleOrganizationsUpdate(newResults) {
  organizations.value = newResults;
}

// 👆 Όταν γίνεται κλικ σε γραμμή πίνακα
function handleRowClick(org) {
  // Zoom σε χάρτη
  selectedOrganization.value = org;
  console.log("📥 Λήφθηκε κλικ για org:", org);
  console.log("📍 Floating button:", showFloatingPopup.value);
  console.log("📌 Selected ID:", selectedOrganization.id);
  if (markersLayer) {
    markersLayer.eachLayer((layer) => {
      if (layer.getLatLng && layer.options.orgId === org.id) {
        const latLng = layer.getLatLng();
        map.setView(latLng, 14, { animate: true });
        layer.openPopup(); // 🧷 Άνοιγμα popup
      }
    });
  }

  // Popup σε click γραμμής
  console.log("🔍 showFloatingPopup.value πριν:", showFloatingPopup.value);
  function tryShowPopup() {
    const row = document.querySelector(`tr[data-org-id="${org.id}"]`);
    if (row) {
      const rect = row.getBoundingClientRect();
      floatingPopupTop.value = rect.top + window.scrollY - 10;
      floatingPopupLeft.value = rect.left + rect.width - 180;
      showFloatingPopup.value = true;
    } else if (retries < maxRetries) {
      retries++;
      setTimeout(tryShowPopup, 50); // δοκιμάζει ξανά μετά από 50ms
    } else {
      console.warn("❌ Δεν βρέθηκε γραμμή για org:", org.id);
    }
  }

  tryShowPopup();
}

function hideFloatingPopup() {
  showFloatingPopup.value = false;
}

onMounted(() => {
  window.addEventListener("scroll", hideFloatingPopup);

  window.addEventListener("click", (e) => {
    const isInsidePopup = e.target.closest(".floating-popup");
    const isInsideTableRow = e.target.closest("tr[data-org-id]");

    if (!isInsidePopup && !isInsideTableRow) {
      hideFloatingPopup();
    }
  });
});

function goToSelectedOrganization() {
  if (selectedOrganization.value && selectedOrganization.value.id) {
    router.push(`/organizations/${selectedOrganization.value.id}`);
  } else {
    console.warn("❌ selectedOrganization is null ή δεν έχει id");
  }
}

// Το id του οργανισμού που κάνουμε hover στον πίνακα
const hoveredOrganizationId = ref(null);

function handleRowHover(orgId) {
  hoveredOrganizationId.value = orgId;

  // 🔁 Highlight marker αν υπάρχει
  if (markersLayer && orgId != null) {
    markersLayer.eachLayer((layer) => {
      if (layer.getLatLng && layer.options.orgId === orgId) {
        layer._icon.classList.add("highlighted-marker");
      } else {
        layer._icon?.classList.remove("highlighted-marker");
      }
    });
  } else {
    // Αφαίρεσε το highlight από όλα
    markersLayer.eachLayer((layer) => {
      layer._icon?.classList.remove("highlighted-marker");
    });
  }
}
</script>

<style scoped>
.organization-search-layout {
  max-width: 1440px;
  margin: 0 auto;
}

/* Layout 2 στηλών */
.content-row {
  display: flex;
  gap: 0rem;
  align-items: flex-start;
}

/* Αριστερό πάνελ: πίνακας και φίλτρα */
.left-panel {
  flex: 2;
}

:deep(.table-section) {
  max-width: 1000px;
}

.floating-popup {
  position: absolute;
  background-color: white;
  border: 1px solid #ccc;
  padding: 0.4rem 1rem;
  border-radius: 8px;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.1);
  z-index: 9999;
}
.floating-popup button {
  background: #2265ba;
  color: white;
  border: none;
  border-radius: 6px;
  padding: 0.4rem 0.8rem;
  font-size: 0.9rem;
  cursor: pointer;
}

/* Δεξιό πάνελ: χάρτης */
.right-map {
  flex: 1.2;
  min-width: 460px;
  min-height: 420px;
  top: 17rem;
  position: sticky;
  align-self: flex-start;
  /* transform: translateX(-190px); */
}

.leaflet-map {
  height: 420px;
  width: 100%;
  border-radius: 12px;
  border: 1px solid #ccc;
}
</style>
