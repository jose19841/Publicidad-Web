import ProviderActionsMenu from "../ProviderActionsMenu";
import { resolveCategory } from "../../utils/providerHelpers";

export function getProviderColumns({ onEdit, onDisable, onEnable }) {
  return [
    { field: "name", label: "Nombre" },
    { field: "lastName", label: "Apellido" },
    { field: "phone", label: "Teléfono" },
    {
      field: "categoryName",
      label: "Categoría",
      render: (value, row) => resolveCategory(value, row),
    },
    {
      field: "isActive",
      label: "Estado",
      render: (value) => (value ? "Activo" : "Inhabilitado"),
    },
    {
      field: "_actions",
      label: "Acciones",
      render: (_, row) => (
        <ProviderActionsMenu
          row={row}
          onEdit={onEdit}
          onDisable={onDisable}
          onEnable={onEnable}
        />
      ),
    },
  ];
}
