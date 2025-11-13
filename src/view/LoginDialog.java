package view;

import controller.UserController;
import model.User;
import javax.swing.*;
import java.awt.*;

/**
 * Diálogo modal para autenticación de usuarios.
 * Captura credenciales y valida con UserController.
 *
 * @author Ceferino, Paiz, Junior
 * @version 1.0
 */
public class LoginDialog extends JDialog {
    private UserController userController;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnCancel;
    private JLabel lblError;
    private User authenticatedUser;

    /**
     * Constructor del diálogo.
     *
     * @param parent frame padre
     * @param userController controlador de usuarios
     */
    public LoginDialog(Frame parent, UserController userController) {
        super(parent, "Iniciar Sesión - CMS EGA", true);
        this.userController = userController;
        this.authenticatedUser = null;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel lblTitle = new JLabel("Sistema de Gestión de Contenidos");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitle, gbc);

        // Subtítulo
        JLabel lblSubtitle = new JLabel("Estudio de Grabación Audiovisual (EGA)");
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        add(lblSubtitle, gbc);

        // Espacio
        gbc.gridy = 2;
        add(Box.createVerticalStrut(20), gbc);

        // Usuario
        gbc.gridwidth = 1;
        gbc.gridy = 3;
        gbc.gridx = 0;
        add(new JLabel("Usuario:"), gbc);

        txtUsername = new JTextField(15);
        gbc.gridx = 1;
        add(txtUsername, gbc);

        // Contraseña
        gbc.gridy = 4;
        gbc.gridx = 0;
        add(new JLabel("Contraseña:"), gbc);

        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        add(txtPassword, gbc);

        // Label de error
        lblError = new JLabel(" ");
        lblError.setForeground(Color.RED);
        lblError.setFont(new Font("Arial", Font.PLAIN, 11));
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(lblError, gbc);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        btnLogin = new JButton("Ingresar");
        btnLogin.setPreferredSize(new Dimension(100, 30));
        btnLogin.addActionListener(e -> handleLogin());
        buttonPanel.add(btnLogin);

        btnCancel = new JButton("Cancelar");
        btnCancel.setPreferredSize(new Dimension(100, 30));
        btnCancel.addActionListener(e -> {
            authenticatedUser = null;
            dispose();
        });
        buttonPanel.add(btnCancel);

        gbc.gridy = 6;
        add(buttonPanel, gbc);

        // Info de usuarios de prueba
        JLabel lblInfo = new JLabel("<html><center>Usuarios de prueba:<br>" +
                "admin / admin123<br>editor / editor123</center></html>");
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 10));
        lblInfo.setForeground(Color.GRAY);
        lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 7;
        add(lblInfo, gbc);

        // Enter para login
        txtPassword.addActionListener(e -> handleLogin());

        // Configuración del diálogo
        pack();
        setResizable(false);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * Maneja el intento de login.
     */
    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showError("Por favor ingrese usuario y contraseña");
            return;
        }

        if (userController.login(username, password)) {
            authenticatedUser = userController.getCurrentUser();
            dispose();
        } else {
            showError("Su usuario o contraseña son incorrectos");
            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }

    /**
     * Pone un mensaje de error.
     *
     * @param message mensaje de error
     */
    private void showError(String message) {
        lblError.setText(message);
    }

    /**
     * Obtiene el usuario autenticado.
     *
     * @return usuario autenticado o null si canceló
     */
    public User getAuthenticatedUser() {
        return authenticatedUser;
    }
}