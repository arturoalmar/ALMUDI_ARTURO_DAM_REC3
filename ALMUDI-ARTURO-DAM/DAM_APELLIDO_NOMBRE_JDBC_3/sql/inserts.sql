-- =========================================
-- AUTOR: ARTURO ALMUDI MARCO
-- GRUPO: DAM3
-- EXAMEN JDBC AWS RDS
-- FECHA: 08/06/2026
-- =========================================

INSERT INTO socs (nombre, pais, nivel_seguridad, autor_examen) VALUES
('SOC Madrid',    'ESPAÑA',   'ALTO',  'Arturo_Almudi_Marco_DAM3'),
('SOC Barcelona', 'ESPAÑA',   'MEDIO', 'Arturo_Almudi_Marco_DAM3'),
('SOC Valencia',  'ESPAÑA',   'ALTO',  'Arturo_Almudi_Marco_DAM3'),
('SOC Paris',     'FRANCIA',  'MEDIO', 'Arturo_Almudi_Marco_DAM3'),
('SOC Berlin',    'ALEMANIA', 'BAJO',  'Arturo_Almudi_Marco_DAM3');

INSERT INTO incidentes (codigo_incidente, tipo_incidente, fecha_deteccion, estado, fk_soc_id, autor_examen) VALUES
('INC-2026-001', 'HACKEO', '15/01/2026', 'EN_PROCESO', 1, 'Arturo_Almudi_Marco_DAM3'),
('INC-2026-002', 'HACKEO', '20/02/2026', 'CERRADO',    2, 'Arturo_Almudi_Marco_DAM3'),
('INC-2026-003', 'APAGON', '05/03/2026', 'EN_PROCESO', 3, 'Arturo_Almudi_Marco_DAM3'),
('INC-2026-004', 'HACKEO', '10/04/2026', 'PENDIENTE',  4, 'Arturo_Almudi_Marco_DAM3'),
('INC-2026-005', 'HACKEO', '28/05/2026', 'EN_PROCESO', 5, 'Arturo_Almudi_Marco_DAM3');

INSERT INTO informes_incidentes (malware_detectado, nivel_severidad, conclusion, fk_incidente_id, autor_examen) VALUES
(TRUE,  95, 'Sistema comprometido. Acceso no autorizado detectado.',        1, 'Arturo_Almudi_Marco_DAM3'),
(FALSE, 45, 'Sin malware. Fallo de configuracion en el firewall.',          2, 'Arturo_Almudi_Marco_DAM3'),
(TRUE,  92, 'Malware detectado. Corte de suministro electrico inducido.',   3, 'Arturo_Almudi_Marco_DAM3'),
(TRUE,  75, 'Ransomware parcial. Datos cifrados en servidores secundarios.', 4, 'Arturo_Almudi_Marco_DAM3'),
(FALSE, 30, 'Sin incidencia grave. Acceso sospechoso registrado.',          5, 'Arturo_Almudi_Marco_DAM3');
