// src/components/header/HeaderLogo.jsx
import { Link } from "react-router-dom";
import logo from "../../../assets/img/logo-clasifi-trenque.png";

export default function HeaderLogo() {
  return (
    <Link to="/" className="header-logo">
      <div className="header-logo-icon" />
      <img src={logo} alt="" className="header-logo-icon" />
      <div className="header-logo-icon-blue" />
      <span className="header-logo-text">ClasifiTrenque</span>
    </Link>
  );
}
