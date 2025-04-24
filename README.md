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
