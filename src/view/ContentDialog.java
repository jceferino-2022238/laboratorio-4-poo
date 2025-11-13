package view;

import model.*;
import model.Image;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Diálogo para crear y editar contenidos.
 * Muestra formulario dinámico según el tipo de contenido seleccionado.
 *
 * @author Franco Paiz
 * @version 1.0
 */
public class ContentDialog extends JDialog {
    private JTextField txtTitle;
    private JTextField txtAuthor;
    private JComboBox<Category> cmbCategory;
    private JComboBox<String> cmbContentType;
    private JPanel dynamicPanel;
    private CardLayout cardLayout;
    private JButton btnSave;
    private JButton btnCancel;

    // Campos para Article
    private JTextArea txtArticleContent;

    // Campos para Video
    private JTextField txtVideoUrl;
    private JTextField txtDuration;
    private JTextField txtResolution;

    // Campos para Image
    private JTextField txtImageUrl;
    private JTextField txtDimensions;
    private JTextField txtFormat;

    private Content resultContent;
    private Content editingContent;
    private List<Category> categories;
    private boolean isEditMode;

    /**
     * Constructor para crear nuevo contenido.
     *
     * @param parent frame padre
     * @param categories lista categorías disponibles
     */
    public ContentDialog(Frame parent, List<Category> categories) {
        this(parent, categories, null);
    }

    /**
     * Constructor para editar contenido existente.
     *
     * @param parent frame padre
     * @param categories lista categorías
     * @param content contenido a editar
     */
    public ContentDialog(Frame parent, List<Category> categories, Content content) {
        super(parent, content == null ? "Nuevo Contenido" : "Editar Contenido", true);
        this.categories = categories;
        this.editingContent = content;
        this.isEditMode = (content != null);
        this.resultContent = null;
        initComponents();

        if (isEditMode) {
            loadContentData(content);
        }
    }

