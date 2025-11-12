package controller;

import interfaces.IManageable;
import interfaces.ISearchable;
import model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador que gestiona todas las operaciones sobre contenidos.
 * Implementa IManageable e ISearchable para garantizar funcionalidad CRUD y búsqueda.
 * Utiliza polimorfismo para manejar diferentes tipos de Content.
 * 
 * @author Ceferino, Paiz, Junior
 * @version 1.0
 */
public class ContentController implements IManageable<Content>, ISearchable<Content> {
    private List<Content> contentList;
    private User currentUser;
    
    /**
     * Constructor del controlador.
     */
    public ContentController() {
        this.contentList = new ArrayList<>();
        this.currentUser = null;
    }
    
    /**
     * Establece el usuario actual para validar permisos.
     * 
     * @param user usuario actual
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    /**
     * Crea un nuevo contenido en el sistema.
     * Utiliza polimorfismo - funciona con Article, Video, Image.
     * 
     * @param content contenido a crear
     */
    @Override
    public void create(Content content) {
        if (currentUser != null && currentUser.getPermissions().contains("CREATE")) {
            contentList.add(content);
        }
    }
    
    /**
     * Edita un contenido existente.
     * 
     * @param content contenido con datos actualizados
     */
    @Override
    public void edit(Content content) {
        if (currentUser != null && currentUser.getPermissions().contains("EDIT")) {
            Content existing = getById(content.getId());
            if (existing != null) {
                int index = contentList.indexOf(existing);
                contentList.set(index, content);
            }
        }
    }
    
    /**
     * Elimina un contenido por su ID.
     * 
     * @param id identificador del contenido
     * @return true si se eliminó correctamente
     */
    @Override
    public boolean delete(String id) {
        if (currentUser != null && currentUser.getPermissions().contains("DELETE")) {
            Content content = getById(id);
            if (content != null) {
                // Decrementar contadores
                content.getCategory().decrementContentCount();
                for (Tag tag : content.getTags()) {
                    tag.decrementUsage();
                }
                return contentList.remove(content);
            }
        }
        return false;
    }
    
    /**
     * Obtiene un contenido por su ID.
     * 
     * @param id identificador del contenido
     * @return contenido encontrado o null
     */
    @Override
    public Content getById(String id) {
        return contentList.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Publica un contenido.
     * Utiliza polimorfismo - cada tipo (Article/Video/Image) ejecuta su propio publish().
     * 
     * @param id identificador del contenido
     * @return true si se publicó correctamente
     */
    public boolean publishContent(String id) {
        if (currentUser != null && currentUser.getPermissions().contains("PUBLISH")) {
            Content content = getById(id);
            if (content != null && !content.isPublished()) {
                content.publish(); // Polimorfismo: llama al método específico
                return true;
            }
        }
        return false;
    }
    
    /**
     * Despublica un contenido.
     * 
     * @param id identificador del contenido
     * @return true si se despublicó correctamente
     */
    public boolean unpublishContent(String id) {
        if (currentUser != null && currentUser.getPermissions().contains("PUBLISH")) {
            Content content = getById(id);
            if (content != null && content.isPublished()) {
                content.unpublish();
                return true;
            }
        }
        return false;
    }
    
    /**
     * Busca contenidos por palabra clave en título o autor.
     * 
     * @param keyword palabra clave
     * @return lista de contenidos que coinciden
     */
    @Override
    public List<Content> searchByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>(contentList);
        }
        
        String lowerKeyword = keyword.toLowerCase();
        return contentList.stream()
                .filter(c -> c.getTitle().toLowerCase().contains(lowerKeyword) ||
                           c.getAuthor().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }
    
    /**
     * Filtra contenidos por categoría.
     * 
     * @param category categoría para filtrar
     * @return lista de contenidos de esa categoría
     */
    @Override
    public List<Content> filterByCategory(Category category) {
        if (category == null) {
            return new ArrayList<>(contentList);
        }
        
        return contentList.stream()
                .filter(c -> c.getCategory().equals(category))
                .collect(Collectors.toList());
    }
    
    /**
     * Filtra contenidos por tipo (Article, Video, Image).
     * Demuestra polimorfismo: trabaja con la jerarquía de Content.
     * 
     * @param type tipo de contenido
     * @return lista de contenidos de ese tipo
     */
    @Override
    public List<Content> filterByType(String type) {
        if (type == null || type.equals("Todos")) {
            return new ArrayList<>(contentList);
        }
        
        return contentList.stream()
                .filter(c -> c.getContentType().equals(type))
                .collect(Collectors.toList());
    }
    
    /**
     * Filtra contenidos por etiqueta.
     * 
     * @param tag etiqueta para filtrar
     * @return lista de contenidos con esa etiqueta
     */
    @Override
    public List<Content> filterByTag(Tag tag) {
        if (tag == null) {
            return new ArrayList<>(contentList);
        }
        
        return contentList.stream()
                .filter(c -> c.getTags().contains(tag))
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene todos los contenidos.
     * 
     * @return lista completa de contenidos
     */
    public List<Content> getAllContent() {
        return new ArrayList<>(contentList);
    }
    
    /**
     * Obtiene solo contenidos publicados.
     * 
     * @return lista de contenidos publicados
     */
    public List<Content> getPublishedContent() {
        return contentList.stream()
                .filter(Content::isPublished)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene contenidos por estado.
     * 
     * @param status estado ("DRAFT" o "PUBLISHED")
     * @return lista de contenidos con ese estado
     */
    public List<Content> getContentByStatus(String status) {
        return contentList.stream()
                .filter(c -> c.getStatus().equals(status))
                .collect(Collectors.toList());
    }
}
