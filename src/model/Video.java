package model;

import java.util.Date;

/**
 * Representa contenido de video.
 * Hereda de Content.
 * 
 * @author ...
 * @version 1.0
 */
public class Video extends Content {
    private String url;
    private int duration;
    private String resolution;
    
    public Video(String title, String author, Category category, String url, int duration, String resolution) {
        super(title, author, category);
        this.url = url;
        this.duration = duration;
        this.resolution = resolution;
    }
    
    @Override
    public void publish() {
        if (url != null && !url.trim().isEmpty() && duration > 0) {
            this.status = "PUBLISHED";
            this.publishDate = new Date();
        }
    }
    
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
    
    private String formatDuration() {
        int hours = duration / 3600;
        int minutes = (duration % 3600) / 60;
        int seconds = duration % 60;
        return (hours > 0)
                ? String.format("%d:%02d:%02d", hours, minutes, seconds)
                : String.format("%d:%02d", minutes, seconds);
    }
    
    public String getThumbnail() {
        return url.replace(".mp4", "_thumbnail.jpg");
    }
    
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; this.lastModified = new Date(); }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; this.lastModified = new Date(); }
    public String getResolution() { return resolution; }
    public void setResolution(String resolution) { this.resolution = resolution; this.lastModified = new Date(); }
}
