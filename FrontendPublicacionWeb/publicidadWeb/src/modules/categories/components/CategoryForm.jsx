import React from "react";
import useCategoryForm from "../hooks/useCategoryForm";

const CategoryForm = ({ onSuccess, editingCategory }) => {
  const {
    form,
    errors,
    handleChange,
    handleSubmit,
  } = useCategoryForm({ onSuccess, editingCategory });

  return (
    <form onSubmit={handleSubmit} className="card card-body shadow-sm mb-4">
      <h5 className="mb-3">
        {editingCategory ? "Editar Categoría" : "Registrar Nueva Categoría"}
      </h5>
      <div className="mb-3">
        <label className="form-label">
          Nombre <span className="text-danger">*</span>
        </label>
        <input
          type="text"
          className={`form-control ${errors?.name ? "is-invalid" : ""}`}
          name="name"
          value={form?.name || ""}
          onChange={handleChange}
          placeholder="Nombre de la categoría"
          maxLength={100}
          autoComplete="off"
        />
        {errors?.name && (
          <div className="invalid-feedback">{errors.name}</div>
        )}
      </div>

      <div className="mb-3">
        <label className="form-label">Descripción</label>
        <input
          type="text"
          className="form-control"
          name="description"
          value={form?.description || ""}
          onChange={handleChange}
          placeholder="Descripción de la categoría"
          maxLength={255}
          autoComplete="off"
        />
      </div>

      <button type="submit" className="btn btn-primary">
        {editingCategory ? "Guardar cambios" : "Registrar"}
      </button>
    </form>
  );
};

export default CategoryForm;
