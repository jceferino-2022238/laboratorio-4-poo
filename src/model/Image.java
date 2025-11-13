package model;

import java.util.Date;

// Esta clase representa a una imagen como un tipo de contenido.
public class Image extends Content {
    private String url;
    private String dimensions; // formato "WIDTHxHEIGHT"
    private String format; 
    
    
    // Constructor de Image.
    public Image(String title, String author, Category category, String url, String dimensions, String format) {
        super(title, author, category);
        this.url = url;
        this.dimensions = dimensions;
        this.format = format.toUpperCase();
    }
    
    // Método para publicar la imagen.
    @Override
    public void publish() {
        if (url != null && !url.trim().isEmpty() && format != null) {
            this.status = "PUBLISHED";
            this.publishDate = new Date();
        }
    }
    
    // Método para mostrar información de la imagen.
    @Override
    public String display() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== IMAGEN ===\n");
        sb.append("Título: ").append(title).append("\n");
        sb.append("Autor: ").append(author).append("\n");
        sb.append("Dimensiones: ").append(dimensions).append("\n");
        sb.append("Formato: ").append(format).append("\n");
        sb.append("Categoría: ").append(category.getName()).append("\n");
        sb.append("URL: ").append(url);
        return sb.toString();
    }
    
    // Calcula el tamaño del archivo en bytes (estimado).
    public long getFileSize() {
        try {
            String[] parts = dimensions.split("x");
            int width = Integer.parseInt(parts[0]);
            int height = Integer.parseInt(parts[1]);
            
            return (long) (width * height * 3 * 0.5);
        } catch (Exception e) {
            return 0;
        }
    }
    
    // Obtiene el tamaño del archivo en formato legible.
    public String getFormattedFileSize() {
        long bytes = getFileSize();
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else {
            return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        }
    }
    
    // Getters y Setters
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
        this.lastModified = new Date();
    }
    
    public String getDimensions() {
        return dimensions;
    }
    
    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
        this.lastModified = new Date();
    }
    
    public String getFormat() {
        return format;
    }
    
    public void setFormat(String format) {
        this.format = format.toUpperCase();
        this.lastModified = new Date();
    }
}
