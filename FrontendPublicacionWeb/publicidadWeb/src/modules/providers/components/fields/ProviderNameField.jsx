export default function ProviderNameField({ value, error, onChange }) {
  return (
    <div className="mb-3">
      <label>Nombre</label>
      <input
        type="text"
        name="name"
        value={value || ""}
        onChange={onChange}
        className={`form-control ${error ? "is-invalid" : ""}`}
      />
      {error && <div className="invalid-feedback">{error}</div>}
    </div>
  );
}
