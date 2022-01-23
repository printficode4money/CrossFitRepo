package models;

public class MiembrosDataTableModel {
    String idmiembro;
    String nombres;
    String apellido_pat;
    String apellido_mat;
    String email;
    String fecha_registro;
    String nombreCompleto;

    public MiembrosDataTableModel(String idmiembro, String nombres, String apellido_pat, String apellido_mat, String fecha_registro, String nombreCompleto){
        super();
        this.idmiembro = idmiembro;
        this.nombres = nombres;
        this.apellido_pat = apellido_pat;
        this.apellido_mat = apellido_mat;
        this.fecha_registro = fecha_registro;
        this.nombreCompleto =  nombreCompleto;
    }

    public MiembrosDataTableModel() {

    }

    public String getIdmiembro() {
        return idmiembro;
    }

    public void setIdmiembro(String idmiembro) {
        this.idmiembro = idmiembro;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellido_pat() {
        return apellido_pat;
    }

    public void setApellido_pat(String apellido_pat) {
        this.apellido_pat = apellido_pat;
    }

    public String getApellido_mat() {
        return apellido_mat;
    }

    public void setApellido_mat(String apellido_mat) {
        this.apellido_mat = apellido_mat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
}
