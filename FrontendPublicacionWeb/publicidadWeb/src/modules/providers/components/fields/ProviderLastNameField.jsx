export default function ProviderLastNameField({ value, error, onChange }) {
  return (
    <div className="mb-3">
      <label>Apellido</label>
      <input
        type="text"
        name="lastName"
        value={value || ""}
        onChange={onChange}
        className={`form-control ${error ? "is-invalid" : ""}`}
      />
      {error && <div className="invalid-feedback">{error}</div>}
    </div>
  );
}
