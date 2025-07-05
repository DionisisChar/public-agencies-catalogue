import axios from "axios";

// Επιστρέφει όλα τα υπουργεία (OptionDTO[])
export const fetchMinistries = () => {
  return axios.get("/api/dropdowns/ministries");
};

// Επιστρέφει όλες τις νομικές μορφές
export const fetchLegalForms = () => axios.get("/api/dropdowns/legal-forms");

// Επιστρέφει ΕΝΤΟΣ/ΕΚΤΟΣ
export const fetchPublicSectorStatus = () =>
  axios.get("/api/dropdowns/public-sector-status");

// Search ΟΤΑ
export const searchLocalAuthorities = (query) =>
  axios.get("/api/dropdowns/local-authorities/search", { params: { query } });
