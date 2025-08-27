import apiClient from "../../../services/apiClient";

/**
 * Llama al endpoint de recuperación de contraseña.
 * @param {{ email: string }} payload
 * @returns {Promise<any>} data devuelta por el backend
 * @throws {Error} con mensaje normalizado en español
 */
export default async function forgotPasswordService({ email }) {
  if (!email) throw new Error("El correo es obligatorio.");
  try {
    const { data } = await apiClient.post("/auth/forgot-password", { email });
    return data;
  } catch (e) {
    const msg =
      e?.response?.data?.message ||
      e?.response?.data?.error ||
      "No se pudo procesar la solicitud. Intentá nuevamente.";
    throw new Error(msg);
  }
}
