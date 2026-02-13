package examen_backend.demo.dto;

public class LoginResponse {
    
    private Long id;
    private String username;
    private String email;
    private String nombreCompleto;
    private Integer activo;
    
    public LoginResponse() {
    }
    
    public LoginResponse(Long id, String username, String email, String nombreCompleto, Integer activo) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.nombreCompleto = nombreCompleto;
        this.activo = activo;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    
    public Integer getActivo() {
        return activo;
    }
    
    public void setActivo(Integer activo) {
        this.activo = activo;
    }
}
