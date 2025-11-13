package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Clase base abstracta para todos los paneles.
 * @author Franco Paiz
 * @version 1.0
 */
public abstract class BasePanel extends JPanel {
    protected Font defaultFont;
    protected Font titleFont;
    protected Font boldFont;


    public BasePanel() {
        this.defaultFont = new Font("Arial", Font.PLAIN, 12);
        this.titleFont = new Font("Arial", Font.BOLD, 16);
        this.boldFont = new Font("Arial", Font.BOLD, 12);
        setupLayout();
    }

    /**
     * Configura el layout por defecto (BorderLayout).
     */
    protected void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    /**
     * Método abstracto para paneles
     */
    protected abstract void initComponents();

    /**
     * Crea un botón con estilo consistente.
     *
     * @param text texto del botón
     * @return botón estilizado
     */
    protected JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(boldFont);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 35));
        return button;
    }

    /**
     * Crea un borde con título.
     *
     * @param title título del borde
     * @return borde con título
     */
    protected TitledBorder createTitledBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(title);
        border.setTitleFont(boldFont);
        return border;
    }

    /**
     * Muestra un mensaje de información.
     *
     * @param message mensaje a mostrar
     */
    protected void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Información",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra un mensaje de error.
     *
     * @param error mensaje de error
     */
    protected void showError(String error) {
        JOptionPane.showMessageDialog(this, error, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Muestra un diálogo de confirmación.
     *
     * @param message mensaje de confirmación
     * @return true si el usuario confirma
     */
    protected boolean showConfirmation(String message) {
        int result = JOptionPane.showConfirmDialog(this, message, "Confirmación",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }
}