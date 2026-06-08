/*
=========================================
AUTOR: ARTURO ALMUDI MARCO
GRUPO: DAM3
EXAMEN JDBC AWS RDS
FECHA: 08/06/2026
=========================================
*/

package examen.almudi.arturo.motores;

public class MotorFactory {

    public static final String ORACLE = "ORACLE";
    public static final String POSTGRE = "POSTGRE";
    public static final String MARIADB = "MARIADB";
    public static final String SQLSERVER = "SQLSERVER";

    public static MotorSQL create(String motor) {
        switch (motor) {
            case POSTGRE:
                return new PostgreMotorSQL();
            default:
                throw new IllegalArgumentException("Motor no soportado");
        }
    }
}