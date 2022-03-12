package models;

import java.util.Date;

public class VentasDTM {

    private int idDetalle_venta;
    private String forma_pago;
    private String concepto;
    private Date fecha_venta;
    private double monto;
    private double cambio;

    public VentasDTM(){

    }

    public VentasDTM(int idDetalle_venta, String forma_pago, String concepto, Date fecha_venta, double monto, double cambio){
        super();
        this.idDetalle_venta =idDetalle_venta;
        this.forma_pago = forma_pago;
        this.concepto = concepto;
        this.fecha_venta = fecha_venta;
        this.monto = monto;
        this.cambio = cambio;
    }

    public int getIdDetalle_venta() {
        return idDetalle_venta;
    }

    public void setIdDetalle_venta(int idDetalle_venta) {
        this.idDetalle_venta = idDetalle_venta;
    }

    public String getForma_pago() {
        return forma_pago;
    }

    public void setForma_pago(String forma_pago) {
        this.forma_pago = forma_pago;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public Date getFecha_venta() {
        return fecha_venta;
    }

    public void setFecha_venta(Date fecha_venta) {
        this.fecha_venta = fecha_venta;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public double getCambio() {
        return cambio;
    }

    public void setCambio(double cambio) {
        this.cambio = cambio;
    }
}
