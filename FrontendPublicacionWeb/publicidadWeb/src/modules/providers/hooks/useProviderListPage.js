import useProvidersTable from "../hooks/useProvidersTable";
import useProviderModal from "../hooks/useProviderModal";
import { resolveCategory } from "../utils/providerHelpers";

export default function useProviderListPage() {
  const table = useProvidersTable();
  const { modalOpen, selectedProvider, openModal, closeModal } = useProviderModal();

  const handleEdit = (row) => openModal(row);
  const handleDisable = (row) => table.handleDisable(row);
  const handleEnable = (row) => table.handleEnable(row);

  return {
    table,
    modalOpen,
    selectedProvider,
    closeModal,
    handleEdit,
    handleDisable,
    handleEnable,
    resolveCategory,
  };
}
