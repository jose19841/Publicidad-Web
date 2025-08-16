import RegisterForm from "../components/RegisterForm";
import Box from "@mui/material/Box";
import "../styles/LoginPage.css"; 

const RegisterPage = () => {
  return (
    <div className="register-container">
    <Box
      sx={{
        height: "100dvh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
      
      }}
    >
      <Box
        sx={{
          background: "#fff",
          padding: 4,
          borderRadius: 3,
          boxShadow: 4,
          width: 400,
          maxWidth: "90%",
        }}
      >
        <RegisterForm />
      </Box>
    </Box>
    </div>
  );
};

export default RegisterPage;
