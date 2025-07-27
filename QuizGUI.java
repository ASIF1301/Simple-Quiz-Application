import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class QuizGUI extends JFrame implements ActionListener {

    private String[] questions = {
            "What is the capital of France?",
            "Which planet is known as the Red Planet?",
            "What is the largest ocean on Earth?",
            "Who invented Java programming language?",
            "Which number is a prime number?"
    };

    private String[][] options = {
            {"Berlin", "Madrid", "Paris", "Rome"},
            {"Earth", "Mars", "Jupiter", "Saturn"},
            {"Atlantic", "Indian", "Arctic", "Pacific"},
            {"Dennis Ritchie", "James Gosling", "Bjarne Stroustrup", "Guido van Rossum"},
            {"4", "6", "9", "7"}
    };

    private char[] answers = {'C', 'B', 'D', 'B', 'D'}; // A, B, C, D
    private int index = 0;
    private int score = 0;

    private JLabel questionLabel;
    private JRadioButton[] optionsButtons = new JRadioButton[4];
    private ButtonGroup group;
    private JButton nextButton;

    // Fonts
    private final Font QUESTION_FONT = new Font("SansSerif", Font.BOLD, 30);
    private final Font OPTION_FONT   = new Font("SansSerif", Font.PLAIN, 24);
    private final Font BUTTON_FONT   = new Font("SansSerif", Font.BOLD, 24);

    public QuizGUI() {
        setTitle("Java Quiz App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen/maximized

        // Root panel with padding
        JPanel root = new JPanel(new BorderLayout(15, 15));
        root.setBorder(new EmptyBorder(20, 40, 20, 40));
        add(root, BorderLayout.CENTER);

        // Question Label
        questionLabel = new JLabel("Question will be here");
        questionLabel.setFont(QUESTION_FONT);
        questionLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        root.add(questionLabel, BorderLayout.NORTH);

        // Options Panel
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(4, 1, 10, 10));
        root.add(optionsPanel, BorderLayout.CENTER);

        group = new ButtonGroup();
        Icon bigIcon = new BigRadioIcon(28); // make icons bigger

        for (int i = 0; i < 4; i++) {
            optionsButtons[i] = new JRadioButton();
            optionsButtons[i].setFont(OPTION_FONT);
            optionsButtons[i].setIcon(bigIcon);
            optionsButtons[i].setSelectedIcon(bigIcon);
            optionsButtons[i].setFocusPainted(false);
            optionsButtons[i].setOpaque(false);
            group.add(optionsButtons[i]);
            optionsPanel.add(optionsButtons[i]);
        }

        // Next Button
        nextButton = new JButton("Next");
        nextButton.setFont(BUTTON_FONT);
        nextButton.addActionListener(this);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(nextButton);
        root.add(bottom, BorderLayout.SOUTH);

        // Make JOptionPane font bigger too
        UIManager.put("OptionPane.messageFont", new Font("SansSerif", Font.BOLD, 26));
        UIManager.put("OptionPane.buttonFont", new Font("SansSerif", Font.PLAIN, 22));

        loadQuestion();
        setVisible(true);
    }

    private void loadQuestion() {
        group.clearSelection();

        if (index < questions.length) {
            questionLabel.setText((index + 1) + ". " + questions[index]);

            for (int i = 0; i < 4; i++) {
                optionsButtons[i].setText((char) ('A' + i) + ". " + options[index][i]);
            }
        } else {
            showResult();
        }
    }

    private void showResult() {
        JOptionPane.showMessageDialog(this,
                "Your score: " + score + "/" + questions.length,
                "Quiz Completed",
                JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        char selected = ' ';
        for (int i = 0; i < 4; i++) {
            if (optionsButtons[i].isSelected()) {
                selected = (char) ('A' + i);
                break;
            }
        }

        if (selected != ' ' && selected == answers[index]) {
            score++;
        }

        index++;
        loadQuestion();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QuizGUI::new);
    }

    /**
     * Simple custom radio button icon with scalable size.
     */
    static class BigRadioIcon implements Icon {
        private final int size;

        public BigRadioIcon(int size) {
            this.size = size;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            AbstractButton b = (AbstractButton) c;
            ButtonModel m = b.getModel();

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int d = size - 1;
            g2.setColor(Color.DARK_GRAY);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(x, y, d, d);

            if (m.isSelected()) {
                int inner = size / 2;
                int offset = (size - inner) / 2;
                g2.fillOval(x + offset, y + offset, inner, inner);
            }

            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }
}
