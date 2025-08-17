import DataTable from "../../../components/DataTable";
import UserSearchBar from "../components/UserSearchBar";
import UserActionsModal from "../components/UserActionsModal";
import useUserListPage from "../hooks/useUserListPage";

export default function UserListPage() {
  const {
    users,
    loading,
    error,
    page,
    setPage,
    total,
    search,
    setSearch,
    columns,
    actions,
    modalOpen,
    selectedUser,
    handleModalClose,
    handleSave,
    pageSize,
  } = useUserListPage();

  if (loading) return <div>Cargando usuarios...</div>;
  if (error) return <div className="text-danger">Error: {error}</div>;

  return (
    <div>
      <h1 className="mb-4">Usuarios del sistema</h1>
      <UserSearchBar search={search} setSearch={setSearch} />
      <DataTable
        columns={columns}
        data={users}
        actions={actions}
        page={page}
        pageSize={pageSize}
        total={total}
        onPageChange={setPage}
      />
     
      <UserActionsModal
        open={modalOpen}
        user={selectedUser}
        onClose={handleModalClose}
        onSave={handleSave}
      />
    </div>
  );
}
