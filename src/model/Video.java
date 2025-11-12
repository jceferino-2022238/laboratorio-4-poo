package model;

import java.util.Date;

// Clase que representa contenido de video
// Autor: Junior | Versión: 2.0
public class Video extends Content {
    private String url;
    private int duration; // segundos
    private String resolution;

    // Constructor con atributos de video
    public Video(String title, String author, Category category, String url, int duration, String resolution) {
        super(title, author, category);
        this.url = url;
        this.duration = duration;
        this.resolution = resolution;
    }

    // Publica si tiene URL y duración válida
    @Override
    public void publish() {
        if (url != null && !url.trim().isEmpty() && duration > 0) {
            this.status = "PUBLISHED";
            this.publishDate = new Date();
        }
    }

    // Muestra los datos del video
    @Override
    public String display() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== VIDEO ===\n");
        sb.append("Título: ").append(title).append("\n");
        sb.append("Autor: ").append(author).append("\n");
        sb.append("Duración: ").append(formatDuration()).append("\n");
        sb.append("Resolución: ").append(resolution).append("\n");
        sb.append("Categoría: ").append(category.getName()).append("\n");
        sb.append("URL: ").append(url);
        return sb.toString();
    }

    // Formatea duración en HH:MM:SS
    private String formatDuration() {
        int hours = duration / 3600;
        int minutes = (duration % 3600) / 60;
        int seconds = duration % 60;
        if (hours > 0) return String.format("%d:%02d:%02d", hours, minutes, seconds);
        return String.format("%d:%02d", minutes, seconds);
    }

    // Obtiene URL de miniatura
    public String getThumbnail() {
        return url.replace(".mp4", "_thumbnail.jpg");
    }

    // Getters y Setters
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; this.lastModified = new Date(); }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; this.lastModified = new Date(); }
    public String getResolution() { return resolution; }
    public void setResolution(String resolution) { this.resolution = resolution; this.lastModified = new Date(); }
}
