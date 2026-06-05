package ec.edu.uce.tutorias.application.dto;

public class UsuarioResponse {
    private String id;
    private String correo;
    private String rol;
    private String estado;

    public UsuarioResponse() {}

    public UsuarioResponse(String id, String correo, String rol, String estado) {
        this.id = id;
        this.correo = correo;
        this.rol = rol;
        this.estado = estado;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
