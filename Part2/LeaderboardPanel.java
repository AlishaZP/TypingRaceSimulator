import javax.swing.*;
import java.awt.*;

public class LeaderboardPanel
{
    private JPanel panel;
    private Leaderboard leaderboard;

    // Constructor
    public LeaderboardPanel(JPanel panel, Leaderboard leaderboard)
    {
        this.panel = panel;
        this.leaderboard = leaderboard;
    }

    public void showLeaderboard()
    {
        leaderboard.sortByPoints(); 
        panel.removeAll();
        panel.revalidate();
        panel.repaint();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Title
        JLabel title = new JLabel("Leaderboard");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(Box.createVerticalStrut(20));
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));

        // Header
        JLabel header = new JLabel("Name          Points    Wins    Races    Best WPM    Badge");
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.setFont(new Font("Monospaced", Font.BOLD, 14));
        panel.add(header);
        panel.add(Box.createVerticalStrut(10));

        // Show each typist sorted by points
        for (int i = 0; i < leaderboard.getSize(); i++)
        {
            String entry = String.format("%-15s %-9d %-7d %-8d %-10.1f %s",
                leaderboard.getName(i),
                leaderboard.getPoints(i),
                leaderboard.getWins(i),
                leaderboard.getRacesPlayed(i),
                leaderboard.getPersonalBest(i),
                leaderboard.getBadge(leaderboard.getName(i)));

            JLabel entryLabel = new JLabel((i + 1) + ". " + entry);
            entryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            entryLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
            panel.add(entryLabel);
            panel.add(Box.createVerticalStrut(5));
        }

        panel.add(Box.createVerticalStrut(20));

        // Play Again Button
        JButton playAgainButton = new JButton("Play Again");
        playAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playAgainButton.addActionListener(e -> {
            TypingRaceGUI gui = new TypingRaceGUI();
            gui.startRaceGUI();
        });
        panel.add(playAgainButton);

        panel.revalidate();
        panel.repaint();
    }
}