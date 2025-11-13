package view;

import controller.ContentController;
import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Panel principal para gestión de contenidos.
 * Muestra tabla de contenidos, búsqueda, filtros y botones CRUD.
 *
 * @author Ceferino, Paiz, Junior
 * @version 1.0
 */
public class ContentPanel extends BasePanel {
    private ContentController contentController;
    private User currentUser;
    private List<Category> categories;

    private JTable contentTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<Category> cmbCategory;
    private JComboBox<String> cmbType;
    private JButton btnCreate;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnPublish;
    private JButton btnView;

    /**
     * Constructor del panel
     * @param contentController controlador de contenidos
     * @param currentUser usuario actual
     * @param categories lista de categorías
     */
    public ContentPanel(ContentController contentController, User currentUser,
                        List<Category> categories) {
        super();
        this.contentController = contentController;
        this.currentUser = currentUser;
        this.categories = categories;
        initComponents();
        loadContentTable();
    }

    /**
     * Inicializa componentes
     */
    @Override
    protected void initComponents() {
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Crea el panel superior con búsqueda y filtros.
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(createTitledBorder("Búsqueda y Filtros"));

        // Búsqueda
        panel.add(new JLabel("Buscar:"));
        txtSearch = new JTextField(20);
        panel.add(txtSearch);

        JButton btnSearch = createStyledButton("Buscar");
        btnSearch.addActionListener(e -> handleSearch());
        panel.add(btnSearch);

        panel.add(new JSeparator(SwingConstants.VERTICAL));


        panel.add(new JLabel("Categoría:"));
        Category allCategories = new Category("Todas", "Todas las categorías");
        Category[] categoryArray = new Category[categories.size() + 1];
        categoryArray[0] = allCategories;
        for (int i = 0; i < categories.size(); i++) {
            categoryArray[i + 1] = categories.get(i);
        }
        cmbCategory = new JComboBox<>(categoryArray);
        cmbCategory.addActionListener(e -> applyFilters());
        panel.add(cmbCategory);

        // Filtro por tipo
        panel.add(new JLabel("Tipo:"));
        String[] types = {"Todos", "Article", "Video", "Image"};
        cmbType = new JComboBox<>(types);
        cmbType.addActionListener(e -> applyFilters());
        panel.add(cmbType);

        return panel;
    }

    /**
     * Crea el panel central con la tabla de contenidos.
     */
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(createTitledBorder("Contenidos"));

