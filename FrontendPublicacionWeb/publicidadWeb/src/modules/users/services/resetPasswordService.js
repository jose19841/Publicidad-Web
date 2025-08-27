import apiClient from "../../../services/apiClient";

/**
 * Llama al endpoint de restablecimiento de contraseña.
 * @param {{ token: string, newPassword: string }} payload
 */
export default async function resetPasswordService({ token, newPassword }) {
  if (!token) throw new Error("Token inválido o ausente.");
  if (!newPassword) throw new Error("La nueva contraseña es obligatoria.");

  try {
    const { data } = await apiClient.post("/auth/reset-password", {
      token,
      newPassword,
    });
    return data;
  } catch (e) {
    const msg =
      e?.response?.data?.message ||
      e?.response?.data?.error ||
      "No se pudo restablecer la contraseña. Intentá nuevamente.";
    throw new Error(msg);
  }
}
