package model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Clase abstracta base para todos los tipos de usuarios.
 * Define autenticación común que heredan Administrator y Editor.
 * 
 * @author Ceferino, Paiz, Junior
 * @version 1.0
 */
public abstract class User {
    protected String userId;
    protected String username;
    protected String password;
    protected String email;
    protected String role;
    protected Date registrationDate;
    
    /**
     * Constructor base para usuarios.
     * 
     * @param username nombre de usuario
     * @param password contraseña
     * @param email correo electrónico
     * @param role rol del usuario
     */
    public User(String username, String password, String email, String role) {
        this.userId = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.registrationDate = new Date();
    }
    
    /**
     * Autentica al usuario validando la contraseña.
     * 
     * @param password contraseña a validar
     * @return true si la contraseña es correcta
     */
    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
    
    /**
     * Método abstracto para obtener permisos del usuario.
     * Cada subclase define sus propios permisos.
     * 
     * @return lista de permisos
     */
    public abstract List<String> getPermissions();
    
    /**
     * Actualiza la información del perfil.
     * 
     * @param email nuevo email
     */
    public void updateProfile(String email) {
        this.email = email;
    }
    
    /**
     * Cambia la contraseña del usuario.
     * 
     * @param oldPassword contraseña anterior
     * @param newPassword nueva contraseña
     * @return true si se cambió correctamente
     */
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
