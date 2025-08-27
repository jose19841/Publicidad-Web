export default function ProviderModalActions({ modal, onClose }) {
  const { openRate, rateLoading, openComment, commentLoading, waHref, telHref } = modal;
  return (
    <div className="actions-bar">
      <div className="actions-grid">
        <button
          className="ct-btn btn-same"
          onClick={openRate}
          disabled={rateLoading}
        >
          Calificar
        </button>
        <button
          className="ct-btn btn-same"
          onClick={openComment}
          disabled={commentLoading}
        >
          Comentar
        </button>
        {waHref && (
          <a
            className="ct-btn primary btn-same"
            href={waHref}
            target="_blank"
            rel="noreferrer"
          >
            WhatsApp
          </a>
        )}
        {telHref && (
          <a className="ct-btn btn-same" href={telHref}>
            Llamar
          </a>
        )}
        <button className="ct-btn btn-same" onClick={onClose}>
          Cerrar
        </button>
      </div>
    </div>
  );
}
