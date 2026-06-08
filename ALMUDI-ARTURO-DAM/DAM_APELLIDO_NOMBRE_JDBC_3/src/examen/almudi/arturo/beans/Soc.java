/*
=========================================
AUTOR: ARTURO ALMUDI MARCO
GRUPO: DAM3
EXAMEN JDBC AWS RDS
FECHA: 08/06/2026
=========================================
*/

package examen.almudi.arturo.beans;

public class Soc {

    private long id;
    private String nombre;
    private String pais;
    private String nivelSeguridad;
    private String autorExamen;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getNivelSeguridad() {
        return nivelSeguridad;
    }

    public void setNivelSeguridad(String nivelSeguridad) {
        this.nivelSeguridad = nivelSeguridad;
    }

    public String getAutorExamen() {
        return autorExamen;
    }

    public void setAutorExamen(String autorExamen) {
        this.autorExamen = autorExamen;
    }

    @Override
    public String toString() {
        return "Soc{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", pais='" + pais + '\'' +
                ", nivelSeguridad='" + nivelSeguridad + '\'' +
                ", autorExamen='" + autorExamen + '\'' +
                '}';
    }
}
