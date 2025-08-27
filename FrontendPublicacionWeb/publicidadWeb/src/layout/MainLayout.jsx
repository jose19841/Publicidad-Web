import Sidebar from "../components/sidebar/Sidebar";

export default function MainLayout({ children }) {
  return (
    <div className="d-flex" style={{ minHeight: "100vh" }}>
      <Sidebar />
      <main className="flex-grow-1 bg-light p-4" style={{ minHeight: "100vh" }}>
        {children}
      </main>
    </div>
  );
}
