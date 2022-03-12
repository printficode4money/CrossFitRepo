package models;

public class InventarioDTM {
    int idInventario;
    String nombre;
    String descripcion;
    String precio;
    int existencias;
    String cantidadVenta;

    public String getTotalPrecioUnitario() {
        return totalPrecioUnitario;
    }

    public void setTotalPrecioUnitario(String totalPrecioUnitario) {
        this.totalPrecioUnitario = totalPrecioUnitario;
    }

    String totalPrecioUnitario;

    public InventarioDTM(String idInventario, String nombre, String descripcion, String precio, int existencias, String cantidadVenta, String totalPrecioUnitario){
        super();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.existencias = existencias;
        this.cantidadVenta = cantidadVenta;
        this.totalPrecioUnitario = totalPrecioUnitario;
    }

    public int getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(int idInventario) {
        this.idInventario = idInventario;
    }

    public InventarioDTM(){

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public int getExistencias() {
        return existencias;
    }

    public void setExistencias(int existencias) {
        this.existencias = existencias;
    }

    public String getCantidadVenta() {
        return cantidadVenta;
    }

    public void setCantidadVenta(String cantidadVenta) {
        this.cantidadVenta = cantidadVenta;
    }
}
