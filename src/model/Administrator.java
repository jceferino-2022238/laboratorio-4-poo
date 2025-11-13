package model;

import java.util.Arrays;
import java.util.List;

// Un usuario con todos los permisos administrativos.
public class Administrator extends User {
    
    // Constructor de Administrator.
    public Administrator(String username, String password, String email) {
        super(username, password, email, "ADMINISTRATOR");
    }
    
    // Devuelve la lista completa de permisos para un administrador.
    @Override
    public List<String> getPermissions() {
        return Arrays.asList("CREATE", "EDIT", "DELETE", "PUBLISH");
    }
    
    // Verifica si el administrador tiene un permiso específico.
    public boolean hasPermission(String permission) {
        return true;
    }
}
