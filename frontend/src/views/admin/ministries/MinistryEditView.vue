<template>
  <div class="ministry-edit-view">
    <!-- Κουμπί για Προσθήκη Νέου -->
    <div class="header-actions">
      <button @click="$router.push('/admin/ministries/create')">
        ➕ Προσθήκη Νέου
      </button>
    </div>
    <!-- Λίστα Υπουργείων σε μορφή καρτών -->
    <div class="card-list">
      <div
        v-for="ministry in ministries"
        :key="ministry.id"
        class="ministry-card"
        @click="selectMinistry(ministry)"
      >
        🏛️ {{ ministry.label }}
      </div>
    </div>

    <!-- 🔽 Modal που εμφανίζεται όταν επιλεγεί κάποιο υπουργείο -->
    <div v-if="isModalOpen" class="modal-overlay" @click.self="closeModal">
      <div class="modal">
        <!-- Εμφάνιση φόρμας όταν είμαστε σε edit mode -->
        <div v-if="editMode">
          <h3>✏️ Επεξεργασία Υπουργείου</h3>
          <div class="form-group">
            <label for="ministryName">Όνομα Υπουργείου</label>
            <input
              id="ministryName"
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

        <!-- Κανονικό μενού όταν ΔΕΝ είμαστε σε edit mode -->
        <div v-else>
          <h3>🏛️ {{ selectedMinistry.label }}</h3>
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
import { ref, onMounted } from "vue";
import { fetchMinistries } from "../../../services/dropdown/DropdownService.js";
import { updateMinistry } from "../../../services/admin/ministries/MinistryAdminService.js";
import { deleteMinistry } from "../../../services/admin/ministries/MinistryAdminService.js";

const ministries = ref([]);
const selectedMinistry = ref(null);
const isModalOpen = ref(false);
const editMode = ref(false);
const editedName = ref("");

// ⬇️ Φόρτωση υπουργείων
onMounted(async () => {
  await loadMinistries();
});

const loadMinistries = async () => {
  try {
    const response = await fetchMinistries();
    ministries.value = response.data;
  } catch (error) {
    console.error("❌ Σφάλμα φόρτωσης υπουργείων:", error);
  }
};

// ⬇️ Όταν πατηθεί μία κάρτα
const selectMinistry = (ministry) => {
  selectedMinistry.value = ministry;
  editedName.value = ministry.label; // Προσυμπληρωμένο όνομα
  editMode.value = false;
  isModalOpen.value = true;
};

// ⬇️ Κλείσιμο modal
const closeModal = () => {
  isModalOpen.value = false;
  selectedMinistry.value = null;
  editedName.value = "";
  editMode.value = false;
};

// ⬇️ Πάτημα για edit
const handleEdit = () => {
  editMode.value = true;
};

// ⬇️ Πάτημα "Άκυρο"
const cancelEdit = () => {
  editMode.value = false;
  editedName.value = selectedMinistry.value.label;
};

// ⬇️ Πάτημα για delete

async function handleDelete() {
  const ministryName = selectedMinistry.value.label;
  const confirmed = confirm(
    `❗ Πρόκειται να διαγράψετε το Υπουργείο "${ministryName}". Είστε βέβαιοι ότι θέλετε να προχωρήσετε;`
  );
  if (!confirmed) {
    return;
  }
  try {
    await deleteMinistry(selectedMinistry.value.id);
    console.log("✅ Επιτυχής διαγραφή");
    // Ανανέωση λίστας
    await loadMinistries();
    // Κλείσιμο modal
    closeModal();
  } catch (error) {
    console.error("❌ Σφάλμα κατά τη διαγραφή του υπουργείου:", error);
    alert(
      "⚠️ Παρουσιάστηκε σφάλμα κατά τη διαγραφή. Παρακαλώ προσπαθήστε ξανά."
    );
  }
}

// ⬇️ Αποθήκευση αλλαγών
const submitEdit = async () => {
  const trimmedName = editedName.value.trim();

  // ➤ Frontend validation
  if (trimmedName.length < 3) {
    alert(
      "⚠️ Το όνομα πρέπει να έχει τουλάχιστον 3 χαρακτήρες και να μην είναι μόνο κενά."
    );
    return;
  }
  const cleanedName = trimmedName.toUpperCase();
  try {
    const updatedDTO = {
      name: cleanedName,
    };

    await updateMinistry(selectedMinistry.value.id, updatedDTO);

    console.log("✅ Επιτυχής ενημέρωση!");

    // Ανανέωση λίστας υπουργείων
    await loadMinistries();

    // Κλείσιμο modal
    closeModal();
  } catch (error) {
    console.error("❌ Σφάλμα κατά την ενημέρωση υπουργείου:", error);
    alert("❌ Σφάλμα κατά την αποθήκευση. Το όνομα μπορεί να υπάρχει ήδη.");
  }
};
</script>

<style scoped>
.ministry-edit-view {
  padding: 20px;
}

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

.card-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 16px;
}

.ministry-card {
  background-color: #001b3b;
  color: white;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.ministry-card:hover {
  background-color: #004c90;
}

.ministry-edit-view {
  padding: 20px;
}

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

.card-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 16px;
}

.ministry-card {
  background-color: #001b3b;
  color: white;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.ministry-card:hover {
  background-color: #004c90;
}

/* 🔽 Modal styling */
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
  margin-top: 20px; /* απόσταση από τον τίτλο */
  text-align: left;
}

.form-group label {
  display: block;
  margin-bottom: 6px;
  font-weight: bold;
}

.form-group input {
  width: 100%;
  padding: 10px;
  border-radius: 6px;
  border: 1px solid #ccc;
  font-size: 16px;
}
</style>
