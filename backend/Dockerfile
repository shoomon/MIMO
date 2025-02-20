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
    PATH="/root/.sdkman/candidates/gradle/8.12.1/bin:${PATH}"

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 관련 파일을 복사
COPY ./backend/build.gradle ./backend/settings.gradle /app/

# 나머지 소스 파일을 복사
COPY ./backend /app/

# Gradle 빌드 실행
RUN bash -c "source /root/.sdkman/bin/sdkman-init.sh && gradle build --no-daemon --refresh-dependencies --no-watch-fs -x test -x checkstyleMain -x checkstyleTest"

# 빌드된 JAR 파일이 있는지 확인하고 출력
RUN echo "Listing JAR files in /app/build/libs:" && ls -l /app/build/libs/

# 애플리케이션 실행
CMD ["java", "-jar", "/app/build/libs/backend-0.0.1-SNAPSHOT.jar"]
