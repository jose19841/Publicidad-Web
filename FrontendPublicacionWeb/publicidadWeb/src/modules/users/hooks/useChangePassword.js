import { useEffect, useState } from "react";

export function useChangePassword(open = false) {
  const [form, setForm] = useState({
    currentPassword: "",
    newPassword: "",
    repeatPassword: "",
  });

  const [showOld, setShowOld] = useState(false);
  const [showNew, setShowNew] = useState(false);
  const [showRepeat, setShowRepeat] = useState(false);

  useEffect(() => {
    if (open) {
      setForm({ currentPassword: "", newPassword: "", repeatPassword: "" });
      setShowOld(false); setShowNew(false); setShowRepeat(false);
      document.body.classList.add("modal-open");
    } else {
      document.body.classList.remove("modal-open");
    }
    return () => document.body.classList.remove("modal-open");
  }, [open]);

  const setField = (name, value) => setForm((f) => ({ ...f, [name]: value }));

  return {
    form, setField,
    showOld, setShowOld,
    showNew, setShowNew,
    showRepeat, setShowRepeat,
  };
}
