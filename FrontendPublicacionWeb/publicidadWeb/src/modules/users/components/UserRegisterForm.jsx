import React, { useEffect, useState } from "react";
import { FaEye, FaEyeSlash } from "react-icons/fa";

export default function UserRegisterForm({
  form,
  handleChange,
  handleSubmit,
  handleBlur,
  touched,
  loading,
  error,
  validation,
}) {
  const [showPassword, setShowPassword] = useState(false);
  const [showRepeat, setShowRepeat] = useState(false);

  // Resetea touched si el form está vacío (opcional, pero lo ideal es desde hook)
  useEffect(() => {
    if (
      form.email === "" &&
      form.username === "" &&
      form.password === "" &&
      form.repeatPassword === "" &&
      Object.keys(touched).length !== 0
    ) {
      // Este efecto es opcional, la lógica de limpiar touched puede estar solo en el hook
      // setTouched({});
    }
  }, [form, touched]);

  return (
    <form
      className="card card-body shadow-sm"
      onSubmit={handleSubmit}
      style={{ maxWidth: 400, margin: "0 auto" }}
      autoComplete="off"
      noValidate
    >
      <h2 className="mb-3">Registrar usuario</h2>
      <div className="mb-3">
        <label className="form-label">Email</label>
        <input
          className={`form-control${touched.email && validation.email ? " is-invalid" : ""}`}
          type="email"
          name="email"
          value={form.email}
          onChange={handleChange}
          onBlur={handleBlur}
          autoComplete="off"
        />
        {touched.email && validation.email && (
          <div className="invalid-feedback">{validation.email}</div>
        )}
      </div>
      <div className="mb-3">
        <label className="form-label">Nombre de usuario</label>
        <input
          className={`form-control${touched.username && validation.username ? " is-invalid" : ""}`}
          type="text"
          name="username"
          value={form.username}
          onChange={handleChange}
          onBlur={handleBlur}
          autoComplete="off"
        />
        {touched.username && validation.username && (
          <div className="invalid-feedback">{validation.username}</div>
        )}
      </div>
      <div className="mb-3">
        <label className="form-label">Contraseña</label>
        <div className="input-group">
          <input
            className={`form-control${touched.password && validation.password ? " is-invalid" : ""}`}
            type={showPassword ? "text" : "password"}
            name="password"
            value={form.password}
            onChange={handleChange}
            onBlur={handleBlur}
            autoComplete="new-password"
          />
          <button
            className="btn btn-outline-secondary"
            type="button"
            tabIndex={-1}
            onClick={() => setShowPassword((v) => !v)}
            title={showPassword ? "Ocultar" : "Mostrar"}
          >
            {showPassword ? <FaEyeSlash /> : <FaEye />}
          </button>
        </div>
        {touched.password && validation.password && (
          <div className="invalid-feedback d-block">{validation.password}</div>
        )}
      </div>
      <div className="mb-3">
        <label className="form-label">Repetir contraseña</label>
        <div className="input-group">
          <input
            className={`form-control${touched.repeatPassword && validation.repeatPassword ? " is-invalid" : ""}`}
            type={showRepeat ? "text" : "password"}
            name="repeatPassword"
            value={form.repeatPassword}
            onChange={handleChange}
            onBlur={handleBlur}
            autoComplete="new-password"
          />
          <button
            className="btn btn-outline-secondary"
            type="button"
            tabIndex={-1}
            onClick={() => setShowRepeat((v) => !v)}
            title={showRepeat ? "Ocultar" : "Mostrar"}
          >
            {showRepeat ? <FaEyeSlash /> : <FaEye />}
          </button>
        </div>
        {touched.repeatPassword && validation.repeatPassword && (
          <div className="invalid-feedback d-block">{validation.repeatPassword}</div>
        )}
      </div>
      {error && <div className="alert alert-danger">{error}</div>}
      <button
        className="btn btn-primary"
        type="submit"
        disabled={loading || Object.keys(validation).length > 0}
      >
        {loading ? "Registrando..." : "Registrar"}
      </button>
    </form>
  );
}
