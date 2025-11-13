package model;

import java.util.Arrays;
import java.util.List;

// Usuario con permisos limitados para crear y editar contenidos.
public class Editor extends User {
    
    // Constructor de Editor.
    public Editor(String username, String password, String email) {
        super(username, password, email, "EDITOR");
    }
    
   // Retorna la lista de permisos para un editor.
    @Override
    public List<String> getPermissions() {
        return Arrays.asList("CREATE", "EDIT");
    }
    
    // Verifica si el editor tiene un permiso específico.
    public boolean hasPermission(String permission) {
        return getPermissions().contains(permission);
    }
    
   // Simula una solicitud de publicación para revisión.
    public String requestPublication(Content content) {
        return "Solicitud de publicación enviada para: " + content.getTitle();
    }
}
