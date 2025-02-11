# 1. Build stage
FROM node:22.12.0 AS build

# 작업 디렉토리 설정
WORKDIR /app

# package.json과 package-lock.json을 복사
COPY ./frontend/package*.json /app/

# 의존성 설치
RUN npm install

# 나머지 소스 파일을 복사
COPY ./frontend/ /app/

# 빌드
VITE_BASE_URL=$VITE_BASE_URL npm run build

# 2. Serve stage using Nginx
FROM nginx:latest

# Vite로 빌드된 파일을 Nginx 서버로 복사
COPY --from=build /app/dist /usr/share/nginx/html

# Nginx 실행
CMD ["nginx", "-g", "daemon off;"]
