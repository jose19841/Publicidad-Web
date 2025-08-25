import DataTable from "../../../components/DataTable";
import ProviderModal from "./ProviderModal";
import useProviderListPage from "../hooks/useProviderListPage";
import { getProviderColumns } from "./columns/providerColumns";

export default function ProviderList() {
  const {
    table,
    modalOpen,
    selectedProvider,
    closeModal,
    handleEdit,
    handleDisable,
    handleEnable,
  } = useProviderListPage();

  if (table.loading) return <div>Cargando...</div>;
  if (table.error) return <div className="text-danger">Error: {table.error}</div>;

  const columns = getProviderColumns({
    onEdit: handleEdit,
    onDisable: handleDisable,
    onEnable: handleEnable,
  });

  return (
    <>
      <DataTable
        columns={columns}
        data={table.data}
        page={table.page}
        pageSize={table.pageSize}
        total={table.total}
        onPageChange={table.handlePageChange}
        onPageSizeChange={table.handlePageSizeChange}
      />

      <ProviderModal
        isOpen={modalOpen}
        provider={selectedProvider}
        onClose={closeModal}
        onSuccess={() => {
          table.refresh();
          closeModal();
        }}
      />
    </>
  );
}
