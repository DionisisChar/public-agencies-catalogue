import axios from 'axios'

const BASE_URL = '/api/organizations';

/**
 * Κάνει POST αναζήτηση χωρίς pagination για χρήση σε χάρτη.
 * @param {Object} criteria - Τα φίλτρα αναζήτησης (DTO: OrganizationMapSearchRequestDTO)
 * @returns {Promise} Axios response με List<OrganizationSearchResponseDTO>
 */

export async function fetchOrganizationsForMap(criteria) {
  try {
    const response = await axios.post(`${BASE_URL}/search/map`, criteria);
    console.log(response.data);
    return response.data;
  } catch (error) {
    console.error("❌ Σφάλμα κατά την αναζήτηση οργανισμών για τον χάρτη:", error);
    throw error;
  }
}