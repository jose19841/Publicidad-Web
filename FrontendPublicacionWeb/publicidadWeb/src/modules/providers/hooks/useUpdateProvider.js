import { useState } from "react";
import editProviderService from "../services/editProviderService";

export default function useUpdateProvider() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const update = async (id, data, isMultipart = false) => {
    try {
      setLoading(true);
      setError(null);
      return await editProviderService(id, data, isMultipart);
    } catch (err) {
      setError(err);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return { update, loading, error };
}
