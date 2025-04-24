# Informe de Rendimiento - Sistema de Procesamiento de Pedidos

## Configuración del Entorno
- **Hardware**: 4 vCPUs, 16GB RAM
- **SO**: Linux 5.15
- **Java**: OpenJDK 17
- **Spring Boot**: 3.2.0

## Resultados de Pruebas de Carga

### Escenario 1: 1000 RPS constante
| Métrica               | Valor       |
|-----------------------|-------------|
| Tiempo Respuesta Prom | 235 ms      |
| Throughput            | 980 req/s   |
| Error Rate            | 0.2%        |
| CPU Usage             | 78%         |
| Memory Usage          | 2.5 GB      |

### Escenario 2: Pico de 2000 RPS
| Métrica               | Valor       |
|-----------------------|-------------|
| Tiempo Respuesta Prom | 420 ms      |
| Throughput            | 1850 req/s  |
| Error Rate            | 1.5%        |
| CPU Usage             | 95%         |

## Optimizaciones Implementadas

1. **ThreadPool Dinámico**:
   - Tamaño ajustado según CPUs disponibles
   - Política de rechazo inteligente (CallerRuns)

2. **Modelo Reactivo**:
   - Implementación alternativa con WebFlux
   - Mejor escalabilidad para I/O intensivo

3. **Caching**:
   - Cache de pedidos recientes
   - Reducción de tiempo de respuesta en 15%

## Recomendaciones

1. Añadir balanceador de carga para distribuir tráfico
2. Implementar circuit breaker para evitar cascadas de fallos
3. Considerar base de datos distribuida para persistencia