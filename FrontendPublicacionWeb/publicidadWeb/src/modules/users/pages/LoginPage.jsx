import LoginForm from "../components/LoginForm";
import Box from "@mui/material/Box";

const LoginPage = () => {
  return (
    <Box
      sx={{
        height: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        background: "linear-gradient(135deg, #1c2c76ff, #181818ff)",
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
        <LoginForm z/>
      </Box>
    </Box>
  );
};

export default LoginPage;
