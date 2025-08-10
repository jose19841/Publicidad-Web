import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import MainLayout from "../layout/MainLayout";
import CategoryListPage from "../modules/categories/pages/CategoryListPage.jsx";
import CreateCategoryPage from "../modules/categories/pages/CreateCategoryPage.jsx";
import LoginPage from "../modules/users/pages/LoginPage.jsx";
import RegisterPage from "../modules/users/pages/RegisterPage.jsx";
import PrivateRoute from "./PrivateRoute";
import HomePage from "../public/pages/HomePage.jsx";
import DashboardAdminPage from "../modules/admin/pages/DashboardAdminPage.jsx";
import UserListPage from "../modules/users/pages/UserListPage.jsx";
import UserRegisterPage from "../modules/users/pages/UserRegisterPage.jsx";
import CreateProviderPage from "../modules/providers/pages/CreateProviderPage.jsx";
import ProviderListPage from "../modules/providers/pages/ProviderListPage.jsx";

const AppRouter = () => (
  <Routes>
    {/* Rutas públicas */}
    <Route path="/login" element={<LoginPage />} />
    <Route path="/register" element={<RegisterPage />} />
    <Route path="/publicaciones" element={<HomePage />} />

    {/* Rutas protegidas solo para ADMIN */}

    <Route
      path="/admin/dashboard"
      element={
        <PrivateRoute requiredRole="ADMIN">
          <MainLayout>
            <DashboardAdminPage />
          </MainLayout>
        </PrivateRoute>
      }
    />
    <Route
      path="/categorias"
      element={
        <PrivateRoute requiredRole="ADMIN">
          <MainLayout>
            <CategoryListPage />
          </MainLayout>
        </PrivateRoute>
      }
    />
    <Route
      path="/categorias/crear"
      element={
        <PrivateRoute requiredRole="ADMIN">
          <MainLayout>
            <CreateCategoryPage />
          </MainLayout>
        </PrivateRoute>
      }
    />


    <Route
  path="/usuarios"
  element={
    <PrivateRoute requiredRole="ADMIN">
      <MainLayout>
        <UserListPage />
      </MainLayout>
    </PrivateRoute>
  }
/>


    <Route
  path="/usuarios/registrar"
  element={
    <PrivateRoute requiredRole="ADMIN">
      <MainLayout>
        <UserRegisterPage />
      </MainLayout>
    </PrivateRoute>
  }
  
/>

  <Route
      path="/prestadores"
      element={
        <PrivateRoute requiredRole="ADMIN">
          <MainLayout>
            <ProviderListPage />
          </MainLayout>
        </PrivateRoute>
      }
    />

<Route
  path="/prestadores/crear"
  element={
    <PrivateRoute requiredRole="ADMIN">
      <MainLayout>
        <CreateProviderPage />
      </MainLayout>
    </PrivateRoute>
  }
  
/>


    {/* ...más rutas */}
    <Route path="*" element={<Navigate to="/" replace />} />
    <Route path="/" element={<HomePage />} />
  </Routes>
);

export default AppRouter;
