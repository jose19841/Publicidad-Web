// src/users/services/adminService.js
import apiClient from "/src/services/apiclient.js";

/** Trae usuario logueado (usa cookie JWT) */
export async function getUserSession() {
  const { data } = await apiClient.get("/user/getUserSession");
  return data; // { id, username, email, ... }
}

/** PUT /user/{id} */
export async function updateAdminProfile(id, payload) {
  const { data } = await apiClient.put(`/user/${id}`, payload);
  return data;
}

/**
 * Actualiza SOLO el username.
 * 1. Pide el usuario actual al backend (usa cookie JWT).
 * 2. Preserva el email y envía el nuevo username.
 */
export async function updateAdminUsernameMe(username) {
  const me = await getUserSession();
  if (!me?.id || !me?.email) {
    throw new Error("No se pudo obtener la sesión del usuario.");
  }
  const payload = { username, email: me.email };
  return updateAdminProfile(me.id, payload);
}

/** POST /user/change-password */
export async function changeAdminPassword({ currentPassword, newPassword, repeatPassword }) {
  const payload = { currentPassword, newPassword, repeatPassword };
  const { data } = await apiClient.post("/user/change-password", payload);
  return data;
}

export default {
  getUserSession,
  updateAdminProfile,
  updateAdminUsernameMe,
  changeAdminPassword,
};