        // Modelo de tabla
        String[] columns = {"ID", "Título", "Autor", "Tipo", "Categoría", "Estado", "Fecha"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        contentTable = new JTable(tableModel);
        contentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contentTable.setFont(defaultFont);
        contentTable.getTableHeader().setFont(boldFont);

        // Ajustar anchos de columnas
        contentTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        contentTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        contentTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        contentTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        contentTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        contentTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        contentTable.getColumnModel().getColumn(6).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(contentTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crea el panel inferior con botones de acciones.
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        // Botón Crear
        btnCreate = createStyledButton("Crear");
        btnCreate.addActionListener(e -> showCreateDialog());
        btnCreate.setEnabled(currentUser.getPermissions().contains("CREATE"));
        panel.add(btnCreate);

        // Botón Editar
        btnEdit = createStyledButton("Editar");
        btnEdit.addActionListener(e -> showEditDialog());
        btnEdit.setEnabled(currentUser.getPermissions().contains("EDIT"));
        panel.add(btnEdit);

        // Botón Eliminar
        btnDelete = createStyledButton("Eliminar");
        btnDelete.addActionListener(e -> handleDelete());
        btnDelete.setEnabled(currentUser.getPermissions().contains("DELETE"));
        panel.add(btnDelete);

        // Botón Publicar/Despublicar
        btnPublish = createStyledButton("Publicar");
        btnPublish.addActionListener(e -> handlePublish());
        btnPublish.setEnabled(currentUser.getPermissions().contains("PUBLISH"));
        panel.add(btnPublish);

        btnView = createStyledButton("Ver Detalles");
        btnView.addActionListener(e -> handleView());
        panel.add(btnView);

        return panel;
    }

    /**
     * Carga los contenidos en la tabla.
     */
    public void loadContentTable() {
        tableModel.setRowCount(0);
        List<Content> contents = contentController.getAllContent();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (Content content : contents) {
            Object[] row = {
                    content.getId().substring(0, 8) + "...",
                    content.getTitle(),
                    content.getAuthor(),
                    content.getContentType(),
                    content.getCategory().getName(),
                    content.getStatus(),
                    sdf.format(content.getCreationDate())
            };
            tableModel.addRow(row);
        }
    }

    /**
     * Diálogo para nuevo contenido
     */
    private void showCreateDialog() {
        ContentDialog dialog = new ContentDialog((Frame) SwingUtilities.getWindowAncestor(this),
                categories);
        dialog.setVisible(true);

        Content newContent = dialog.getContent();
        if (newContent != null) {
            contentController.create(newContent);
            loadContentTable();
            showMessage("Contenido creado exitosamente");
        }
    }

    /**
     * Dialogo pa edición
     */
    private void showEditDialog() {
        int selectedRow = contentTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Seleccione un contenido para editar");
            return;
        }

        String id = getFullId(selectedRow);
        Content content = contentController.getById(id);

        if (content != null) {
            ContentDialog dialog = new ContentDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    categories, content);
            dialog.setVisible(true);

            Content editedContent = dialog.getContent();
            if (editedContent != null) {
                contentController.edit(editedContent);
                loadContentTable();
                showMessage("Contenido actualizado exitosamente");
            }
        }
    }

    /**
     * Maneja la eliminación de contenido.
     */
    private void handleDelete() {
        int selectedRow = contentTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Seleccione un contenido para eliminar");
            return;
        }

        if (showConfirmation("¿Está seguro de eliminar este contenido?")) {
            String id = getFullId(selectedRow);
            if (contentController.delete(id)) {
                loadContentTable();
                showMessage("Contenido eliminado exitosamente");
            } else {
                showError("No se pudo eliminar el contenido");
            }
        }
    }

    /**
     * Maneja la publicación/despublicación de contenido.
     */
    private void handlePublish() {
        int selectedRow = contentTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Seleccione un contenido");
            return;
        }

        String id = getFullId(selectedRow);
        Content content = contentController.getById(id);

        if (content != null) {
            if (content.isPublished()) {
                if (contentController.unpublishContent(id)) {
                    loadContentTable();
                    showMessage("Contenido despublicado");
                }
            } else {
                if (contentController.publishContent(id)) {
                    loadContentTable();
                    showMessage("Contenido publicado exitosamente");
                } else {
                    showError("No se pudo publicar. Verifique que cumpla los requisitos.");
                }
            }
        }
    }

    /**
     * Muestra los detalles del contenido seleccionado.
     */
    private void handleView() {
        int selectedRow = contentTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Seleccione un contenido para ver");
            return;
        }

        String id = getFullId(selectedRow);
        Content content = contentController.getById(id);

        if (content != null) {
            // Demuestra polimorfismo: display() se ejecuta según el tipo real
            String details = content.display();

            JTextArea textArea = new JTextArea(details);
            textArea.setEditable(false);
            textArea.setFont(defaultFont);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));

            JOptionPane.showMessageDialog(this, scrollPane,
                    "Detalles del Contenido", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Maneja la búsqueda de contenidos.
     */
    private void handleSearch() {
        String keyword = txtSearch.getText().trim();
        List<Content> results = contentController.searchByKeyword(keyword);
        displayResults(results);
    }

    /**
     * Aplica los filtros seleccionados.
     */
    private void applyFilters() {
        Category selectedCategory = (Category) cmbCategory.getSelectedItem();
        String selectedType = (String) cmbType.getSelectedItem();

        List<Content> results = contentController.getAllContent();

        // Filtrar por categoría
        if (selectedCategory != null && !selectedCategory.getName().equals("Todas")) {
            results = contentController.filterByCategory(selectedCategory);
        }

        // Filtrar por tipo
        if (!"Todos".equals(selectedType)) {
            results = contentController.filterByType(selectedType);
        }

        // Si ambos filtros están activos, combinar
        if (selectedCategory != null && !selectedCategory.getName().equals("Todas")
                && !"Todos".equals(selectedType)) {
            List<Content> categoryFiltered = contentController.filterByCategory(selectedCategory);
            List<Content> typeFiltered = contentController.filterByType(selectedType);
            results.retainAll(typeFiltered);
        }

        displayResults(results);
    }

    /**
     * Muestra los resultados en la tabla.
     */
    private void displayResults(List<Content> contents) {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (Content content : contents) {
            Object[] row = {
                    content.getId().substring(0, 8) + "...",
                    content.getTitle(),
                    content.getAuthor(),
                    content.getContentType(),
                    content.getCategory().getName(),
                    content.getStatus(),
                    sdf.format(content.getCreationDate())
            };
            tableModel.addRow(row);
        }
    }

    /**
     * Obtiene el ID completo del contenido en la fila seleccionada.
     */
    private String getFullId(int row) {
        List<Content> contents = contentController.getAllContent();
        if (row >= 0 && row < contents.size()) {
            return contents.get(row).getId();
        }
        return null;
    }

    /**
     * Actualiza el panel cuando cambia el usuario.
     */
    public void updateUser(User user) {
        this.currentUser = user;
        btnCreate.setEnabled(user.getPermissions().contains("CREATE"));
        btnEdit.setEnabled(user.getPermissions().contains("EDIT"));
        btnDelete.setEnabled(user.getPermissions().contains("DELETE"));
        btnPublish.setEnabled(user.getPermissions().contains("PUBLISH"));
    }
}