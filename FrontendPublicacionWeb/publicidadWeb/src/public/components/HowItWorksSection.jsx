import React from "react";
import "../styles/theme.css";

function StepCard({ icon, title, text }) {
  return (
    <div
      style={{
        background: "var(--ct-surface)",
        border: "1px solid var(--ct-border)",
        borderRadius: 16,
        padding: 16,
        display: "grid",
        gap: 10,
        transition: "transform .15s ease, box-shadow .2s ease",
      }}
      onMouseEnter={(e) => (e.currentTarget.style.transform = "translateY(-2px)")}
      onMouseLeave={(e) => (e.currentTarget.style.transform = "translateY(0)")}
    >
      <div aria-hidden="true" style={{ width: 44, height: 44 }}>
        {icon}
      </div>
      <h3 style={{ margin: 0, color: "var(--ct-text)", fontSize: 18 }}>{title}</h3>
      <p style={{ margin: 0, color: "var(--ct-muted)", lineHeight: 1.5 }}>{text}</p>
    </div>
  );
}

const IconSearch = (
  <svg viewBox="0 0 24 24" width="44" height="44" fill="none" xmlns="http://www.w3.org/2000/svg">
    <circle cx="11" cy="11" r="7" stroke="var(--ct-primary)" strokeWidth="2"/>
    <path d="M20 20L16.5 16.5" stroke="var(--ct-primary)" strokeWidth="2" strokeLinecap="round"/>
  </svg>
);

const IconCompare = (
  <svg viewBox="0 0 24 24" width="44" height="44" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect x="3" y="5" width="7" height="14" rx="2" stroke="var(--ct-primary)" strokeWidth="2"/>
    <rect x="14" y="5" width="7" height="14" rx="2" stroke="var(--ct-primary)" strokeWidth="2"/>
    <path d="M6.5 9v6M17.5 13v2" stroke="var(--ct-primary)" strokeWidth="2" strokeLinecap="round"/>
  </svg>
);

const IconContact = (
  <svg viewBox="0 0 24 24" width="44" height="44" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path d="M3 5h18v12H7l-4 4V5z" stroke="var(--ct-primary)" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
    <path d="M7 9h10M7 12h7" stroke="var(--ct-primary)" strokeWidth="2" strokeLinecap="round"/>
  </svg>
);

export default function HowItWorksSection(){
  return (
    <section id="como-funciona" aria-labelledby="how-title" style={{ padding: "36px 0", background: "var(--ct-bg)" }}>
      <div className="ct-container" style={{ display: "grid", gap: 16 }}>
        <header style={{ display: "grid", gap: 6 }}>
          <h2 id="how-title" style={{ margin: 0, color: "var(--ct-text)", fontSize: 22 }}>
            ¿Cómo funciona?
          </h2>
          <p style={{ margin: 0, color: "var(--ct-muted)" }}>
            Encontrá prestadores en tres pasos simples.
          </p>
        </header>

        <div style={{ display: "grid", gap: 12, gridTemplateColumns: "1fr" }}>
          <style>{`
            @media (min-width: 840px){
              #how-grid { grid-template-columns: repeat(3, 1fr); }
            }
          `}</style>
          <div id="how-grid" style={{ display:"grid", gap:12, gridTemplateColumns:"1fr" }}>
            <StepCard
              icon={IconSearch}
              title="1) Buscá"
              text="Escribí el servicio que necesitás (ej.: electricista, plomero, peluquería) y explorá resultados."
            />
            <StepCard
              icon={IconCompare}
              title="2) Elegí"
              text="Entrá a los perfiles, leé la descripción y elegí el prestador que mejor encaja con tu necesidad."
            />
            <StepCard
              icon={IconContact}
              title="3) Contactá"
              text="Usá los datos de contacto para coordinar. Podés iniciar una conversación por WhatsApp o llamar."
            />
          </div>
        </div>
      </div>
    </section>
  );
}
