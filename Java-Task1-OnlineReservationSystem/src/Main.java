import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Database.initialize();

        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}