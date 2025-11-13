package controller;

import model.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador que genera y gestiona reportes estadísticos del sistema.
 * Procesa datos de contenidos para generar análisis.
 *
 * @author Franco Paiz
 * @version 1.0
 */
public class ReportController {
    private ContentController contentController;
    private List<Report> reportList;

    /**
     * Constructor del controlador de reportes.
     *
     * @param contentController
     */
    public ReportController(ContentController contentController) {
        this.contentController = contentController;
        this.reportList = new ArrayList<>();
    }

    /**
     * Genera un reporte general de contenidos.
     *
     * @return reporte generado
     */
    public Report generateContentReport() {
        Report report = new Report("Reporte General de Contenidos");

        List<Content> allContent = contentController.getAllContent();
        List<Content> published = contentController.getPublishedContent();

        // Estadísticas generales
        report.addData("Total de contenidos", allContent.size());
        report.addData("Contenidos publicados", published.size());
        report.addData("Contenidos en borrador", allContent.size() - published.size());

        // Contenidos por tipo (demuestra polimorfismo)
        long articles = allContent.stream().filter(c -> c instanceof Article).count();
        long videos = allContent.stream().filter(c -> c instanceof Video).count();
        long images = allContent.stream().filter(c -> c instanceof Image).count();

        report.addData("Artículos", articles);
        report.addData("Videos", videos);
        report.addData("Imágenes", images);

        reportList.add(report);
        return report;
    }

    /**
     * Obtiene estadísticas generales del sistema.
     *
     * @return mapa con estadísticas clave
     */
    public Map<String, Integer> getStatistics() {
        Map<String, Integer> stats = new HashMap<>();

        List<Content> allContent = contentController.getAllContent();

        stats.put("Total", allContent.size());
        stats.put("Publicados", (int) allContent.stream().filter(Content::isPublished).count());
        stats.put("Borradores", (int) allContent.stream().filter(c -> !c.isPublished()).count());
        stats.put("Artículos", (int) allContent.stream().filter(c -> c instanceof Article).count());
        stats.put("Videos", (int) allContent.stream().filter(c -> c instanceof Video).count());
        stats.put("Imágenes", (int) allContent.stream().filter(c -> c instanceof Image).count());

        return stats;
    }

    /**
     * Genera reporte de contenidos por categoría.
     *
     * @return mapa con cantidad de contenidos por categoría
     */
    public Map<Category, Integer> getContentsByCategory() {
        List<Content> allContent = contentController.getAllContent();

        return allContent.stream()
                .collect(Collectors.groupingBy(
                        Content::getCategory,
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));
    }

    /**
     * Genera reporte de actividad por autor.
     *
     * @return mapa con cantidad de contenidos por autor
     */
    public Map<String, Integer> getContentsByAuthor() {
        List<Content> allContent = contentController.getAllContent();

        return allContent.stream()
                .collect(Collectors.groupingBy(
                        Content::getAuthor,
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));
    }

    /**
     * Obtiene los contenidos más recientes.
     *
     * @param limit cantidad max de contenidos
     * @return lista de contenidos recientes
     */
    public List<Content> getMostRecentContent(int limit) {
        return contentController.getAllContent().stream()
                .sorted((c1, c2) -> c2.getCreationDate().compareTo(c1.getCreationDate()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Exporta un reporte a formato CSV.
     *
     * @param reportId ID del reporte a exportar
     * @param format formato de exportación
     * @return string con el reporte
     */
    public String exportReport(String reportId, String format) {
        Report report = reportList.stream()
                .filter(r -> r.getReportId().equals(reportId))
                .findFirst()
                .orElse(null);

        if (report != null && "CSV".equals(format)) {
            return report.exportToCSV();
        }

        return report != null ? report.generateSummary() : "";
    }

    /**
     * Obtiene todos los reportes generados.
     *
     * @return lista de reportes
     */
    public List<Report> getAllReports() {
        return new ArrayList<>(reportList);
    }
}