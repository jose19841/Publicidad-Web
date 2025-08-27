export default function ProviderAddressField({ value, error, onChange }) {
  return (
    <div className="mb-3">
      <label>Dirección</label>
      <input
        type="text"
        name="address"
        value={value || ""}
        onChange={onChange}
        className={`form-control ${error ? "is-invalid" : ""}`}
      />
      {error && <div className="invalid-feedback">{error}</div>}
    </div>
  );
}
