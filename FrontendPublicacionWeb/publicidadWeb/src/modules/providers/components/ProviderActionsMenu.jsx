export default function ProviderActionsMenu({ row, onEdit, onDisable, onEnable }) {
  return (
    <div className="dropdown text-center">
      <button
        className="btn btn-light btn-sm"
        type="button"
        data-bs-toggle="dropdown"
        aria-expanded="false"
        title="Acciones"
      >
        <span style={{ fontSize: 18 }}>⋮</span>
      </button>
      <ul className="dropdown-menu dropdown-menu-end">
        <li>
          <button className="dropdown-item" onClick={() => onEdit(row)}>
            Editar
          </button>
        </li>
        <li>
          {row.isActive ? (
            <button
              className="dropdown-item text-danger"
              onClick={() => onDisable(row)}
            >
              Inhabilitar prestador
            </button>
          ) : (
            <button
              className="dropdown-item"
              onClick={() => onEnable(row)}
            >
              Habilitar prestador
            </button>
          )}
        </li>
      </ul>
    </div>
  );
}
