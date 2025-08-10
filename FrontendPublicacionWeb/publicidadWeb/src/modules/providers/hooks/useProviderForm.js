import { useState, useEffect, useCallback } from "react";
import Swal from "sweetalert2";
import getCategoriesService from "../../categories/services/getCategoriesService";
import createProviderService from "../services/createProviderService";
import editProviderService from "../services/editProviderService";

export default function useProviderForm({ onSuccess, editingProvider = null }) {
  const [form, setForm] = useState({
    name: "",
    lastName: "",
    address: "",
    phone: "",
    description: "",
    photoUrl: "",
    isActive: true,
    categoryId: ""
  });

  const [errors, setErrors] = useState({});
  const [categories, setCategories] = useState([]);
  const [loadingCats, setLoadingCats] = useState(true);
  const isEdit = Boolean(editingProvider?.id);

  // Cargar categorías
  const loadCategories = useCallback(async () => {
    try {
      const res = await getCategoriesService({ page: 1, pageSize: 1000 });
      const list = Array.isArray(res) ? res : res?.data || [];
      setCategories(list);
    } catch (e) {
      Swal.fire("Error", "No se pudieron cargar las categorías.", "error");
    } finally {
      setLoadingCats(false);
    }
  }, []);

  useEffect(() => {
    loadCategories();
  }, [loadCategories]);

  // Precargar datos si es edición
  useEffect(() => {
    if (!isEdit) return;
    setForm({
      name: editingProvider?.name ?? "",
      lastName: editingProvider?.lastName ?? "",
      address: editingProvider?.address ?? "",
      phone: editingProvider?.phone ?? "",
      description: editingProvider?.description ?? "",
      photoUrl: editingProvider?.photoUrl ?? "",
      isActive: Boolean(editingProvider?.isActive),
      categoryId: editingProvider?.categoryId ?? ""
    });
  }, [isEdit, editingProvider]);

  // Resolver categoryId desde categoryName si es edición
  useEffect(() => {
    if (!isEdit) return;
    if (form.categoryId) return;
    const nameFromBackend =
      editingProvider?.categoryName ?? editingProvider?.CategoryName;
    if (!nameFromBackend || categories.length === 0) return;

    const match = categories.find(
      (c) => String(c.name).toLowerCase() === String(nameFromBackend).toLowerCase()
    );
    if (match?.id) {
      setForm((prev) => ({ ...prev, categoryId: Number(match.id) }));
    }
  }, [isEdit, form.categoryId, editingProvider, categories]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    const newValue =
      name === "categoryId"
        ? value
          ? Number(value)
          : ""
        : type === "checkbox"
        ? checked
        : value;

    setForm((prev) => ({ ...prev, [name]: newValue }));
  };

  const validate = () => {
    const errs = {};
    if (!form.name.trim()) errs.name = "El nombre es obligatorio";
    if (!form.lastName.trim()) errs.lastName = "El apellido es obligatorio";
    if (!form.address.trim()) errs.address = "La dirección es obligatoria";
    if (!form.phone.trim()) errs.phone = "El teléfono es obligatorio";
    if (!form.categoryId && !isEdit) errs.categoryId = "Seleccioná una categoría";
    return errs;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const errs = validate();
    setErrors(errs);
    if (Object.keys(errs).length) return;

    try {
      if (isEdit) {
        await editProviderService(editingProvider.id, form);
        Swal.fire("¡Actualizado!", "Proveedor actualizado correctamente.", "success");
        onSuccess && onSuccess(); // Edición: refrescar
      } else {
        const nuevoProv = await createProviderService(form);
        Swal.fire("¡Creado!", "Proveedor registrado correctamente.", "success");

        setForm({
          name: "",
          lastName: "",
          address: "",
          phone: "",
          description: "",
          photoUrl: "",
          isActive: true,
          categoryId: ""
        });
        setErrors({});
        onSuccess && onSuccess(nuevoProv); // Registro: agregar a tabla
      }
    } catch (err) {
      Swal.fire(
        "Error",
        err?.response?.data?.message || "No se pudo guardar el prestador.",
        "error"
      );
    }
  };

  return {
    form,
    errors,
    categories,
    loadingCats,
    handleChange,
    handleSubmit,
    setForm,
    setErrors
  };
}
