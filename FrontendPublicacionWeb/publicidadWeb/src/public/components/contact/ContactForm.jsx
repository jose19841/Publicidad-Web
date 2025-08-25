import React, { useState } from "react";

export default function ContactForm() {
  const [msg, setMsg] = useState("");
  const max = 600;

  const handleSubmit = (e) => {
    e.preventDefault(); // solo visual
  };

  const handleClear = () => setMsg("");

  return (
    <form onSubmit={handleSubmit} className="contact-form" aria-label="Formulario de contacto">
      <div className="contact-form-group">
        <label htmlFor="c-name" className="contact-label">Nombre</label>
        <input id="c-name" type="text" placeholder="Tu nombre" className="contact-input" />
      </div>

      <div className="contact-form-group">
        <label htmlFor="c-email" className="contact-label">Email</label>
        <input id="c-email" type="email" placeholder="tunombre@email.com" className="contact-input" />
      </div>

      <div className="contact-form-group">
        <label htmlFor="c-msg" className="contact-label">Mensaje</label>
        <textarea
          id="c-msg"
          rows={6}
          placeholder="Contanos brevemente tu consulta…"
          value={msg}
          onChange={(e) => setMsg(e.target.value.slice(0, max))}
          className="contact-textarea"
        />
        <div className="contact-counter">
          {msg.length}/{max}
        </div>
      </div>

      <div className="contact-form-actions">
        <button type="button" className="ct-btn" onClick={handleClear}>Limpiar</button>
        <button type="submit" className="ct-btn primary">Enviar</button>
      </div>
    </form>
  );
}
