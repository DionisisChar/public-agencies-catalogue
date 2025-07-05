<template>
  <div class="org-details-view">
    <h1>Αναλυτική Καρτέλα Δημόσιου Οργανισμού / Υπηρεσίας</h1>

    <!-- 📡 Κατάσταση φόρτωσης -->
    <div v-if="isLoading">
      <p>⏳ Φόρτωση δεδομένων οργανισμού...</p>
    </div>

    <!-- ❌ Σφάλμα -->
    <div v-if="hasError && !isLoading">
      <p style="color: red">
        ⚠️ Αδυναμία φόρτωσης του οργανισμού. Παρακαλώ δοκιμάστε ξανά.
      </p>
    </div>

    <!-- 🧾 Πληροφορίες οργανισμού -->
    <div v-if="organization && !isLoading && !hasError" class="org-card">
      <h2 class="org-name">{{ organization.name }}</h2>

      <table class="org-details-table">
        <tbody>
          <tr>
            <th>Ονομασία</th>
            <td>{{ organization.name }}</td>
          </tr>
          <tr v-if="organization.organizationType === 'PUBLIC'">
            <th>Εποπτεύον Υπουργείο</th>
            <td>{{ organization.supervisingMinistryName || "—" }}</td>
          </tr>
          <tr v-else>
            <th>Εποπτεύων ΟΤΑ</th>
            <td>
              <span v-if="organization.supervisingLocalAuthorityNames?.length">
                {{ organization.supervisingLocalAuthorityNames.join(", ") }}
              </span>
              <span v-else>—</span>
            </td>
          </tr>
          <tr>
            <th>Νομική Μορφή</th>
            <td>{{ organization.legalFormName || "—" }}</td>
          </tr>
          <tr>
            <th>Τομέας Πολιτικής</th>
            <td>{{ organization.policySector || "—" }}</td>
          </tr>
          <tr>
            <th>Υπαγωγή στο Δημόσιο Τομέα</th>
            <td>{{ organization.publicSectorStatus }}</td>
          </tr>
          <tr>
            <th>Διεύθυνση</th>
            <td>{{ organization.address || "—" }}</td>
          </tr>
          <tr>
            <th>Τηλέφωνο</th>
            <td>{{ organization.phoneNumber || "—" }}</td>
          </tr>
          <tr>
            <th>Ιστότοπος</th>
            <td>
              <a
                v-if="organization.website"
                :href="organization.website"
                target="_blank"
              >
                {{ organization.website }}
              </a>
              <span v-else>—</span>
            </td>
          </tr>
          <tr>
            <th>Λοιπές Πληροφορίες</th>
            <td>{{ organization.additionalInfo || "—" }}</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Διαύγεια -->
    <div
      v-if="organization && !isLoading && !hasError"
      class="diavgeia-section"
    >
      <h2 class="diavgeia-title">
        Πρόσφατες Αποφάσεις από το Πρόγραμμα «Διαύγεια» που Αφορούν τον
        Οργανισμό
      </h2>

      <div v-if="decisions.length">
        <div
          v-for="(decision, index) in decisions"
          :key="index"
          class="decision-card"
        >
          <div class="decision-header">
            <strong>ΑΔΑ:</strong> {{ decision.ada }}
          </div>
          <div class="decision-body">
            <p><strong>Θέμα:</strong> {{ decision.subject }}</p>
            <p><strong>Τύπος:</strong> {{ decision.documentType }}</p>
            <p>
              <strong>Ημερομηνία:</strong> {{ formatDate(decision.issueDate) }}
            </p>
            <div class="decision-links">
              <a :href="decision.documentUrl" target="_blank" title="Λήψη PDF" class="icon-link">
                <Download /> Λήψη
              </a>
              <a
                :href="getGoogleDocsViewerUrl(decision.documentUrl)"
                target="_blank"
                title="Η προβολή ενδέχεται να μην λειτουργεί σε όλους τους browsers"
                class="icon-link"
                style="margin-left: 1rem"
              >
                <Eye /> Προβολή
              </a>
            </div>
          </div>
        </div>
      </div>
      <p v-else style="font-style: italic; color: gray">
        Δεν βρέθηκαν αποφάσεις για τον οργανισμό.
      </p>
    </div>
  </div>
