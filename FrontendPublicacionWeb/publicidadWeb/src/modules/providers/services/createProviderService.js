// src/modules/providers/services/createProviderService.js
import axios from "axios";

export default async function createProviderService(formData) {
  const { data } = await axios.post("http://localhost:8080/api/providers", formData, { withCredentials: true} );
  return data;
}

