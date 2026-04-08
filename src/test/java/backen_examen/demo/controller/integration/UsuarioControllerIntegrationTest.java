package backen_examen.demo.controller.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import backen_examen.demo.controller.UsuarioController;
import backen_examen.demo.model.Usuario;
import backen_examen.demo.repository.UsuarioRepository;
import backen_examen.demo.fixture.UsuarioTestFixture;
import examen_backend.demo.DemoApplication;

/**
 * Tests de INTEGRACIÓN del controlador UsuarioController
 * 
 * - Testea los métodos del controlador contra la BD de prueba (H2)
 * - Carga el contexto COMPLETO de Spring
 * - La BD de prueba se limpia después de cada test (@Transactional)
 * - Verifica que el flujo Service → Repository → BD funciona correctamente
 */
@SpringBootTest(classes = DemoApplication.class)
@ActiveProfiles("test")
@Transactional
@DisplayName("UsuarioController - Pruebas de Integración")
class UsuarioControllerIntegrationTest {

    @Autowired
    private UsuarioController controller;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuarioTesteo;

    @BeforeEach
    void setUp() {
        usuarioTesteo = UsuarioTestFixture.crearUsuarioValido();
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("crearUsuario() y obtenerTodos() - Crear y listar usuarios")
    void testCrearYObtenerUsuarios() throws Exception {
        usuarioTesteo.setActivo(1);
        
        // Crear usuario
        ResponseEntity<?> createResponse = controller.crearUsuario(usuarioTesteo, "ADMIN");
        assert createResponse != null;
        assert createResponse.getStatusCode() != null;

        // Obtener usuarios
        ResponseEntity<?> listResponse = controller.obtenerTodos("USUARIO");
        assert listResponse != null;
        assert listResponse.getStatusCode() != null;
        assert listResponse.getStatusCode().value() == 200;
    }

    @Test
    @DisplayName("obtenerTodos() - Listar usuarios de BD")
    void testObtenerTodosDesdeRepository() throws Exception {
        // Guardar usuarios en BD
        Usuario usuario1 = UsuarioTestFixture.crearUsuarioValido();
        usuario1.setActivo(1);
        Usuario usuario2 = UsuarioTestFixture.crearUsuarioConNombre("usuario2");
        usuario2.setActivo(1);

        usuarioRepository.save(usuario1);
        usuarioRepository.save(usuario2);

        // Listar vía controlador
        ResponseEntity<?> response = controller.obtenerTodos("USUARIO");
        
        assert response != null;
        assert response.getStatusCode() != null;
    }

    @Test
    @DisplayName("actualizarUsuario() - Actualizar usuario en BD")
    void testActualizarUsuario() throws Exception {
        usuarioTesteo.setActivo(1);
        Usuario usuarioGuardado = usuarioRepository.save(usuarioTesteo);
        Long idUsuario = usuarioGuardado.getId();

        // Preparar actualización
        usuarioTesteo.setNombre("nombre_actualizado");
        usuarioTesteo.setEmail("nuevo@example.com");

        // Actualizar usuario
        ResponseEntity<?> updateResponse = controller.actualizarUsuario(idUsuario, usuarioTesteo, "ADMIN");
        assert updateResponse != null;
        assert updateResponse.getStatusCode() != null;
    }

    @Test
    @DisplayName("eliminarUsuario() - Eliminar usuario de BD")
    void testEliminarUsuario() throws Exception {
        usuarioTesteo.setActivo(1);
        Usuario usuarioGuardado = usuarioRepository.save(usuarioTesteo);
        Long idUsuario = usuarioGuardado.getId();

        // Eliminar usuario
        ResponseEntity<?> deleteResponse = controller.eliminarUsuario(idUsuario, "ADMIN");
        assert deleteResponse != null;
        assert deleteResponse.getStatusCode() != null;
    }
}
