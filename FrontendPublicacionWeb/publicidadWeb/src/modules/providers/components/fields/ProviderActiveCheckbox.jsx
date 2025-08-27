export default function ProviderActiveCheckbox({ value, onChange }) {
  return (
    <div className="form-check mb-3">
      <input
        type="checkbox"
        name="isActive"
        checked={value || false}
        onChange={onChange}
        className="form-check-input"
        id="isActive"
      />
      <label htmlFor="isActive" className="form-check-label">
        Activo
      </label>
    </div>
  );
}
