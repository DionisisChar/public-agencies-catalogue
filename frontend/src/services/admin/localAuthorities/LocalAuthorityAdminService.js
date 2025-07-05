import axios from 'axios'

// 🔍 Αναζήτηση ΟΤΑ με βάση όνομα
export const searchLocalAuthoritiesOld = (query) => {
  return axios.get(`/api/admin/local-authorities/search`, {
    params: { query }
  })
}

// Αναζήτηση ΟΤΑ Paged
export const searchLocalAuthorities = (query, page, size) => {
  return axios.get('/api/admin/local-authorities/search', {
    params: {
      query,
      page,
      size
    }
  }).then(res => res.data)
}

// 🔄 Ενημέρωση ΟΤΑ
export const updateLocalAuthority = (id, updateDto) => {
  return axios.put(`/api/admin/local-authorities/${id}`, updateDto)
}

// ❌ Διαγραφή ΟΤΑ
export async function deleteLocalAuthorityById(id) {
  return axios.delete(`/api/admin/local-authorities/${id}`)
}