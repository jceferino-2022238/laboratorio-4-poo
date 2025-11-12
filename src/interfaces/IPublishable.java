package interfaces;

import java.util.Date;

// Interfaz para definir la funcionalidad de publicación
// Autor: Junior | Versión: 2.0
public interface IPublishable {

    // Publica el contenido en el sistema
    void publish();

    // Despublica el contenido del sistema
    void unpublish();

    // Verifica si el contenido está publicado
    boolean isPublished();

    // Obtiene la fecha de publicación
    Date getPublishDate();
}
