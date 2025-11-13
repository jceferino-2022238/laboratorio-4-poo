package view;

import controller.*;
import model.*;
import model.Image;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Ventana principal del CMS.
 * Contiene menú de navegación y panel central con CardLayout para cambiar vistas.
 *
 * @author Franco Paiz
 * @version 1.0
 */
public class MainFrame extends JFrame {
    private ContentController contentController;
    private UserController userController;
    private ReportController reportController;

    private ContentPanel contentPanel;
    private ReportPanel reportPanel;

    private JMenuBar menuBar;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private User currentUser;
    private JLabel statusLabel;

    private List<Category> categories;

    /**
     * Constructor del frame principal.
     *
     * @param userController controlador de usuarios
     * @param currentUser usuario autenticado
     */
    public MainFrame(UserController userController, User currentUser) {
        this.userController = userController;
        this.currentUser = currentUser;

        // Inicializar controladores
        this.contentController = new ContentController();
        contentController.setCurrentUser(currentUser);
        this.reportController = new ReportController(contentController);

        // Inicializar categorías de prueba
        initializeCategories();

        // Inicializar contenidos de ejemplo
        initializeSampleContent();

        // Configurar frame
        initialize();
    }

    /**
     * Inicializa categorías de prueba.
     */
    private void initializeCategories() {
        categories = new ArrayList<>();
        categories.add(new Category("Programación", "Contenidos sobre programación"));
        categories.add(new Category("Matemáticas", "Contenidos sobre matemáticas"));
        categories.add(new Category("Física", "Contenidos sobre física"));
        categories.add(new Category("Diseño", "Contenidos sobre diseño gráfico"));
        categories.add(new Category("Música", "Contenidos sobre teoría musical"));
    }

    /**
     * Inicializa contenidos de ejemplo para demostración.
     */
    private void initializeSampleContent() {
        // Artículo ejemplo
        Article article1 = new Article(
                "Introducción a Java",
                "Dr. García",
                categories.get(0),
                "Java es un lenguaje de programación de alto nivel, orientado a objetos y " +
                        "diseñado para tener pocas dependencias de implementación. Es uno de los " +
                        "lenguajes más populares en la actualidad y se utiliza ampliamente en el " +
                        "desarrollo de aplicaciones empresariales, móviles y web."
        );
        article1.publish();
        contentController.create(article1);

        // Video ejemplo
        Video video1 = new Video(
                "Tutorial de POO",
                "Prof. Martínez",
                categories.get(0),
                "https://example.com/video1.mp4",
                1800,
                "1080p"
        );
        video1.publish();
        contentController.create(video1);

        // Imagen ejemplo
        Image image1 = new Image(
                "Diagrama UML",
                "Ing. López",
                categories.get(0),
                "https://example.com/uml-diagram.png",
                "1920x1080",
                "PNG"
        );
        contentController.create(image1);

        Article article2 = new Article(
                "Cálculo Diferencial",
                "Dr. Ramírez",
                categories.get(1),
                "El cálculo diferencial es una rama de las matemáticas que estudia las " +
                        "tasas de cambio de las funciones. Es fundamental para el análisis " +
                        "matemático y tiene aplicaciones en física, ingeniería y economía."
        );
        contentController.create(article2);
    }

    /**
     * Inicializa la ventana y sus componentes.
     */
    private void initialize() {
        setTitle("CMS - Estudio de Grabación Audiovisual");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        // Crear menú
        createMenuBar();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Crear paneles
        contentPanel = new ContentPanel(contentController, currentUser, categories);
        reportPanel = new ReportPanel(reportController);

        mainPanel.add(contentPanel, "CONTENT");
        mainPanel.add(reportPanel, "REPORT");

        // Agregar al frame
        add(mainPanel, BorderLayout.CENTER);

        createStatusBar();

        // Mostrar panel de contenidos por defecto
        showContentPanel();
    }

