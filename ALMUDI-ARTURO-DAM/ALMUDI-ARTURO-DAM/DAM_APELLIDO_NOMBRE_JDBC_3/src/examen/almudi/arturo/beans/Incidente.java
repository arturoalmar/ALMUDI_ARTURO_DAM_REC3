/*
=========================================
AUTOR: ARTURO ALMUDI MARCO
GRUPO: DAM3
EXAMEN JDBC AWS RDS
FECHA: 08/06/2026
=========================================
*/
package examen.almudi.arturo.beans;

public class Incidente {

    private long id;
    private String codigo_incidente;
    private String tipo_incidente;
    private String fecha_deteccion;
    private String estado;
    private Soc soc;
    private InformeIncidente informe;
    private String autorExamen;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCodigo_incidente() {
        return codigo_incidente;
    }

    public void setCodigo_incidente(String codigo_incidente) {
        this.codigo_incidente = codigo_incidente;
    }

    public String getTipo_incidente() {
        return tipo_incidente;
    }

    public void setTipo_incidente(String tipo_incidente) {
        this.tipo_incidente = tipo_incidente;
    }

    public String getFecha_deteccion() {
        return fecha_deteccion;
    }

    public void setFecha_deteccion(String fecha_deteccion) {
        this.fecha_deteccion = fecha_deteccion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Soc getSoc() {
        return soc;
    }

    public void setSoc(Soc soc) {
        this.soc = soc;
    }

    public InformeIncidente getInforme() {
        return informe;
    }

    public void setInforme(InformeIncidente informe) {
        this.informe = informe;
    }

    public String getAutorExamen() {
        return autorExamen;
    }

    public void setAutorExamen(String autorExamen) {
        this.autorExamen = autorExamen;
    }

    @Override
    public String toString() {
        return "Incidente{" +
                "id=" + id +
                ", codigo_incidente='" + codigo_incidente + '\'' +
                ", tipo_incidente='" + tipo_incidente + '\'' +
                ", fecha_deteccion='" + fecha_deteccion + '\'' +
                ", estado='" + estado + '\'' +
                ", soc=" + soc +
                ", informe=" + informe +
                ", autorExamen='" + autorExamen + '\'' +
                '}';
    }

}