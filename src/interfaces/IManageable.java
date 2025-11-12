package interfaces;

// Interfaz CRUD para gestión de entidades
// Autor: Junior | Versión: 2.0
public interface IManageable<T> {

    // Crea una nueva entidad
    void create(T entity);

    // Edita una entidad existente
    void edit(T entity);

    // Elimina una entidad por su ID
    boolean delete(String id);

    // Obtiene una entidad por su ID
    T getById(String id);
}
