import React from "react";

export default function ContactAside() {
  return (
    <aside className="contact-aside" aria-label="Información de contacto">
      <h3 className="contact-aside-title">Información</h3>
      <div className="contact-info">
        <strong>Email:</strong> contacto@clasifitrenque.com
      </div>
      <div className="contact-info">
        <strong>Teléfono:</strong> 2392-000000
      </div>
      <div className="contact-info">
        <strong>Horario:</strong> Lun a Vie 9:00–18:00
      </div>

      <hr />

      <div className="contact-social">
        <span className="contact-social-title">Redes</span>
        <div className="contact-social-links">
          <a href="#">WhatsApp</a>
        </div>
      </div>
    </aside>
  );
}
