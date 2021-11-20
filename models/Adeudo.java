package models;

import java.util.Date;

public class Adeudo {
    int idAdeudo;
    int idMiembro;
    String concepto;
    double cantidadAdeudo;
    String mensajeRespuesta;
    Date fechaAdeudo;
    String estatus;

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public Date getFechaAdeudo() {
        return fechaAdeudo;
    }

    public void setFechaAdeudo(Date fechaAdeudo) {
        this.fechaAdeudo = fechaAdeudo;
    }

    public int getIdAdeudo() {
        return idAdeudo;
    }

    public void setIdAdeudo(int idAdeudo) {
        this.idAdeudo = idAdeudo;
    }

    public int getIdMiembro() {
        return idMiembro;
    }

    public void setIdMiembro(int idMiembro) {
        this.idMiembro = idMiembro;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public double getCantidadAdeudo() {
        return cantidadAdeudo;
    }

    public void setCantidadAdeudo(double cantidadAdeudo) {
        this.cantidadAdeudo = cantidadAdeudo;
    }

    public String getMensajeRespuesta() {
        return mensajeRespuesta;
    }

    public void setMensajeRespuesta(String mensajeRespuesta) {
        this.mensajeRespuesta = mensajeRespuesta;
    }
}
