import { useState } from "react";

export function useAdminProfile({ initialName = "admin", initialEmail = "admin@admin.com" } = {}) {
  const [name, setName] = useState(initialName);
  const [email, setEmail] = useState(initialEmail);
  const [showPwd, setShowPwd] = useState(false);

  const openPwd = () => setShowPwd(true);
  const closePwd = () => setShowPwd(false);

  const handleSave = (e) => {
    e?.preventDefault?.();
    // acá solo UI; el servicio para guardar lo enchufamos después
    // return { name, email }
  };

  return { name, setName, email, setEmail, showPwd, openPwd, closePwd, handleSave };
}
