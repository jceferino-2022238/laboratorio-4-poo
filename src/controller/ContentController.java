package controller;

import interfaces.IManageable;
import interfaces.ISearchable;
import model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Controlador que gestiona la creación, edición, eliminación y búsqueda de contenidos.
public class ContentController implements IManageable<Content>, ISearchable<Content> {
    private List<Content> contentList;
    private User currentUser;
    
    // Constructor de ContentController.
    public ContentController() {
        this.contentList = new ArrayList<>();
        this.currentUser = null;
    }
    
    // Establece el usuario actual para verificar permisos.
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    // Crea un nuevo contenido.
    @Override
    public void create(Content content) {
        if (currentUser != null && currentUser.getPermissions().contains("CREATE")) {
            contentList.add(content);
        }
    }
    
    // Edita un contenido existente.
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
    
    // Elimina un contenido por su ID.
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
    
    // Obtiene un contenido por su ID.
    @Override
    public Content getById(String id) {
        return contentList.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    // Publica un contenido.
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
    
    // Despublica un contenido.
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
    
    // Busca contenidos por palabra clave en título o autor.
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
    
    // Filtra contenidos por categoría.
    @Override
    public List<Content> filterByCategory(Category category) {
        if (category == null) {
            return new ArrayList<>(contentList);
        }
        
        return contentList.stream()
                .filter(c -> c.getCategory().equals(category))
                .collect(Collectors.toList());
    }
    
    // Filtra contenidos por tipo (Artículo, Video, Imagen).
    @Override
    public List<Content> filterByType(String type) {
        if (type == null || type.equals("Todos")) {
            return new ArrayList<>(contentList);
        }
        
        return contentList.stream()
                .filter(c -> c.getContentType().equals(type))
                .collect(Collectors.toList());
    }
    
    // Filtra contenidos por etiqueta (tag).
    @Override
    public List<Content> filterByTag(Tag tag) {
        if (tag == null) {
            return new ArrayList<>(contentList);
        }
        
        return contentList.stream()
                .filter(c -> c.getTags().contains(tag))
                .collect(Collectors.toList());
    }
    
    // Obtiene todos los contenidos.
    public List<Content> getAllContent() {
        return new ArrayList<>(contentList);
    }
    
    // Obtiene solo los contenidos publicados.
    public List<Content> getPublishedContent() {
        return contentList.stream()
                .filter(Content::isPublished)
                .collect(Collectors.toList());
    }
    
    // Obtiene contenidos por estado (PUBLISHED, DRAFT).
    public List<Content> getContentByStatus(String status) {
        return contentList.stream()
                .filter(c -> c.getStatus().equals(status))
                .collect(Collectors.toList());
    }
}
