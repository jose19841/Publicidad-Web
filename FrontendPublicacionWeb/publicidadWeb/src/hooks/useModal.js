import { useState } from "react";

export default function useModal() {
  const [show, setShow] = useState(false);
  const [data, setData] = useState(null); // útil para pasar datos al modal

  const openModal = (initialData = null) => {
    setData(initialData);
    setShow(true);
  };

  const closeModal = () => {
    setShow(false);
    setData(null);
  };

  return {
    show,
    data,
    openModal,
    closeModal,
  };
}
