package models;

public class MiembrosDataTableModel {
    int idmiembro;
    String nombres;
    String apellido_pat;
    String apellido_mat;
    String email;

    public MiembrosDataTableModel(int idmiembro, String nombres, String apellido_pat, String apellido_mat)
    {
        super();
        this.idmiembro = idmiembro;
        this.nombres = nombres;
        this.apellido_pat = apellido_pat;
        this.apellido_mat = apellido_mat;
    }

    public int getIdmiembro() {
        return idmiembro;
    }

    public void setIdmiembro(int idmiembro) {
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
}
