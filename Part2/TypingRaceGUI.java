import javax.swing.*;
import java.awt.*;

public class TypingRaceGUI
{
    private JComboBox<String> passageBox;
    private JTextField customField;
    private JCheckBox autocorrect;
    private JCheckBox caffeineMode;
    private JCheckBox nightShift;
    private JComboBox<String> typistBox;
    private JFrame frame;
    private JPanel panel;
    private Leaderboard leaderboard = new Leaderboard();
    private SponsorSystem sponsorSystem = new SponsorSystem();
    private static final String SHORT_PASSAGE = "The quick brown fox jumps over the lazy dog.";
    private static final String MEDIUM_PASSAGE = "The quick brown fox jumps over the lazy dog. Pack my box with five dozen liquor jugs.";
    private static final String LONG_PASSAGE = "The quick brown fox jumps over the lazy dog. Pack my box with five dozen liquor jugs. How vexingly quick daft zebras jump over the wax fence.";

    public TypingRaceGUI()
    {
        frame = new JFrame("Typing Race Simulator");
        panel = new JPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
    }
    public void startRaceGUI()
    {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Title
        JLabel title = new JLabel("Typing Race Simulator");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 24));

        // Passage selection
        JLabel passageLabel = new JLabel("Select Passage:");
        passageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        String[] passages = {"Short (20 chars)", "Medium (40 chars)", "Long (80 chars)", "Custom"};
        passageBox = new JComboBox<>(passages);
        passageBox.setMaximumSize(new Dimension(200, 30));
        passageBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Custom passage length input - hidden by default
        JLabel customLabel = new JLabel("Enter passage length:");
        customLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        customLabel.setVisible(false);

        customField = new JTextField();
        customField.setMaximumSize(new Dimension(200, 30));
        customField.setAlignmentX(Component.CENTER_ALIGNMENT);
        customField.setVisible(false);
        
        passageBox.addActionListener(e -> {
            boolean isCustom = passageBox.getSelectedItem().equals("Custom");
            customLabel.setVisible(isCustom);
            customField.setVisible(isCustom);
            panel.revalidate();
            panel.repaint();
        });

        // Typist count selection
        JLabel typistLabel = new JLabel("Number of Typists:");
        typistLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        String[] typistCounts = {"2", "3", "4", "5", "6"};
        typistBox = new JComboBox<>(typistCounts);
        typistBox.setMaximumSize(new Dimension(200, 30));
        typistBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Difficulty modifiers
        JLabel modifierLabel = new JLabel("Difficulty Modifiers:");
        modifierLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        autocorrect = new JCheckBox("Autocorrect On (halves slide back amount)");
        autocorrect.setAlignmentX(Component.CENTER_ALIGNMENT);

        caffeineMode = new JCheckBox("Caffeine Mode (speed boost, increased burnout risk)");
        caffeineMode.setAlignmentX(Component.CENTER_ALIGNMENT);

        nightShift = new JCheckBox("Night Shift (everyone is tired, accuracy reduced)");
        nightShift.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Start button
        JButton startButton = new JButton("Start Race");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(e -> {
            int numTypists = Integer.parseInt((String) typistBox.getSelectedItem());
            
            String selected = (String) passageBox.getSelectedItem();
            String passageText;
            int passageLength;
            
            if (selected.equals("Short (20 chars)")) {
                passageText = SHORT_PASSAGE;
                passageLength = SHORT_PASSAGE.length();
            } else if (selected.equals("Medium (40 chars)")) {
                passageText = MEDIUM_PASSAGE;
                passageLength = MEDIUM_PASSAGE.length();
            } else if (selected.equals("Long (80 chars)")) {
                passageText = LONG_PASSAGE;
                passageLength = LONG_PASSAGE.length();
            } else {
                passageText = customField.getText();
                passageLength = passageText.length();
            }

            TypistSetupPanel setup = new TypistSetupPanel(panel, passageLength, passageText, leaderboard, sponsorSystem,
                autocorrect.isSelected(), caffeineMode.isSelected(), nightShift.isSelected());
            setup.showTypistSetup(numTypists);
        });

        // Add everything to panel with spacing
        panel.add(Box.createVerticalStrut(30));
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        panel.add(passageLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(passageBox);
        panel.add(Box.createVerticalStrut(5));
        panel.add(customLabel);  
        panel.add(Box.createVerticalStrut(5));
        panel.add(customField);  
        panel.add(Box.createVerticalStrut(20));
        panel.add(typistLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(typistBox);
        panel.add(Box.createVerticalStrut(30));
        panel.add(Box.createVerticalStrut(20));
        panel.add(modifierLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(autocorrect);
        panel.add(caffeineMode);
        panel.add(nightShift);
        panel.add(Box.createVerticalStrut(30));
        panel.add(startButton);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        frame.add(scrollPane);
        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame; 
    }

    public JPanel getPanel() {
         return panel; 
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> {
            TypingRaceGUI gui = new TypingRaceGUI();
            gui.startRaceGUI();
        });
    }
}