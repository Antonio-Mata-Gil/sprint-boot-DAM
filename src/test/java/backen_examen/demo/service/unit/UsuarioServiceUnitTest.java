package backen_examen.demo.service.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import backen_examen.demo.model.Usuario;
import backen_examen.demo.repository.UsuarioRepository;
import backen_examen.demo.service.UsuarioService;
import backen_examen.demo.fixture.UsuarioTestFixture;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Tests UNITARIOS del servicio UsuarioService
 * 
 * - Solo testea la lógica de UsuarioService
 * - Las dependencias (Repository) se mockean
 * - NO accede a la base de datos
 * - Rápidos de ejecutar
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioService - Pruebas Unitarias")
class UsuarioServiceUnitTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioTesteo;

    @BeforeEach
    void setUp() {
        usuarioTesteo = UsuarioTestFixture.crearUsuarioValido();
    }

    @Test
    @DisplayName("Debe crear un usuario correctamente")
    void testCrearUsuario_Exitoso() {
        // Given (Dado)
        usuarioTesteo.setId(1L);
        when(usuarioRepository.save(any(Usuario.class)))
            .thenReturn(usuarioTesteo);

        // When (Cuando)
        Usuario resultado = usuarioService.crearUsuario(usuarioTesteo);

        // Then (Entonces)
        assertNotNull(resultado);
        assertEquals("testuser", resultado.getNombre());
        assertEquals("test@example.com", resultado.getEmail());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe obtener usuario por ID existente")
    void testObtenerPorId_Encontrado() {
        // Given
        Long id = 1L;
        usuarioTesteo.setId(id);
        when(usuarioRepository.findById(id))
            .thenReturn(Optional.of(usuarioTesteo));

        // When
        Usuario resultado = usuarioService.obtenerPorId(id);

        // Then
        assertNotNull(resultado);
        assertEquals("testuser", resultado.getNombre());
        verify(usuarioRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Debe retornar null cuando usuario no existe")
    void testObtenerPorId_NoEncontrado() {
        // Given
        Long id = 999L;
        when(usuarioRepository.findById(id))
            .thenReturn(Optional.empty());

        // When
        Usuario resultado = usuarioService.obtenerPorId(id);

        // Then
        assertNull(resultado);
        verify(usuarioRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Debe obtener todos los usuarios")
    void testObtenerTodos_Exitoso() {
        // Given
        Usuario usuario2 = UsuarioTestFixture.crearUsuarioConNombre("otro_usuario");
        usuario2.setId(2L);
        List<Usuario> usuarios = Arrays.asList(usuarioTesteo, usuario2);
        when(usuarioRepository.findAll())
            .thenReturn(usuarios);

        // When
        List<Usuario> resultado = usuarioService.obtenerTodos();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe actualizar un usuario correctamente")
    void testActualizarUsuario_Exitoso() {
        // Given
        Long id = 1L;
        usuarioTesteo.setId(id);
        Usuario usuarioActualizado = UsuarioTestFixture.crearUsuarioConNombre("usuario_actualizado");
        usuarioActualizado.setId(id);
        usuarioActualizado.setRol("ADMIN");
        
        when(usuarioRepository.findById(id))
            .thenReturn(Optional.of(usuarioTesteo));
        when(usuarioRepository.save(any(Usuario.class)))
            .thenReturn(usuarioActualizado);

        // When
        Usuario resultado = usuarioService.actualizarUsuario(id, usuarioActualizado);

        // Then
        assertNotNull(resultado);
        assertEquals("usuario_actualizado", resultado.getNombre());
        verify(usuarioRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Debe eliminar usuario correctamente")
    void testEliminarUsuario_Exitoso() {
        // Given
        Long id = 1L;
        when(usuarioRepository.existsById(id)).thenReturn(true);

        // When
        boolean resultado = usuarioService.eliminarUsuario(id);

        // Then
        assertTrue(resultado);
        verify(usuarioRepository, times(1)).deleteById(id);
    }
}
