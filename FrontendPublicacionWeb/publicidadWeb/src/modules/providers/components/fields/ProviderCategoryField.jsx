export default function ProviderCategoryField({ value, error, categories, loading, onChange }) {
  return (
    <div className="mb-3">
      <label>Categoría</label>
      {loading ? (
        <p>Cargando categorías...</p>
      ) : (
        <select
          name="categoryId"
          value={value || ""}
          onChange={onChange}
          className={`form-control ${error ? "is-invalid" : ""}`}
        >
          <option value="">Seleccione una categoría</option>
          {categories.map((cat) => (
            <option key={cat.id} value={cat.id}>
              {cat.name}
            </option>
          ))}
        </select>
      )}
      {error && <div className="invalid-feedback">{error}</div>}
    </div>
  );
}
