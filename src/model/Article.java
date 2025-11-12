package model;

import java.util.Date;

// Clase que representa un artículo o blog
// Autor: Junior | Versión: 2.0
public class Article extends Content {
    private String content;
    private int wordCount;

    // Constructor con inicialización del texto
    public Article(String title, String author, Category category, String content) {
        super(title, author, category);
        this.content = content;
        this.wordCount = calculateWordCount();
    }

    // Publica si cumple con longitud mínima
    @Override
    public void publish() {
        if (content != null && content.trim().length() >= 50) {
            this.status = "PUBLISHED";
            this.publishDate = new Date();
        }
    }

    // Muestra el artículo en formato legible
    @Override
    public String display() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ARTÍCULO ===\n");
        sb.append("Título: ").append(title).append("\n");
        sb.append("Autor: ").append(author).append("\n");
        sb.append("Palabras: ").append(wordCount).append("\n");
        sb.append("Categoría: ").append(category.getName()).append("\n");
        sb.append("\nContenido:\n").append(content);
        return sb.toString();
    }

    // Calcula cantidad de palabras
    private int calculateWordCount() {
        if (content == null || content.trim().isEmpty()) return 0;
        return content.trim().split("\\s+").length;
    }

    // Calcula tiempo estimado de lectura
    public int calculateReadingTime() {
        return Math.max(1, wordCount / 200);
    }

    // Getters y Setters
    public String getContent() { return content; }
    public void setContent(String content) {
        this.content = content;
        this.wordCount = calculateWordCount();
        this.lastModified = new Date();
    }
    public int getWordCount() { return wordCount; }
}
