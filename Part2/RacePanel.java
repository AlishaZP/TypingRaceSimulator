import javax.swing.*;
import java.awt.*;

public class RacePanel
{
    private JPanel panel;
    private Typist[] typists;
    private int passageLength;
    private JProgressBar[] progressBars;
    private long startTime; 
    private double wpm;
    private Leaderboard leaderboard;
    private SponsorSystem sponsorSystem;
    private boolean autocorrect;
    private boolean caffeineMode;
    private boolean nightShift;
    private boolean[] hasWristSupport;
    private boolean[] hasEnergyDrink;
    private boolean[] hasHeadphones;
    private String passageText;

    public RacePanel(JPanel panel, Typist[] typists, int passageLength, String passageText, 
        Leaderboard leaderboard, SponsorSystem sponsorSystem, boolean autocorrect, 
        boolean caffeineMode, boolean nightShift, boolean[] hasWristSupport, 
        boolean[] hasEnergyDrink, boolean[] hasHeadphones)
    {
        this.passageText = passageText;
        this.panel = panel;
        this.typists = typists;
        this.passageLength = passageLength;
        this.progressBars = new JProgressBar[typists.length];
        this.leaderboard = leaderboard;
        this.sponsorSystem = sponsorSystem;
        this.autocorrect = autocorrect;
        this.caffeineMode = caffeineMode;
        this.nightShift = nightShift;
        this.hasWristSupport = hasWristSupport;
        this.hasEnergyDrink = hasEnergyDrink;
        this.hasHeadphones = hasHeadphones;
    }

    public void showRace()
    {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Race in Progress!");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(Box.createVerticalStrut(20));
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));

        // Passage text display
        JTextPane passagePane = new JTextPane();
        passagePane.setText(passageText);
        passagePane.setEditable(false);
        passagePane.setMaximumSize(new Dimension(600, 100));
        passagePane.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(passagePane);
        panel.add(Box.createVerticalStrut(20));

        for (int i = 0; i < typists.length; i++)
        {
            JLabel nameLabel = new JLabel(typists[i].getSymbol() + " " + typists[i].getName());
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            progressBars[i] = new JProgressBar(0, passageLength);
            progressBars[i].setValue(0);
            progressBars[i].setStringPainted(true);
            progressBars[i].setMaximumSize(new Dimension(600, 30));
            progressBars[i].setAlignmentX(Component.CENTER_ALIGNMENT);

            panel.add(nameLabel);
            panel.add(Box.createVerticalStrut(5));
            panel.add(progressBars[i]);
            panel.add(Box.createVerticalStrut(15));
        }

        panel.revalidate();
        panel.repaint();

        startRaceThread();
    }

    private void startRaceThread()
    {
        Thread raceThread = new Thread(() -> {

            // At the start, register with sponsor system too:
            for (Typist t : typists) {
                leaderboard.registerTypist(t.getName());
                sponsorSystem.registerTypist(t.getName());
                t.resetToStart();
            }

            startTime = System.currentTimeMillis();
            boolean finished = false;
            Typist winner = null;

            // Apply night shift - reduce all accuracies
            if (nightShift) {
                for (Typist t : typists) {
                    t.setAccuracy(t.getAccuracy() - 0.1);
                }
            }
            int turn = 0;
            while (!finished)
            {
                turn++;
                for (int i = 0; i < typists.length; i++) {
                    advanceTypist(typists[i], turn, i);
                }

                SwingUtilities.invokeLater(() -> {
                for (int i = 0; i < typists.length; i++) {
                    progressBars[i].setValue(typists[i].getProgress());
                    int pos = Math.min(typists[i].getProgress(), passageText.length());
                    progressBars[i].setString(typists[i].getName() +
                        (typists[i].isBurntOut() ? " ~ BURNT OUT" : "") +
                        " [" + pos + "/" + passageLength + "]");
                    }
                });

                for (Typist t : typists) {
                    if (t.getProgress() >= passageLength && winner == null) {
                        winner = t;
                        finished = true;
                    }
                }

                try {
                    Thread.sleep(200);
                } catch (Exception e) {}
            }

            long endTime = System.currentTimeMillis();
            long timeSeconds = (endTime - startTime) / 1000;
            wpm = (passageLength / 5.0) / (timeSeconds / 60.0);
            final double finalWpm = wpm;

            for (int i = 0; i < typists.length; i++) {
                int position = typists[i] == winner ? 1 : 2;
                leaderboard.recordResult(typists[i].getName(), position, typists[i].getBurnoutCount() > 0);
                leaderboard.updatePersonalBest(typists[i].getName(), finalWpm);
                leaderboard.addRaceHistory(typists[i].getName(), position, finalWpm, typists[i].getBurnoutCount());
                sponsorSystem.recordResult(typists[i].getName(), position, finalWpm, typists[i].getBurnoutCount() > 0);
                
            }

            final Typist finalWinner = winner;
            SwingUtilities.invokeLater(() -> {
                JLabel winnerLabel = new JLabel("And the winner is... " + finalWinner.getName() + "!");
                winnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                winnerLabel.setFont(new Font("Arial", Font.BOLD, 20));
                panel.add(winnerLabel);

                JButton continueButton = new JButton("View Stats");
                continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                continueButton.addActionListener(e -> {
                    StatsPanel statsPanel = new StatsPanel(panel, typists, finalWinner, finalWpm, leaderboard, sponsorSystem);
                    statsPanel.showStats();
            });
        panel.add(Box.createVerticalStrut(10));
        panel.add(continueButton);
        panel.revalidate();
        panel.repaint();

        });
    });
    raceThread.start();
}

    private void advanceTypist(Typist theTypist, int turn, int index)
    {
        double MISTYPE_BASE_CHANCE = 0.3;
        int SLIDE_BACK_AMOUNT = autocorrect ? 1 : 2;
        int BURNOUT_DURATION = hasWristSupport[index] ? 2 : 3; // wrist support reduces burnout

        // Headphones reduce mistype chance
        if (hasHeadphones[index]) {
            MISTYPE_BASE_CHANCE = 0.15;
        }

        // Energy drink - boost first half, penalty second half
        double accuracyBoost = 0.0;
        if (caffeineMode && turn <= 10) {
            accuracyBoost += 0.1;
        }
        if (hasEnergyDrink[index]) {
            if (theTypist.getProgress() < passageLength / 2) {
                accuracyBoost += 0.1; // first half boost
            } else {
                accuracyBoost -= 0.1; // second half penalty
            }
        }

        if (theTypist.isBurntOut())
        {
            theTypist.recoverFromBurnout();
            return;
        }

        if (Math.random() < theTypist.getAccuracy() + accuracyBoost)
        {
            theTypist.typeCharacter();
        }

        if (Math.random() < (1 - theTypist.getAccuracy()) * MISTYPE_BASE_CHANCE)
        {
            theTypist.slideBack(SLIDE_BACK_AMOUNT);
        }

        double burnoutMultiplier = (caffeineMode && turn > 10) ? 0.1 : 0.05;
        if (Math.random() < burnoutMultiplier * theTypist.getAccuracy() * theTypist.getAccuracy())
        {
            theTypist.burnOut(BURNOUT_DURATION);
        }
    }
}