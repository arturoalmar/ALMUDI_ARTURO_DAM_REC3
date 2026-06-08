/*
=========================================
AUTOR: ARTURO ALMUDI MARCO
GRUPO: DAM3
EXAMEN JDBC AWS RDS
FECHA: 08/06/2026
=========================================
*/

package examen.almudi.arturo.dao;

import examen.almudi.arturo.beans.Soc;
import examen.almudi.arturo.beans.InformeIncidente;
import examen.almudi.arturo.beans.Incidente;
import examen.almudi.arturo.motores.MotorSQL;

import java.sql.ResultSet;
import java.util.ArrayList;

public class IncidenteDAOImpl extends AbstractDAO<Incidente> {

    // INNER JOIN con socs — aliases para evitar ambiguedad de columnas duplicadas (id, autor_examen)
    private static final String SQL_FIND_ALL =
            "SELECT incidentes.id AS incidente_id, incidentes.codigo_incidente, incidentes.tipo_incidente, " +
                    "incidentes.fecha_deteccion, incidentes.estado, incidentes.autor_examen AS incidente_autor, " +
                    "socs.id AS soc_id, socs.nombre AS soc_nombre, socs.pais, socs.nivel_seguridad, socs.autor_examen AS soc_autor " +
                    "FROM incidentes " +
                    "INNER JOIN socs ON incidentes.fk_soc_id = socs.id " +
                    "ORDER BY incidentes.id";

    private static final String SQL_FIND =
            "SELECT incidentes.id AS incidente_id, incidentes.codigo_incidente, incidentes.tipo_incidente, " +
                    "incidentes.fecha_deteccion, incidentes.estado, incidentes.autor_examen AS incidente_autor, " +
                    "socs.id AS soc_id, socs.nombre AS soc_nombre, socs.pais, socs.nivel_seguridad, socs.autor_examen AS soc_autor " +
                    "FROM incidentes " +
                    "INNER JOIN socs ON incidentes.fk_soc_id = socs.id " +
                    "WHERE incidentes.id = ?";

    private static final String SQL_FIND_BY_SOC =
            "SELECT incidentes.id AS incidente_id, incidentes.codigo_incidente, incidentes.tipo_incidente, " +
                    "incidentes.fecha_deteccion, incidentes.estado, incidentes.autor_examen AS incidente_autor, " +
                    "socs.id AS soc_id, socs.nombre AS soc_nombre, socs.pais, socs.nivel_seguridad, socs.autor_examen AS soc_autor " +
                    "FROM incidentes " +
                    "INNER JOIN socs ON incidentes.fk_soc_id = socs.id " +
                    "WHERE incidentes.fk_soc_id = ?";

    // INNER JOIN triple: socs + informes — aliases para las tres tablas
    private static final String SQL_FIND_WITH_INFORME =
            "SELECT incidentes.id AS incidente_id, incidentes.codigo_incidente, incidentes.tipo_incidente, " +
                    "incidentes.fecha_deteccion, incidentes.estado, incidentes.autor_examen AS incidente_autor, " +
                    "socs.id AS soc_id, socs.nombre AS soc_nombre, socs.pais, socs.nivel_seguridad, socs.autor_examen AS soc_autor, " +
                    "informes_incidentes.id AS informe_id, informes_incidentes.malware_detectado, " +
                    "informes_incidentes.nivel_severidad, informes_incidentes.conclusion, informes_incidentes.autor_examen AS informe_autor " +
                    "FROM incidentes " +
                    "INNER JOIN socs ON incidentes.fk_soc_id = socs.id " +
                    "INNER JOIN informes_incidentes ON incidentes.id = informes_incidentes.fk_incidente_id " +
                    "WHERE incidentes.id = ?";

