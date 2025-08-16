import "../../styles/ProviderModal.css";
import useProviderModal from "../../hooks/useProviderModal";
import ProviderModalHeader from "./ProviderModalHeader";
import ProviderModalContent from "./ProviderModalContent";
import ProviderModalActions from "./ProviderModalActions";
import ProviderModalDialogs from "./ProviderModalDialogs";

export default function ProviderModal({ open, provider, onClose, onUpdated }) {
  const modal = useProviderModal(provider, open, onClose, onUpdated);

  if (!open || !modal.current) return null;

  return (
    <div
      className="provider-modal-overlay"
      onClick={(e) => e.target === e.currentTarget && onClose?.()}
    >
      <div className="provider-modal-container">
        <ProviderModalHeader modal={modal} onClose={onClose} />
        <ProviderModalContent modal={modal} onUpdated={onUpdated} />
        <ProviderModalActions modal={modal} onClose={onClose} />
      </div>

      <ProviderModalDialogs modal={modal} />
    </div>
  );
}
