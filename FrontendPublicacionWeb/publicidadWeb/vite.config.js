import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    host: true,       // permite acceder desde la red (0.0.0.0)
    port: 5173,       // podés cambiar el puerto si querés
  },
})
