import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class QuizApp extends JFrame implements ActionListener {
    private int currentQuestionIndex = 0;
    private int score = 0;
    private List<Question> questions;
    private JButton nextButton, prevButton;
    private ButtonGroup optionsGroup;
    private JLabel questionLabel, timerLabel;
    private JRadioButton[] options;
    private Timer quizTimer;
    private final int quizDuration = 60; // Quiz duration in seconds

    public QuizApp() {
        setTitle("Computer Science Quiz");
        setSize(500, 400);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

        questions = createQuestions();
        questionLabel = new JLabel();
        add(questionLabel);

        timerLabel = new JLabel("Time left: " + quizDuration);
        add(timerLabel);

        optionsGroup = new ButtonGroup();
        options = new JRadioButton[4];
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.PAGE_AXIS));
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            optionsGroup.add(options[i]);
            optionsPanel.add(options[i]);
        }
        add(optionsPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        prevButton = new JButton("Previous");
        prevButton.addActionListener(this);
        buttonPanel.add(prevButton);

        nextButton = new JButton("Next");
        nextButton.addActionListener(this);
        buttonPanel.add(nextButton);

        add(buttonPanel);

        quizTimer = new Timer(1000, new ActionListener() {
            int timeLeft = quizDuration;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeft > 0) {
                    timeLeft--;
                    timerLabel.setText("Time left: " + timeLeft);
                } else {
                    quizTimer.stop();
                    showResults();
                }
            }
        });
        quizTimer.start();

        displayQuestion();
    }

    private void displayQuestion() {
        if (currentQuestionIndex >= 0 && currentQuestionIndex < questions.size()) {
            Question q = questions.get(currentQuestionIndex);
            questionLabel.setText("<html>" + q.getQuestion() + "</html>");
            for (int i = 0; i < 4; i++) {
                options[i].setText(q.getOptions()[i]);
                options[i].setSelected(false);
            }
        }
    }

    private List<Question> createQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("What does CPU stand for?", new String[]{"Central Process Unit", "Central Processor Unit", "Computer Personal Unit", "Central Processing Unit"}, 3));
        questions.add(new Question("Which of the following is a non-volatile memory?", new String[]{"RAM", "ROM", "CPU Cache", "Register"}, 1));
        questions.add(new Question("What is the time complexity of a binary search algorithm?", new String[]{"O(n)", "O(log n)", "O(n log n)", "O(n^2)"}, 1));
        questions.add(new Question("Which data structure uses LIFO?", new String[]{"Queue", "Stack", "Array", "LinkedList"}, 1));
        questions.add(new Question("What does 'HTTP' stand for?", new String[]{"HyperText Transfer Product", "HyperText Transfer Protocol", "HyperText Transfer Procedure", "HyperText Transfer Process"}, 1));
        questions.add(new Question("In object-oriented programming, what does 'OOP' stand for?", new String[]{"Object-Oriented Programming", "Object-Oriented Procedure", "Object-Oriented Process", "Object-Oriented Product"}, 0));
        questions.add(new Question("What is the main function of the ALU?", new String[]{"Control Unit Operations", "Perform Arithmetic and Logical Operations", "Manage Memory Operations", "Handle Input/Output Operations"}, 1));
        questions.add(new Question("Which language is primarily used for Android App Development?", new String[]{"Swift", "Kotlin", "C#", "JavaScript"}, 1));
        questions.add(new Question("What does 'CSS' stand for in web development?", new String[]{"Computing Style Sheet", "Creative Style System", "Cascading Style Sheets", "Computer Style System"}, 2));
        questions.add(new Question("In what year was the '@' chosen for its use in email addresses?", new String[]{"1976", "1980", "1972", "1984"}, 2));
        return questions;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextButton) {
            if (currentQuestionIndex < questions.size() - 1) {
                updateScore();
                currentQuestionIndex++;
                displayQuestion();
            } else {
                quizTimer.stop();
                showResults();
            }
        } else if (e.getSource() == prevButton) {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                displayQuestion();
            }
        }
    }

    private void updateScore() {
        Question currentQuestion = questions.get(currentQuestionIndex);
        for (int i = 0; i < 4; i++) {
            if (options[i].isSelected() && i == currentQuestion.getCorrectAnswerIndex()) {
                score++;
                break;
            }
        }
    }

    private void showResults() {
        JOptionPane.showMessageDialog(this, "Your score is: " + score);
        // Show correct answers (not implemented in this template)
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new QuizApp().setVisible(true);
        });
    }

    class Question {
        private String question;
        private String[] options;
        private int correctAnswerIndex;

        public Question(String question, String[] options, int correctAnswerIndex) {
            this.question = question;
            this.options = options;
            this.correctAnswerIndex = correctAnswerIndex;
        }

        public String getQuestion() {
            return question;
        }

        public String[] getOptions() {
            return options;
        }

        public int getCorrectAnswerIndex() {
            return correctAnswerIndex;
        }
    }
}
