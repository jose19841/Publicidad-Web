import React from 'react';
import { useNavigate } from 'react-router-dom';
import CategoryList from '../components/CategoryList';

const CategoryListPage = () => {
  const navigate = useNavigate();
  return (
    <div className="container py-5">
      <div className="row justify-content-between align-items-center mb-4">
        <div className="col">
          <h1>Categorías</h1>
        </div>
        <div className="col-auto">
          <button className="btn btn-primary" onClick={() => navigate('/categorias/crear')}>
            + Nueva Categoría
          </button>
        </div>
      </div>
      <CategoryList />
    </div>
  );
};

export default CategoryListPage;
