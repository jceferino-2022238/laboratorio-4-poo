package model;

import java.util.Date;

/**
 * Representa contenido de imagen.
 * Hereda de Content y agrega propiedades específicas de archivos gráficos.
 * 
 * @author Ceferino, Paiz, Junior
 * @version 1.0
 */
public class Image extends Content {
    private String url;
    private String dimensions; // formato "WIDTHxHEIGHT"
    private String format; // JPG, PNG, GIF, etc.
    
    /**
     * Constructor de Image.
     * 
     * @param title título de la imagen
     * @param author autor de la imagen
     * @param category categoría de la imagen
     * @param url URL del archivo de imagen
     * @param dimensions dimensiones (ej: "1920x1080")
     * @param format formato de archivo (ej: "PNG")
     */
    public Image(String title, String author, Category category, String url, String dimensions, String format) {
        super(title, author, category);
        this.url = url;
        this.dimensions = dimensions;
        this.format = format.toUpperCase();
    }
    
    /**
     * Publica la imagen validando que tenga URL y formato válidos.
     */
    @Override
    public void publish() {
        if (url != null && !url.trim().isEmpty() && format != null) {
            this.status = "PUBLISHED";
            this.publishDate = new Date();
        }
    }
    
    /**
     * Retorna representación visual de la imagen.
     * 
     * @return string con información de la imagen
     */
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
    
    /**
     * Calcula el tamaño estimado del archivo en bytes.
     * Esta es una estimación simplificada basada en dimensiones.
     * 
     * @return tamaño estimado en bytes
     */
    public long getFileSize() {
        try {
            String[] parts = dimensions.split("x");
            int width = Integer.parseInt(parts[0]);
            int height = Integer.parseInt(parts[1]);
            
            // Estimación simple: 3 bytes por pixel (RGB) con compresión 50%
            return (long) (width * height * 3 * 0.5);
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Obtiene el tamaño formateado en KB o MB.
     * 
     * @return string con tamaño formateado
     */
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
