/*
=========================================
AUTOR: ARTURO ALMUDI MARCO
GRUPO: DAM3
EXAMEN JDBC AWS RDS
FECHA: 08/06/2026
=========================================
*/

package examen.almudi.arturo.beans;

public class InformeIncidente {

    private long id;
    private boolean malware_detectado;
    private int nivel_severidad;
    private String conclusion;
    private long idIncidente;
    private String autorExamen;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isMalware_detectado() {
        return malware_detectado;
    }

    public void setMalware_detectado(boolean malware_detectado) {
        this.malware_detectado = malware_detectado;
    }

    public int getNivel_severidad() {
        return nivel_severidad;
    }

    public void setNivel_severidad(int nivel_severidad) {
        this.nivel_severidad = nivel_severidad;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public long getIdIncidente() {
        return idIncidente;
    }

    public void setIdIncidente(long idIncidente) {
        this.idIncidente = idIncidente;
    }

    public String getAutorExamen() {
        return autorExamen;
    }

    public void setAutorExamen(String autorExamen) {
        this.autorExamen = autorExamen;
    }

    @Override
    public String toString() {
        return "InformeIncidente{" +
                "id=" + id +
                ", malware_detectado=" + malware_detectado +
                ", nivel_severidad=" + nivel_severidad +
                ", conclusion='" + conclusion + '\'' +
                ", idIncidente=" + idIncidente +
                ", autorExamen='" + autorExamen + '\'' +
                '}';
    }

}
