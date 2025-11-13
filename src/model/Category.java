package model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Clase para representar categorías de contenido
// Autor: Junior | Versión: 2.0
public class Category {
    private String categoryId;
    private String name;
    private String description;
    private Category parentCategory;
    private List<Category> subcategories;
    private int contentCount;

    // Constructor con ID automático
    public Category(String name, String description) {
        this.categoryId = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.parentCategory = null;
        this.subcategories = new ArrayList<>();
        this.contentCount = 0;
    }

    // Agrega una subcategoría
    public void addSubcategory(Category category) {
        if (category != null && !subcategories.contains(category)) {
            subcategories.add(category);
            category.setParentCategory(this);
        }
    }

    // Incrementa el número de contenidos
    public void incrementContentCount() {
        this.contentCount++;
    }

    // Decrementa el número de contenidos
    public void decrementContentCount() {
        if (this.contentCount > 0) {
            this.contentCount--;
        }
    }

    // Getters y Setters principales
    public String getCategoryId() { return categoryId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Category getParentCategory() { return parentCategory; }
    public void setParentCategory(Category parentCategory) { this.parentCategory = parentCategory; }
    public List<Category> getSubcategories() { return new ArrayList<>(subcategories); }
    public int getContentCount() { return contentCount; }

    @Override
    public String toString() { return name; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Category category = (Category) obj;
        return categoryId.equals(category.categoryId);
    }

    @Override
    public int hashCode() { return categoryId.hashCode(); }
}
