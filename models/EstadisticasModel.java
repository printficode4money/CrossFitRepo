package models;

public class EstadisticasModel {
    String fechas;
    int total;

    public String getFechas() {
        return fechas;
    }

    public void setFechas(String fechas) {
        this.fechas = fechas;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public EstadisticasModel(String fechas, int total){
        super();
        this.fechas = fechas;
        this.total = total;
    }

    public EstadisticasModel(){}
}
