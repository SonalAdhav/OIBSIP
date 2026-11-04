import javax.swing.*;
import java.awt.*;

public class CancellationForm extends JFrame {

    private JTextField pnrField;
    private JTextArea detailsArea;
    private JButton cancelButton;
    private int currentPnr = -1;

    public CancellationForm() {
        setTitle("Online Reservation System - Cancel Ticket");
        setSize(450, 430);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Heading
        JLabel heading = new JLabel("Cancellation Form", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        add(heading, BorderLayout.NORTH);

        // Middle: PNR box + details area
        JPanel middle = new JPanel(new BorderLayout(10, 10));
        middle.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        JPanel top = new JPanel(new BorderLayout(10, 0));
        top.add(new JLabel("PNR Number:"), BorderLayout.WEST);
        pnrField = new JTextField();
        top.add(pnrField, BorderLayout.CENTER);
        JButton fetchButton = new JButton("Fetch");
        top.add(fetchButton, BorderLayout.EAST);
        middle.add(top, BorderLayout.NORTH);

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        detailsArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        middle.add(new JScrollPane(detailsArea), BorderLayout.CENTER);

        add(middle, BorderLayout.CENTER);

        // Bottom buttons
        cancelButton = new JButton("Cancel Ticket");
        cancelButton.setEnabled(false); // only works after a booking is fetched
        JButton backButton = new JButton("Back to Booking");
        JPanel bottom = new JPanel();
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        bottom.add(cancelButton);
        bottom.add(backButton);
        add(bottom, BorderLayout.SOUTH);

        fetchButton.addActionListener(e -> fetchBooking());
        pnrField.addActionListener(e -> fetchBooking());
        cancelButton.addActionListener(e -> doCancel());
        backButton.addActionListener(e -> {
            new ReservationForm().setVisible(true);
            dispose();
        });
    }

    private void fetchBooking() {
        String text = pnrField.getText().trim();

        if (text.isEmpty()) {
            showError("Please enter a PNR number.");
            return;
        }

        int pnr;
        try {
            pnr = Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            showError("PNR must be a number.");
            return;
        }

        String[] b = Database.getBooking(pnr);
        if (b == null) {
            detailsArea.setText("");
            cancelButton.setEnabled(false);
            currentPnr = -1;
            showError("No booking found for PNR " + pnr + ".");
            return;
        }

        currentPnr = pnr;
        detailsArea.setText(
                "PNR Number : " + b[0] + "\n"
              + "Passenger  : " + b[1] + "\n"
              + "Train      : " + b[2] + " - " + b[3] + "\n"
              + "Class      : " + b[4] + "\n"
              + "Date       : " + b[5] + "\n"
              + "From       : " + b[6] + "\n"
              + "To         : " + b[7]);
        cancelButton.setEnabled(true);
    }

    private void doCancel() {
        if (currentPnr == -1) {
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel PNR " + currentPnr + "?",
                "Confirm Cancellation", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            if (Database.cancelBooking(currentPnr)) {
                JOptionPane.showMessageDialog(this,
                        "Ticket cancelled successfully.",
                        "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                detailsArea.setText("");
                pnrField.setText("");
                cancelButton.setEnabled(false);
                currentPnr = -1;
            } else {
                showError("Cancellation failed. Please try again.");
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}