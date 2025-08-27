import apiClient from "../../services/apiClient";

export async function createRating({ providerId, score }) {
  // URL con path param + body solo {score}
  const { data } = await apiClient.post(
    `/providers/${providerId}/ratings`,
    { score: Number(score) },
    { withCredentials: true } // si usás cookie JWT
  );
  return data;
}