import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import MainLayout from "../layout/MainLayout";
import CategoryListPage from "../modules/categories/pages/CategoryListPage.jsx";
import CreateCategoryPage from "../modules/categories/pages/CreateCategoryPage.jsx";

const AppRoutes = () => (
  <Routes>
    <Route path="/" element={<Navigate to="/categorias/crear" />} />
    <Route path="/categorias/crear" element={
      <MainLayout>
        <CreateCategoryPage />
      </MainLayout>
    } />
    <Route path="/categorias" element={
      <MainLayout>
        <CategoryListPage />
      </MainLayout>
    } />
  </Routes>
);

export default AppRoutes;
