# Reto Técnico – Luis Miguel Gamarra

## Descripción
Este proyecto implementa una API REST basada en arquitectura de microservicios utilizando **Spring Boot 3, WebFlux y comunicación HTTP reactiva**.

El sistema está compuesto por **dos servicios independientes**:

| Servicio | Puerto | Responsabilidad |
|--------|------|------|
| ms-catalog | 8081 | Gestión de productos |
| ms-pricing | 8082 | Proveer precios de productos por SKU |

El servicio `ms-catalog` consulta a `ms-pricing` mediante **WebClient reactivo** para obtener el precio de un producto.

---

## Tecnologías utilizadas
- Java 17
- Spring Boot 3
- Spring WebFlux
- Spring Data JPA
- H2 Database (en memoria)
- Maven
- Project Reactor (Mono / Flux)

---

## Cómo ejecutar

### 1. Levantar ms-pricing
cd ms-pricing
mvn clean spring-boot:run

El servicio quedará disponible en:
http://localhost:8082

Verificar:
http://localhost:8082/actuator/health

### 2. Levantar ms-catalog
En otra consola:
cd ms-catalog
mvn clean spring-boot:run

Disponible en:
http://localhost:8081

##Base de datos (H2)
Consola:
http://localhost:8081/h2-console

Configuración:
| Campo    | Valor              |
| -------- | ------------------ |
| JDBC URL | jdbc:h2:mem:testdb |
| User     | sa                 |
| Password | (vacío)            |

##Endpoints principales
###Crear producto
POST http://localhost:8081/api/products
Body:
{
  "sku": "KEY-001",
  "name": "Keyboard",
  "description": "Mechanical keyboard",
  "active": true
}

###Obtener producto
GET http://localhost:8081/api/products/sku/KEY-001

###Obtener producto CON PRECIO (endpoint principal del reto)
GET http://localhost:8081/api/products/sku/KEY-001/with-price
{
  "id": 1,
  "sku": "KEY-001",
  "name": "Keyboard",
  "description": "Mechanical keyboard",
  "active": true,
  "price": 150.00
}
Este endpoint consulta al microservicio ms-pricing usando WebClient reactivo

###Endpoint de precios directo
GET http://localhost:8082/api/prices/KEY-001
{
  "sku": "KEY-001",
  "price": 150.00
}

##Manejo de errores
La API implementa manejo global de excepciones:
| Código | Descripción                     |
| ------ | ------------------------------- |
| 400    | Request inválido                |
| 404    | Producto o precio no encontrado |
| 500    | Error interno del servidor      |

##Consideraciones de arquitectura
ms-catalog es reactivo (WebFlux).
La base de datos usa JPA (bloqueante), por lo que se ejecuta en boundedElastic() para no bloquear el event loop.
Comunicación entre servicios mediante WebClient (no RestTemplate).
Uso de DTOs para desacoplar entidades de la API.

#Autor
Luis Miguel Gamarra