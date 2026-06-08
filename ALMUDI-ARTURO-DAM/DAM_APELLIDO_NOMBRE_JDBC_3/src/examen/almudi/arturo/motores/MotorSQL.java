/*
=========================================
AUTOR: ARTURO ALMUDI MARCO
GRUPO: DAM3
EXAMEN JDBC AWS RDS
FECHA: 08/06/2026
=========================================
*/

package examen.almudi.arturo.motores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public abstract class MotorSQL {

    protected String url;
    protected String user;
    protected String password;
    protected String driver;
    public Connection conn;
    protected PreparedStatement ps;

    public MotorSQL(String url, String user, String password, String driver) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.driver = driver;
    }

    public abstract void connect();

    public void prepare(String SQL) {
        try {
            ps = conn.prepareStatement(SQL);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public ResultSet executeQuery() {
        ResultSet rs = null;
        try {
            rs = ps.executeQuery();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }

    public int executeUpdate() {
        int rows = 0;
        try {
            rows = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return rows;
    }

    public PreparedStatement getPs() {
        return ps;
    }

    public void close() {
        try {
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
