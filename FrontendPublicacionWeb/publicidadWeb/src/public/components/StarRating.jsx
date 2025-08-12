// StarRating.jsx (fix: IDs únicos con useId)
import React, { useId } from "react";

export default function StarRating({ value = 0, size = 16, showValue = false }) {
  const uid = useId(); // <- único por render
  const full = Math.floor(value);
  const frac = Math.max(0, Math.min(1, value - full));

  return (
    <div style={{ display: "inline-flex", alignItems: "center", gap: 6 }}>
      <div style={{ display: "inline-flex", gap: 2, color: "gold" }}>
        {Array.from({ length: 5 }).map((_, i) => {
          const fill = i < full ? 1 : i === full ? frac : 0;
          const gradId = `star-grad-${uid}-${i}`;
          return (
            <svg key={i} width={size} height={size} viewBox="0 0 24 24" aria-hidden>
              <defs>
                <linearGradient id={gradId} x1="0" x2="1" y1="0" y2="0">
                  <stop offset={`${fill * 100}%`} stopColor="currentColor" />
                  <stop offset={`${fill * 100}%`} stopColor="transparent" />
                </linearGradient>
              </defs>
              <path
                d="M12 2l2.92 6.26 6.88.55-5.19 4.5 1.62 6.69L12 16.9 5.77 20l1.62-6.69-5.19-4.5 6.88-.55L12 2z"
                fill={`url(#${gradId})`}
                stroke="currentColor"
                strokeWidth="1"
                // baja opacidad cuando está vacío para que se note el “hueco”
                opacity={fill > 0 ? 1 : 0.35}
              />
            </svg>
          );
        })}
      </div>
      {showValue && (
        <span style={{ fontSize: 13, color: "var(--ct-muted)" }}>{value.toFixed(1)}</span>
      )}
    </div>
  );
}
