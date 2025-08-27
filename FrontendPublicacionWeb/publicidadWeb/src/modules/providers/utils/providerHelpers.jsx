import React from "react";

/**
 * Resuelve el nombre de la categoría para una fila dada.
 * @param {*} value - Valor directo de la celda.
 * @param {*} row - Fila completa del DataTable.
 * @returns {JSX.Element|string} - Nombre de la categoría o guion si no existe.
 */
export function resolveCategory(value, row) {
  if (value) return value;
  if (row?.categoryName) return row.categoryName;
  if (row?.CategoryName) return row.CategoryName;
  if (row?.category?.name) return row.category.name;
  if (row?.category?.categoryName) return row.category.categoryName;
  return <span className="text-muted">-</span>;
}

export function initials(name = "", lastName = "") {
  const first = name.trim().charAt(0).toUpperCase();
  const last = lastName.trim().charAt(0).toUpperCase();
  return `${first}${last}` || "";
}