</template>

<script setup>
import { useRoute } from "vue-router";
import { ref, onMounted } from "vue";
import { fetchOrganizationById } from "../../services/organizations/OrganizationSearchService";
import { fetchDiavgeiaDecisionsByOrganizationName } from "../../services/diavgeia/DiavgeiaService";
import { format } from "date-fns";
import { el } from "date-fns/locale";
import { Download, Eye } from 'lucide-vue-next';


const route = useRoute();
const orgId = route.params.id;

const organization = ref(null);
const isLoading = ref(true);
const hasError = ref(false);
const decisions = ref([]);



function getGoogleDocsViewerUrl(documentUrl) {
  if (!documentUrl) return "#";
  return `https://docs.google.com/viewer?url=${encodeURIComponent(documentUrl)}&embedded=true`;
}

onMounted(async () => {
  try {
    const data = await fetchOrganizationById(orgId);
    organization.value = data;
    console.log("✅ Λήφθηκε οργανισμός:", data);

    if (data.name) {
      const response = await fetchDiavgeiaDecisionsByOrganizationName(data.name);
      decisions.value = response;
      console.log("✅ Λήφθηκαν αποφάσεις:", response)
    }
  } catch (error) {
    console.error("❌ Σφάλμα κατά τη φόρτωση οργανισμού:", error);
    hasError.value = true;
  } finally {
    isLoading.value = false;
  }
});

function formatDate(epochMillis) {
  if (!epochMillis) return "—";
  const date = new Date(epochMillis);
  return format(date, "dd/MM/yyyy", { locale: el });
}
</script>

<style scoped>
h1{
  background-color: #001b3b;
  color: white;
  text-align: center;
}

.org-details-view {
  max-width: auto;
  margin: 2rem auto;
  padding: 1rem;
}

.org-card {
  background-color: #fbfbfc;
  padding: 1.5rem;
  border-radius: 10px;
  box-shadow: 0 0 12px rgba(2, 32, 112, 0.08);
  margin-top: 1rem;
}

.org-name {
  font-size: 1.6rem;
  font-weight: bold;
  margin-bottom: 1rem;
  color: #0c3b75;
}

/* Πίνακας στοιχείων */
.org-details-table {
  width: 100%;
  border-collapse: collapse;
  border-color: #2265ba;
  margin-top: 1rem;
}

.org-details-table th {
  text-align: left;
  width: 230px;
  padding: 0.6rem 1rem 0.6rem 0;
  color: #333;
  font-weight: 600;
  vertical-align: top;
}

.org-details-table td {
  padding: 0.6rem 0;
  color: #222;
  font-size: 0.95rem;
  vertical-align: top;
}

.org-details-table a {
  color: #2265ba;
  text-decoration: none;
}
.org-details-table a:hover {
  text-decoration: underline;
}

/* Διαύγεια */
.diavgeia-section {
  margin-top: 3rem;
  max-width: 900px;
}

.diavgeia-title {
  font-size: 1.3rem;
  font-weight: 600;
  margin-bottom: 1rem;
  color: #444;
}

.diavgeia-section {
  margin-top: 3rem;
}

.diavgeia-title {
  font-size: 1.4rem;
  font-weight: 600;
  margin-bottom: 1.2rem;
  color: #444;
}

.decision-card {
  background-color: #021f47;
  color: rgb(248, 247, 247);
  padding: 1rem 1.2rem;
  border-radius: 10px;
  margin-bottom: 1rem;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.08);
}

.decision-header {
  font-size: 1.3rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
}

.decision-body p {
  margin: 0.3rem 0;
  font-size: 1rem;
}

.decision-links {
  margin-top: 0.6rem;
}

.decision-links a {
  background-color: white;
  color: #021f47;
  padding: 0.4rem 0.8rem;
  border-radius: 6px;
  text-decoration: none;
  font-size: 0.85rem;
  font-weight: 500;
  transition: background-color 0.2s ease;
}
.decision-links a:hover {
  background-color: #e0e0e0;
}

.icon-link {
  display: inline-flex;
  align-items: center;
  gap: 0.4rem;
  color: #2265ba;
  font-weight: 500;
  text-decoration: none;
}
.icon-link:hover {
  text-decoration: underline;
}
</style>
