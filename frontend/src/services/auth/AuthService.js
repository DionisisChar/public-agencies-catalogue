import axios from "axios";

export async function loginUser(credentials) {
  const response = await axios.post("/api/auth/login", credentials, {
    withCredentials: true,
  });
  return response.data; // περιέχει email, fullName, role
}

export async function logoutUser(){
  const response = await axios.post("/api/auth/logout");

}
