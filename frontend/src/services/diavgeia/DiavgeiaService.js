import axios from "axios";

const BASE_URL = "/api/diavgeia";

export async function fetchDiavgeiaDecisionsByOrganizationName(orgName) {
  try {
    const response = await axios.get(`${BASE_URL}/decisions`, {
      params: { name: orgName },
    });
    return response.data;
  } catch (error) {
    console.error("❌ Σφάλμα στη λήψη αποφάσεων Διαύγειας:", error);
    return [];
  }
}
