// src/pages/DashboardAdminPage.jsx
import React from "react";
import { FaUsers, FaListAlt, FaStar, FaUserTie } from "react-icons/fa";
import { Bar, Pie } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  ArcElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";

ChartJS.register(CategoryScale, LinearScale, BarElement, ArcElement, Title, Tooltip, Legend);

export default function DashboardAdminPage() {
  // Ejemplos estáticos — estos valores los podés traer del backend
  const stats = {
    totalProviders: 128,
    totalCategories: 14,
    totalUsers: 450,
    avgRating: 4.6,
  };

  const providersPerCategory = {
    labels: ["Albañiles", "Electricistas", "Pintores", "Plomeros", "Otros"],
    datasets: [
      {
        label: "Prestadores",
        data: [40, 25, 18, 22, 23],
        backgroundColor: "#007bff",
      },
    ],
  };

  const ratingsDistribution = {
    labels: ["1 estrella", "2 estrellas", "3 estrellas", "4 estrellas", "5 estrellas"],
    datasets: [
      {
        label: "Cantidad",
        data: [3, 5, 12, 25, 83],
        backgroundColor: ["#dc3545", "#fd7e14", "#ffc107", "#0dcaf0", "#198754"],
      },
    ],
  };

  return (
    <div className="container">
      <h1 className="mb-4">Dashboard de Administración</h1>

      {/* Métricas rápidas */}
      <div className="row g-3 mb-4">
        <div className="col-md-3">
          <div className="card text-center shadow-sm">
            <div className="card-body">
              <FaUserTie size={32} className="text-primary mb-2" />
              <h5>{stats.totalProviders}</h5>
              <p className="mb-0">Prestadores</p>
            </div>
          </div>
        </div>

        <div className="col-md-3">
          <div className="card text-center shadow-sm">
            <div className="card-body">
              <FaListAlt size={32} className="text-success mb-2" />
              <h5>{stats.totalCategories}</h5>
              <p className="mb-0">Categorías</p>
            </div>
          </div>
        </div>

        <div className="col-md-3">
          <div className="card text-center shadow-sm">
            <div className="card-body">
              <FaUsers size={32} className="text-warning mb-2" />
              <h5>{stats.totalUsers}</h5>
              <p className="mb-0">Usuarios</p>
            </div>
          </div>
        </div>

        <div className="col-md-3">
          <div className="card text-center shadow-sm">
            <div className="card-body">
              <FaStar size={32} className="text-info mb-2" />
              <h5>{stats.avgRating}</h5>
              <p className="mb-0">Rating promedio</p>
            </div>
          </div>
        </div>
      </div>

      {/* Gráficos */}
      <div className="row g-4">
        <div className="col-md-8">
          <div className="card shadow-sm">
            <div className="card-header">Prestadores por categoría</div>
            <div className="card-body">
              <Bar data={providersPerCategory} options={{ responsive: true }} style={{ height: "300px" }}/>
            </div>
          </div>
        </div>

        <div className="col-md-4">
          <div className="card shadow-sm">
            <div className="card-header">Distribución de calificaciones</div>
            <div className="card-body">
              <Pie data={ratingsDistribution} options={{ responsive: true }} style={{ height: "300px" }} />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
