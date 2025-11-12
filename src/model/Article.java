package model;

import java.util.Date;

/**
 * Representa un artículo académico o blog.
 * Hereda de Content.
 * 
 * @author ...
 * @version 1.0
 */
public class Article extends Content {
    private String content;
    private int wordCount;
    
    public Article(String title, String author, Category category, String content) {
        super(title, author, category);
        this.content = content;
        this.wordCount = calculateWordCount();
    }
    
    @Override
    public void publish() {
        if (content != null && content.trim().length() >= 50) {
            this.status = "PUBLISHED";
            this.publishDate = new Date();
        }
    }
    
    @Override
    public String display() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ARTÍCULO ===\n");
        sb.append("Título: ").append(title).append("\n");
        sb.append("Autor: ").append(author).append("\n");
        sb.append("Palabras: ").append(wordCount).append("\n");
        sb.append("Categoría: ").append(category.getName()).append("\n\n");
        sb.append("Contenido:\n").append(content);
        return sb.toString();
    }
    
    private int calculateWordCount() {
        if (content == null || content.trim().isEmpty()) return 0;
        return content.trim().split("\\s+").length;
    }
    
    public int calculateReadingTime() {
        return Math.max(1, wordCount / 200);
    }
    
    public String getContent() { return content; }
    public void setContent(String content) {
        this.content = content;
        this.wordCount = calculateWordCount();
        this.lastModified = new Date();
    }
    public int getWordCount() { return wordCount; }
}
