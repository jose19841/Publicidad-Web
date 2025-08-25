import { useState, useEffect } from "react";
import Swal from "sweetalert2";
import useUpdateProvider from "../hooks/useUpdateProvider";

export default function useProviderModalLogic({ provider, onClose, onSuccess }) {
  const [formValues, setFormValues] = useState({
    name: "",
    lastName: "",
    address: "",
    phone: "",
    description: "",
    isActive: true,
    categoryId: null,
  });

  const [imageAction, setImageAction] = useState("keep");
  const [imageValue, setImageValue] = useState(null);
  const [saving, setSaving] = useState(false);

  const { update } = useUpdateProvider();

  useEffect(() => {
    if (provider) {
      setFormValues({
        name: provider.name || "",
        lastName: provider.lastName || "",
        address: provider.address || "",
        phone: provider.phone || "",
        description: provider.description || "",
        isActive: provider.isActive ?? true,
        categoryId: provider.categoryId || null,
      });
      setImageAction(provider.photoUrl ? "keep" : "replace");
      setImageValue(provider.photoUrl || null);
    }
  }, [provider]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormValues((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleSave = async () => {
    try {
      setSaving(true);

      const providerData = {
        name: formValues.name,
        lastName: formValues.lastName,
        address: formValues.address,
        phone: formValues.phone,
        description: formValues.description,
        isActive: formValues.isActive,
        categoryId: formValues.categoryId,
      };

      const formData = new FormData();
      formData.append(
        "provider",
        new Blob([JSON.stringify(providerData)], { type: "application/json" })
      );

      if (imageAction === "replace" && typeof imageValue === "object") {
        formData.append("image", imageValue);
        formData.append("imageAction", "replace");
      } else if (imageAction === "remove") {
        formData.append("imageAction", "remove");
      } else {
        formData.append("imageAction", "keep");
      }

      await update(provider.id, formData, true);
      Swal.fire("Éxito", "Proveedor actualizado correctamente", "success");

      onSuccess ? onSuccess() : onClose();
    } catch (err) {
      Swal.fire(
        "Error",
        err?.response?.data?.message || "No se pudo actualizar el proveedor",
        "error"
      );
    } finally {
      setSaving(false);
    }
  };

  return {
    formValues,
    setFormValues,
    imageAction,
    setImageAction,
    imageValue,
    setImageValue,
    saving,
    handleChange,
    handleSave
  };
}
