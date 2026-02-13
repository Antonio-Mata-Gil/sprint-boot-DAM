package examen_backend.demo.service;

import examen_backend.demo.dto.LoginResponse;
import examen_backend.demo.entity.User;
import examen_backend.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Obtiene todos los usuarios
     */
    public List<User> obtenerTodosLosUsuarios() {
        return userRepository.findAll();
    }
    
    /**
     * Obtiene un usuario por ID
     */
    public Optional<User> obtenerUsuarioPorId(Long id) {
        return userRepository.findById(id);
    }
    
    /**
     * Obtiene un usuario por username
     */
    public Optional<User> obtenerUsuarioPorUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * Obtiene un usuario por email
     */
    public Optional<User> obtenerUsuarioPorEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Obtiene usuarios activos
     */
    public List<User> obtenerUsuariosActivos() {
        return userRepository.findByActivo(true);
    }
    
    /**
     * Valida el login de un usuario
     * Busca por username o email y verifica que la contraseña sea correcta
     * 
     * @param usernameOEmail - Username o Email del usuario
     * @param password - Contraseña proporcionada
     * @return LoginResponse si las credenciales son válidas, null en caso contrario
     */
    public LoginResponse validarLogin(String usernameOEmail, String password) {
        Optional<User> usuarioOpt = userRepository.findByUsername(usernameOEmail)
                .or(() -> userRepository.findByEmail(usernameOEmail));
        
        if (usuarioOpt.isPresent()) {
            User usuario = usuarioOpt.get();
            
            // Verificar contraseña (PLANTILLA - aquí irá encriptación con BCrypt)
            if (usuario.getPassword().equals(password)) {
                // Crear respuesta sin exponer la contraseña
                return new LoginResponse(
                    usuario.getId(),
                    usuario.getUsername(),
                    usuario.getEmail(),
                    usuario.getNombreCompleto(),
                    usuario.getActivo()
                );
            }
        }
        
        return null;
    }
    
    /**
     * Guarda un nuevo usuario
     */
    public User guardarUsuario(User usuario) {
        return userRepository.save(usuario);
    }
    
    /**
     * Actualiza un usuario existente
     */
    public User actualizarUsuario(Long id, User usuarioActualizado) {
        Optional<User> usuarioOpt = userRepository.findById(id);
        
        if (usuarioOpt.isPresent()) {
            User usuario = usuarioOpt.get();
            
            if (usuarioActualizado.getUsername() != null) {
                usuario.setUsername(usuarioActualizado.getUsername());
            }
            if (usuarioActualizado.getEmail() != null) {
                usuario.setEmail(usuarioActualizado.getEmail());
            }
            if (usuarioActualizado.getNombreCompleto() != null) {
                usuario.setNombreCompleto(usuarioActualizado.getNombreCompleto());
            }
            if (usuarioActualizado.getActivo() != null) {
                usuario.setActivo(usuarioActualizado.getActivo());
            }
            
            return userRepository.save(usuario);
        }
        
        return null;
    }
    
    /**
     * Elimina un usuario por ID
     */
    public void eliminarUsuario(Long id) {
        userRepository.deleteById(id);
    }
}
