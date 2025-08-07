import React from "react";
import CategoryForm from "./CategoryForm";

const CategoryModal = ({ isOpen, category, onClose, onUpdate }) => {
  if (!isOpen) return null;

  const handleSuccess = () => {
    onClose();
    if (onUpdate) onUpdate();
  };

  return (
    <div className="modal fade show d-block" tabIndex="-1" style={{ backgroundColor: "#00000080" }}>
      <div className="modal-dialog">
        <div className="modal-content">
          <CategoryForm
            editingCategory={category}
            onSuccess={handleSuccess}
          />
          <div className="modal-footer">
            <button type="button" className="btn btn-secondary" onClick={onClose}>
              Cancelar
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CategoryModal;
