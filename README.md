## Estructura del Proyecto

- `config/`: Configuraciones de Spring (seguridad, OpenAPI, etc.)
- `controller/`: Controladores REST
- `dto/`: Objetos de transferencia de datos (request/response)
- `exception/`: Excepciones personalizadas y manejadores
- `model/`: Entidades de persistencia
- `repository/`: Interfaces de Spring Data JPA
- `security/`: Configuración de seguridad y JWT
- `service/`: Lógica de negocio

## Configuración de Desarrollo

1. **Base de datos H2**: 
   - Accesible en `/h2-console`
   - JDBC URL: `jdbc:h2:mem:userdb`
   - User: `sa`
   - Password: `password`

2. **Variables de entorno**:
   - `JWT_SECRET`: Clave secreta para JWT (default: mySecretKey)
   - `JWT_EXPIRATION`: Tiempo de expiración en segundos (default: 3600)

3. **Perfiles**:
   - `dev`: Habilita H2 console y muestra SQL
   - `prod`: Configuración para producción

## API Documentation

La documentación interactiva está disponible en:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Ejemplos de Uso

### Registro de Usuario

```bash
curl -X POST "http://localhost:8080/api/v1/users/sign-up" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "ValidPass12",
    "phones": [
      {
        "number": 123456789,
        "cityCode": 1,
        "countryCode": "57"
      }
    ]
  }'
