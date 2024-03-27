import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class HotelReservationGUI {
    private JFrame frame;
    private JPanel mainPanel;
    private JTextField customerNameField, emailField, mobileField, priceField, totalAmountField, amountPaidField, balanceField;
    private JComboBox<String> genderComboBox, roomNumberComboBox;
    private JSpinner checkInDateSpinner, noOfGuestsSpinner, noOfDaysSpinner;
    private JCheckBox singleRoomCheckBox, doubleRoomCheckBox, suiteRoomCheckBox;
    private JButton saveButton, updateStatusButton, calculateTotalButton, calculateBalanceButton;
    private JLabel customerIdLabel;
    private JTable table;
    private DefaultTableModel tableModel;
    private ArrayList<CustomerInfo> customerDataList;

    // Base prices for room types
    private final int BASE_PRICE_SINGLE = 100;
    private final int BASE_PRICE_DOUBLE = 150;
    private final int BASE_PRICE_SUITE = 200;

    // Customer information class to hold the data
    class CustomerInfo {
        String customerId, name, gender, email, mobile, roomType, price, totalAmount, balance;
        // Other fields as needed
    }

    public HotelReservationGUI() {
        customerDataList = new ArrayList<>();
        initializeUI();
        attachEventHandlers();
    }

    private void initializeUI() {
        frame = new JFrame("Hotel Reservation System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // Fixed size frame
        frame.setLayout(new BorderLayout());

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Customer Information Section
        JPanel customerInfoPanel = createTitledPanel("Customer Info");
        customerIdLabel = new JLabel("Customer ID: " + generateCustomerId());
        customerNameField = new JTextField(20);
        String[] genders = {"Male", "Female", "Other"};
        genderComboBox = new JComboBox<>(genders);
        emailField = new JTextField(20);
        mobileField = new JTextField(20);
        addComponents(customerInfoPanel, customerIdLabel, new JLabel("Name:"), customerNameField, new JLabel("Gender:"), genderComboBox, new JLabel("Email:"), emailField, new JLabel("Mobile:"), mobileField);

        // Check-in Information Section
        JPanel checkInInfoPanel = createTitledPanel("Check-in Info");
        checkInDateSpinner = new JSpinner(new SpinnerDateModel());
        noOfGuestsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        singleRoomCheckBox = new JCheckBox("Single");
        doubleRoomCheckBox = new JCheckBox("Double");
        suiteRoomCheckBox = new JCheckBox("Suite");
        String[] roomNumbers = {"101", "102", "103", "201", "202", "203"};
        roomNumberComboBox = new JComboBox<>(roomNumbers);
        addComponents(checkInInfoPanel, new JLabel("Check-in Date:"), checkInDateSpinner, new JLabel("No. of Guests:"), noOfGuestsSpinner, new JLabel("Room Type:"), singleRoomCheckBox, doubleRoomCheckBox, suiteRoomCheckBox, new JLabel("Room Number:"), roomNumberComboBox);

        // Billing Information Section
        JPanel billingInfoPanel = createTitledPanel("Billing Info");
        priceField = new JTextField(10);
        noOfDaysSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 30, 1));
        totalAmountField = new JTextField(10);
        amountPaidField = new JTextField(10);
        balanceField = new JTextField(10);
        saveButton = new JButton("Save Data");
        updateStatusButton = new JButton("Update Status");
        calculateTotalButton = new JButton("Calculate Total");
        calculateBalanceButton = new JButton("Calculate Balance");
        addComponents(billingInfoPanel, new JLabel("Price:"), priceField, new JLabel("No. of Days:"), noOfDaysSpinner, new JLabel("Total Amount:"), totalAmountField, new JLabel("Amount Paid:"), amountPaidField, new JLabel("Balance:"), balanceField, saveButton, updateStatusButton, calculateTotalButton, calculateBalanceButton);

        // Table to display saved data
        String[] columnNames = {"Customer ID", "Name", "Email", "Mobile", "Room Number", "Total Amount", "Balance"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Reservation Data", TitledBorder.CENTER, TitledBorder.TOP));

        // Add panels to the main panel
        mainPanel.add(customerInfoPanel);
        mainPanel.add(checkInInfoPanel);
        mainPanel.add(billingInfoPanel);

        // Add main panel and table scroll pane to the frame
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.SOUTH);

        // Display the frame
        frame.setVisible(true);
    }

    private JPanel createTitledPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), title, TitledBorder.CENTER, TitledBorder.TOP));
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        return panel;
    }

    private void addComponents(JPanel panel, JComponent... components) {
        for (JComponent component : components) {
            panel.add(component);
        }
    }

    private String generateCustomerId() {
        Random rand = new Random();
        return "CID" + rand.nextInt(9999);
    }

    private void attachEventHandlers() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveData();
            }
        });

        calculateTotalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateTotal();
            }
        });

        calculateBalanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateBalance();
            }
        });

        singleRoomCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePrice();
            }
        });

        doubleRoomCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePrice();
            }
        });

        suiteRoomCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePrice();
            }
        });

        noOfGuestsSpinner.addChangeListener(e -> updatePrice());
    }

    private void updatePrice() {
        int basePrice = 0;
        if (singleRoomCheckBox.isSelected()) {
            basePrice = BASE_PRICE_SINGLE;
        } else if (doubleRoomCheckBox.isSelected()) {
            basePrice = BASE_PRICE_DOUBLE;
        } else if (suiteRoomCheckBox.isSelected()) {
            basePrice = BASE_PRICE_SUITE;
        }

        int noOfGuests = (Integer) noOfGuestsSpinner.getValue();
        int totalPrice = basePrice * noOfGuests;
        priceField.setText(String.valueOf(totalPrice));
    }

    private void calculateTotal() {
        try {
            int price = Integer.parseInt(priceField.getText());
            int noOfDays = (Integer) noOfDaysSpinner.getValue();
            int totalAmount = price * noOfDays;
            totalAmountField.setText(String.valueOf(totalAmount));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid price.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculateBalance() {
        try {
            int totalAmount = Integer.parseInt(totalAmountField.getText());
            int amountPaid = Integer.parseInt(amountPaidField.getText());
            int balance = totalAmount - amountPaid;
            balanceField.setText(String.valueOf(balance));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter valid amounts for total and paid.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveData() {
        String customerId = customerIdLabel.getText();
        String name = customerNameField.getText();
        String gender = (String) genderComboBox.getSelectedItem();
        String email = emailField.getText();
        String mobile = mobileField.getText();
        String roomNumber = (String) roomNumberComboBox.getSelectedItem();
        String price = priceField.getText();
        String totalAmount = totalAmountField.getText();
        String balance = balanceField.getText();

        // Add data to the table model
        tableModel.addRow(new Object[]{customerId, name, email, mobile, roomNumber, totalAmount, balance});

        // Clear input fields for next entry
        customerNameField.setText("");
        emailField.setText("");
        mobileField.setText("");
        priceField.setText("");
        totalAmountField.setText("");
        amountPaidField.setText("");
        balanceField.setText("");

        // Generate a new customer ID for the next customer
        customerIdLabel.setText("Customer ID: " + generateCustomerId());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HotelReservationGUI());
    }
}
