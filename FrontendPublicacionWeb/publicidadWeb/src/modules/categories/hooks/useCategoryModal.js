import { useState } from "react";

export default function useCategoryModal() {
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedCategory, setSelectedCategory] = useState(null);

  const openModal = (category) => {
    setSelectedCategory(category);
    setModalOpen(true);
  };

  const closeModal = () => {
    setSelectedCategory(null);
    setModalOpen(false);
  };

  return {
    modalOpen,
    selectedCategory,
    openModal,
    closeModal,
  };
}
