import React from "react";

const ImageUpload = ({ value, onChange, error }) => {
  return (
    <div className="mb-3">
      <label>Foto</label>
      <input
        type="file"
        name="image"
        accept="image/*"
        onChange={(e) => onChange(e.target.files[0])}
        className={`form-control ${error ? "is-invalid" : ""}`}
      />
      {error && <div className="invalid-feedback">{error}</div>}

      {/* Vista previa */}
      {value && typeof value !== "string" && (
        <img
          src={URL.createObjectURL(value)}
          alt="Vista previa"
          className="mt-2"
          style={{ maxWidth: "150px", maxHeight: "150px", objectFit: "cover" }}
        />
      )}
      {value && typeof value === "string" && (
        <img
          src={value}
          alt="Vista previa"
          className="mt-2"
          style={{ maxWidth: "150px", maxHeight: "150px", objectFit: "cover" }}
        />
      )}
    </div>
  );
};

export default ImageUpload;
