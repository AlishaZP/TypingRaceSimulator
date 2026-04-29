import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StatsPanel
{
    private JPanel panel;
    private Typist[] typists;
    private Typist winner;
    private double wpm;
    private Leaderboard leaderboard;
    private SponsorSystem sponsorSystem;

    // Constructor
    public StatsPanel(JPanel panel, Typist[] typists, Typist winner, double wpm, Leaderboard leaderboard, SponsorSystem sponsorSystem)
    {
        this.panel = panel;
        this.typists = typists;
        this.winner = winner;
        this.wpm = wpm;
        this.leaderboard = leaderboard;
        this.sponsorSystem = sponsorSystem;
    }

    public void showStats()
    {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Title
        JLabel title = new JLabel("Race Statistics");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(Box.createVerticalStrut(20));
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));

        JLabel winnerLabel = new JLabel("Winner: " + winner.getName());
        winnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(winnerLabel);
        panel.add(Box.createVerticalStrut(5));

        JLabel wpmLabel = new JLabel(String.format("WPM: %.1f", wpm));
        wpmLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(wpmLabel);
        panel.add(Box.createVerticalStrut(20));

        // Display stats for each typist
        for (Typist t : typists)
        {
            JLabel nameLabel = new JLabel(t.getSymbol() + " " + t.getName());
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            nameLabel.setFont(new Font("Arial", Font.BOLD, 16));

            JLabel accuracyLabel = new JLabel(String.format("Final Accuracy: %.2f", t.getAccuracy()));
            accuracyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel burnoutLabel = new JLabel("Burnout Count: " + t.getBurnoutCount());
            burnoutLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            int sponsorIndex = -1;
            for (int i = 0; i < sponsorSystem.getSize(); i++) {
                if (sponsorSystem.getName(i).equals(t.getName())) {
                    sponsorIndex = i;
                    break;
                }
            }

            // Show sponsor if they have one, otherwise show "No Sponsor"
            JLabel sponsorLabel = new JLabel("Sponsor: " + sponsorSystem.getSponsor(sponsorIndex));
            sponsorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel earningsLabel = new JLabel("Earnings: " + sponsorSystem.getEarnings(sponsorIndex) + " coins");
            earningsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Race history
            ArrayList<String> history = leaderboard.getRaceHistory(t.getName());
            JLabel historyTitle = new JLabel("Race History:");
            historyTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

            panel.add(nameLabel);
            panel.add(Box.createVerticalStrut(5));
            panel.add(accuracyLabel);
            panel.add(Box.createVerticalStrut(5));
            panel.add(burnoutLabel);
            panel.add(Box.createVerticalStrut(5));
            panel.add(sponsorLabel);
            panel.add(Box.createVerticalStrut(5));
            panel.add(earningsLabel);
            panel.add(Box.createVerticalStrut(5));
            panel.add(historyTitle);

            // Display each race record in the history
            for (String record : history) {
                JLabel historyLabel = new JLabel(record);
                historyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                historyLabel.setFont(new Font("Arial", Font.PLAIN, 11));
                panel.add(historyLabel);
            }
            panel.add(Box.createVerticalStrut(15));
        }

        // Buttons
        JButton compareButton = new JButton("Compare Typists");
        compareButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        compareButton.addActionListener(e -> showComparison());

        JButton playAgainButton = new JButton("Play Again");
        playAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playAgainButton.addActionListener(e -> {
            TypingRaceGUI gui = new TypingRaceGUI();
            gui.startRaceGUI();
        });

        JButton leaderboardButton = new JButton("View Leaderboard");
        leaderboardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderboardButton.addActionListener(e -> {
            LeaderboardPanel lp = new LeaderboardPanel(panel, leaderboard);
            lp.showLeaderboard();
        });

        panel.add(Box.createVerticalStrut(20));
        panel.add(compareButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(playAgainButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(leaderboardButton);
        panel.revalidate();
        panel.repaint();
    }

    private void showComparison()
    {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Title
        JLabel title = new JLabel("Typist Comparison");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(Box.createVerticalStrut(20));
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));

        JLabel header = new JLabel(String.format("%-15s %-10s %-10s %-10s",
            "Name", "Accuracy", "Burnouts", "Best WPM"));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.setFont(new Font("Monospaced", Font.BOLD, 14));
        panel.add(header);
        panel.add(Box.createVerticalStrut(10));

        // Display comparison stats for each typist
        for (Typist t : typists)
        {
            double personalBest = leaderboard.getPersonalBest(leaderboard.getNames().indexOf(t.getName()));
            JLabel row = new JLabel(String.format("%-15s %-10.2f %-10d %-10.1f",
                t.getName(),
                t.getAccuracy(),
                t.getBurnoutCount(),
                personalBest));
            row.setAlignmentX(Component.CENTER_ALIGNMENT);
            row.setFont(new Font("Monospaced", Font.PLAIN, 14));
            panel.add(row);
            panel.add(Box.createVerticalStrut(5));
        }

        // Back button to return to stats
        JButton backButton = new JButton("Back to Stats");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> showStats());
        panel.add(Box.createVerticalStrut(20));
        panel.add(backButton);

        panel.revalidate();
        panel.repaint();
    }
}