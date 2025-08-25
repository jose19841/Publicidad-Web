// src/modules/providers/hooks/useProviderModal.js
import { useState } from "react";

export default function useProviderModal() {
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedProvider, setSelectedProvider] = useState(null);

  const openModal = (provider) => {
    setSelectedProvider(provider);
    setModalOpen(true);
  };

  const closeModal = () => {
    setSelectedProvider(null);
    setModalOpen(false);
  };

  return {
    modalOpen,
    selectedProvider,
    openModal,
    closeModal,
  };
}