    // BLOQUE 6 — BONUS_QUERY_ADVANCED: INCIDENTES_CRITICOS
    // Devuelve incidentes con malware detectado, nivel_severidad > 90 y soc en ESPANA
    private static final String SQL_INCIDENTES_CRITICOS =
            "SELECT incidentes.id AS incidente_id, incidentes.codigo_incidente, incidentes.tipo_incidente, " +
                    "incidentes.fecha_deteccion, incidentes.estado, incidentes.autor_examen AS incidente_autor, " +
                    "socs.id AS soc_id, socs.nombre AS soc_nombre, socs.pais, socs.nivel_seguridad, socs.autor_examen AS soc_autor, " +
                    "informes_incidentes.id AS informe_id, informes_incidentes.malware_detectado, " +
                    "informes_incidentes.nivel_severidad, informes_incidentes.conclusion, informes_incidentes.autor_examen AS informe_autor " +
                    "FROM incidentes " +
                    "INNER JOIN socs ON incidentes.fk_soc_id = socs.id " +
                    "INNER JOIN informes_incidentes ON incidentes.id = informes_incidentes.fk_incidente_id " +
                    "WHERE informes_incidentes.malware_detectado = TRUE " +
                    "AND informes_incidentes.nivel_severidad > 90 " +
                    "AND socs.pais = 'ESPAÑA'";

    private static final String SQL_INSERT =
            "INSERT INTO incidentes (codigo_incidente, tipo_incidente, fecha_deteccion, estado, fk_soc_id, autor_examen) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE =
            "UPDATE incidentes " +
                    "SET codigo_incidente = ?, tipo_incidente = ?, fecha_deteccion = ?, estado = ?, " +
                    "fk_soc_id = ?, autor_examen = ? " +
                    "WHERE id = ?";

    private static final String SQL_DELETE =
            "DELETE FROM incidentes WHERE id = ?";

    public IncidenteDAOImpl(MotorSQL motorSQL) {
        super(motorSQL);
    }

    // TEST 5 — FIND INCIDENTES BY SOC
    public ArrayList<Incidente> findBySoc(int socId) {
        ArrayList<Incidente> lst = new ArrayList<>();
        try {
            motorSQL.connect();
            motorSQL.prepare(SQL_FIND_BY_SOC);
            motorSQL.getPs().setInt(1, socId);
            ResultSet rs = motorSQL.executeQuery();
            while (rs.next()) {
                lst.add(mapRowBase(rs));
            }
        } catch (Exception e) {
            printError(e);
        } finally {
            motorSQL.close();
        }
        return lst;
    }

    // TEST 6 — FIND INCIDENTE WITH INFORME (join triple)
    public Incidente findWithInforme(int id) {
        Incidente incidente = null;
        try {
            motorSQL.connect();
            motorSQL.prepare(SQL_FIND_WITH_INFORME);
            motorSQL.getPs().setInt(1, id);
            ResultSet rs = motorSQL.executeQuery();
            if (rs.next()) {
                incidente = mapRowWithInforme(rs);
            }
        } catch (Exception e) {
            printError(e);
        } finally {
            motorSQL.close();
        }
        return incidente;
    }

    // BLOQUE 6 — BONUS_QUERY_ADVANCED: INCIDENTES_CRITICOS
    public ArrayList<Incidente> findIncidentesCriticos() {
        ArrayList<Incidente> lst = new ArrayList<>();
        try {
            motorSQL.connect();
            motorSQL.prepare(SQL_INCIDENTES_CRITICOS);
            ResultSet rs = motorSQL.executeQuery();
            while (rs.next()) {
                lst.add(mapRowWithInforme(rs));
            }
        } catch (Exception e) {
            printError(e);
        } finally {
            motorSQL.close();
        }
        return lst;
    }

