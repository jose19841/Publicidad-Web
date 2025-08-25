import Box from "@mui/material/Box";
import LoginForm from "../components/LoginForm";
import "../styles/LoginPage.css";

const LoginPage = () => {
  return (
    <div className="login-container pt-3 pb-5 ">
      <Box
        sx={{
          height: "100vh",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          padding: 2,
        }}
      >
        <Box
          sx={{
            background: "#fff",
            padding: 4,
            borderRadius: 3,
            boxShadow: 4,
            width: 400,
            maxWidth: "100%",
          }}
        >
          <LoginForm />
        </Box>
      </Box>
    </div>
  );
};

export default LoginPage;
