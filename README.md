# Reto Técnico - Luis Miguel Gamarra

## Requisitos
- Java 21
- Maven 3.9+

## Cómo ejecutar
mvn clean spring-boot:run

## Endpoints
- Health: http://localhost:8080/health
- H2 Console: http://localhost:8080/h2-console
  JDBC URL: jdbc:h2:mem:testdb
  user: sa
  password: (vacío)

## Notas
Proyecto base con manejo global de errores (400/404/500) y base de datos H2 en memoria.