    private Incidente mapRowBase(ResultSet rs) throws Exception {
        Soc soc = new Soc();
        soc.setId(rs.getLong("soc_id"));
        soc.setNombre(rs.getString("soc_nombre"));
        soc.setPais(rs.getString("pais"));
        soc.setNivelSeguridad(rs.getString("nivel_seguridad"));
        soc.setAutorExamen(rs.getString("soc_autor"));

        Incidente incidente = new Incidente();
        incidente.setId(rs.getLong("incidente_id"));
        incidente.setCodigo_incidente(rs.getString("codigo_incidente"));
        incidente.setTipo_incidente(rs.getString("tipo_incidente"));
        incidente.setFecha_deteccion(rs.getString("fecha_deteccion"));
        incidente.setEstado(rs.getString("estado"));
        incidente.setAutorExamen(rs.getString("incidente_autor"));
        incidente.setSoc(soc);
        return incidente;
    }

    private Incidente mapRowWithInforme(ResultSet rs) throws Exception {
        Incidente incidente = mapRowBase(rs);

        InformeIncidente informe = new InformeIncidente();
        informe.setId(rs.getLong("informe_id"));
        informe.setMalware_detectado(rs.getBoolean("malware_detectado"));
        informe.setNivel_severidad(rs.getInt("nivel_severidad"));
        informe.setConclusion(rs.getString("conclusion"));
        informe.setAutorExamen(rs.getString("informe_autor"));

        incidente.setInforme(informe);
        return incidente;
    }

    // TEST 1 — ADD INCIDENTE
    @Override
    public void add(Incidente object) {
        try {
            motorSQL.connect();
            motorSQL.prepare(SQL_INSERT);
            motorSQL.getPs().setString(1, object.getCodigo_incidente());
            motorSQL.getPs().setString(2, object.getTipo_incidente());
            motorSQL.getPs().setString(3, object.getFecha_deteccion());
            motorSQL.getPs().setString(4, object.getEstado());
            motorSQL.getPs().setLong(5, object.getSoc().getId());
            motorSQL.getPs().setString(6, object.getAutorExamen());
            motorSQL.executeUpdate();
        } catch (Exception e) {
            printError(e);
        } finally {
            motorSQL.close();
        }
    }

    // TEST 2 — UPDATE INCIDENTE
    @Override
    public void update(int id, Incidente object) {
        try {
            motorSQL.connect();
            motorSQL.prepare(SQL_UPDATE);
            motorSQL.getPs().setString(1, object.getCodigo_incidente());
            motorSQL.getPs().setString(2, object.getTipo_incidente());
            motorSQL.getPs().setString(3, object.getFecha_deteccion());
            motorSQL.getPs().setString(4, object.getEstado());
            motorSQL.getPs().setLong(5, object.getSoc().getId());
            motorSQL.getPs().setString(6, object.getAutorExamen());
            motorSQL.getPs().setInt(7, id);
            motorSQL.executeUpdate();
        } catch (Exception e) {
            printError(e);
        } finally {
            motorSQL.close();
        }
    }

    @Override
    public void delete(int id) {
        try {
            motorSQL.connect();
            motorSQL.prepare(SQL_DELETE);
            motorSQL.getPs().setInt(1, id);
            motorSQL.executeUpdate();
        } catch (Exception e) {
            printError(e);
        } finally {
            motorSQL.close();
        }
    }

    // TEST 3 — FIND INCIDENTE
    @Override
    public Incidente find(int id) {
        Incidente incidente = null;
        try {
            motorSQL.connect();
            motorSQL.prepare(SQL_FIND);
            motorSQL.getPs().setInt(1, id);
            ResultSet rs = motorSQL.executeQuery();
            if (rs.next()) {
                incidente = mapRowBase(rs);
            }
        } catch (Exception e) {
            printError(e);
        } finally {
            motorSQL.close();
        }
        return incidente;
    }

    // TEST 4 — FIND ALL INCIDENTES
    @Override
    public ArrayList<Incidente> findAll() {
        ArrayList<Incidente> lst = new ArrayList<>();
        try {
            motorSQL.connect();
            motorSQL.prepare(SQL_FIND_ALL);
            ResultSet rs = motorSQL.executeQuery();
            while (rs.next()) {
                lst.add(mapRowBase(rs));
            }
        } catch (Exception e) {
            printError(e);
        } finally {
            motorSQL.close();
        }
        return lst;
    }
}
