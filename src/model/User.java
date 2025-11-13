package model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

// Clase abstracta que representa a un usuario del sistema.
public abstract class User {
    protected String userId;
    protected String username;
    protected String password;
    protected String email;
    protected String role;
    protected Date registrationDate;
    
    // Constructor de User.
    public User(String username, String password, String email, String role) {
        this.userId = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.registrationDate = new Date();
    }
    
    // Método para autenticar al usuario.
    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
    
    // Método abstracto para obtener los permisos del usuario.
    public abstract List<String> getPermissions();
    
    // Método para actualizar el perfil del usuario.
    public void updateProfile(String email) {
        this.email = email;
    }
    
    // Método para cambiar la contraseña del usuario.
    public boolean changePassword(String oldPassword, String newPassword) {
        if (authenticate(oldPassword)) {
            this.password = newPassword;
            return true;
        }
        return false;
    }
    
    // Getters y Setters
    public String getUserId() {
        return userId;
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
    
    public String getRole() {
        return role;
    }
    
    public Date getRegistrationDate() {
        return registrationDate;
    }
    
    @Override
    public String toString() {
        return username + " (" + role + ")";
    }
}
