package examen_backend.demo.controller;

import examen_backend.demo.dto.LoginRequest;
import examen_backend.demo.dto.LoginResponse;
import examen_backend.demo.entity.User;
import examen_backend.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * Obtiene todos los usuarios
     * GET /api/usuarios
     */
    @GetMapping
    public ResponseEntity<List<User>> obtenerTodos() {
        List<User> usuarios = userService.obtenerTodosLosUsuarios();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }
    
    /**
     * Obtiene un usuario por ID
     * GET /api/usuarios/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<User> usuario = userService.obtenerUsuarioPorId(id);
        
        if (usuario.isPresent()) {
            return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
    }
    
    /**
     * Obtiene usuarios activos
     * GET /api/usuarios/filtro/activos
     */
    @GetMapping("/filtro/activos")
    public ResponseEntity<List<User>> obtenerActivos() {
        List<User> usuarios = userService.obtenerUsuariosActivos();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }
    
    /**
     * Login de usuario
     * Valida credenciales por username o email
     * POST /api/usuarios/login
     * 
     * Body: {
     *   "username": "usuario_o_email",
     *   "password": "contraseña"
     * }
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            return new ResponseEntity<>("Username/Email y password son requeridos", HttpStatus.BAD_REQUEST);
        }
        
        LoginResponse response = userService.validarLogin(loginRequest.getUsername(), loginRequest.getPassword());
        
        if (response != null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        
        return new ResponseEntity<>("Credenciales inválidas", HttpStatus.UNAUTHORIZED);
    }
    
    /**
     * Crea un nuevo usuario
     * POST /api/usuarios
     */
    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody User usuario) {
        if (usuario.getUsername() == null || usuario.getEmail() == null || usuario.getPassword() == null) {
            return new ResponseEntity<>("Username, email y password son requeridos", HttpStatus.BAD_REQUEST);
        }
        
        User nuevoUsuario = userService.guardarUsuario(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }
    
    /**
     * Actualiza un usuario existente
     * PUT /api/usuarios/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody User usuarioActualizado) {
        User usuarioModificado = userService.actualizarUsuario(id, usuarioActualizado);
        
        if (usuarioModificado != null) {
            return new ResponseEntity<>(usuarioModificado, HttpStatus.OK);
        }
        return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
    }
    
    /**
     * Elimina un usuario
     * DELETE /api/usuarios/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        Optional<User> usuario = userService.obtenerUsuarioPorId(id);
        
        if (usuario.isPresent()) {
            userService.eliminarUsuario(id);
            return new ResponseEntity<>("Usuario eliminado correctamente", HttpStatus.OK);
        }
        return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
    }
}
