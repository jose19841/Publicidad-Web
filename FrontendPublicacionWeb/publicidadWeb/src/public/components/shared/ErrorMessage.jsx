
export default function ErrorMessage({ message }) {
  if (!message) return null;
  return (
    <div className="ct-container" style={{ color: "tomato", paddingTop: 12 }}>
      {message}
    </div>
  );
}
