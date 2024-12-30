import path from "path"
import react from "@vitejs/plugin-react"
import { defineConfig } from "vite"

const isDevelopment = process.env.VITE_MODE === "development";

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
  server: {
    proxy: isDevelopment
      ? {
          "/api": {
            target: "https://bracusocial-web-backend.vercel.app/",
            changeOrigin: true,
            secure: false,
          },
        }
      : {},
  },
})
