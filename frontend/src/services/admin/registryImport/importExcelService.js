import axios from 'axios';

const API_URL = '/api/admin/import/excel';

/**
 * Υποβάλλει το αρχείο Excel στο backend.
 * @param {File} file - Το αρχείο Excel που επιλέχθηκε από τον χρήστη.
 * @returns {Promise<string>} - Το μήνυμα επιβεβαίωσης από το backend.
 */
export async function submitRegistryExcelFile(file) {
  const formData = new FormData();
  formData.append('file', file);

  try {
    const response = await axios.post(API_URL, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
    return response.data;
  } catch (error) {
    console.error('❌ Σφάλμα κατά την αποστολή Excel:', error);
    throw error.response?.data || 'Απέτυχε η αποστολή του αρχείου';
  }
}