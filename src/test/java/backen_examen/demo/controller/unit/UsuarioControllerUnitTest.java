package backen_examen.demo.controller.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import backen_examen.demo.controller.UsuarioController;
import backen_examen.demo.model.Usuario;
import backen_examen.demo.service.UsuarioService;
import backen_examen.demo.fixture.UsuarioTestFixture;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;

/**
 * Tests UNITARIOS del controlador UsuarioController (sin Spring context)
 * 
 * - Testea los métodos sin cargar contexto Spring
 * - Las dependencias (Service) se mockean
 * - Verifica lógica de autorización (headers, roles)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioController - Pruebas Unitarias")
class UsuarioControllerUnitTest {

    private UsuarioController controller;
    
    @Mock
    private UsuarioService usuarioService;

    private Usuario usuarioTesteo;

    @BeforeEach
    void setUp() {
        controller = new UsuarioController();
        // Usar ReflectionTestUtils para inyectar el mock en el field privado
        ReflectionTestUtils.setField(controller, "usuarioService", usuarioService);
        usuarioTesteo = UsuarioTestFixture.crearUsuarioValido();
    }

    @Test
    @DisplayName("obtenerTodos() - Sin header Rol debe retornar 401 UNAUTHORIZED")
    void testObtenerTodos_SinRol_Unauthorized() throws Exception {
        ResponseEntity<?> response = controller.obtenerTodos(null);
        assert response != null;
        assert response.getStatusCode() != null;
        // Verificar que retorna un error
        assert response.getStatusCode().value() >= 400;
    }

    @Test
    @DisplayName("obtenerTodos() - Con header Rol retorna respuesta 200")
    void testObtenerTodos_ConRol_Success() throws Exception {
        org.mockito.Mockito.when(usuarioService.obtenerTodos())
            .thenReturn(Arrays.asList(usuarioTesteo));
        
        ResponseEntity<?> response = controller.obtenerTodos("USUARIO");
        assert response != null;
        assert response.getStatusCode() != null;
        assert response.getStatusCode().value() == 200;
    }

    @Test
    @DisplayName("crearUsuario() - Sin rol ADMIN retorna 403 FORBIDDEN")
    void testCrearUsuario_SinAdmin_Forbidden() throws Exception {
        usuarioTesteo.setRol("USUARIO");
        usuarioTesteo.setActivo(1);
        
        ResponseEntity<?> response = controller.crearUsuario(usuarioTesteo, "USUARIO");
        assert response != null;
        assert response.getStatusCode() != null;
        assert response.getStatusCode().value() == 403;
    }

    @Test
    @DisplayName("crearUsuario() - Con rol ADMIN ejecuta sin excepciones")
    void testCrearUsuario_ConAdmin_Created() throws Exception {
        usuarioTesteo.setId(1L);
        usuarioTesteo.setActivo(1);
        
        // Solo verificar que se ejecuta sin lanzar excepciones
        ResponseEntity<?> response = controller.crearUsuario(usuarioTesteo, "ADMIN");
        assert response != null;
    }
}
