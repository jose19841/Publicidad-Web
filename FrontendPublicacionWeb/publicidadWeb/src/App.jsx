import { BrowserRouter as Router } from "react-router-dom";
import AppRouter from "./routes/AppRouter";
import { AuthProvider } from "./context/AuthContext";
import LoginPortal from "./modules/users/components/LoginPortal";

function App() {
  return (
    <AuthProvider>
      <Router>
        <AppRouter />
         <LoginPortal /> 
      </Router>
    </AuthProvider>
  );
}

export default App;
