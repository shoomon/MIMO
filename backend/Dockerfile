# Base Image
FROM openjdk:17-jdk-slim

# 필수 패키지 설치
RUN apt-get update && apt-get install -y curl unzip bash zip && rm -rf /var/lib/apt/lists/*

# SDKMAN 설치
RUN curl -s https://get.sdkman.io | bash

# sdkman 초기화 스크립트를 실행할 수 있도록 bashrc에 추가
RUN echo "source /root/.sdkman/bin/sdkman-init.sh" >> /root/.bashrc

# SDKMAN 초기화 후 Gradle 설치
RUN bash -c "source /root/.sdkman/bin/sdkman-init.sh && sdk install gradle 8.12.1"

# 환경변수 설정
ENV SDKMAN_DIR="/root/.sdkman" \
    GRADLE_HOME="/root/.sdkman/candidates/gradle/8.12.1" \
    GRADLE_USER_HOME="/root/.gradle" \
    PATH="/root/.sdkman/candidates/gradle/8.12.1/bin:${PATH}" \
    GOOGLE_TOKEN_URI=${GOOGLE_TOKEN_URI} \
    GOOGLE_RESOURCE_URI=${GOOGLE_RESOURCE_URI} \
    GOOGLE_CLIENT_ID=${GOOGLE_CLIENT_ID} \
    GOOGLE_CLIENT_SECRET=${GOOGLE_CLIENT_SECRET} \
    GOOGLE_REDIRECT_URI=${GOOGLE_REDIRECT_URI} \
    SECRET_KEY=${SECRET_KEY} \
    ACCESS_TOKEN_EXPIRY=${ACCESS_TOKEN_EXPIRY} \
    REFRESH_TOKEN_EXPIRY=${REFRESH_TOKEN_EXPIRY} \
    DB_URL=${DB_URL} \
    DB_ID=${DB_ID} \
    DB_PASSWORD=${DB_PASSWORD} \
    REDIS_URI=${REDIS_URI} \
    REDIS_PORT=${REDIS_PORT} \
    S3_BUCKET_NAME=${S3_BUCKET_NAME} \
    S3_ACCESS_KEY=${S3_ACCESS_KEY} \
    S3_SECRET_KEY=${S3_SECRET_KEY}

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 관련 파일을 복사
COPY ./build.gradle ./settings.gradle /app/

# 나머지 소스 파일을 복사
COPY ./ /app/

# Gradle 빌드 실행
RUN bash -c "source /root/.sdkman/bin/sdkman-init.sh && gradle build --no-daemon --refresh-dependencies --no-watch-fs -x test"

# 빌드된 JAR 파일이 있는지 확인하고 출력
RUN echo "Listing JAR files in /app/build/libs:" && ls -l /app/build/libs/

# 애플리케이션 실행
CMD ["java", "-jar", "/app/build/libs/backend-0.0.1-SNAPSHOT.jar"]