    /**
     * Crea la barra de menú.
     */
    private void createMenuBar() {
        menuBar = new JMenuBar();

        // Menú Archivo
        JMenu menuFile = new JMenu("Archivo");

        JMenuItem itemExit = new JMenuItem("Salir");
        itemExit.addActionListener(e -> {
            if (confirmExit()) {
                System.exit(0);
            }
        });
        menuFile.add(itemExit);

        // Menú Contenidos
        JMenu menuContent = new JMenu("Contenidos");

        JMenuItem itemViewContent = new JMenuItem("Gestionar Contenidos");
        itemViewContent.addActionListener(e -> showContentPanel());
        menuContent.add(itemViewContent);

        menuContent.addSeparator();

        JMenuItem itemNewContent = new JMenuItem("Nuevo Contenido");
        itemNewContent.addActionListener(e -> {
            showContentPanel();
            // El panel de contenidos maneja la creación
        });
        itemNewContent.setEnabled(currentUser.getPermissions().contains("CREATE"));
        menuContent.add(itemNewContent);

        // Menú Reportes
        JMenu menuReports = new JMenu("Reportes");

        JMenuItem itemViewReports = new JMenuItem("Ver Reportes");
        itemViewReports.addActionListener(e -> showReportPanel());
        menuReports.add(itemViewReports);

        // Menú Usuario
        JMenu menuUser = new JMenu("Usuario");

        JMenuItem itemProfile = new JMenuItem("Perfil: " + currentUser.getUsername());
        itemProfile.setEnabled(false);
        menuUser.add(itemProfile);

        JMenuItem itemRole = new JMenuItem("Rol: " + currentUser.getRole());
        itemRole.setEnabled(false);
        menuUser.add(itemRole);

        menuUser.addSeparator();

        JMenuItem itemLogout = new JMenuItem("Cerrar Sesión");
        itemLogout.addActionListener(e -> logout());
        menuUser.add(itemLogout);

        // Menú Ayuda
        JMenu menuHelp = new JMenu("Ayuda");

        JMenuItem itemAbout = new JMenuItem("Acerca de");
        itemAbout.addActionListener(e -> showAbout());
        menuHelp.add(itemAbout);

        // Agregar menús a la barra
        menuBar.add(menuFile);
        menuBar.add(menuContent);
        menuBar.add(menuReports);
        menuBar.add(menuUser);
        menuBar.add(menuHelp);

        setJMenuBar(menuBar);
    }

    /**
     * Crea la barra de estado.
     */
    private void createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEtchedBorder());

        statusLabel = new JLabel(" Usuario: " + currentUser.getUsername() +
                " | Rol: " + currentUser.getRole());
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        statusBar.add(statusLabel, BorderLayout.WEST);

        JLabel timeLabel = new JLabel(new java.text.SimpleDateFormat(
                "dd/MM/yyyy HH:mm").format(new java.util.Date()) + " ");
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        statusBar.add(timeLabel, BorderLayout.EAST);

        add(statusBar, BorderLayout.SOUTH);
    }

    /**
     * Muestra el panel de contenidos.
     */
    private void showContentPanel() {
        cardLayout.show(mainPanel, "CONTENT");
        contentPanel.loadContentTable();
    }

    /**
     * Muestra el panel de reportes.
     */
    private void showReportPanel() {
        cardLayout.show(mainPanel, "REPORT");
        reportPanel.refreshReport();
    }

    /**
     * Cierra sesión y vuelve al login.
     */
    private void logout() {
        int option = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de cerrar sesión?",
                "Cerrar Sesión",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            userController.logout();
            dispose();

            LoginDialog loginDialog = new LoginDialog(null, userController);
            loginDialog.setVisible(true);

            User user = loginDialog.getAuthenticatedUser();
            if (user != null) {
                SwingUtilities.invokeLater(() -> {
                    MainFrame newFrame = new MainFrame(userController, user);
                    newFrame.setVisible(true);
                });
            }
        }
    }


    private boolean confirmExit() {
        int option = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de salir del sistema?",
                "Confirmar Salida",
                JOptionPane.YES_NO_OPTION);
        return option == JOptionPane.YES_OPTION;
    }

    private void showAbout() {
        String message = "Sistema de Gestión de Contenidos (CMS)\n\n" +
                "Estudio de Grabación Audiovisual (EGA)\n\n" +
                "Desarrollado por:\n" +
                "Ceferino, Paiz, Junior\n\n" +
                "Laboratorio 4 - Polimorfismo\n" +
                "Programación Orientada a Objetos\n\n" +
                "Versión 1.0 - 2024";

        JOptionPane.showMessageDialog(this, message,
                "Acerca de CMS EGA", JOptionPane.INFORMATION_MESSAGE);
    }
}