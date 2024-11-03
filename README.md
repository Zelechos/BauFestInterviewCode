# EldarInterviewCode

Este repositorio contiene la solución a un desafío técnico de backend en Java solicitado por el equipo de ELDAR. El objetivo de este proyecto es demostrar habilidades en el desarrollo de sistemas para manejar operaciones de tarjetas de crédito, implementación de APIs y otras tareas relacionadas con procesamiento de datos y patrones de diseño.

## Estructura del Proyecto

La estructura de carpetas del repositorio está organizada de la siguiente manera:

- **Ejercicio 1**: Contiene la solución para el primer ejercicio, que consiste en desarrollar un menú interactivo por consola. Este menú permite al usuario registrar personas y tarjetas de crédito, consultar información de tarjetas asociadas y verificar tasas de interés de las marcas disponibles.

- **Ejercicio 2/ejercicio2**: Contiene la implementación del segundo ejercicio, que es una API REST para manejar operaciones con tarjetas de crédito. La API permite el alta de usuarios y tarjetas, consulta de tasas y procesamiento de compras. Se envían notificaciones por email al usuario para ciertas acciones sensibles.

- **Ejercicio 3**: En esta carpeta se encuentra la respuesta al tercer ejercicio, que analiza un problema de ancho de banda en un sistema de monitoreo de cámaras de seguridad. El objetivo es reducir el ancho de banda, actualizando la imagen solo cuando haya cambios. Aquí se detalla el patrón de diseño elegido para resolver esta situación.

- **Ejercicio 4**: Contiene la solución al cuarto ejercicio, donde se implementa una lógica para identificar posibles fraudes bancarios basándose en los gastos diarios de un cliente y comparándolos con el promedio de un número de días anteriores.

- **README.md**: Archivo de documentación inicial del repositorio.

## Ejercicios

### Ejercicio 1
Desarrollo de un menú interactivo en consola que permite:
- Registrar personas (nombre, apellido, DNI, fecha de nacimiento, email).
- Registrar tarjetas de crédito y consultar información de tarjetas por DNI.
- Consultar tasas de todas las marcas de tarjetas disponibles (VISA, NARA, AMEX).

### Ejercicio 2
Implementación de una API REST que permite:
- Registrar usuarios y tarjetas, incluyendo notificaciones por email para ciertos datos sensibles (CVV y PAN).
- Consultar tasas de operación según la marca y realizar compras utilizando una tarjeta y CVV.

### Ejercicio 3
Respuesta teórica a un problema de ancho de banda en un sistema de monitoreo de cámaras, explicando el uso de un patrón de diseño que permite actualizar la imagen solo cuando hay cambios.

### Ejercicio 4
Algoritmo para detectar posibles fraudes bancarios, enviando notificaciones cuando el gasto diario excede el doble del gasto medio de los días previos.

## Instrucciones para Ejecutar
1. Clonar el repositorio.
2. Seguir las instrucciones en cada carpeta de ejercicio para compilar y ejecutar las soluciones.

## Requerimientos
- JDK 11 o superior
- Dependencias para realizar pruebas (JUnit, Mockito)
