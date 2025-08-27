import useProviderForm from "../hooks/useProviderForm";
import ImageUpload from "./ImageUpload";
import ProviderNameField from "./fields/ProviderNameField";
import ProviderLastNameField from "./fields/ProviderLastNameField";
import ProviderAddressField from "./fields/ProviderAddressField";
import ProviderPhoneField from "./fields/ProviderPhoneField";
import ProviderCategoryField from "./fields/ProviderCategoryField";
import ProviderDescriptionField from "./fields/ProviderDescriptionField";
import ProviderActiveCheckbox from "./fields/ProviderActiveCheckbox";

export default function ProviderForm({ onSuccess, editingProvider }) {
  const {
    form,
    errors,
    categories,
    loadingCats,
    handleChange,
    handleSubmit,
    setForm
  } = useProviderForm({ onSuccess, editingProvider });

  return (
    <form onSubmit={handleSubmit} className="card card-body shadow-sm mb-4">
      <ProviderNameField value={form.name} error={errors.name} onChange={handleChange} />
      <ProviderLastNameField value={form.lastName} error={errors.lastName} onChange={handleChange} />
      <ProviderAddressField value={form.address} error={errors.address} onChange={handleChange} />
      <ProviderPhoneField value={form.phone} error={errors.phone} onChange={handleChange} />
      <ProviderCategoryField
        value={form.categoryId}
        error={errors.categoryId}
        categories={categories}
        loading={loadingCats}
        onChange={handleChange}
      />
      <ProviderDescriptionField value={form.description} onChange={handleChange} />

      <ImageUpload
        value={form.image}
        onChange={(file) => setForm({ ...form, image: file })}
        error={errors.image}
      />

      <ProviderActiveCheckbox value={form.isActive} onChange={handleChange} />

      <button type="submit" className="btn btn-primary">
        {editingProvider ? "Actualizar" : "Crear"} Proveedor
      </button>
    </form>
  );
}
