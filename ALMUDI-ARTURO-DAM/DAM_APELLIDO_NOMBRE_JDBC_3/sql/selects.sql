-- =========================================
-- AUTOR: ARTURO ALMUDI MARCO
-- GRUPO: DAM3
-- EXAMEN JDBC AWS RDS
-- FECHA: 08/06/2026
-- =========================================

-- =============================================
-- CONSULTAS DAO — IncidenteDAOImpl
-- =============================================

-- SQL_FIND_ALL: todos los incidentes con su soc (INNER JOIN + aliases)
SELECT incidentes.id AS incidente_id, incidentes.codigo_incidente, incidentes.tipo_incidente,
       incidentes.fecha_deteccion, incidentes.estado, incidentes.autor_examen AS incidente_autor,
       socs.id AS soc_id, socs.nombre AS soc_nombre, socs.pais, socs.nivel_seguridad, socs.autor_examen AS soc_autor
FROM incidentes
INNER JOIN socs ON incidentes.fk_soc_id = socs.id
ORDER BY incidentes.id;

-- SQL_FIND: incidente por id con su soc (INNER JOIN + aliases)
SELECT incidentes.id AS incidente_id, incidentes.codigo_incidente, incidentes.tipo_incidente,
       incidentes.fecha_deteccion, incidentes.estado, incidentes.autor_examen AS incidente_autor,
       socs.id AS soc_id, socs.nombre AS soc_nombre, socs.pais, socs.nivel_seguridad, socs.autor_examen AS soc_autor
FROM incidentes
INNER JOIN socs ON incidentes.fk_soc_id = socs.id
WHERE incidentes.id = 1;

-- SQL_FIND_BY_SOC: incidentes filtrados por soc (INNER JOIN + aliases)
SELECT incidentes.id AS incidente_id, incidentes.codigo_incidente, incidentes.tipo_incidente,
       incidentes.fecha_deteccion, incidentes.estado, incidentes.autor_examen AS incidente_autor,
       socs.id AS soc_id, socs.nombre AS soc_nombre, socs.pais, socs.nivel_seguridad, socs.autor_examen AS soc_autor
FROM incidentes
INNER JOIN socs ON incidentes.fk_soc_id = socs.id
WHERE incidentes.fk_soc_id = 1;

-- SQL_FIND_WITH_INFORME: incidente con soc e informe (INNER JOIN triple + aliases)
SELECT incidentes.id AS incidente_id, incidentes.codigo_incidente, incidentes.tipo_incidente,
       incidentes.fecha_deteccion, incidentes.estado, incidentes.autor_examen AS incidente_autor,
       socs.id AS soc_id, socs.nombre AS soc_nombre, socs.pais, socs.nivel_seguridad, socs.autor_examen AS soc_autor,
       informes_incidentes.id AS informe_id, informes_incidentes.malware_detectado,
       informes_incidentes.nivel_severidad, informes_incidentes.conclusion, informes_incidentes.autor_examen AS informe_autor
FROM incidentes
INNER JOIN socs ON incidentes.fk_soc_id = socs.id
INNER JOIN informes_incidentes ON incidentes.id = informes_incidentes.fk_incidente_id
WHERE incidentes.id = 1;

-- SQL_INSERT
INSERT INTO incidentes (codigo_incidente, tipo_incidente, fecha_deteccion, estado, fk_soc_id, autor_examen)
VALUES ('INC-2026-006', 'HACKEO', '15/01/2026', 'EN_PROCESO', 1, 'Arturo_Almudi_Marco_DAM3');

-- SQL_UPDATE
UPDATE incidentes
SET codigo_incidente = 'INC-2026-006', tipo_incidente = 'HACKEO', fecha_deteccion = '15/01/2026',
    estado = 'CERRADO', fk_soc_id = 1, autor_examen = 'Arturo_Almudi_Marco_DAM3'
WHERE id = 1;

-- SQL_DELETE
DELETE FROM incidentes WHERE id = 1;


-- =============================================
-- CONSULTAS DAO — SocDAOImpl
-- =============================================

