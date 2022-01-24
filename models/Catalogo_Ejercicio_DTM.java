package models;

public class Catalogo_Ejercicio_DTM {

    int idEjercicio;
    String nombreEjercicio;
    String complejidad;

    String grupoMuscular;

    public Catalogo_Ejercicio_DTM(int idEjercicio, String nombreEjercicio, String complejidad, String grupoMuscular){
        super();
        this.idEjercicio = idEjercicio;
        this.nombreEjercicio = nombreEjercicio;
        this.complejidad = complejidad;
        this.grupoMuscular = grupoMuscular;
    }

    public Catalogo_Ejercicio_DTM(){
    }

    public int getIdEjercicio() {
        return idEjercicio;
    }

    public void setIdEjercicio(int idEjercicio) {
        this.idEjercicio = idEjercicio;
    }

    public String getNombreEjercicio() {
        return nombreEjercicio;
    }

    public void setNombreEjercicio(String nombreEjercicio) {
        this.nombreEjercicio = nombreEjercicio;
    }

    public String getComplejidad() {
        return complejidad;
    }

    public void setComplejidad(String complejidad) {
        this.complejidad = complejidad;
    }

    public String getGrupoMuscular() {
        return grupoMuscular;
    }

    public void setGrupoMuscular(String grupoMuscular) {
        this.grupoMuscular = grupoMuscular;
    }
}
