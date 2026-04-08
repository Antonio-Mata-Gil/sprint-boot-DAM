package backen_examen.demo.fixture;

import backen_examen.demo.model.Usuario;

/**
 * FIXTURE / TEST DATA BUILDER
 * 
 * Proporciona objetos de prueba reutilizables con datos válidos.
 * Evita duplicación de código en los tests y facilita el mantenimiento.
 */
public class UsuarioTestFixture {

    /**
     * Crea un usuario válido con datos por defecto
     */
    public static Usuario crearUsuarioValido() {
        Usuario usuario = new Usuario();
        usuario.setNombre("testuser");
        usuario.setEmail("test@example.com");
        usuario.setPassword("Password123!");
        usuario.setRol("USUARIO");
        usuario.setActivo(1);
        return usuario;
    }

    /**
     * Crea un usuario con nombre personalizado
     */
    public static Usuario crearUsuarioConNombre(String nombre) {
        Usuario usuario = crearUsuarioValido();
        usuario.setNombre(nombre);
        usuario.setEmail(nombre + "@example.com");
        return usuario;
    }

    /**
     * Crea un usuario con email personalizado
     */
    public static Usuario crearUsuarioConEmail(String email) {
        Usuario usuario = crearUsuarioValido();
        usuario.setEmail(email);
        return usuario;
    }

    /**
     * Crea un usuario con rol personalizado
     */
    public static Usuario crearUsuarioConRol(String rol) {
        Usuario usuario = crearUsuarioValido();
        usuario.setRol(rol);
        return usuario;
    }

}
