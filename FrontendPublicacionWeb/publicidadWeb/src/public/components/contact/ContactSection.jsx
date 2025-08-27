import React from "react";
import "../../styles/theme.css";
import "../../styles/ContactSection.css";
import ContactHeader from "./ContactHeader";
import ContactForm from "./ContactForm";
import ContactAside from "./ContactAside";

export default function ContactSection() {
  return (
    <section id="contacto" aria-labelledby="contact-title" className="contact-section">
      <div className="ct-container contact-container">
        <ContactHeader />
        <div className="contact-grid" id="contact-grid">
          <ContactForm />
          <ContactAside />
        </div>
      </div>
    </section>
  );
}
