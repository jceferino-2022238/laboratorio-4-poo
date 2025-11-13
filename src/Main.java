import controller.UserController;
import model.User;
import view.LoginDialog;
import view.MainFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Clase principal que inicia el Sistema de Gestión de Contenidos ósea el CMS
 * Muestra el diálogo de login y posteriormente la ventana principal.
 *
 * @author Franco Paiz, José Ceferino, Junior Lancerio
 * @version 1.0
 */
public class Main {

    /**
     * Método principal que inicia la aplicación.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        // Configurar Look and Feel del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo establecer el Look and Feel del sistema: "
                    + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            iniciarAplicacion();
        });
    }

    /**
     * Primero muestra el diálogo de login y luego la ventana principal.
     */
    private static void iniciarAplicacion() {
        UserController userController = new UserController();

        mostrarSplashScreen();

        // Mostrar diálogo de login
        LoginDialog loginDialog = new LoginDialog(null, userController);
        loginDialog.setVisible(true);

        User authenticatedUser = loginDialog.getAuthenticatedUser();

        if (authenticatedUser != null) {
            MainFrame mainFrame = new MainFrame(userController, authenticatedUser);
            mainFrame.setVisible(true);

            JOptionPane.showMessageDialog(mainFrame,
                    "¡Bienvenido al CMS EGA, " + authenticatedUser.getUsername() + "!\n\n" +
                            "Rol: " + authenticatedUser.getRole() + "\n" +
                            "Permisos: " + String.join(", ", authenticatedUser.getPermissions()),
                    "Bienvenida",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Si no se autenticó, cerrar app
            System.exit(0);
        }
    }

    /**
     * Muestra una pantalla de bienvenida temporal.
     */
    private static void mostrarSplashScreen() {
        JWindow splash = new JWindow();

        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        //Panel con la infoo
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(240, 240, 240));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Sistema de Gestión de Contenidos");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        infoPanel.add(title);

        infoPanel.add(Box.createVerticalStrut(10));

        JLabel subtitle = new JLabel("Estudio de Grabación Audiovisual (EGA)");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        infoPanel.add(subtitle);

        infoPanel.add(Box.createVerticalStrut(20));

        JLabel loading = new JLabel("Cargando...");
        loading.setFont(new Font("Arial", Font.ITALIC, 12));
        loading.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        infoPanel.add(loading);

        content.add(infoPanel, BorderLayout.CENTER);
        splash.setContentPane(content);
        splash.setSize(400, 200);
        splash.setLocationRelativeTo(null);
        splash.setVisible(true);

        // Mostrar por 2 segundos
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        splash.dispose();
    }
}