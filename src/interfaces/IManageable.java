package interfaces;

/**
 * Interfaz que define el contrato CRUD para gestión de entidades.
 * Garantiza consistencia en operaciones básicas.
 * 
 * @author Junior...
 * @version 1.0
 */
public interface IManageable<T> {
    
    /**
     * Crea una nueva entidad en el sistema.
     * 
     * @param entity la entidad a crear
     */
    void create(T entity);
    
    /**
     * Edita una entidad existente.
     * 
     * @param entity la entidad con los datos actualizados
     */
    void edit(T entity);
    
    /**
     * Elimina una entidad por su ID.
     * 
     * @param id identificador de la entidad
     * @return true si se eliminó correctamente, false en caso contrario
     */
    boolean delete(String id);
    
    /**
     * Obtiene una entidad por su ID.
     * 
     * @param id identificador de la entidad
     * @return la entidad encontrada o null si no existe
     */
    T getById(String id);
}
