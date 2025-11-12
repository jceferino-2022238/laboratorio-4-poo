package model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Representa una categoría para clasificar contenidos.
 * Permite organización jerárquica con categorías padre e hijas.
 * 
 * @author Junior...
 * @version 1.0
 */
public class Category {
    private String categoryId;
    private String name;
    private String description;
    private Category parentCategory;
    private List<Category> subcategories;
    private int contentCount;
    
    public Category(String name, String description) {
        this.categoryId = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.parentCategory = null;
        this.subcategories = new ArrayList<>();
        this.contentCount = 0;
    }
    
    public void addSubcategory(Category category) {
        if (category != null && !subcategories.contains(category)) {
            subcategories.add(category);
            category.setParentCategory(this);
        }
    }
    
    public void incrementContentCount() {
        this.contentCount++;
    }
    
    public void decrementContentCount() {
        if (this.contentCount > 0) {
            this.contentCount--;
        }
    }
    
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
    public int hashCode() {
        return categoryId.hashCode();
    }
}
