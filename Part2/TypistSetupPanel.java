import javax.swing.*;
import java.awt.*;

public class TypistSetupPanel
{
    private JPanel panel;
    private int passageLength;
    private Leaderboard leaderboard;
    private SponsorSystem sponsorSystem;
    private boolean autocorrect;
    private boolean caffeineMode;
    private boolean nightShift;
    private String passageText;

    public TypistSetupPanel(JPanel panel, int passageLength, String passageText, Leaderboard leaderboard, 
    SponsorSystem sponsorSystem, boolean autocorrect, boolean caffeineMode, boolean nightShift)
    {
        this.panel = panel;
        this.passageLength = passageLength;
        this.passageText = passageText;
        this.leaderboard = leaderboard;
        this.sponsorSystem = sponsorSystem;
        this.autocorrect = autocorrect;
        this.caffeineMode = caffeineMode;
        this.nightShift = nightShift;
    }

    public void showTypistSetup(int numTypists)
    {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Customise Your Typists");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(Box.createVerticalStrut(20));
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));

        JTextField[] nameFields = new JTextField[numTypists];
        JTextField[] symbolFields = new JTextField[numTypists];
        JComboBox[] styleBoxes = new JComboBox[numTypists];
        JComboBox[] keyboardBoxes = new JComboBox[numTypists];
        JCheckBox[] wristSupports = new JCheckBox[numTypists];
        JCheckBox[] energyDrinks = new JCheckBox[numTypists];
        JCheckBox[] headphones = new JCheckBox[numTypists];

        String[] styles = {"Touch Typist", "Hunt & Peck", "Phone Thumbs", "Voice-to-Text"};
        String[] keyboards = {"Mechanical", "Membrane", "Touchscreen", "Stenography"};

        for (int i = 0; i < numTypists; i++)
        {
            JLabel typistTitle = new JLabel("Typist " + (i + 1));
            typistTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            typistTitle.setFont(new Font("Arial", Font.BOLD, 16));

            JLabel nameLabel = new JLabel("Name:");
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            nameFields[i] = new JTextField("TYPIST" + (i + 1));
            nameFields[i].setMaximumSize(new Dimension(200, 30));
            nameFields[i].setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel symbolLabel = new JLabel("Symbol (single character):");
            symbolLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            symbolFields[i] = new JTextField(String.valueOf((char)('①' + i)));
            symbolFields[i].setMaximumSize(new Dimension(200, 30));
            symbolFields[i].setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel styleLabel = new JLabel("Typing Style:");
            styleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            styleBoxes[i] = new JComboBox<>(styles);
            styleBoxes[i].setMaximumSize(new Dimension(200, 30));
            styleBoxes[i].setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel keyboardLabel = new JLabel("Keyboard Type:");
            keyboardLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            keyboardBoxes[i] = new JComboBox<>(keyboards);
            keyboardBoxes[i].setMaximumSize(new Dimension(200, 30));
            keyboardBoxes[i].setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel accessoryLabel = new JLabel("Accessories:");
            accessoryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            wristSupports[i] = new JCheckBox("Wrist Support (reduces burnout duration)");
            wristSupports[i].setAlignmentX(Component.CENTER_ALIGNMENT);

            energyDrinks[i] = new JCheckBox("Energy Drink (accuracy boost first half, penalty second half)");
            energyDrinks[i].setAlignmentX(Component.CENTER_ALIGNMENT);

            headphones[i] = new JCheckBox("Noise-Cancelling Headphones (reduces mistype chance)");
            headphones[i].setAlignmentX(Component.CENTER_ALIGNMENT);

            panel.add(typistTitle);
            panel.add(Box.createVerticalStrut(5));
            panel.add(nameLabel);
            panel.add(nameFields[i]);
            panel.add(Box.createVerticalStrut(5));
            panel.add(symbolLabel);
            panel.add(symbolFields[i]);
            panel.add(Box.createVerticalStrut(5));
            panel.add(styleLabel);
            panel.add(styleBoxes[i]);
            panel.add(Box.createVerticalStrut(5));
            panel.add(keyboardLabel);
            panel.add(keyboardBoxes[i]);
            panel.add(Box.createVerticalStrut(5));
            panel.add(accessoryLabel);
            panel.add(wristSupports[i]);
            panel.add(energyDrinks[i]);
            panel.add(headphones[i]);
            panel.add(Box.createVerticalStrut(20));
        }

        Typist[] typists = new Typist[numTypists];

        JButton nextButton = new JButton("Continue to Race");
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextButton.addActionListener(e -> {
            boolean[] hasWristSupport = new boolean[numTypists];
            boolean[] hasEnergyDrink = new boolean[numTypists];
            boolean[] hasHeadphones = new boolean[numTypists];

            for (int i = 0; i < numTypists; i++)
            {
                String name = nameFields[i].getText();
                char symbol = symbolFields[i].getText().charAt(0);

                double accuracy;
                String style = (String) styleBoxes[i].getSelectedItem();
                if (style.equals("Touch Typist")) {
                    accuracy = 0.85;
                } else if (style.equals("Hunt & Peck")) {
                    accuracy = 0.60;
                } else if (style.equals("Phone Thumbs")) {
                    accuracy = 0.50;
                } else {
                    accuracy = 0.40;
                }

                String keyboard = (String) keyboardBoxes[i].getSelectedItem();
                if (keyboard.equals("Mechanical")) {
                    accuracy += 0.05;
                } else if (keyboard.equals("Membrane")) {
                    accuracy += 0.0;
                } else if (keyboard.equals("Touchscreen")) {
                    accuracy -= 0.05;
                } else {
                    accuracy += 0.10;
                }

                // Apply accessory accuracy adjustments
                if (energyDrinks[i].isSelected()) {
                    accuracy += 0.05;
                }
                if (headphones[i].isSelected()) {
                    accuracy += 0.05;
                }

                typists[i] = new Typist(symbol, name, accuracy);

                hasWristSupport[i] = wristSupports[i].isSelected();
                hasEnergyDrink[i] = energyDrinks[i].isSelected();
                hasHeadphones[i] = headphones[i].isSelected();
            }

            RacePanel racePanel = new RacePanel(panel, typists, passageLength, passageText, leaderboard, sponsorSystem,
            autocorrect, caffeineMode, nightShift, hasWristSupport, hasEnergyDrink, hasHeadphones);
            racePanel.showRace();
        });

        panel.add(nextButton);
        panel.add(Box.createVerticalStrut(20));
    }
}