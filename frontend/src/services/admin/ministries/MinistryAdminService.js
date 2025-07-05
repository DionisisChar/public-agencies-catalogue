import axios from 'axios'

// 🔄 Ενημέρωση (Edit) ενός Υπουργείου
export const updateMinistry = (id, updateDto) => {
  return axios.put(`/api/admin/ministries/${id}`, updateDto)
}

export const deleteMinistry = (id) => {
    return axios.delete(`/api/admin/ministries/${id}`)
}
