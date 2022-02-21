package models;

public class UsuariosDTM {
    int id;
    String usuario;
    String password;
    String email;
    boolean admin;
    String nombre_completo;

    public UsuariosDTM(int id, String usuario, String password, String email, boolean admin, String nombre_completo){
        super();
        this.id = id;
        this.usuario = usuario;
        this.password = password;
        this.email = email;
        this.admin = admin;
        this.nombre_completo =  nombre_completo;
    }

    public UsuariosDTM() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getNombre_completo() {
        return nombre_completo;
    }

    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }

}
