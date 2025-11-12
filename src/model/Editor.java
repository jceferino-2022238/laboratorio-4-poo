package model;

import java.util.Arrays;
import java.util.List;

/**
 * Usuario con permisos limitados.
 * Solo puede crear y editar contenido, pero no publicar ni eliminar.
 * 
 * @author Ceferino, Paiz, Junior
 * @version 1.0
 */
public class Editor extends User {
    
    /**
     * Constructor de Editor.
     * 
     * @param username nombre de usuario
     * @param password contraseña
     * @param email correo electrónico
     */
    public Editor(String username, String password, String email) {
        super(username, password, email, "EDITOR");
    }
    
    /**
     * Retorna lista limitada de permisos para editor.
     * 
     * @return lista con permisos de crear y editar
     */
    @Override
    public List<String> getPermissions() {
        return Arrays.asList("CREATE", "EDIT");
    }
    
    /**
     * Verifica si el editor tiene un permiso específico.
     * 
     * @param permission permiso a verificar
     * @return true si tiene el permiso
     */
    public boolean hasPermission(String permission) {
        return getPermissions().contains(permission);
    }
    
    /**
     * Solicita publicación de un contenido a un administrador.
     * En una implementación real, esto enviaría una notificación.
     * 
     * @param content contenido a solicitar publicación
     * @return mensaje de confirmación
     */
    public String requestPublication(Content content) {
        return "Solicitud de publicación enviada para: " + content.getTitle();
    }
}