-- SQL_FIND_ALL
SELECT * FROM socs ORDER BY id;

-- SQL_FIND
SELECT * FROM socs WHERE id = 1;

-- SQL_INSERT
INSERT INTO socs (nombre, pais, nivel_seguridad, autor_examen)
VALUES ('SOC Nuevo', 'ESPAÑA', 'ALTO', 'Arturo_Almudi_Marco_DAM3');

-- SQL_UPDATE
UPDATE socs
SET nombre = 'SOC Madrid Actualizado', pais = 'ESPAÑA', nivel_seguridad = 'MEDIO', autor_examen = 'Arturo_Almudi_Marco_DAM3'
WHERE id = 1;

-- SQL_DELETE
DELETE FROM socs WHERE id = 1;


-- =============================================
-- CONSULTAS DAO — InformeIncidenteDAOImpl
-- =============================================

-- SQL_FIND_ALL
SELECT * FROM informes_incidentes ORDER BY id;

-- SQL_FIND
SELECT * FROM informes_incidentes WHERE id = 1;

-- SQL_INSERT
INSERT INTO informes_incidentes (malware_detectado, nivel_severidad, conclusion, fk_incidente_id, autor_examen)
VALUES (TRUE, 95, 'Sistema comprometido.', 1, 'Arturo_Almudi_Marco_DAM3');

-- SQL_UPDATE (completo)
UPDATE informes_incidentes
SET malware_detectado = TRUE, nivel_severidad = 95, conclusion = 'Sistema comprometido.', autor_examen = 'Arturo_Almudi_Marco_DAM3'
WHERE id = 1;

-- SQL_DELETE
DELETE FROM informes_incidentes WHERE id = 1;


-- =============================================
-- BLOQUE 6 — BONUS_QUERY_ADVANCED: INCIDENTES_CRITICOS
-- =============================================
-- Devuelve incidentes con malware detectado, nivel_severidad > 90 y soc en ESPANA.
-- Usa INNER JOIN triple: incidentes + socs + informes_incidentes.
-- Cada objeto Incidente devuelto contiene Soc e InformeIncidente relacionados.
SELECT incidentes.id AS incidente_id, incidentes.codigo_incidente, incidentes.tipo_incidente,
       incidentes.fecha_deteccion, incidentes.estado, incidentes.autor_examen AS incidente_autor,
       socs.id AS soc_id, socs.nombre AS soc_nombre, socs.pais, socs.nivel_seguridad, socs.autor_examen AS soc_autor,
       informes_incidentes.id AS informe_id, informes_incidentes.malware_detectado,
       informes_incidentes.nivel_severidad, informes_incidentes.conclusion, informes_incidentes.autor_examen AS informe_autor
FROM incidentes
INNER JOIN socs ON incidentes.fk_soc_id = socs.id
INNER JOIN informes_incidentes ON incidentes.id = informes_incidentes.fk_incidente_id
WHERE informes_incidentes.malware_detectado = TRUE
  AND informes_incidentes.nivel_severidad > 90
  AND socs.pais = 'ESPAÑA';


-- =============================================
-- BLOQUE 7 — DYNAMIC_UPDATE_ENGINE
-- =============================================
-- El sistema construye el SET dinamicamente en AbstractDAO.
-- Solo se incluyen en el UPDATE los campos que se pasen (no null).
-- Se aplica sobre la tabla informes_incidentes.

-- Ejemplo 1: actualizar nivel_severidad y conclusion (malware_detectado no se toca)
UPDATE informes_incidentes
SET nivel_severidad = 99, conclusion = 'ACTUALIZADO_DINAMICAMENTE'
WHERE id = 1;

-- Ejemplo 2: actualizar solo malware_detectado
UPDATE informes_incidentes
SET malware_detectado = TRUE
WHERE id = 2;

-- Ejemplo 3: actualizar solo conclusion
UPDATE informes_incidentes
SET conclusion = 'REVISION COMPLETADA'
WHERE id = 3;
