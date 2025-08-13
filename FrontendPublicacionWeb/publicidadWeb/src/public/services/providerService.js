import apiClient from "../../services/apiClient";

export const getAllProviders = async () => {
  const { data } = await apiClient.get("/api/providers/getAll");
  return data; // Array<ProviderResponseDTO>
};

export async function getProviderById(id) {
  if (!id) throw new Error("providerId requerido");

  try {
    const { data } = await apiClient.get(`/api/providers/${id}`, {
      withCredentials: true,
    });

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
      averageRating: data.averageRating ?? 0,
      totalRatings: data.totalRatings ?? 0,
    };
  } catch (e) {
    const msg = e?.response?.data?.message || "No se pudo obtener el prestador";
    throw new Error(msg);
  }
}

export async function searchProviders({ name, category }) {
  try {
    const params = {};
    if (name) params.name = name;
    if (category) params.category = category;

    // ✅ Endpoint público: no enviar cookies para evitar redirección OAuth
    const { data } = await apiClient.get("/api/providers/search", {
      params,
      withCredentials: false,
    });

    // Normalización para asegurar estructura consistente
    return (data || []).map(item => ({
      id: item.id,
      name: item.name ?? "",
      lastName: item.lastName ?? "",
      phone: item.phone ?? "",
      address: item.address ?? "",
      description: item.description ?? "",
      photoUrl: item.photoUrl ?? "",
      categoryName: item.categoryName ?? "",
      isActive: item.isActive ?? true,
      createdAt: item.createdAt,
      updatedAt: item.updatedAt,
      averageRating: item.averageRating ?? 0,
      totalRatings: item.totalRatings ?? 0,
    }));
  } catch (e) {
    if (e.response?.status === 204) {
      return [];
    }
    const msg = e?.response?.data?.message || "No se pudo buscar prestadores";
    throw new Error(msg);
  }
}
