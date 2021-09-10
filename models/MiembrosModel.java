package models;
import java.util.Date;

public class MiembrosModel {


    private byte[] huella;

    private String nombres;

    private int idMiembro;

    public byte[] getHuella() {
        return this.huella;
    }

    public void setHuella(byte[] huella) {
        this.huella = huella;
    }

    public int getIdMiembro() {
        return idMiembro;
    }

    public void setIdMiembro(int idMiembro) {
        this.idMiembro = idMiembro;
    }

    public Date getFecha_Ultima_Visita() {
        return fecha_Ultima_Visita;
    }

    public void setFecha_Ultima_Visita(Date fecha_Ultima_Visita) {
        this.fecha_Ultima_Visita = fecha_Ultima_Visita;
    }

    private Date fecha_Ultima_Visita;

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
}