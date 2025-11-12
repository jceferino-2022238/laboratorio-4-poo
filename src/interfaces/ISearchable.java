package interfaces;

import model.Category;
import model.Tag;
import java.util.List;

// Interfaz para búsqueda y filtrado de entidades
// Autor: Junior | Versión: 2.0
public interface ISearchable<T> {

    // Busca entidades por palabra clave
    List<T> searchByKeyword(String keyword);

    // Filtra por categoría
    List<T> filterByCategory(Category category);

    // Filtra por tipo de entidad
    List<T> filterByType(String type);

    // Filtra por etiqueta
    List<T> filterByTag(Tag tag);
}
