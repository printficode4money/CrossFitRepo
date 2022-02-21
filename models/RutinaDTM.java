package models;

import controllers.ReservacionesExt.DateEvent;
import controllers.ReservacionesExt.LocalDateTimeAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@XmlAccessorType(XmlAccessType.FIELD)
public class RutinaDTM implements Comparable<controllers.ReservacionesExt.DateEvent>, Serializable {

    private int idWod;
    private String nombre_ejercicio;
    private String nombreEntrenador;
    private String tipo;
    private String reps;
    private String tiempo;
    private LocalDateTime fecha_wod;
    private String nombre_set;
    private String tiempo_por_set;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime dateTime;
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime notifyTime;

    public RutinaDTM(LocalDate date,String nombreEntrenador, String description) {
        setDateTime(LocalDateTime.of(date, LocalTime.parse("00:00:00")));
        setNombreEntrenador(nombreEntrenador);
    }


    public RutinaDTM(int idWod, LocalDateTime dateTime, String nombre_ejercicio, String nombreEntrenador, String tipo, String reps, String tiempo, LocalDateTime fecha_wod, String nombre_set) {
        this.idWod = idWod;
        this.dateTime = dateTime;
        this.nombre_ejercicio = nombre_ejercicio;
        this.nombreEntrenador = nombreEntrenador;
        this.tipo = tipo;
        this.reps = reps;
        this.tiempo = tiempo;
        this.fecha_wod = fecha_wod;
        this.nombre_set = nombre_set;
    }

    public RutinaDTM(String nombre_set, String tipo, String nombre_ejercicio, String reps, String tiempo, String tiempo_por_set) {
        this.nombre_ejercicio = nombre_ejercicio;
        this.tipo = tipo;
        this.reps = reps;
        this.tiempo = tiempo;
        this.nombre_set = nombre_set;
        this.tiempo_por_set = tiempo_por_set;
    }

    public RutinaDTM() {

    }


    public LocalDateTime getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(int seconds) {
        notifyTime = dateTime.minusSeconds(seconds);
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getNombreEntrenador() {
        return nombreEntrenador;
    }

    public void setNombreEntrenador(String nombreEntrenador) {
        this.nombreEntrenador = nombreEntrenador;
    }

    public int getIdWod() {
        return idWod;
    }

    public void setIdWod(int idWod) {
        this.idWod = idWod;
    }

    public String getNombre_ejercicio() {
        return nombre_ejercicio;
    }

    public void setNombre_ejercicio(String nombre_ejercicio) {
        this.nombre_ejercicio = nombre_ejercicio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public LocalDateTime getFecha_wod() {
        return fecha_wod;
    }

    public void setFecha_wod(LocalDateTime fecha_wod) {
        this.fecha_wod = fecha_wod;
    }

    public String getNombre_set() {
        return nombre_set;
    }

    public void setNombre_set(String nombre_set) {
        this.nombre_set = nombre_set;
    }

    public void setNotifyTime(LocalDateTime notifyTime) {
        this.notifyTime = notifyTime;
    }

    public String getTiempo_por_set() {
        return tiempo_por_set;
    }

    public void setTiempo_por_set(String tiempo_por_set) {
        this.tiempo_por_set = tiempo_por_set;
    }


    @Override
    public int compareTo(DateEvent o) {
        return dateTime.compareTo(o.getDateTime());
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//
//        if (o == null || getClass() != o.getClass()) return false;
//
//        RutinaDTM dateEvent = (RutinaDTM) o;
//
//        return new EqualsBuilder()
//                .append(dateTime, dateEvent.dateTime)
//                .append(notifyTime, dateEvent.notifyTime)
//                .append(description, dateEvent.description)
//                .append(nombreEntrenador, dateEvent.nombreEntrenador)
//                .isEquals();
//    }

//    @Override
//    public int hashCode() {
//        return new HashCodeBuilder(17, 37)
//                .append(dateTime)
//                .append(notifyTime)
//                .append(description)
//                .append(nombreEntrenador)
//                .toHashCode();
//    }

//    @Override
//    public String toString() {
//        return new ToStringBuilder(this)
//                .append("description", description)
//                .append("nombreEntrenador", nombreEntrenador)
//                .append("dateTime", dateTime)
//                .append("notifyTime", notifyTime)
//                .toString();
//    }
}
