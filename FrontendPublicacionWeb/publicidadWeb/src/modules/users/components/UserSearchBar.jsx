// src/modules/users/components/UserSearchBar.jsx
import React from "react";

export default function UserSearchBar({ search, setSearch }) {
  return (
    <div className="mb-3">
      <input
        className="form-control"
        type="search"
        placeholder="Buscar por email o rol"
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        style={{ maxWidth: 350 }}
      />
    </div>
  );
}
