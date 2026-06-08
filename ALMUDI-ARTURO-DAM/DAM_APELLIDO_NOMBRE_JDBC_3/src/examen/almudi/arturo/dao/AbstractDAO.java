/*
=========================================
AUTOR: ARTURO ALMUDI MARCO
GRUPO: DAM3
EXAMEN JDBC AWS RDS
FECHA: 08/06/2026
=========================================
*/

package examen.almudi.arturo.dao;

import examen.almudi.arturo.motores.MotorSQL;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public abstract class AbstractDAO<T> implements DAO<T> {

    protected MotorSQL motorSQL;

    public AbstractDAO(MotorSQL motorSQL) {
        this.motorSQL = motorSQL;
    }

    protected void printError(Exception e) {
        System.out.println("[ERROR] " + e.getMessage());
    }

    public void check() {
        try {
            motorSQL.connect();
            if (motorSQL.conn != null && !motorSQL.conn.isClosed()) {
                System.out.println("CONEXION OK");
            }
        } catch (Exception e) {
            printError(e);
        } finally {
            motorSQL.close();
        }
    }

    // =========================================================
    // BLOQUE 7 — DYNAMIC_UPDATE_ENGINE
    // Construye dinamicamente el SQL UPDATE segun los campos
    // que se reciban en el LinkedHashMap. De este modo no hace
    // falta un metodo update por cada combinacion de campos.
    // El SET se genera en tiempo de ejecucion y se usa
    // PreparedStatement para evitar SQL injection.
    // =========================================================
    protected void updateDynamic(String tabla, int id, LinkedHashMap<String, Object> campos) {
        if (campos == null || campos.isEmpty()) {
            System.out.println("[WARN] No hay campos para actualizar.");
            return;
        }

        StringBuilder sql = new StringBuilder("UPDATE " + tabla + " SET ");
        List<String> keys = new ArrayList<>(campos.keySet());
        for (int i = 0; i < keys.size(); i++) {
            sql.append(keys.get(i)).append(" = ?");
            if (i < keys.size() - 1) sql.append(", ");
        }
        sql.append(" WHERE id = ?");

        try {
            motorSQL.connect();
            motorSQL.prepare(sql.toString());
            int idx = 1;
            for (Object val : campos.values()) {
                motorSQL.getPs().setObject(idx++, val);
            }
            motorSQL.getPs().setInt(idx, id);
            motorSQL.executeUpdate();
            System.out.println("[OK] UPDATE dinamico ejecutado: " + sql);
        } catch (Exception e) {
            printError(e);
        } finally {
            motorSQL.close();
        }
    }

    // =========================================================
    // BLOQUE 8 — ABSTRACTDAO_ARCHITECTURE_REFACTOR
    //
    // ANALISIS CRITICO:
    // AbstractDAO<T> define e implementa los 5 metodos CRUD
    // (add, update, delete, find, findAll) que son comunes a
    // TODAS las entidades. Esto es correcto.
    //
    // SIN EMBARGO, metodos como findIncidentesCriticos() o
    // findBySoc() NO deben vivir en AbstractDAO ni en DAO<T>
    // porque solo tienen sentido para la entidad Incidente.
    // Si los pusieramos en la interfaz obligariamos a SocDAOImpl
    // e InformeIncidenteDAOImpl a implementarlos sin sentido.
    //
    // MEJORA ARQUITECTONICA IMPLEMENTADA:
    // Las consultas avanzadas y especializadas viven UNICAMENTE
    // en el DAO concreto que las necesita (IncidenteDAOImpl).
    // updateDynamicInforme() vive solo en InformeIncidenteDAOImpl.
    // AbstractDAO solo contiene logica verdaderamente comun:
    // CRUD base + updateDynamic (motor generico reutilizable).
    // =========================================================
}
