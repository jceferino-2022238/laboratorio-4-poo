package model;

import java.util.*;

/**
 * Genera reportes y estadísticas sobre los contenidos publicados.
 * 
 * @author Ceferino, Paiz, Junior
 * @version 1.0
 */
public class Report {
    private String reportId;
    private String reportType;
    private Date generationDate;
    private Map<String, Object> data;
    
    /**
     * Constructor de Report.
     * 
     * @param reportType tipo de reporte
     */
    public Report(String reportType) {
        this.reportId = UUID.randomUUID().toString();
        this.reportType = reportType;
        this.generationDate = new Date();
        this.data = new HashMap<>();
    }
    
    /**
     * Agrega un dato al reporte.
     * 
     * @param key clave del dato
     * @param value valor del dato
     */
    public void addData(String key, Object value) {
        data.put(key, value);
    }
    
    /**
     * Genera resumen del reporte en formato texto.
     * 
     * @return string con el resumen
     */
    public String generateSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== REPORTE: ").append(reportType).append(" ===\n");
        sb.append("Generado: ").append(generationDate).append("\n\n");
        
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        
        return sb.toString();
    }
    
    /**
     * Exporta el reporte a formato CSV.
     * 
     * @return string en formato CSV
     */
    public String exportToCSV() {
        StringBuilder csv = new StringBuilder();
        csv.append("Clave,Valor\n");
        
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            csv.append(entry.getKey()).append(",").append(entry.getValue()).append("\n");
        }
        
        return csv.toString();
    }
    
    // Getters
    public String getReportId() {
        return reportId;
    }
    
    public String getReportType() {
        return reportType;
    }
    
    public Date getGenerationDate() {
        return generationDate;
    }
    
    public Map<String, Object> getData() {
        return new HashMap<>(data);
    }
}
