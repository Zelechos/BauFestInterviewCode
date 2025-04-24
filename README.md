# Interview Code

## Explicación de Decisiones de Diseño

1. **Concurrencia**: Usamos `CompletableFuture` para manejar operaciones asíncronas, lo que permite un mejor aprovechamiento de los recursos del sistema.

2. **Thread Pool**: Configuramos un ThreadPool personalizado para controlar exactamente cuántos hilos se usan para procesar pedidos, evitando sobrecargar el sistema.

3. **Estructuras de Datos Concurrentes**: Usamos `ConcurrentHashMap` para almacenar pedidos procesados, que es thread-safe y ofrece buen rendimiento en escenarios de alta concurrencia.

4. **Resiliencia**: Implementamos manejo de errores adecuado y reinicio de la bandera de interrupción para evitar problemas con los hilos.

5. **Observabilidad**: Agregamos métricas y logs para monitorear el rendimiento del sistema en producción.

6. **Pruebas**: Incluimos pruebas unitarias y de integración que validan tanto la funcionalidad como el comportamiento bajo carga.

## Optimizaciones Adicionales

Para mejorar aún más el rendimiento, podríamos considerar:

1. **Patrón Reactor**: Implementar WebFlux para un modelo completamente reactivo.
2. **Caching**: Añadir Redis para cachear pedidos frecuentes.
3. **Particionamiento**: Dividir el procesamiento por tipo de cliente o categoría de producto.
4. **Batch Processing**: Procesar pedidos en lotes para operaciones que lo permitan.

## Estructura del Proyecto Order Processing

```
order-processing/
├── .mvn/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── order/
│   │   │               └── processing/
│   │   │                   ├── config/
│   │   │                   │   ├── AsyncConfig.java
│   │   │                   │   └── MetricsConfig.java
│   │   │                   ├── controller/
│   │   │                   │   └── OrderController.java
│   │   │                   ├── model/
│   │   │                   │   ├── Order.java
│   │   │                   │   └── OrderItem.java
│   │   │                   ├── service/
│   │   │                   │   ├── OrderProcessingService.java
│   │   │                   │   └── ReactiveOrderProcessingService.java
│   │   │                   └── OrderProcessingApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       ├── templates/
│   │       └── application.yml
│   └── test/
│       ├── gatling/
│       └── java/
│           └── com/
│               └── example/
│                   └── order/
│                       └── processing/
│                           ├── config/
│                           │   ├── AsyncConfigTest.java
│                           │   └── MetricsConfigTest.java
│                           ├── controller/
│                           │   └── OrderControllerTest.java
│                           ├── model/
│                           │   ├── OrderItemTest.java
│                           │   └── OrderTest.java
│                           ├── service/
│                           │   ├── OrderProcessingServiceTest.java
│                           │   └── ReactiveOrderProcessingServiceTest.java
│                           ├── ConcurrencyTest.java
│                           ├── IntegrationTest.java
│                           └── OrderProcessingApplicationTests.java
└── pom.xml
```

## Explicación de la Estructura

1. **Configuración principal**:
   - `AsyncConfig.java`: Configuración del pool de hilos para procesamiento asíncrono
   - `MetricsConfig.java`: Configuración de métricas y monitoreo

2. **Controladores**:
   - `OrderController.java`: Endpoints REST para el procesamiento de pedidos

3. **Modelos**:
   - `Order.java`: Entidad principal para pedidos
   - `OrderItem.java`: Elementos individuales de un pedido

4. **Servicios**:
   - `OrderProcessingService.java`: Lógica de negocio para procesamiento tradicional
   - `ReactiveOrderProcessingService.java`: Implementación reactiva del servicio

5. **Pruebas**:
   - Pruebas unitarias para cada componente
   - Pruebas de integración
   - Pruebas de concurrencia
   - Pruebas de carga con Gatling

6. **Configuración**:
   - `application.yml`: Configuración de la aplicación Spring Boot

Esta estructura sigue las mejores prácticas de Spring Boot y Maven, separando claramente el código de producción de las pruebas, y organizando los componentes por responsabilidades.

## Reporte de Covertura de tests 

---

### ✅ 1. Agregar JaCoCo a tu proyecto Maven

En el `pom.xml`, agregá este plugin dentro de `<build><plugins>`:

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version> <!-- Usa la última versión -->
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>verify</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

---

### ✅ 2. Ejecutar los tests y generar el reporte

Corré lo siguiente en la terminal:

```bash
mvn clean verify
```

Esto hace dos cosas:
- Ejecuta todos los tests (`mvn test`)
- Genera el reporte de cobertura con JaCoCo

---

### ✅ 3. Ver el reporte de cobertura

Una vez que se genera el reporte, lo vas a encontrar en:

```
target/site/jacoco/index.html
```

📂 Abrí ese archivo en tu navegador y vas a ver un **informe visual** con:
- Cobertura de clases, métodos, líneas y ramas.
- Colores para ver qué líneas se ejecutaron y cuáles no.
- Links navegables por paquete/clase.

---

![image](https://github.com/user-attachments/assets/d9f3f0d4-4793-4869-b6f1-74caa13f85c2)

