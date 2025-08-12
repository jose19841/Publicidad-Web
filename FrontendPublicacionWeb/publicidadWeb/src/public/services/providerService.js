import apiClient from "../../services/apiClient";

export const getAllProviders = async () => {
  const { data } = await apiClient.get("/api/providers/getAll");
  return data; // [{id,name,lastName,address,phone,description,photoUrl,isActive,categoryName,...}]
};


export async function getProviderById(id) {
  if (!id) throw new Error("providerId requerido");

  try {
    const { data } = await apiClient.get(`/api/providers/${id}`, {
      // Si tu backend usa cookie/JWT vía cookie, mantené esto
      withCredentials: true,
    });

    // Normalización opcional por si algún campo viene null/undefined
    return {
      id: data.id,
      name: data.name ?? "",
      lastName: data.lastName ?? "",
      phone: data.phone ?? "",
      address: data.address ?? "",
      description: data.description ?? "",
      photoUrl: data.photoUrl ?? "",
      categoryName: data.categoryName ?? "",
      isActive: data.isActive ?? true,
      createdAt: data.createdAt,
      updatedAt: data.updatedAt,

      // Campos críticos para el modal de reseñas
      averageRating: data.averageRating ?? 0,
      totalRatings: data.totalRatings ?? 0,
    };
  } catch (e) {
    const msg = e?.response?.data?.message || "No se pudo obtener el prestador";
    throw new Error(msg);
  }
}