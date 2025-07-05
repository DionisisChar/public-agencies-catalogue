import axios from "axios";

export async function createOrganization(payload) {
  console.log("📤 Δημιουργία οργανισμού με payload:", payload);
  return await axios.post("/api/admin/organizations", payload);
}

export async function deleteOrganization(id) {
  return await axios.delete(`/api/admin/organizations/${id}`);
}

// 🔄 Φέρνει τα δεδομένα ενός οργανισμού για edit
export async function fetchOrganizationEditData(id) {
 const response = await axios.get(`/api/admin/organizations/${id}/edit`);
  return response.data; // ✅ μόνο τα δεδομένα
}

export async function updatePublicOrganization(id, payload) {
  console.log(`✏️ Ενημέρωση PUBLIC οργανισμού με id=${id}`, payload);
  return await axios.put(`/api/admin/organizations/public/${id}`, payload);
}

export async function updateLocalOrganization(id, payload) {
  console.log(`✏️ Ενημέρωση LOCAL οργανισμού με id=${id}`, payload);
  return await axios.put(`/api/admin/organizations/local/${id}`, payload);
}