import React from "react";
import useProviderForm from "../hooks/useProviderForm";

const ProviderForm = ({ onSuccess, editingProvider }) => {
  const {
    form,
    errors,
    categories,
    loadingCats,
    handleChange,
    handleSubmit,
  } = useProviderForm({ onSuccess, editingProvider });

  return (
    <form onSubmit={handleSubmit} className="card card-body shadow-sm mb-4">
      
      {/* Nombre */}
      <div className="mb-3">
        <label>Nombre</label>
        <input
          type="text"
          name="name"
          value={form.name || ""}
          onChange={handleChange}
          className={`form-control ${errors.name ? "is-invalid" : ""}`}
        />
        {errors.name && <div className="invalid-feedback">{errors.name}</div>}
      </div>

      {/* Apellido */}
      <div className="mb-3">
        <label>Apellido</label>
        <input
          type="text"
          name="lastName"
          value={form.lastName || ""}
          onChange={handleChange}
          className={`form-control ${errors.lastName ? "is-invalid" : ""}`}
        />
        {errors.lastName && (
          <div className="invalid-feedback">{errors.lastName}</div>
        )}
      </div>

      {/* Dirección */}
      <div className="mb-3">
        <label>Dirección</label>
        <input
          type="text"
          name="address"
          value={form.address || ""}
          onChange={handleChange}
          className={`form-control ${errors.address ? "is-invalid" : ""}`}
        />
        {errors.address && (
          <div className="invalid-feedback">{errors.address}</div>
        )}
      </div>

      {/* Teléfono */}
      <div className="mb-3">
        <label>Teléfono</label>
        <input
          type="text"
          name="phone"
          value={form.phone || ""}
          onChange={handleChange}
          className={`form-control ${errors.phone ? "is-invalid" : ""}`}
        />
        {errors.phone && (
          <div className="invalid-feedback">{errors.phone}</div>
        )}
      </div>

      {/* Categoría */}
      <div className="mb-3">
        <label>Categoría</label>
        {loadingCats ? (
          <p>Cargando categorías...</p>
        ) : (
          <select
            name="categoryId"
            value={form.categoryId || ""}
            onChange={handleChange}
            className={`form-control ${errors.categoryId ? "is-invalid" : ""}`}
          >
            <option value="">Seleccione una categoría</option>
            {categories.map((cat) => (
              <option key={cat.id} value={cat.id}>
                {cat.name}
              </option>
            ))}
          </select>
        )}
        {errors.categoryId && (
          <div className="invalid-feedback">{errors.categoryId}</div>
        )}
      </div>

      {/* Descripción */}
      <div className="mb-3">
        <label>Descripción</label>
        <textarea
          name="description"
          value={form.description || ""}
          onChange={handleChange}
          className="form-control"
        />
      </div>

      {/* Foto */}
      <div className="mb-3">
        <label>URL de la Foto</label>
        <input
          type="text"
          name="photoUrl"
          value={form.photoUrl || ""}
          onChange={handleChange}
          className="form-control"
        />
      </div>

      {/* Activo */}
      <div className="form-check mb-3">
        <input
          type="checkbox"
          name="isActive"
          checked={form.isActive || false}
          onChange={handleChange}
          className="form-check-input"
          id="isActive"
        />
        <label htmlFor="isActive" className="form-check-label">
          Activo
        </label>
      </div>

      {/* Botón */}
      <button type="submit" className="btn btn-primary">
        {editingProvider ? "Actualizar" : "Crear"} Proveedor
      </button>
    </form>
  );
};

export default ProviderForm;
