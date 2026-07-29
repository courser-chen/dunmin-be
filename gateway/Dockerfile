# ==========================================
# 运行阶段 (Runner)
# ==========================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# 从 Jenkins 编译好的 target 目录复制 jar 包
COPY target/*.jar app.jar

# 暴露端口 (根据你的 application.yml 配置修改)
EXPOSE 8080

# 【K8s 最佳实践】
# 不写死 -Xmx，而是告诉 JVM 最多使用容器内存限制的 75%
# 这样无论 K8s 分配 512M 还是 2G，JVM 都能完美适配，且不会 OOMKilled
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=85.0 -Djava.security.egd=file:/dev/./urandom"

# 启动命令
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]