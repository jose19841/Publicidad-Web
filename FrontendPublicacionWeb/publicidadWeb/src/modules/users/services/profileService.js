// src/users/services/profileService.js
import apiClient from "/src/services/apiclient.js";

/**
 * Usuario común: actualiza SOLO su username.
 * El backend ignora cualquier email del body y usa el de la sesión.
 */
export async function updateLoggedUserUsername(username) {
  const { data } = await apiClient.put("/user/updateLoggedUser", { username });
  return data; // { message: "Usuario actualizado correctamente" }
}
export async function changeMyPassword({ currentPassword, newPassword, repeatPassword }) {
  const { data } = await apiClient.post("/user/change-password", {
    currentPassword,
    newPassword,
    repeatPassword,
  });
  return data; // "Contraseña actualizada correctamente"
}