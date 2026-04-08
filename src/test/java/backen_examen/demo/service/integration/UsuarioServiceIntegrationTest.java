package backen_examen.demo.service.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import backen_examen.demo.model.Usuario;
import backen_examen.demo.repository.UsuarioRepository;
import backen_examen.demo.service.UsuarioService;
import backen_examen.demo.fixture.UsuarioTestFixture;
import examen_backend.demo.DemoApplication;

/**
 * Tests de INTEGRACIÓN del servicio UsuarioService
 * 
 * - Testea la integración entre UsuarioService y UsuarioRepository
 * - Usa la base de datos REAL (o en memoria H2 si está configurada)
 * - Verifica que la persistencia funciona correctamente
 * - Más lentos que los tests unitarios
 */
@SpringBootTest(classes = DemoApplication.class)
@ActiveProfiles("test") // Usa application-test.properties
@Transactional // Cada test se ejecuta en una transacción que se revierte
@DisplayName("UsuarioService - Pruebas de Integración")
class UsuarioServiceIntegrationTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuarioTesteo;

    @BeforeEach
    void setUp() {
        usuarioTesteo = UsuarioTestFixture.crearUsuarioValido();
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("Debe crear y recuperar usuario de la BD")
    void testCrearYRecuperarUsuario() {
        // Given
        String nombreUsuario = "testuser";
        usuarioTesteo.setNombre(nombreUsuario);

        // When
        Usuario usuarioGuardado = usuarioService.crearUsuario(usuarioTesteo);
        Usuario usuarioRecuperado = usuarioService.obtenerPorId(usuarioGuardado.getId());

        // Then
        assertNotNull(usuarioRecuperado);
        assertNotNull(usuarioRecuperado.getId());
        assertEquals(nombreUsuario, usuarioRecuperado.getNombre());
    }

    @Test
    @DisplayName("Debe listar todos los usuarios guardados")
    void testObtenerTodos() {
        // Given
        usuarioService.crearUsuario(usuarioTesteo);
        Usuario usuario2 = UsuarioTestFixture.crearUsuarioConNombre("otro_usuario");
        usuarioService.crearUsuario(usuario2);

        // When
        var usuarios = usuarioService.obtenerTodos();

        // Then
        assertFalse(usuarios.isEmpty());
        assertEquals(2, usuarios.size());
    }

    @Test
    @DisplayName("Debe actualizar un usuario correctamente")
    void testActualizarUsuario() {
        // Given
        Usuario usuarioGuardado = usuarioService.crearUsuario(usuarioTesteo);
        Usuario usuarioActualizado = UsuarioTestFixture.crearUsuarioValido();
        usuarioActualizado.setNombre("nombre_actualizado");
        usuarioActualizado.setEmail("nuevo@example.com");
        usuarioActualizado.setRol("ADMIN");
        usuarioActualizado.setActivo(1);

        // When
        Usuario resultado = usuarioService.actualizarUsuario(usuarioGuardado.getId(), usuarioActualizado);

        // Then
        assertNotNull(resultado);
        assertEquals("nombre_actualizado", resultado.getNombre());
        assertEquals("nuevo@example.com", resultado.getEmail());
    }

    @Test
    @DisplayName("Debe eliminar un usuario correctamente")
    void testEliminarUsuario() {
        // Given
        Usuario usuarioGuardado = usuarioService.crearUsuario(usuarioTesteo);
        Long idAEliminar = usuarioGuardado.getId();

        // When
        boolean eliminado = usuarioService.eliminarUsuario(idAEliminar);
        Usuario usuarioBuscado = usuarioService.obtenerPorId(idAEliminar);

        // Then
        assertTrue(eliminado);
        assertNull(usuarioBuscado);
    }
}
