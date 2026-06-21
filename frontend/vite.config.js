import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      // Forward /chat calls to the backend running on localhost:8080
      // (was incorrectly pointing at :8081 which caused proxy errors / non-JSON responses)
      "/chat": {
        target: "http://localhost:8080",
        // Preserve the browser's Origin header so backend sees the real frontend origin.
        // If changeOrigin is true, the proxy sets Origin to the target which can cause
        // the backend to reject the request as an invalid CORS origin.
        changeOrigin: false,
        secure: false,
        rewrite: (path) => path.replace(/^\/chat/, "/api/chat"),
      },
    },
  },
});
