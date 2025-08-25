import "../../styles/theme.css";
import "../../styles/HowItWorksSection.css";
import StepCard from "./StepCard";
import { IconSearch, IconCompare, IconContact } from "./icons";

export default function HowItWorksSection() {
  return (
    <section id="como-funciona" aria-labelledby="how-title" className="how">
      <div className="ct-container how-container">
        <header className="how-header">
          <h2 id="how-title" className="how-title">
            ¿Cómo funciona?
          </h2>
          <p className="how-subtitle">
            Encontrá prestadores en tres pasos simples.
          </p>
        </header>

        <div className="how-grid">
          <StepCard
            icon={<IconSearch />}
            title="1) Buscá"
            text="Escribí el servicio que necesitás (ej.: electricista, plomero, peluquería) y explorá resultados."
          />
          <StepCard
    
            icon={<IconCompare />}
            title="2) Elegí"
            text="Entrá a los perfiles, leé la descripción y elegí el prestador que mejor encaja con tu necesidad."
          />
          <StepCard
            icon={<IconContact />}
            title="3) Contactá"
            text="Usá los datos de contacto para coordinar. Podés iniciar una conversación por WhatsApp o llamar."
          />
        </div>
      </div>
    </section>
  );
}
