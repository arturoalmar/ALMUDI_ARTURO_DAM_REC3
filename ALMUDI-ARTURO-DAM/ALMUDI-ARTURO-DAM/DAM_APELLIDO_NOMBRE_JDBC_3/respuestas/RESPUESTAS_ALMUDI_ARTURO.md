# RESPUESTAS EXAMEN JDBC AWS RDS
**Alumno:** Arturo Almudi Marco
**Grupo:** DAM3
**Fecha:** 08/06/2026

---

## PREGUNTA 1
**Explica cómo funciona la relación 1:N entre Soc e Incidente tanto en SQL como en Java.**

### En SQL

La relación 1:N se implementa añadiendo una clave foránea en la tabla del lado "muchos". En este caso, la tabla INCIDENTES tiene la columna `fk_soc_id` que guarda el `id` del SOC al que pertenece cada incidente. Un SOC puede tener muchos incidentes pero cada incidente pertenece a un único SOC.

La integridad referencial la garantiza la base de datos: si intentas insertar un incidente con un `fk_soc_id` que no existe en SOCS, PostgreSQL lanza un error y rechaza la inserción.

```sql
CREATE TABLE incidentes (
    id               SERIAL,
    ...
    fk_soc_id        INTEGER NOT NULL,
    CONSTRAINT FK_fk_soc_id FOREIGN KEY (fk_soc_id) REFERENCES socs(id)
);
```

Para recuperar los datos relacionados en una sola consulta se usa INNER JOIN:

```sql
SELECT incidentes.id AS incidente_id, ..., socs.id AS soc_id, socs.nombre AS soc_nombre, ...
FROM incidentes
INNER JOIN socs ON incidentes.fk_soc_id = socs.id;
```

### En Java

En Java la relación 1:N se representa haciendo que la clase `Incidente` contenga un atributo de tipo `Soc`:

```java
private Soc soc;
```

El DAO popula ese objeto `Soc` en el mismo momento en que mapea el `Incidente`, usando los datos que devuelve el INNER JOIN. De este modo el objeto `Incidente` ya viene completamente relacionado con su `Soc`, sin necesidad de hacer una segunda consulta.

---

## PREGUNTA 2
**Explica por qué en Java utilizamos `private Soc soc;` y no `private int socId;`**

Porque en orientación a objetos trabajamos con objetos, no con identificadores sueltos.

Si usáramos `private int socId` tendríamos que hacer una segunda consulta a la base de datos cada vez que necesitáramos acceder a cualquier dato del SOC (nombre, país, nivel de seguridad). Esto va en contra del principio de encapsulación y obliga a la capa de negocio a conocer detalles internos de la base de datos.

Usando `private Soc soc` el objeto `Incidente` ya contiene toda la información del SOC relacionado. El DAO lo resuelve con un INNER JOIN en una única consulta SQL, y el resultado es un objeto Java completamente relacionado y listo para usar.

Además, el enunciado lo indica explícitamente:
- **NO válido:** `private int socId;`
- **Válido:** `private Soc soc;`

Lo mismo aplica para la relación 1:1 entre `Incidente` e `InformeIncidente`:
- **NO válido:** `private int informeId;`
- **Válido:** `private InformeIncidente informe;`

---

## PREGUNTA 3
**Explica qué ventaja aporta PreparedStatement frente a concatenar SQL manualmente.**

### Seguridad — prevención de SQL Injection

Con concatenación manual un usuario malicioso podría inyectar código SQL y alterar la consulta:

```java
// PELIGROSO — concatenación manual
String sql = "SELECT * FROM socs WHERE nombre = '" + nombre + "'";
// Si nombre = "' OR '1'='1" → devuelve TODOS los registros
```

Con `PreparedStatement` los valores se pasan como parámetros, nunca se interpretan como SQL:

```java
// SEGURO — PreparedStatement
motorSQL.prepare("SELECT * FROM socs WHERE nombre = ?");
motorSQL.getPs().setString(1, nombre);
// El valor se escapa automáticamente — no hay riesgo de inyección
```

### Rendimiento

El motor de base de datos precompila la sentencia SQL una sola vez. Si se reutiliza la misma consulta con distintos valores, PostgreSQL no necesita volver a parsear ni planificar la ejecución, lo que mejora el rendimiento.

### Claridad del código

El uso de `?` como marcador de posición hace el código más limpio y fácil de leer que construir cadenas SQL concatenadas.

---

## BLOQUE 6 — Justificación de INCIDENTES_CRITICOS

La consulta `INCIDENTES_CRITICOS` utiliza un **INNER JOIN triple** entre tres tablas:
- `incidentes` INNER JOIN `socs` → para filtrar por país del SOC
- `incidentes` INNER JOIN `informes_incidentes` → para filtrar por malware y severidad

Se usa INNER JOIN (no LEFT JOIN) porque la consulta solo tiene sentido cuando el incidente tiene tanto un SOC asignado como un informe registrado. Si alguna de las dos relaciones no existe, el incidente no debe aparecer en los resultados.

El método `findIncidentesCriticos()` vive **únicamente en `IncidenteDAOImpl`** porque la consulta solo tiene sentido para la entidad Incidente. No pertenece ni a `AbstractDAO` ni a la interfaz `DAO<T>`.

---

## BLOQUE 8 — Justificación de ABSTRACTDAO_ARCHITECTURE_REFACTOR

### Análisis crítico de AbstractDAO

`AbstractDAO<T>` define los 5 métodos CRUD (`add`, `update`, `delete`, `find`, `findAll`) que son **comunes a todas las entidades**. Esto es correcto y necesario.

El problema aparece cuando se añaden métodos especializados. Si `findIncidentesCriticos()` viviera en `AbstractDAO<T>` o en la interfaz `DAO<T>`, estaríamos obligando a `SocDAOImpl` e `InformeIncidenteDAOImpl` a implementar un método que no tiene ningún sentido para ellos, violando el principio de responsabilidad única.

### Mejora arquitectónica implementada

Las consultas especializadas viven **únicamente en el DAO concreto** que las necesita:
- `findBySoc()`, `findWithInforme()`, `findIncidentesCriticos()` → solo en `IncidenteDAOImpl`
- `updateDynamicInforme()` → solo en `InformeIncidenteDAOImpl`

`AbstractDAO` contiene únicamente lógica verdaderamente común a todas las entidades:
- Los 5 métodos CRUD (vía la interfaz `DAO<T>`)
- `updateDynamic()` como motor genérico reutilizable (Bloque 7)
- `check()` para verificar la conexión
- `printError()` para logging de errores

Esta arquitectura respeta la jerarquía DAO trabajada en clase y no añade capas innecesarias.
