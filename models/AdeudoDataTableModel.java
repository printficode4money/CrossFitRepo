package models;

public class AdeudoDataTableModel {
    String idAdeudo;
    String idmiembro;
    String concepto;
    double cantidadAdeudo;
    String fechaAdeudo;
    String estatus;

    public AdeudoDataTableModel(String idAdeudo, String idmiembro, String concepto, double cantidadAdeudo, String fechaAdeudo, String estatus){
        super();
        this.idAdeudo = idAdeudo;
        this.idmiembro = idmiembro;
        this.concepto = concepto;
        this.cantidadAdeudo = cantidadAdeudo;
        this.fechaAdeudo = fechaAdeudo;
        this.estatus = estatus;
    }

    public AdeudoDataTableModel() {
    }

    public String getIdAdeudo() {
        return idAdeudo;
    }

    public void setIdAdeudo(String idAdeudo) {
        this.idAdeudo = idAdeudo;
    }

    public String getIdmiembro() {
        return idmiembro;
    }

    public void setIdmiembro(String idmiembro) {
        this.idmiembro = idmiembro;
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

    public String getFechaAdeudo() {
        return fechaAdeudo;
    }

    public void setFechaAdeudo(String fechaAdeudo) {
        this.fechaAdeudo = fechaAdeudo;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
}


