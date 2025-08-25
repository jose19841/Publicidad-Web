import React from "react";
import { useNavigate } from "react-router-dom";
import ProviderForm from "../components/ProviderForm";

const CreateProviderPage = () => {
  const navigate = useNavigate();

  return (
    <div className="container py-5">
      <h1>Nuevo Prestador</h1>
      {/* Acá no se llama directamente al hook, sino que lo maneja ProviderForm */}
      <ProviderForm onSuccess={() => navigate("/prestadores")} />
    </div>
  );
};

export default CreateProviderPage;
