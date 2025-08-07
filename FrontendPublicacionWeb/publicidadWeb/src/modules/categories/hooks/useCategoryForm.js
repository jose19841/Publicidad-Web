import { useState, useEffect } from "react";
import createCategoryService from "../services/createCategoryService";
import editCategoryService from "../services/editCategoryService";
import Swal from "sweetalert2";

export default function useCategoryForm({ onSuccess, editingCategory }) {
  const [form, setForm] = useState({
    name: editingCategory?.name || "",
    description: editingCategory?.description || "",
  });
  const [errors, setErrors] = useState({});

  // Si cambia la categoría a editar, actualiza el form
  useEffect(() => {
    setForm({
      name: editingCategory?.name || "",
      description: editingCategory?.description || "",
    });
  }, [editingCategory]);

  // Actualiza campos
  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  // Validación simple
  const validate = () => {
    const errs = {};
    if (!form.name.trim()) errs.name = "El nombre es obligatorio";
    return errs;
  };

  // Submit alta o edición
  const handleSubmit = async (e) => {
    e.preventDefault();
    const errs = validate();
    setErrors(errs);
    if (Object.keys(errs).length) return;

    try {
      if (editingCategory) {
        await editCategoryService(editingCategory.id, form);
        Swal.fire("¡Actualizado!", "Categoría editada.", "success");
      } else {
        await createCategoryService(form);
        Swal.fire("¡Creada!", "Categoría registrada.", "success");
      }
      setForm({ name: "", description: "" });
      setErrors({});
      if (onSuccess) onSuccess();
    } catch (err) {
      Swal.fire("Error", err.message || "No se pudo guardar.", "error");
    }
  };

  return {
    form,
    errors,
    handleChange,
    handleSubmit,
    setForm,
    setErrors,
  };
}
