import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class ReservationForm extends JFrame {

    private JTextField nameField, trainNoField, trainNameField, dateField,
            sourceField, destinationField;
    private JComboBox<String> classBox;

    public ReservationForm() {
        setTitle("Online Reservation System - Book Ticket");
        setSize(450, 470);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Heading
        JLabel heading = new JLabel("Reservation Form", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        add(heading, BorderLayout.NORTH);

        // Form fields
        JPanel form = new JPanel(new GridLayout(7, 2, 10, 12));
        form.setBorder(BorderFactory.createEmptyBorder(15, 30, 10, 30));

        nameField = new JTextField();
        trainNoField = new JTextField();
        trainNameField = new JTextField();
        trainNameField.setEditable(false); // filled automatically
        classBox = new JComboBox<>(new String[]{"Sleeper", "AC 3 Tier", "AC 2 Tier", "AC 1st Class"});
        dateField = new JTextField("dd-MM-yyyy");
        sourceField = new JTextField();
        destinationField = new JTextField();

        form.add(new JLabel("Passenger Name:"));
        form.add(nameField);
        form.add(new JLabel("Train Number:"));
        form.add(trainNoField);
        form.add(new JLabel("Train Name:"));
        form.add(trainNameField);
        form.add(new JLabel("Class Type:"));
        form.add(classBox);
        form.add(new JLabel("Date of Journey:"));
        form.add(dateField);
        form.add(new JLabel("From (Source):"));
        form.add(sourceField);
        form.add(new JLabel("To (Destination):"));
        form.add(destinationField);

        add(form, BorderLayout.CENTER);

        // Buttons at the bottom
        JButton bookButton = new JButton("Book Ticket");
        JButton cancelPageButton = new JButton("Go to Cancellation");
        JPanel bottom = new JPanel();
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        bottom.add(bookButton);
        bottom.add(cancelPageButton);
        add(bottom, BorderLayout.SOUTH);

        // Train name appears when you leave the train number box
        trainNoField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent e) {
                fillTrainName();
            }
        });
        trainNoField.addActionListener(e -> fillTrainName());

        bookButton.addActionListener(e -> doBooking());

                cancelPageButton.addActionListener(e -> {
            new CancellationForm().setVisible(true);
            dispose();
        });
    }

    // Finds the train name from the train number
    private void fillTrainName() {
        String text = trainNoField.getText().trim();
        if (text.isEmpty()) {
            trainNameField.setText("");
            return;
        }
        try {
            int trainNo = Integer.parseInt(text);
            String name = Database.getTrainName(trainNo);
            trainNameField.setText(name == null ? "Train not found" : name);
        } catch (NumberFormatException ex) {
            trainNameField.setText("Invalid number");
        }
    }

    private void doBooking() {
        String name = nameField.getText().trim();
        String trainNoText = trainNoField.getText().trim();
        String date = dateField.getText().trim();
        String source = sourceField.getText().trim();
        String destination = destinationField.getText().trim();
        String classType = (String) classBox.getSelectedItem();

        // Check 1: no empty fields
        if (name.isEmpty() || trainNoText.isEmpty() || date.isEmpty()
                || source.isEmpty() || destination.isEmpty()) {
            showError("Please fill all the fields.");
            return;
        }

        // Check 2: train number must be a number
        int trainNo;
        try {
            trainNo = Integer.parseInt(trainNoText);
        } catch (NumberFormatException ex) {
            showError("Train number must be numeric.");
            return;
        }

        // Check 3: train must exist
        String trainName = Database.getTrainName(trainNo);
        if (trainName == null) {
            showError("Train number not found. Try 12123, 12126, 12301, 12951 or 12009.");
            return;
        }
        trainNameField.setText(trainName);

        // Check 4: date format must be dd-MM-yyyy
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            sdf.setLenient(false);
            sdf.parse(date);
        } catch (Exception ex) {
            showError("Date must be in dd-MM-yyyy format (example: 15-11-2026).");
            return;
        }

        // Save to database
        int pnr = Database.bookTicket(name, trainNo, trainName, classType, date, source, destination);

        if (pnr == -1) {
            showError("Booking failed. Please try again.");
            return;
        }

        // Confirmation popup
        JOptionPane.showMessageDialog(this,
                "Booking Confirmed!\n\n"
                        + "PNR Number: " + pnr + "\n"
                        + "Passenger: " + name + "\n"
                        + "Train: " + trainNo + " - " + trainName + "\n"
                        + "Class: " + classType + "\n"
                        + "Date: " + date + "\n"
                        + "From: " + source + "  To: " + destination,
                "Booking Details", JOptionPane.INFORMATION_MESSAGE);

        clearForm();
    }

    private void clearForm() {
        nameField.setText("");
        trainNoField.setText("");
        trainNameField.setText("");
        dateField.setText("dd-MM-yyyy");
        sourceField.setText("");
        destinationField.setText("");
        classBox.setSelectedIndex(0);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}