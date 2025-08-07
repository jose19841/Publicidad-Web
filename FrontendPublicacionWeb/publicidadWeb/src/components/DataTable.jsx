import React from "react";

const DataTable = ({
  columns = [],
  data = [],
  actions = [],
  page = 1,
  pageSize = 10,
  total = 0,
  onPageChange,
}) => {
  const totalPages = Math.ceil(total / pageSize);

  return (
    <div className="card card-body shadow-sm">
      <div className="table-responsive">
        <table className="table table-striped align-middle">
          <thead>
            <tr>
              {columns.map((col) => (
                <th key={col.field}>{col.label}</th>
              ))}
              {actions.length > 0 && <th className="text-center">Acciones</th>}
            </tr>
          </thead>
          <tbody>
            {data.length > 0 ? (
              data.map((item) => (
                <tr key={item.id}>
                  {columns.map((col) => (
                    <td key={col.field}>
                      {col.render
                        ? col.render(item[col.field], item)
                        : item[col.field] || <span className="text-muted">-</span>}
                    </td>
                  ))}
                  {actions.length > 0 && (
                    <td className="text-center">
                      <div className="dropdown">
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
                          {actions.map((action, idx) => (
                            <li key={idx}>
                              <button
                                className={`dropdown-item${action.variant === "danger" ? " text-danger" : ""}`}
                                onClick={() => action.onClick && action.onClick(item)}
                              >
                                {action.label}
                              </button>
                            </li>
                          ))}
                        </ul>
                      </div>
                    </td>
                  )}
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan={columns.length + (actions.length > 0 ? 1 : 0)} className="text-center text-muted">
                  No hay datos para mostrar.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {totalPages > 1 && (
        <nav>
          <ul className="pagination justify-content-center">
            <li className={`page-item${page === 1 ? " disabled" : ""}`}>
              <button className="page-link" onClick={() => onPageChange(page - 1)}>
                Anterior
              </button>
            </li>
            {Array.from({ length: totalPages }, (_, idx) => (
              <li key={idx + 1} className={`page-item${page === idx + 1 ? " active" : ""}`}>
                <button className="page-link" onClick={() => onPageChange(idx + 1)}>
                  {idx + 1}
                </button>
              </li>
            ))}
            <li className={`page-item${page === totalPages ? " disabled" : ""}`}>
              <button className="page-link" onClick={() => onPageChange(page + 1)}>
                Siguiente
              </button>
            </li>
          </ul>
        </nav>
      )}
    </div>
  );
};

export default DataTable;
