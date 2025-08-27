export default function validateUserForm(form) {
  const errors = {};

  // Email
  if (!form.email) errors.email = "El email es obligatorio";
  else if (
    !/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i.test(form.email)
  )
    errors.email = "Email no válido";

  // Username
  if (!form.username) errors.username = "El nombre de usuario es obligatorio";
  else if (form.username.length < 4)
    errors.username = "Debe tener al menos 4 caracteres";

  // Password
  if (!form.password) errors.password = "La contraseña es obligatoria";
  else if (form.password.length < 8)
    errors.password = "Debe tener al menos 8 caracteres";

  // Repeat
  if (!form.repeatPassword)
    errors.repeatPassword = "Repite la contraseña";
  else if (form.password !== form.repeatPassword)
    errors.repeatPassword = "Las contraseñas no coinciden";

  return errors;
}
