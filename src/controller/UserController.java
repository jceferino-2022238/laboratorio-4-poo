package controller;

import model.Administrator;
import model.Editor;
import model.User;
import java.util.ArrayList;
import java.util.List;

// Controlador que gestiona la autenticación y administración de usuarios.
public class UserController {
    private List<User> userList;
    private User currentSession;
    
    // Constructor de UserController.
    public UserController() {
        this.userList = new ArrayList<>();
        this.currentSession = null;
        initializeDefaultUsers();
    }
    
    // Inicializa usuarios por defecto para pruebas.
    private void initializeDefaultUsers() {
        // Usuario administrador por defecto
        userList.add(new Administrator("admin", "admin123", "admin@ega.com"));
        
        // Usuario editor por defecto
        userList.add(new Editor("editor", "editor123", "editor@ega.com"));
    }
    
    // Inicia sesión con nombre de usuario y contraseña.
    public boolean login(String username, String password) {
        for (User user : userList) {
            if (user.getUsername().equals(username) && user.authenticate(password)) {
                this.currentSession = user;
                return true;
            }
        }
        return false;
    }
    
    // Cierra la sesión actual.
    public void logout() {
        this.currentSession = null;
    }
    
   // Valida si el usuario actual tiene permiso para una acción específica.
    public boolean validatePermission(String action) {
        if (currentSession == null) {
            return false;
        }
        return currentSession.getPermissions().contains(action);
    }
    
    // Registra un nuevo usuario.
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
    
    // Obtiene el usuario actualmente autenticado.
    public User getCurrentUser() {
        return currentSession;
    }
    
    // Verifica si hay un usuario autenticado.
    public boolean isLoggedIn() {
        return currentSession != null;
    }
    
    // Obtiene todos los usuarios registrados.
    public List<User> getAllUsers() {
        return new ArrayList<>(userList);
    }
    
    // Busca un usuario por nombre de usuario.
    public User findUserByUsername(String username) {
        return userList.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }
}
