package interfaces;

import model.Category;
import model.Tag;
import java.util.List;

/**
 * Interfaz que estandariza funcionalidades de búsqueda y filtrado.
 * Garantiza experiencia consistente de búsqueda en el sistema.
 * 
 * @author Junior...
 * @version 1.0
 */
public interface ISearchable<T> {
    
    /**
     * Busca entidades por palabra clave.
     * 
     * @param keyword palabra clave a buscar
     * @return lista de entidades que coinciden
     */
    List<T> searchByKeyword(String keyword);
    
    /**
     * Filtra entidades por categoría.
     * 
     * @param category categoría para filtrar
     * @return lista de entidades de esa categoría
     */
    List<T> filterByCategory(Category category);
    
    /**
     * Filtra entidades por tipo.
     * 
     * @param type tipo de entidad (Article, Video, Image)
     * @return lista de entidades de ese tipo
     */
    List<T> filterByType(String type);
    
    /**
     * Filtra entidades por etiqueta.
     * 
     * @param tag etiqueta para filtrar
     * @return lista de entidades con esa etiqueta
     */
    List<T> filterByTag(Tag tag);
}
