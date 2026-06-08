/*
=========================================
AUTOR: ARTURO ALMUDI MARCO
GRUPO: DAM3
EXAMEN JDBC AWS RDS
FECHA: 08/06/2026
=========================================
*/

package examen.almudi.arturo.dao;

import examen.almudi.arturo.beans.InformeIncidente;
import examen.almudi.arturo.motores.MotorSQL;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class InformeIncidenteDAOImpl extends AbstractDAO<InformeIncidente> {

    private static final String SQL_FIND_ALL =
            "SELECT * FROM informes_incidentes ORDER BY id";

    private static final String SQL_FIND =
            "SELECT * FROM informes_incidentes WHERE id = ?";

    private static final String SQL_INSERT =
            "INSERT INTO informes_incidentes (malware_detectado, nivel_severidad, conclusion, fk_incidente_id, autor_examen) " +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE =
            "UPDATE informes_incidentes " +
                    "SET malware_detectado = ?, nivel_severidad = ?, conclusion = ?, autor_examen = ? " +
                    "WHERE id = ?";

    private static final String SQL_DELETE =
            "DELETE FROM informes_incidentes WHERE id = ?";

    public InformeIncidenteDAOImpl(MotorSQL motorSQL) {
        super(motorSQL);
    }

    private InformeIncidente mapRow(ResultSet rs) throws Exception {
        InformeIncidente informe = new InformeIncidente();
        informe.setId(rs.getLong("id"));
        informe.setMalware_detectado(rs.getBoolean("malware_detectado"));
        informe.setNivel_severidad(rs.getInt("nivel_severidad"));
        informe.setConclusion(rs.getString("conclusion"));
        informe.setIdIncidente(rs.getLong("fk_incidente_id"));
        informe.setAutorExamen(rs.getString("autor_examen"));
        return informe;
    }

    @Override
    public void add(InformeIncidente object) {
        try {
            motorSQL.connect();
            motorSQL.prepare(SQL_INSERT);
            motorSQL.getPs().setBoolean(1, object.isMalware_detectado());
            motorSQL.getPs().setInt(2, object.getNivel_severidad());
            motorSQL.getPs().setString(3, object.getConclusion());
            motorSQL.getPs().setLong(4, object.getIdIncidente());
            motorSQL.getPs().setString(5, object.getAutorExamen());
            motorSQL.executeUpdate();
        } catch (Exception e) {
            printError(e);
        } finally {
            motorSQL.close();
        }
    }

    @Override
    public void update(int id, InformeIncidente object) {
        try {
            motorSQL.connect();
            motorSQL.prepare(SQL_UPDATE);
            motorSQL.getPs().setBoolean(1, object.isMalware_detectado());
            motorSQL.getPs().setInt(2, object.getNivel_severidad());
            motorSQL.getPs().setString(3, object.getConclusion());
            motorSQL.getPs().setString(4, object.getAutorExamen());
            motorSQL.getPs().setInt(5, id);
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

    @Override
    public InformeIncidente find(int id) {
        InformeIncidente informe = null;
        try {
            motorSQL.connect();
            motorSQL.prepare(SQL_FIND);
            motorSQL.getPs().setInt(1, id);
            ResultSet rs = motorSQL.executeQuery();
            if (rs.next()) {
                informe = mapRow(rs);
            }
        } catch (Exception e) {
            printError(e);
        } finally {
            motorSQL.close();
        }
        return informe;
    }

    @Override
    public ArrayList<InformeIncidente> findAll() {
        ArrayList<InformeIncidente> lst = new ArrayList<>();
        try {
            motorSQL.connect();
            motorSQL.prepare(SQL_FIND_ALL);
            ResultSet rs = motorSQL.executeQuery();
            while (rs.next()) {
                lst.add(mapRow(rs));
            }
        } catch (Exception e) {
            printError(e);
        } finally {
            motorSQL.close();
        }
        return lst;
    }

    // BLOQUE 7 — DYNAMIC_UPDATE_ENGINE
    // Permite actualizar solo los campos que se indiquen (null = no se toca).
    // Llama a updateDynamic de AbstractDAO que construye el SET dinamicamente.
    public void updateDynamicInforme(int id, Boolean malwareDetectado, Integer nivelSeveridad, String conclusion) {
        LinkedHashMap<String, Object> campos = new LinkedHashMap<>();
        if (malwareDetectado != null) campos.put("malware_detectado", malwareDetectado);
        if (nivelSeveridad != null) campos.put("nivel_severidad", nivelSeveridad);
        if (conclusion != null) campos.put("conclusion", conclusion);
        updateDynamic("informes_incidentes", id, campos);
    }
}
