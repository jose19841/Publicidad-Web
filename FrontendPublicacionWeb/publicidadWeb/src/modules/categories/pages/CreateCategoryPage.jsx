import React from 'react';
import { useNavigate } from 'react-router-dom';
import CategoryForm from '../components/CategoryForm';

const CreateCategoryPage = () => {
  const navigate = useNavigate();
  return (
    <div className="container py-5">
      <h1>Nueva Categoría</h1>
      <CategoryForm onSuccess={() => navigate('/categorias')} />
    </div>
  );
};

export default CreateCategoryPage;