    /**
     * Inicializa los componentes del diálogo.
     */
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Panel superior con campos comunes
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Panel central con campos dinámicos
        dynamicPanel = createDynamicPanel();
        add(dynamicPanel, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setMinimumSize(new Dimension(500, 450));
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * Crea el panel superior con campos comunes.
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Información General"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Título:"), gbc);

        txtTitle = new JTextField(30);
        gbc.gridx = 1;
        panel.add(txtTitle, gbc);

        // Autor
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Autor:"), gbc);

        txtAuthor = new JTextField(30);
        gbc.gridx = 1;
        panel.add(txtAuthor, gbc);

        // Categoría
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Categoría:"), gbc);

        cmbCategory = new JComboBox<>(categories.toArray(new Category[0]));
        gbc.gridx = 1;
        panel.add(cmbCategory, gbc);

        // Tipo de contenido
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Tipo:"), gbc);

        String[] types = {"Article", "Video", "Image"};
        cmbContentType = new JComboBox<>(types);
        cmbContentType.addActionListener(e -> updateDynamicPanel());
        gbc.gridx = 1;
        panel.add(cmbContentType, gbc);

        if (isEditMode) {
            cmbContentType.setEnabled(false); // No cambiar tipo al editar
        }

        return panel;
    }

    /**
     * Crea el panel dinámico con CardLayout.
     */
    private JPanel createDynamicPanel() {
        cardLayout = new CardLayout();
        JPanel panel = new JPanel(cardLayout);

        // Panel para Article
        panel.add(createArticlePanel(), "Article");

        // Panel para Video
        panel.add(createVideoPanel(), "Video");

        // Panel para Image
        panel.add(createImagePanel(), "Image");

        return panel;
    }

    /**
     * Crea panel específico para artículos.
     */
    private JPanel createArticlePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Contenido del Artículo"));

        JLabel lblInfo = new JLabel("Contenido (mínimo 50 caracteres):");
        panel.add(lblInfo, BorderLayout.NORTH);

        txtArticleContent = new JTextArea(10, 40);
        txtArticleContent.setLineWrap(true);
        txtArticleContent.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtArticleContent);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crea panel específico para videos.
     */
    private JPanel createVideoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Detalles del Video"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // URL
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("URL:"), gbc);

        txtVideoUrl = new JTextField(30);
        gbc.gridx = 1;
        panel.add(txtVideoUrl, gbc);

        // Duración
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Duración (segundos):"), gbc);

        txtDuration = new JTextField(30);
        gbc.gridx = 1;
        panel.add(txtDuration, gbc);

        // Resolución
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Resolución:"), gbc);

        txtResolution = new JTextField(30);
        txtResolution.setText("1080p");
        gbc.gridx = 1;
        panel.add(txtResolution, gbc);

        return panel;
    }

    /**
     * Crea panel específico para imágenes.
     */
    private JPanel createImagePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Detalles de la Imagen"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // URL
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("URL:"), gbc);

        txtImageUrl = new JTextField(30);
        gbc.gridx = 1;
        panel.add(txtImageUrl, gbc);

        // Dimensiones
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Dimensiones (WxH):"), gbc);

        txtDimensions = new JTextField(30);
        txtDimensions.setText("1920x1080");
        gbc.gridx = 1;
        panel.add(txtDimensions, gbc);

        // Formato
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Formato:"), gbc);

        txtFormat = new JTextField(30);
        txtFormat.setText("PNG");
        gbc.gridx = 1;
        panel.add(txtFormat, gbc);

        return panel;
    }

    /**
     * Crea el panel inferior con botones.
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        btnSave = new JButton(isEditMode ? "Guardar Cambios" : "Crear Contenido");
        btnSave.setPreferredSize(new Dimension(150, 35));
        btnSave.addActionListener(e -> saveContent());
        panel.add(btnSave);

        btnCancel = new JButton("Cancelar");
        btnCancel.setPreferredSize(new Dimension(100, 35));
        btnCancel.addActionListener(e -> {
            resultContent = null;
            dispose();
        });
        panel.add(btnCancel);

        return panel;
    }

    /**
     * Actualiza el panel dinámico según el tipo seleccionado.
     */
    private void updateDynamicPanel() {
        String selectedType = (String) cmbContentType.getSelectedItem();
        cardLayout.show(dynamicPanel, selectedType);
    }

    /**
     * Carga datos del contenido en modo edición.
     */
    private void loadContentData(Content content) {
        txtTitle.setText(content.getTitle());
        txtAuthor.setText(content.getAuthor());
        cmbCategory.setSelectedItem(content.getCategory());

        String type = content.getContentType();
        cmbContentType.setSelectedItem(type);
        updateDynamicPanel();

        if (content instanceof Article) {
            Article article = (Article) content;
            txtArticleContent.setText(article.getContent());
        } else if (content instanceof Video) {
            Video video = (Video) content;
            txtVideoUrl.setText(video.getUrl());
            txtDuration.setText(String.valueOf(video.getDuration()));
            txtResolution.setText(video.getResolution());
        } else if (content instanceof Image) {
            Image image = (Image) content;
            txtImageUrl.setText(image.getUrl());
            txtDimensions.setText(image.getDimensions());
            txtFormat.setText(image.getFormat());
        }
    }

    /**
     * Válida los campos del formulario.
     */
    private boolean validateFields() {
        if (txtTitle.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El título es requerido",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (txtAuthor.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El autor es requerido",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String type = (String) cmbContentType.getSelectedItem();

        if ("Article".equals(type)) {
            if (txtArticleContent.getText().trim().length() < 50) {
                JOptionPane.showMessageDialog(this,
                        "El contenido del artículo debe tener al menos 50 caracteres",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else if ("Video".equals(type)) {
            if (txtVideoUrl.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "La URL del video es requerida",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            try {
                int duration = Integer.parseInt(txtDuration.getText().trim());
                if (duration <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "La duración debe ser un número entero positivo",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else if ("Image".equals(type)) {
            if (txtImageUrl.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "La URL de la imagen es requerida",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }

    /**
     * Guarda el contenido creado o editado.
     */
    private void saveContent() {
        if (!validateFields()) {
            return;
        }

        String title = txtTitle.getText().trim();
        String author = txtAuthor.getText().trim();
        Category category = (Category) cmbCategory.getSelectedItem();
        String type = (String) cmbContentType.getSelectedItem();

        if ("Article".equals(type)) {
            String content = txtArticleContent.getText().trim();
            if (isEditMode && editingContent instanceof Article) {
                Article article = (Article) editingContent;
                article.setTitle(title);
                article.setAuthor(author);
                article.setCategory(category);
                article.setContent(content);
                resultContent = article;
            } else {
                resultContent = new Article(title, author, category, content);
            }
        } else if ("Video".equals(type)) {
            String url = txtVideoUrl.getText().trim();
            int duration = Integer.parseInt(txtDuration.getText().trim());
            String resolution = txtResolution.getText().trim();

            if (isEditMode && editingContent instanceof Video) {
                Video video = (Video) editingContent;
                video.setTitle(title);
                video.setAuthor(author);
                video.setCategory(category);
                video.setUrl(url);
                video.setDuration(duration);
                video.setResolution(resolution);
                resultContent = video;
            } else {
                resultContent = new Video(title, author, category, url, duration, resolution);
            }
        } else if ("Image".equals(type)) {
            String url = txtImageUrl.getText().trim();
            String dimensions = txtDimensions.getText().trim();
            String format = txtFormat.getText().trim();

            if (isEditMode && editingContent instanceof Image) {
                Image image = (Image) editingContent;
                image.setTitle(title);
                image.setAuthor(author);
                image.setCategory(category);
                image.setUrl(url);
                image.setDimensions(dimensions);
                image.setFormat(format);
                resultContent = image;
            } else {
                resultContent = new Image(title, author, category, url, dimensions, format);
            }
        }

        dispose();
    }

    /**
     * Obtiene el contenido creado o editado.
     *
     * @return contenido resultado o null si cancela
     */
    public Content getContent() {
        return resultContent;
    }
}