import React from "react";
import "../styles/theme.css";

export default function Footer() {
  return (
    <footer
      style={{
        background: "var(--ct-bg)",
        borderTop: "1px solid var(--ct-border)",
        padding: "16px 0",
        marginTop: "auto"
      }}
    >
      <div
        className="ct-container"
        style={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          gap: 6,
          textAlign: "center",
          color: "var(--ct-muted)",
          fontSize: 14
        }}
      >
        <span>
          © {new Date().getFullYear()} ClasifiTrenque — Todos los derechos reservados.
        </span>
        <span>
          Desarrollado por{" "}
          <a
            href="https://imperial-net.com"
            target="_blank"
            rel="noreferrer"
            style={{ color: "var(--ct-primary)", textDecoration: "none" }}
          >
            Imperial-Net
          </a>
        </span>
      </div>
    </footer>
  );
}
