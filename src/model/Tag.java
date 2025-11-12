package model;

import java.util.UUID;

/**
 * Representa una etiqueta para clasificación flexible de contenidos.
 * Permite búsqueda por palabras clave.
 * 
 * @author Junior...
 * @version 1.0
 */
public class Tag {
    private String tagId;
    private String name;
    private int usageCount;
    
    public Tag(String name) {
        this.tagId = UUID.randomUUID().toString();
        this.name = name.toLowerCase().trim();
        this.usageCount = 0;
    }
    
    public void incrementUsage() { this.usageCount++; }
    public void decrementUsage() { if (this.usageCount > 0) this.usageCount--; }
    
    public String getTagId() { return tagId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name.toLowerCase().trim(); }
    public int getUsageCount() { return usageCount; }
    
    @Override
    public String toString() { return "#" + name; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Tag tag = (Tag) obj;
        return name.equals(tag.name);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
