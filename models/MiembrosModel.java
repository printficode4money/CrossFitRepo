package models;
import java.util.Date;

public class MiembrosModel {

    private byte[] huella;

    private String nombres;

    private int idMiembro;

    private String apellidoPat;

    private String apellidoMat;

    private String sexo;

    private String email;

    private String fecha_Nacimiento;

    private Date fecha_Registro;

    private String telefono;

    public String getFecha_Nacimiento() {
        return fecha_Nacimiento;
    }

    public void setFecha_Nacimiento(String fecha_Nacimiento) {
        this.fecha_Nacimiento = fecha_Nacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getApellidoPat() {
        return apellidoPat;
    }

    public void setApellidoPat(String apellidoPat) {
        this.apellidoPat = apellidoPat;
    }

    public String getApellidoMat() {
        return apellidoMat;
    }

    public void setApellidoMat(String apellidoMat) {
        this.apellidoMat = apellidoMat;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFecha_Registro() {
        return fecha_Registro;
    }

    public void setFecha_Registro(Date fecha_Registro) {
        this.fecha_Registro = fecha_Registro;
    }

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