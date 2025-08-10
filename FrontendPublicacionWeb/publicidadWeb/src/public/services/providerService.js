import apiClient from "../../services/apiClient";

export const getAllProviders = async () => {
  const { data } = await apiClient.get("/api/providers/getAll");
  return data; // [{id,name,lastName,address,phone,description,photoUrl,isActive,categoryName,...}]
};
