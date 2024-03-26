import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentGradingSystem extends JFrame {
    private JTextField studentIdField, studentNameField, courseCodeField, subjectField, scoreField;
    private JButton computeButton, clearAllButton, saveButton, showDetailsButton, addButton;
    private JLabel totalScoreLabel, averageLabel, gradeLabel;
    private ArrayList<StudentRecord> records;
    private DefaultTableModel tableModel;
    private Map<String, Double> subjectsScores;

    public StudentGradingSystem() {
        subjectsScores = new HashMap<>();
        records = new ArrayList<>();
        createUI();
    }

    private void createUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        setSize(800, 600);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        studentIdField = new JTextField();
        studentNameField = new JTextField();
        courseCodeField = new JTextField();
        subjectField = new JTextField();
        scoreField = new JTextField();

        inputPanel.add(new JLabel("Student ID:"));
        inputPanel.add(studentIdField);
        inputPanel.add(new JLabel("Student Name:"));
        inputPanel.add(studentNameField);
        inputPanel.add(new JLabel("Course Code:"));
        inputPanel.add(courseCodeField);
        inputPanel.add(new JLabel("Subject:"));
        inputPanel.add(subjectField);
        inputPanel.add(new JLabel("Score:"));
        inputPanel.add(scoreField);

        // Results Panel
        JPanel resultsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        totalScoreLabel = new JLabel("Total Score: ");
        averageLabel = new JLabel("Average: ");
        gradeLabel = new JLabel("Grade: ");

        resultsPanel.add(totalScoreLabel);
        resultsPanel.add(new JLabel());
        resultsPanel.add(averageLabel);
        resultsPanel.add(new JLabel());
        resultsPanel.add(gradeLabel);
        resultsPanel.add(new JLabel());

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        computeButton = new JButton("Compute");
        clearAllButton = new JButton("Clear All");
        saveButton = new JButton("Save");
        showDetailsButton = new JButton("Show Details");
        addButton = new JButton("Add Subject");

        buttonsPanel.add(computeButton);
        buttonsPanel.add(clearAllButton);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(showDetailsButton);
        buttonsPanel.add(addButton);

        // Table Model
        String[] columnNames = {"Student ID", "Name", "Course Code", "Total Score", "Average", "Grade"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable resultsTable = new JTable(tableModel);

        // Adding Action Listeners
        computeButton.addActionListener(e -> computeResults());
        clearAllButton.addActionListener(e -> clearFields());
        saveButton.addActionListener(e -> saveRecord());
        showDetailsButton.addActionListener(e -> showDetails());
        addButton.addActionListener(e -> addSubject());

        // Adding components to frame
        add(inputPanel, BorderLayout.NORTH);
        add(resultsPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
        add(new JScrollPane(resultsTable), BorderLayout.EAST);

        setVisible(true);
    }

    private void addSubject() {
        try {
            String subject = subjectField.getText();
            double score = Double.parseDouble(scoreField.getText());
            if (!subjectsScores.containsKey(subject)) {
                subjectsScores.put(subject, score);
                JOptionPane.showMessageDialog(this, "Subject added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Subject already exists!");
            }
            subjectField.setText("");
            scoreField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid score.");
        }
    }

    private void computeResults() {
        double totalScore = subjectsScores.values().stream().mapToDouble(Double::doubleValue).sum();
        double average = totalScore / subjectsScores.size();
        String grade = calculateGrade(average);

        totalScoreLabel.setText("Total Score: " + totalScore);
        averageLabel.setText("Average: " + average);
        gradeLabel.setText("Grade: " + grade);
    }

    private String calculateGrade(double average) {
        if (average >= 90) return "A";
        else if (average >= 80) return "B";
        else if (average >= 70) return "C";
        else if (average >= 60) return "D";
        else return "F";
    }

    private void clearFields() {
        studentIdField.setText("");
        studentNameField.setText("");
        courseCodeField.setText("");
        subjectField.setText("");
        scoreField.setText("");
        totalScoreLabel.setText("Total Score: ");
        averageLabel.setText("Average: ");
        gradeLabel.setText("Grade: ");
        subjectsScores.clear();
    }

    private void saveRecord() {
        try {
            if (studentIdField.getText().isEmpty() || studentNameField.getText().isEmpty() || courseCodeField.getText().isEmpty() || subjectsScores.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields and add at least one subject.");
                return;
            }

            StudentRecord record = new StudentRecord(
                    studentIdField.getText(),
                    studentNameField.getText(),
                    courseCodeField.getText(),
                    subjectsScores,
                    subjectsScores.values().stream().mapToDouble(Double::doubleValue).sum(),
                    subjectsScores.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0),
                    calculateGrade(subjectsScores.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0))
            );

            records.add(record);
            tableModel.addRow(new Object[]{record.studentId, record.name, record.courseCode, record.totalScore, record.average, record.grade});

            clearFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid data.");
        }
    }

    private void showDetails() {
        JOptionPane.showMessageDialog(this, "Records:\n" + records.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentGradingSystem());
    }

    class StudentRecord {
        String studentId;
        String name;
        String courseCode;
        Map<String, Double> subjectsScores;
        double totalScore;
        double average;
        char grade;

        public StudentRecord(String studentId, String name, String courseCode, Map<String, Double> subjectsScores, double totalScore, double average, String grade) {
            this.studentId = studentId;
            this.name = name;
            this.courseCode = courseCode;
            this.subjectsScores = new HashMap<>(subjectsScores);
            this.totalScore = totalScore;
            this.average = average;
            this.grade = grade.charAt(0);
        }

        @Override
        public String toString() {
            return studentId + ", " + name + ", " + courseCode + ", " + subjectsScores.toString() + ", " + totalScore + ", " + average + ", " + grade;
        }
    }
}
