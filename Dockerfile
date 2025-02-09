# OpenJDK를 사용하여 Gradle 환경 설정
FROM openjdk:11-jdk-slim

# Gradle 설치
RUN apt-get update && apt-get install -y curl unzip && \
    curl -s https://get.sdkman.io | bash && \
    source "$HOME/.sdkman/bin/sdkman-init.sh" && \
    sdk install gradle 7.3

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 의존성 파일을 복사하고 빌드
COPY backend/build.gradle /app
COPY backend/settings.gradle /app
RUN gradle build --no-daemon

# 나머지 소스 파일 복사
COPY backend/ .

# 백엔드 실행
CMD ["gradle", "bootRun"]
