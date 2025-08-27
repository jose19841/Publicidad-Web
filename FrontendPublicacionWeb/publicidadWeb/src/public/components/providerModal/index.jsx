import Swal from "sweetalert2"; // ya lo usamos para success

// helpers
const ok = (title, text) => {
  if (Swal?.fire) return Swal.fire({ icon: "success", title, text, timer: 1600, showConfirmButton: false });
  alert(`${title}\n${text || ""}`);
};

    const fail = (title, err) => {
    const msg =
        err?.response?.data?.message ||
        err?.response?.data?.error ||
        err?.message ||
        "Ocurrió un error. Intentá nuevamente.";
    if (Swal?.fire) return Swal.fire({ icon: "error", title, text: msg, timer: 1600 });
    alert(`${title}\n${msg}`);
    };
    export { ok, fail };