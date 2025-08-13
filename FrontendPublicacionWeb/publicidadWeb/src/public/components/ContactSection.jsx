import React, { useState } from "react";
import "../styles/theme.css";

export default function ContactSection() {
  const [msg, setMsg] = useState("");
  const max = 600;

  const handleSubmit = (e) => {
    e.preventDefault(); // solo visual por ahora
  };

  return (
    <section id="contacto" aria-labelledby="contact-title" style={{ padding: "36px 0", background: "var(--ct-bg)" }}>
      <div className="ct-container" style={{ display: "grid", gap: 18 }}>
        <header style={{ display: "grid", gap: 6 }}>
          <h2 id="contact-title" style={{ margin: 0, color: "var(--ct-text)", fontSize: 22 }}>
            Contacto
          </h2>
          <p style={{ margin: 0, color: "var(--ct-muted)" }}>
            Escribinos tu consulta y te respondemos a la brevedad.
          </p>
        </header>

        <div style={{ display: "grid", gap: 16, gridTemplateColumns: "1fr" }}>
          {/* 2 columnas en desktop */}
          <style>{`
            @media (min-width: 960px){
              #contact-grid { grid-template-columns: 1.2fr 0.8fr; }
            }
          `}</style>

          <div id="contact-grid" style={{ display: "grid", gap: 16, gridTemplateColumns: "1fr" }}>
            {/* Formulario */}
            <form onSubmit={handleSubmit}
                  style={{
                    background: "var(--ct-surface)",
                    border: "1px solid var(--ct-border)",
                    borderRadius: 16,
                    padding: 16,
                    display: "grid",
                    gap: 12
                  }}
                  aria-label="Formulario de contacto">
              <div style={{ display: "grid", gap: 6 }}>
                <label htmlFor="c-name" style={{ color: "var(--ct-text)", fontWeight: 600 }}>Nombre</label>
                <input id="c-name" type="text" placeholder="Tu nombre"
                       style={{
                         background: "transparent", color: "var(--ct-text)",
                         border: "1px solid var(--ct-border)", borderRadius: 10, padding: "10px 12px"
                       }}/>
              </div>

              <div style={{ display: "grid", gap: 6 }}>
                <label htmlFor="c-email" style={{ color: "var(--ct-text)", fontWeight: 600 }}>Email</label>
                <input id="c-email" type="email" placeholder="tunombre@email.com"
                       style={{
                         background: "transparent", color: "var(--ct-text)",
                         border: "1px solid var(--ct-border)", borderRadius: 10, padding: "10px 12px"
                       }}/>
              </div>

              <div style={{ display: "grid", gap: 6 }}>
                <label htmlFor="c-msg" style={{ color: "var(--ct-text)", fontWeight: 600 }}>Mensaje</label>
                <textarea id="c-msg" rows={6}
                          placeholder="Contanos brevemente tu consulta…"
                          value={msg}
                          onChange={(e)=>setMsg(e.target.value.slice(0, max))}
                          style={{
                            background: "transparent", color: "var(--ct-text)",
                            border: "1px solid var(--ct-border)", borderRadius: 10, padding: "10px 12px",
                            resize: "vertical"
                          }}/>
                <div style={{ color: "var(--ct-muted)", fontSize: 12, textAlign: "right" }}>
                  {msg.length}/{max}
                </div>
              </div>

              <div style={{ display: "flex", gap: 10, justifyContent: "flex-end" }}>
                <button type="button" className="ct-btn">Limpiar</button>
                <button type="submit" className="ct-btn primary">Enviar</button>
              </div>
            </form>

            {/* Datos de contacto / info lateral */}
            <aside
              style={{
                background: "var(--ct-surface)",
                border: "1px solid var(--ct-border)",
                borderRadius: 16,
                padding: 16,
                display: "grid",
                gap: 10,
                height: "fit-content"
              }}
              aria-label="Información de contacto"
            >
              <h3 style={{ margin: 0, color: "var(--ct-text)", fontSize: 18 }}>Información</h3>
              <div style={{ color: "var(--ct-muted)" }}>
                <strong style={{ color: "var(--ct-text)" }}>Email:</strong> contacto@clasifitrenque.com
              </div>
              <div style={{ color: "var(--ct-muted)" }}>
                <strong style={{ color: "var(--ct-text)" }}>Teléfono:</strong> 2392-000000
              </div>
              <div style={{ color: "var(--ct-muted)" }}>
                <strong style={{ color: "var(--ct-text)" }}>Horario:</strong> Lun a Vie 9:00–18:00
              </div>

              <hr style={{ borderColor: "var(--ct-border)" }} />

              <div style={{ display: "grid", gap: 8 }}>
                <span style={{ color: "var(--ct-text)", fontWeight: 600 }}>Redes</span>
                <div style={{ display: "flex", gap: 10, flexWrap: "wrap" }}>
                  <a href="#" style={{ color: "var(--ct-primary)", textDecoration: "none" }}>WhatsApp</a>
                </div>
              </div>
            </aside>
          </div>
        </div>
      </div>
    </section>
  );
}
