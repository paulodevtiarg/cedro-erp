FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build

# Copiar tudo
COPY CedroErp2026/ .

# DEBUG 1: Listar arquivos fonte
RUN echo "=== CONTEÚDO DO SRC MAIN ===" && \
    ls -la src/main/resources/templates/ && \
    echo "=== ARVORE COMPLETA DE TEMPLATES ===" && \
    find src/main/resources -type f -name "*.html" | sort

# Build com verbose
RUN mvn clean package -DskipTests -X

# DEBUG 2: Listar o que foi para o target
RUN echo "=== CONTEÚDO DO TARGET/CLASSES ===" && \
    ls -la target/classes/templates/ && \
    echo "=== TEMPLATES NO TARGET ===" && \
    find target/classes -type f -name "*.html" | sort

# DEBUG 3: Listar conteúdo do JAR
RUN echo "=== CONTEÚDO DO JAR ===" && \
    jar tf target/*.jar | grep -E "(templates|html)" || echo "NENHUM TEMPLATE ENCONTRADO NO JAR!"

FROM eclipse-temurin:21-jre-jammy

# DEBUG 4: Copiar e verificar
COPY --from=build /build/target/*.jar /app.jar

RUN echo "=== VERIFICANDO JAR FINAL ===" && \
    jar tf /app.jar | grep -E "(templates|html)" || echo "NENHUM TEMPLATE NO JAR FINAL!"

EXPOSE 8080

# Comando com debug de classpath
ENTRYPOINT ["sh", "-c", "echo '=== CLASSPATH ===' && ls -la && java -jar /app.jar"]