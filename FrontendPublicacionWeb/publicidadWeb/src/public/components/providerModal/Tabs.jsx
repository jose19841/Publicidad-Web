// ProviderModal/Tabs.jsx
import React from "react";
export default function Tabs({ value, onChange, count }) {
  return (
    <div style={{ padding:"0 16px", borderBottom:"1px solid var(--ct-border)", background:"var(--ct-surface)" }}>
      <div style={{ display:"flex", gap:8 }}>
        <button className="ct-tab" aria-selected={value==="info"} onClick={() => onChange("info")}>Información</button>
        <button className="ct-tab" aria-selected={value==="opiniones"} onClick={() => onChange("opiniones")}>
          Opiniones ({count})
        </button>
      </div>
    </div>
  );
}
