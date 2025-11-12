package model;

import java.util.Arrays;
import java.util.List;

/**
 * Usuario con permisos completos.
 * Puede crear, editar, eliminar y publicar cualquier contenido.
 * 
 * @author Ceferino, Paiz, Junior
 * @version 1.0
 */
public class Administrator extends User {
    
    /**
     * Constructor de Administrator.
     * 
     * @param username nombre de usuario
     * @param password contraseña
     * @param email correo electrónico
     */
    public Administrator(String username, String password, String email) {
        super(username, password, email, "ADMINISTRATOR");
    }
    
    /**
     * Retorna lista completa de permisos para administrador.
     * 
     * @return lista con todos los permisos
     */
    @Override
    public List<String> getPermissions() {
        return Arrays.asList("CREATE", "EDIT", "DELETE", "PUBLISH");
    }
    
    /**
     * Verifica si el administrador tiene un permiso específico.
     * Los administradores siempre tienen todos los permisos.
     * 
     * @param permission permiso a verificar
     * @return siempre true
     */
    public boolean hasPermission(String permission) {
        return true;
    }
}
