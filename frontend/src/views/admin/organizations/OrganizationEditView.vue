<template>
  <div class="admin-page-container">
    <div class="page-header">
      <h2>Διαχείριση Οργανισμών/Υπηρεσιών</h2>
      <div class="header-actions">
        <button @click="$router.push('/admin/organizations/create')">
          ➕ Προσθήκη Νέου
        </button>
      </div>
    </div>
    <!-- Εδώ αργότερα θα μπει το περιεχόμενο (πίνακας, φίλτρα κλπ) -->
    <OrganizationSearchComponent :isAdmin="true" />
  </div>
</template>

<script setup>
// Δεν βάζουμε λογική προς το παρόν
import OrganizationSearchComponent from '../../../components/organizations/OrganizationSearchComponent.vue'
</script>

<style scoped>
.admin-page-container {
  padding: 2rem;
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 2rem;
}

.header-actions button {
  background: #2265ba;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 0.6rem 1.3rem;
  font-size: 1rem;
  cursor: pointer;
  transition: background 0.15s;
}
.header-actions button:hover {
  background: #174a8c;
}
.coming-soon {
  color: #888;
  margin-top: 3rem;
  font-size: 1.15rem;
}
</style>