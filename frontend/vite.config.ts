import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import tailwindcss from '@tailwindcss/vite';

// https://vite.dev/config/
export default defineConfig({
    define: {
        global: 'globalThis',
    },
    server: {
        host: true,
        port: 5173,
    },
    plugins: [react(), tailwindcss()],
    resolve: {
        alias: {
            '@': '/src', // 프로젝트 루트 기준 src 폴더를 가리킴
        },
    },
});
