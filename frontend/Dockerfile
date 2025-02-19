# 1. Build stage
FROM node:22.12.0 AS build

# 작업 디렉토리 설정
WORKDIR /app

# package.json과 package-lock.json을 복사
COPY ./frontend/package*.json /app/

# 나머지 소스 파일을 복사 (이 부분을 npm install 전에 실행)
COPY ./frontend/ /app/

# 의존성 설치
RUN npm install

# 빌드
RUN npm run build

# 2. Serve stage using Nginx
FROM nginx:latest

# Vite로 빌드된 파일을 Nginx 서버로 복사
COPY --from=build /app/dist /usr/share/nginx/html

# 소스 코드를 /app 디렉토리로 복사하여 보존
COPY --from=build /app /app

# Nginx 실행
CMD ["nginx", "-g", "daemon off;"]
