import {
  FaHome,
  FaUsers,
  FaTags,
  FaStar,
  FaCommentDots,
  FaUserCircle,
  FaUserPlus,
  FaPlus,
  FaList
} from "react-icons/fa";

export const sidebarSections = [
  {
    title: "Inicio",
    path: "/admin/dashboard",
    icon: <FaHome />,
  },
  {
    title: "Gestión de Prestadores",
    icon: <FaUsers />,
    children: [
      { title: "Registrar Prestador", path: "/prestadores/crear", icon: <FaUserPlus /> },
      { title: "Listar Prestadores", path: "/prestadores", icon: <FaList /> }
      
      
    ]
  },
  {
    title: "Gestión de Categorías",
    icon: <FaTags />,
    children: [
      { title: "Registrar Categoría", path: "/categorias/crear", icon: <FaPlus /> },
      { title: "Listar Categorías", path: "/categorias", icon: <FaList /> }
    ]
  },
  {
    title: "Interacción",
    icon: <FaCommentDots />,
    children: [
      { title: "Calificaciones y Comentarios", path: "/admin/ratings", icon: <FaStar /> },
    ]
  },
  {
    title: "Gestión de Usuarios",
    icon: <FaUserCircle />,
    children: [
      { title: "Usuarios", path: "/usuarios", icon: <FaUserCircle /> },
      { title: "Crear Usuario", path: "/usuarios/registrar", icon: <FaPlus /> }
    ]
  }
];
