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
import examen.almudi.arturo.motores.MotorSQL;

import java.sql.ResultSet;
import java.util.ArrayList;

public class SocDAOImpl extends AbstractDAO<Soc> {

    private static final String SQL_FIND_ALL =
            "SELECT * FROM socs ORDER BY id";

    private static final String SQL_FIND =
            "SELECT * FROM socs WHERE id = ?";

    private static final String SQL_INSERT =
            "INSERT INTO socs (nombre, pais, nivel_seguridad, autor_examen) " +
                    "VALUES (?, ?, ?, ?)";

    private static final String SQL_UPDATE =
            "UPDATE socs " +
                    "SET nombre = ?, pais = ?, nivel_seguridad = ?, autor_examen = ? " +
                    "WHERE id = ?";

    private static final String SQL_DELETE =
            "DELETE FROM socs WHERE id = ?";

    public SocDAOImpl(MotorSQL motorSQL) {
        super(motorSQL);
    }

    private Soc mapRow(ResultSet rs) throws Exception {
        Soc soc = new Soc();
        soc.setId(rs.getLong("id"));
        soc.setNombre(rs.getString("nombre"));
        soc.setPais(rs.getString("pais"));
        soc.setNivelSeguridad(rs.getString("nivel_seguridad"));
        soc.setAutorExamen(rs.getString("autor_examen"));
        return soc;
    }

    @Override
    public void add(Soc object) {
        try {
            motorSQL.connect();
            motorSQL.prepare(SQL_INSERT);
            motorSQL.getPs().setString(1, object.getNombre());
            motorSQL.getPs().setString(2, object.getPais());
            motorSQL.getPs().setString(3, object.getNivelSeguridad());
            motorSQL.getPs().setString(4, object.getAutorExamen());
            motorSQL.executeUpdate();
        } catch (Exception e) {
            printError(e);
        } finally {
            motorSQL.close();
        }
    }

    @Override
    public void update(int id, Soc object) {
        try {
            motorSQL.connect();
            motorSQL.prepare(SQL_UPDATE);
            motorSQL.getPs().setString(1, object.getNombre());
            motorSQL.getPs().setString(2, object.getPais());
            motorSQL.getPs().setString(3, object.getNivelSeguridad());
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
    public Soc find(int id) {
        Soc soc = null;
        try {
            motorSQL.connect();
            motorSQL.prepare(SQL_FIND);
            motorSQL.getPs().setInt(1, id);
            ResultSet rs = motorSQL.executeQuery();
            if (rs.next()) {
                soc = mapRow(rs);
            }
        } catch (Exception e) {
            printError(e);
        } finally {
            motorSQL.close();
        }
        return soc;
    }

    @Override
    public ArrayList<Soc> findAll() {
        ArrayList<Soc> lst = new ArrayList<>();
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
}
