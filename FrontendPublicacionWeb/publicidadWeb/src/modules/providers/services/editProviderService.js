import axios from "axios";

export default async function editProviderService(id, data, isMultipart = false) {
  if (!id) {
    throw new Error("El ID del proveedor es obligatorio para actualizarlo.");
  }

  const config = { withCredentials: true };

  if (isMultipart) {
    config.headers = { "Content-Type": "multipart/form-data" };
  }

  const response = await axios.put(
    `http://localhost:8080/api/providers/update/${id}`,
    data,
    config
  );

  return response.data;
}
