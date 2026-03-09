# Logger

Librería Java sencilla para generar logs en memoria, imprimirlos por consola y persistirlos en ficheros de texto.

> Estado real del proyecto: funciona como utilidad básica, pero es una implementación **legacy** (Java 8, API estática global y test muy mínimo).

## Qué hace

- Activa o desactiva el registro de logs (`On` / `Off`).
- Genera entradas con severidad (`INFO`, `WARNING`, `SEVERE`, etc.).
- Soporta escritura de mensajes individuales o colecciones de mensajes.
- Acumula logs en memoria y permite volcarlos a texto (`toText()`).
- Persiste logs en archivos dentro del directorio `Logs/`.

## Estructura del proyecto

```text
.
├── pom.xml
├── src/main/java/es/enpici/logger/
│   ├── Logger.java      # Fachada de uso para cliente
│   ├── Log.java         # Núcleo estático de almacenamiento y persistencia
│   └── Severity.java    # Enum de niveles
└── src/test/java/es/enpici/logger/
    └── LogTest.java     # Test de ejemplo (actualmente trivial)
```

## Requisitos

- JDK 8+
- Maven 3.x

## Compilar

```bash
mvn clean package
```

## Ejecutar tests

```bash
mvn test
```

## Uso rápido

```java
import es.enpici.logger.Logger;
import es.enpici.logger.Severity;

public class App {
    public static void main(String[] args) {
        Logger logger = new Logger(App.class.getSimpleName());

        logger.On();

        logger.info("Inicio de aplicación");
        logger.warning("Esto es una advertencia");
        logger.log(Severity.SEVERE, "Esto es un error severo");

        // Volcado a fichero
        logger.WriteFile();

        logger.Off();
    }
}
```

## Comportamiento de persistencia

- Si no existe el directorio `Logs`, intenta crearlo.
- Genera archivos secuenciales con nombre `1Log.txt`, `2Log.txt`, etc.
- Si el buffer supera 1000 entradas, activa escritura en modo continuo.

## Limitaciones actuales (importante)

- **Estado global estático**: `Log` usa miembros estáticos; no está aislado por instancia.
- **No es thread-safe**: no hay sincronización para acceso concurrente.
- **API no idiomática Java moderna**: métodos públicos en PascalCase (`On`, `Off`, `WriteFile`, etc.).
- **Tests insuficientes**: el test actual no valida comportamiento real.
- **Configuración Maven legacy**: Java 8 y dependencia de JUnit 3.8.1.

## Mejoras recomendadas

1. Migrar a Java 17+ y JUnit 5.
2. Añadir tests unitarios reales para:
   - activación/desactivación,
   - formato de mensajes,
   - persistencia a fichero,
   - manejo de errores IO.
3. Reemplazar estado estático por instancias inmutables o un servicio inyectable.
4. Hacer la implementación thread-safe (`synchronized`/locks o estructuras concurrentes).
5. Normalizar naming Java (`on/off/writeFile/restart`).

## Licencia

No se ha definido licencia en este repositorio. Si vas a publicarlo o reutilizarlo, añade una (por ejemplo, MIT o Apache-2.0).
