package view;

import controller.ReportController;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Panel para visualizar reportes y estadísticas del sistema.
 * Muestra gráficas, tablas de datos y opciones de exportación.
 *
 * @author Franco Paiz
 * @version 1.0
 */
public class ReportPanel extends BasePanel {
    private ReportController reportController;
    private JTextArea txtReport;
    private JPanel chartPanel;
    private JButton btnGenerate;
    private JButton btnExport;
    private JComboBox<String> cmbReportType;

    /**
     * Constructor del panel de reportes.
     *
     * @param reportController controlador de reportes
     */
    public ReportPanel(ReportController reportController) {
        super();
        this.reportController = reportController;
        initComponents();
    }

    /**
     * Inicializa componentes
     */
    @Override
    protected void initComponents() {
        // Panel superior con controles
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Panel central dividido: gráfica y texto
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        // Generar reporte inicial
        generateReport();
    }

    /**
     * Crea el panel superior con controles de reporte.
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(createTitledBorder("Configuración de Reporte"));

        panel.add(new JLabel("Tipo de Reporte:"));

        String[] reportTypes = {
                "Resumen General",
                "Por Categoría",
                "Por Autor",
                "Contenidos Recientes"
        };
        cmbReportType = new JComboBox<>(reportTypes);
        panel.add(cmbReportType);

        btnGenerate = createStyledButton("Generar");
        btnGenerate.addActionListener(e -> generateReport());
        panel.add(btnGenerate);

        return panel;
    }

    /**
     * Crea el panel central con visualizaciones.
     */
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));

        // Panel izquierdo: gráfica
        chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawChart(g);
            }
        };
        chartPanel.setBorder(createTitledBorder("Visualización"));
        chartPanel.setPreferredSize(new Dimension(400, 300));
        chartPanel.setBackground(Color.WHITE);
        panel.add(chartPanel);

        // Panel derecho: texto del reporte
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBorder(createTitledBorder("Detalles del Reporte"));

        txtReport = new JTextArea();
        txtReport.setEditable(false);
        txtReport.setFont(new Font("Courier New", Font.PLAIN, 12));
        txtReport.setLineWrap(true);
        txtReport.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(txtReport);
        textPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(textPanel);

        return panel;
    }

    /**
     * Crea el panel inferior con botones de acción.
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        btnExport = createStyledButton("Exportar CSV");
        btnExport.addActionListener(e -> exportReport());
        panel.add(btnExport);

        return panel;
    }

    /**
     * Genera el reporte según el tipo seleccionado.
     */
    private void generateReport() {
        String reportType = (String) cmbReportType.getSelectedItem();
        StringBuilder reportText = new StringBuilder();

        if ("Resumen General".equals(reportType)) {
            Report report = reportController.generateContentReport();
            reportText.append(report.generateSummary());
        } else if ("Por Categoría".equals(reportType)) {
            Map<Category, Integer> byCategory = reportController.getContentsByCategory();
            reportText.append("=== CONTENIDOS POR CATEGORÍA ===\n\n");
            for (Map.Entry<Category, Integer> entry : byCategory.entrySet()) {
                reportText.append(entry.getKey().getName())
                        .append(": ")
                        .append(entry.getValue())
                        .append(" contenidos\n");
            }
        } else if ("Por Autor".equals(reportType)) {
            Map<String, Integer> byAuthor = reportController.getContentsByAuthor();
            reportText.append("=== CONTENIDOS POR AUTOR ===\n\n");
            for (Map.Entry<String, Integer> entry : byAuthor.entrySet()) {
                reportText.append(entry.getKey())
                        .append(": ")
                        .append(entry.getValue())
                        .append(" contenidos\n");
            }
        } else if ("Contenidos Recientes".equals(reportType)) {
            java.util.List<Content> recent = reportController.getMostRecentContent(10);
            reportText.append("=== 10 CONTENIDOS MÁS RECIENTES ===\n\n");
            int i = 1;
            for (Content content : recent) {
                reportText.append(i++).append(". ")
                        .append(content.getTitle())
                        .append(" (").append(content.getContentType()).append(")\n")
                        .append("   Autor: ").append(content.getAuthor())
                        .append(" | Estado: ").append(content.getStatus())
                        .append("\n\n");
            }
        }

        txtReport.setText(reportText.toString());
        chartPanel.repaint();
    }

    /**
     * Dibuja la gráfica de barras con estadísticas.
     */
    private void drawChart(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        Map<String, Integer> stats = reportController.getStatistics();

        int width = chartPanel.getWidth();
        int height = chartPanel.getHeight();
        int margin = 50;
        int barWidth = 60;
        int spacing = 20;

        // Título
        g2d.setFont(titleFont);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Estadísticas del CMS", margin, 30);

        // Encontrar el valor máximo para escalar
        int maxValue = stats.values().stream().max(Integer::compare).orElse(1);
        if (maxValue == 0) maxValue = 1;

        // Dibujar ejes
        g2d.setColor(Color.GRAY);
        g2d.drawLine(margin, height - margin, width - margin, height - margin); // Eje X
        g2d.drawLine(margin, margin, margin, height - margin); // Eje Y

        // Datos a graficar
        String[] labels = {"Artículos", "Videos", "Imágenes", "Publicados", "Borradores"};
        String[] keys = {"Artículos", "Videos", "Imágenes", "Publicados", "Borradores"};
        Color[] colors = {
                new Color(52, 152, 219),  // Azul
                new Color(231, 76, 60),   // Rojo
                new Color(46, 204, 113),  // Verde
                new Color(241, 196, 15),  // Amarillo
                new Color(155, 89, 182)   // Púrpura
        };

        int x = margin + spacing;
        int chartHeight = height - 2 * margin;

        g2d.setFont(defaultFont);

        for (int i = 0; i < labels.length; i++) {
            int value = stats.getOrDefault(keys[i], 0);
            int barHeight = (int) ((double) value / maxValue * chartHeight * 0.8);
            int y = height - margin - barHeight;

            // Dibujar barra
            g2d.setColor(colors[i]);
            g2d.fillRect(x, y, barWidth, barHeight);

            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, barWidth, barHeight);

            g2d.drawString(String.valueOf(value), x + barWidth/2 - 10, y - 5);

            g2d.drawString(labels[i], x - 10, height - margin + 20);

            x += barWidth + spacing;
        }

        g2d.setColor(Color.GRAY);
        g2d.drawString("0", margin - 20, height - margin + 5);
        g2d.drawString(String.valueOf(maxValue), margin - 30, margin + 10);
    }

    /**
     * Exporta el reporte actual a formato CSV.
     */
    private void exportReport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Reporte");
        fileChooser.setSelectedFile(new java.io.File("reporte_cms.csv"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                java.io.FileWriter writer = new java.io.FileWriter(fileToSave);

                // Escribir encabezados
                writer.write("Métrica,Valor\n");

                // Escribir datos
                Map<String, Integer> stats = reportController.getStatistics();
                for (Map.Entry<String, Integer> entry : stats.entrySet()) {
                    writer.write(entry.getKey() + "," + entry.getValue() + "\n");
                }

                writer.close();
                showMessage("Reporte exportado exitosamente a:\n" + fileToSave.getAbsolutePath());
            } catch (Exception ex) {
                showError("Error al exportar el reporte: " + ex.getMessage());
            }
        }
    }

    /**
     * Actualiza el panel con la data.
     */
    public void refreshReport() {
        generateReport();
    }
}