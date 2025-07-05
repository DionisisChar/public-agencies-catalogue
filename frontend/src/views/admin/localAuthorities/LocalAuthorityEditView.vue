<template>
  <div class="local-authority-edit-container">
    <!-- Κουμπί για Προσθήκη Νέου -->
    <div class="header-actions">
      <button @click="$router.push('/admin/local-authorities/create')">
        ➕ Προσθήκη Νέου
      </button>
    </div>
    <h2>Διαχείριση ΟΤΑ</h2>

    <!-- 🔍 Search bar -->
    <div
      class="search-bar"
      style="margin-bottom: 1rem; display: flex; gap: 0.5rem"
    >
      <InputText v-model="searchQuery" placeholder="Αναζήτηση ΟΤΑ..." />
    </div>

    <!-- 📋 Πίνακας ΟΤΑ -->
    <DataTable
      :value="authorities"
      :paginator="true"
      :rows="rows"
      :totalRecords="totalRecords"
      :loading="loading"
      :first="page * rows"
      lazy
      @page="onPageChange"
      @rowClick="onRowClick"
      responsiveLayout="scroll"
    >
      <Column field="label" header="Ονομασία ΟΤΑ" />
    </DataTable>

    <!-- 🔽 Modal όταν επιλεγεί ΟΤΑ -->
    <div v-if="isModalOpen" class="modal-overlay" @click.self="closeModal">
      <div class="modal">
        <!-- Φόρμα επεξεργασίας -->
        <div v-if="editMode">
          <h3>✏️ Επεξεργασία ΟΤΑ</h3>
          <div class="form-group">
            <label for="otaName">Όνομα ΟΤΑ</label>
            <input
              id="otaName"
              v-model="editedName"
              type="text"
              placeholder="Νέο όνομα"
            />
          </div>
          <div class="modal-actions">
            <button @click="submitEdit">💾 Αποθήκευση</button>
            <button @click="cancelEdit">❌ Άκυρο</button>
          </div>
        </div>

        <!-- Προβολή ενέργειας -->
        <div v-else>
          <h3>🏛️ {{ selectedAuthority.label }}</h3>
          <div class="modal-actions">
            <button @click="handleEdit">✏️ Επεξεργασία</button>
            <button @click="handleDelete">🗑️ Διαγραφή</button>
          </div>
          <button class="modal-close" @click="closeModal">✖ Κλείσιμο</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from "vue";
import { h } from "vue";
import debounce from "lodash.debounce";
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import InputText from "primevue/inputtext";
import {
  searchLocalAuthorities,
  deleteLocalAuthorityById,
  updateLocalAuthority,
} from "../../../services/admin/localAuthorities/LocalAuthorityAdminService";

// 📊 Reactive state
const authorities = ref([]);
const totalRecords = ref(0);
const searchQuery = ref("");
const loading = ref(false);
const rows = ref(10);
const page = ref(0);
const selectedAuthority = ref(null);
const isModalOpen = ref(false);
const editMode = ref(false);
const editedName = ref("");

// 📡 Φόρτωση ΟΤΑ από backend
const fetchAuthorities = async () => {
  loading.value = true;
  try {
    const response = await searchLocalAuthorities(
      searchQuery.value,
      page.value,
      rows.value
    );
    authorities.value = response.content;
    totalRecords.value = response.totalElements;
  } catch (error) {
    console.error("⚠️ Σφάλμα στη φόρτωση ΟΤΑ:", error);
  } finally {
    loading.value = false;
  }
};

// 🌀 Αλλαγή σελίδας
const onPageChange = (event) => {
  page.value = event.page;
  fetchAuthorities();
};

// Επιλογή Φορέα (Ενέργειες)
const onRowClick = (event) => {
  selectedAuthority.value = event.data;
  editedName.value = event.data.label;
  editMode.value = false;
  isModalOpen.value = true;
};

// 🔄 Live search με debounce
watch(
  searchQuery,
  debounce(() => {
    page.value = 0; // reset page
    fetchAuthorities();
  }, 500)
);

// 🚀 Φόρτωση κατά το mount
onMounted(fetchAuthorities);

const closeModal = () => {
  isModalOpen.value = false;
  selectedAuthority.value = null;
  editedName.value = "";
  editMode.value = false;
};

const handleEdit = () => {
  editMode.value = true;
};

const cancelEdit = () => {
  editMode.value = false;
  editedName.value = selectedAuthority.value.label;
};

// ❌ Διαγραφή ΟΤΑ
const handleDelete = async () => {
  const confirmed = confirm(
    `❗ Πρόκειται να διαγράψετε τον ΟΤΑ "${selectedAuthority.value.label}". Είστε βέβαιοι;`
  );
  if (!confirmed) return;

  try {
    const deletedName = selectedAuthority.value.label; // αποθήκευσε το label πριν διαγραφεί
    await deleteLocalAuthorityById(selectedAuthority.value.id);
    await fetchAuthorities();
    closeModal();
    alert(`✅ Ο ΟΤΑ "${deletedName}" διαγράφηκε με επιτυχία.`);
  } catch (error) {
    alert("❌ Σφάλμα κατά τη διαγραφή");
  }
};

// ✏️ Επεξεργασία ΟΤΑ
const submitEdit = async () => {
  const trimmed = editedName.value.trim();
  if (trimmed.length < 3) {
    alert("⚠️ Το όνομα πρέπει να έχει τουλάχιστον 3 χαρακτήρες.");
    return;
  }

  const updatedDTO = { name: trimmed.toUpperCase() };
  console.log("🛠 Updated DTO", updatedDTO);
  try {
    await updateLocalAuthority(selectedAuthority.value.id, updatedDTO); // ➕ Να το γράψω μετά στο service
    await fetchAuthorities();
    closeModal();
    alert("✅ Η ενημέρωση πραγματοποιήθηκε με επιτυχία.");
    console.log("🛠 Updated DTO", updatedDTO);
  } catch (error) {
    console.error("❌ Σφάλμα κατά την αποθήκευση:", error);
    alert("❌ Σφάλμα κατά την αποθήκευση");
  }
};
</script>

<style scoped>
.header-actions {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}

.header-actions button {
  background-color: #207ad4;
  color: white;
  padding: 8px 14px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
.local-authority-edit-container {
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
}

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

.modal label {
  display: block;
  font-weight: bold;
  margin-bottom: 6px;
  text-align: left;
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

.form-group {
  margin-top: 20px;
  text-align: left;
}

.form-group input {
  width: 100%;
  padding: 10px;
  border-radius: 6px;
  border: 1px solid #ccc;
  font-size: 16px;
}
</style>
