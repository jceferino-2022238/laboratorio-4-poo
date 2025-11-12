package controller;

import model.Administrator;
import model.Editor;
import model.User;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador que gestiona autenticación y administración de usuarios.
 * Maneja login, logout y validación de permisos.
 * 
 * @author Ceferino, Paiz, Junior
 * @version 1.0
 */
public class UserController {
    private List<User> userList;
    private User currentSession;
    
    /**
     * Constructor que inicializa usuarios de prueba.
     */
    public UserController() {
        this.userList = new ArrayList<>();
        this.currentSession = null;
        initializeDefaultUsers();
    }
    
    /**
     * Inicializa usuarios por defecto para pruebas.
     */
    private void initializeDefaultUsers() {
        // Usuario administrador por defecto
        userList.add(new Administrator("admin", "admin123", "admin@ega.com"));
        
        // Usuario editor por defecto
        userList.add(new Editor("editor", "editor123", "editor@ega.com"));
    }
    
    /**
     * Autentica un usuario en el sistema.
     * Utiliza polimorfismo - funciona con Administrator y Editor.
     * 
     * @param username nombre de usuario
     * @param password contraseña
     * @return true si la autenticación fue exitosa
     */
    public boolean login(String username, String password) {
        for (User user : userList) {
            if (user.getUsername().equals(username) && user.authenticate(password)) {
                this.currentSession = user;
                return true;
            }
        }
        return false;
    }
    
    /**
     * Cierra la sesión del usuario actual.
     */
    public void logout() {
        this.currentSession = null;
    }
    
    /**
     * Valida si el usuario actual tiene un permiso específico.
     * Demuestra polimorfismo: Administrator y Editor tienen diferentes permisos.
     * 
     * @param action acción a validar
     * @return true si tiene permiso
     */
    public boolean validatePermission(String action) {
        if (currentSession == null) {
            return false;
        }
        return currentSession.getPermissions().contains(action);
    }
    
    /**
     * Registra un nuevo usuario en el sistema.
     * 
     * @param user usuario a registrar
     * @return true si se registró correctamente
     */
    public boolean registerUser(User user) {
        if (user == null) {
            return false;
        }
        
        // Verificar que no exista otro usuario con el mismo nombre
        boolean exists = userList.stream()
                .anyMatch(u -> u.getUsername().equals(user.getUsername()));
        
        if (!exists) {
            userList.add(user);
            return true;
        }
        return false;
    }
    
    /**
     * Obtiene el usuario actualmente autenticado.
     * 
     * @return usuario actual o null si no hay sesión
     */
    public User getCurrentUser() {
        return currentSession;
    }
    
    /**
     * Verifica si hay una sesión activa.
     * 
     * @return true si hay un usuario autenticado
     */
    public boolean isLoggedIn() {
        return currentSession != null;
    }
    
    /**
     * Obtiene todos los usuarios registrados.
     * 
     * @return lista de usuarios
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(userList);
    }
    
    /**
     * Busca un usuario por nombre de usuario.
     * 
     * @param username nombre de usuario a buscar
     * @return usuario encontrado o null
     */
    public User findUserByUsername(String username) {
        return userList.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }
}
