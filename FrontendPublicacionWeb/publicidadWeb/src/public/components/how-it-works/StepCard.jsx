import React from "react";
import "../../styles/HowItWorksSection.css";

export default function StepCard({ icon, title, text }) {
  return (
    <div className="stepcard">
      <div className="stepcard-icon" aria-hidden="true">
        {icon}
      </div>
      <h3 className="stepcard-title">{title}</h3>
      <p className="stepcard-text">{text}</p>
    </div>
  );
}
