import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItineraryPlanner extends JFrame {
    private JTextField customerNameField, fromDestinationField, toDestinationField, dateField;
    private JButton generateButton;
    private JTable itineraryTable;
    private ArrayList<String[]> travelData;
    private Map<String, Integer> averageCosts;

    public ItineraryPlanner() {
        travelData = new ArrayList<>();
        averageCosts = new HashMap<>();
        populateAverageCosts();
        createGUI();
    }

    private void populateAverageCosts() {
        // These are example costs, you'll need to provide actual data
        averageCosts.put("Paris-New York", 100000); // Lowered cost for realism
        averageCosts.put("Tokyo-Sydney", 250000);
        averageCosts.put("Mumbai-London", 300000);
        // Add more destination pairs and their average costs
    }

    private void createGUI() {
        setTitle("Travel Itinerary Planner");
        setSize(700, 700);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Customer Name:"));
        customerNameField = new JTextField();
        inputPanel.add(customerNameField);
        inputPanel.add(new JLabel("From:"));
        fromDestinationField = new JTextField();
        inputPanel.add(fromDestinationField);
        inputPanel.add(new JLabel("To:"));
        toDestinationField = new JTextField();
        inputPanel.add(toDestinationField);
        inputPanel.add(new JLabel("Date:"));
        dateField = new JTextField();
        inputPanel.add(dateField);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        generateButton = new JButton("Generate Plan");
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateItinerary();
            }
        });
        buttonPanel.add(generateButton);

        // Itinerary Table
        String[] columnNames = {"Customer Name", "From", "To", "Date", "Estimated Budget"};
        Object[][] data = {};
        itineraryTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(itineraryTable);

        // Adding components to the frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    private void generateItinerary() {
        String customerName = customerNameField.getText();
        String fromDestination = fromDestinationField.getText();
        String toDestination = toDestinationField.getText();
        String date = dateField.getText();
        String route = fromDestination + "-" + toDestination;
        Integer estimatedBudget = calculateBudget(route);
        travelData.add(new String[]{customerName, fromDestination, toDestination, date, estimatedBudget.toString()});
        updateTable();
    }

    private Integer calculateBudget(String route) {
        // Default budget if route is not found
        Integer defaultBudget = 200; // Lowered default budget for realism
        return averageCosts.getOrDefault(route, defaultBudget);
    }

    private void updateTable() {
        String[] columnNames = {"Customer Name", "From", "To", "Date", "Estimated Budget"};
        Object[][] data = new Object[travelData.size()][5];
        for (int i = 0; i < travelData.size(); i++) {
            data[i] = travelData.get(i);
        }
        itineraryTable.setModel(new DefaultTableModel(data, columnNames));
    }

    public static void main(String[] args) {
        new ItineraryPlanner();
    }
}
