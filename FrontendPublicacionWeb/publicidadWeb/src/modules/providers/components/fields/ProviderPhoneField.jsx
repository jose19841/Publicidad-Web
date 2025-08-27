export default function ProviderPhoneField({ value, error, onChange }) {
  return (
    <div className="mb-3">
      <label>Teléfono</label>
      <input
        type="text"
        name="phone"
        value={value || ""}
        onChange={onChange}
        className={`form-control ${error ? "is-invalid" : ""}`}
      />
      {error && <div className="invalid-feedback">{error}</div>}
    </div>
  );
}
