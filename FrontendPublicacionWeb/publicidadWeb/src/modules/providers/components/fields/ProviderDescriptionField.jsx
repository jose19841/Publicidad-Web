export default function ProviderDescriptionField({ value, onChange }) {
  return (
    <div className="mb-3">
      <label>Descripción</label>
      <textarea
        name="description"
        value={value || ""}
        onChange={onChange}
        className="form-control"
      />
    </div>
  );
}
