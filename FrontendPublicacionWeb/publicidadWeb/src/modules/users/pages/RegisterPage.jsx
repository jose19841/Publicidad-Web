import RegisterForm from "../components/RegisterForm";
import Box from "@mui/material/Box";

const RegisterPage = () => {
  return (
    <Box
      sx={{
        height: "100dvh",
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
          maxWidth: "90%",
        }}
      >
        <RegisterForm />
      </Box>
    </Box>
  );
};

export default RegisterPage;
