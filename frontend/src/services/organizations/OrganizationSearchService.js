import axios from "axios";

const BASE_URL = "/api/organizations";

/**
 * Κάνει POST αναζήτηση με δυναμικά κριτήρια.
 * @param {Object} criteria - Τα φίλτρα αναζήτησης (DTO: OrganizationSearchCriteriaDTO)
 * @returns {Promise} Axios response με Page<OrganizationSearchResponseDTO>
 */

export async function searchOrganizations(criteria) {
  try {
    const response = await axios.post(`${BASE_URL}/search`, criteria);
    console.log(response.data);
    return response.data;
  } catch (error) {
    console.error("❌ Σφάλμα κατά την αναζήτηση οργανισμών:", error);
    throw error;
  }
}

export async function fetchOrganizationById(id){
  try{
    const response = await axios.get(`${BASE_URL}/${id}`);
    console.log(response.data);
    return response.data;
  } catch (error){
    console.error("❌ Σφάλμα κατά τη λήψη οργανισμού:", error);
  }
}


