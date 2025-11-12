package model;

import interfaces.IPublishable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Clase abstracta base para todos los tipos de contenido.
 * Define atributos y comportamientos comunes.
 * Implementa IPublishable.
 * 
 * @author Junior...
 * @version 1.0
 */
public abstract class Content implements IPublishable {
    protected String id;
    protected String title;
    protected String author;
    protected Date creationDate;
    protected Date lastModified;
    protected Category category;
    protected List<Tag> tags;
    protected String status;
    protected Date publishDate;
    
    public Content(String title, String author, Category category) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.author = author;
        this.creationDate = new Date();
        this.lastModified = new Date();
        this.category = category;
        this.tags = new ArrayList<>();
        this.status = "DRAFT";
        this.publishDate = null;
    }
    
    @Override
    public abstract void publish();
    public abstract String display();
    
    @Override
    public void unpublish() {
        this.status = "DRAFT";
        this.publishDate = null;
    }
    
    @Override
    public boolean isPublished() { return "PUBLISHED".equals(this.status); }
    @Override
    public Date getPublishDate() { return publishDate; }
    
    public String getMetadata() {
        return String.format("ID: %s | Título: %s | Autor: %s | Estado: %s | Categoría: %s",
                id.substring(0, 8), title, author, status, category.getName());
    }
    
    public void addTag(Tag tag) {
        if (tag != null && !tags.contains(tag)) {
            tags.add(tag);
            tag.incrementUsage();
        }
    }
    
    public void removeTag(Tag tag) {
        if (tags.remove(tag)) tag.decrementUsage();
    }
    
    public String getContentType() { return this.getClass().getSimpleName(); }
    
    public String getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; this.lastModified = new Date(); }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; this.lastModified = new Date(); }
    public Date getCreationDate() { return creationDate; }
    public Date getLastModified() { return lastModified; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) {
        if (this.category != null) this.category.decrementContentCount();
        this.category = category;
        if (category != null) category.incrementContentCount();
        this.lastModified = new Date();
    }
    public List<Tag> getTags() { return new ArrayList<>(tags); }
    public String getStatus() { return status; }
    
    @Override
    public String toString() {
        return String.format("[%s] %s - %s", getContentType(), title, status);
    }
}
