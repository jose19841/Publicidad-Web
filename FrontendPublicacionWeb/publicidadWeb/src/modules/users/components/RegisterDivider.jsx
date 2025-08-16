import Divider from "@mui/material/Divider";
import Typography from "@mui/material/Typography";

export default function RegisterDivider() {
  return (
    <Divider sx={{
          "&::before, &::after": {
            borderTopStyle: "solid",
            borderTopWidth: 2,
            borderColor: "divider"
          },
          display: "flex",
          alignItems: "center"
        }}
      >
      <Typography variant="overline" sx={{ color: "text.secondary", fontWeight: "fontWeightMedium" }}>
        O
      </Typography>
    </Divider>
  );
}
