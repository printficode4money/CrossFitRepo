package models;

public class Catalogo_Ejercicio_DTM {

    int idEjercicio;
    String nombreEjercicio;
    String complejidad;

    public Catalogo_Ejercicio_DTM(String descanso, String s, String s1) {
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    String reps;
    String tiempo;

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


    public Catalogo_Ejercicio_DTM(String descanso, int i, String s) {
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
