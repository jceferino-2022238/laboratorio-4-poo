package interfaces;

import java.util.Date;

/**
 * Interfaz que define el contrato para contenidos publicables.
 * Garantiza que cualquier tipo de contenido implemente funcionalidad de publicación.
 * 
 * @author Junior
 * @version 1.0
 */
public interface IPublishable {
    
    /**
     * Publica el contenido en el sistema.
     */
    void publish();
    
    /**
     * Despublica el contenido del sistema.
     */
    void unpublish();
    
    /**
     * Verifica si el contenido está publicado.
     * 
     * @return true si está publicado, false en caso contrario
     */
    boolean isPublished();
    
    /**
     * Obtiene la fecha de publicación del contenido.
     * 
     * @return fecha de publicación o null si no está publicado
     */
    Date getPublishDate();
}
