import java.util.ArrayList;
import java.util.Collections;

public class Leaderboard
{
    private ArrayList<String> names;
    private ArrayList<Integer> points;
    private ArrayList<Integer> wins;
    private ArrayList<Integer> racesPlayed;
    private ArrayList<Double> personalBests; 
    private ArrayList<ArrayList<String>> raceHistory; 

    public Leaderboard()
    {
        names = new ArrayList<>();
        points = new ArrayList<>();
        wins = new ArrayList<>();
        racesPlayed = new ArrayList<>();
        personalBests = new ArrayList<>();
        raceHistory = new ArrayList<>();
    }

    // Add a typist to the leaderboard if not already there
    public void registerTypist(String name)
    {
        if (!names.contains(name))
        {
            names.add(name);
            points.add(0);
            wins.add(0);
            racesPlayed.add(0);
            personalBests.add(0.0);
            raceHistory.add(new ArrayList<>());
        }
    }

    public void updatePersonalBest(String name, double wpm)
    {
        int index = names.indexOf(name);
        if (index == -1) return;
        if (wpm > personalBests.get(index)) {
            personalBests.set(index, wpm);
        }
    }

    public void addRaceHistory(String name, int position, double wpm, int burnouts)
    {
        int index = names.indexOf(name);
        if (index == -1) return;
        String record = "Position: " + position + " | WPM: " + String.format("%.1f", wpm) + " | Burnouts: " + burnouts;
        raceHistory.get(index).add(record);
    }

    public ArrayList<String> getRaceHistory(String name)
    {
        int index = names.indexOf(name);
        if (index == -1) return new ArrayList<>();
        return raceHistory.get(index);
    }

public double getPersonalBest(int index) { 
    return personalBests.get(index);
} 

public ArrayList<String> getNames() { 
    return names; 
}

    // Award points based on finishing position
    // 1st = 3pts, 2nd = 2pts, 3rd = 1pt
    public void recordResult(String name, int position, boolean burntOut)
    {
        int index = names.indexOf(name);
        if (index == -1) return;

        // Points based on position
        int earned = 0;
        if (position == 1) earned = 3;
        else if (position == 2) earned = 2;
        else earned = 1;

        // Burnout penalty
        if (burntOut) earned = Math.max(0, earned - 1);

        points.set(index, points.get(index) + earned);
        racesPlayed.set(index, racesPlayed.get(index) + 1);

        if (position == 1)
        {
            wins.set(index, wins.get(index) + 1);
        }
    }

    public String getName(int index) { return names.get(index); }
    public int getPoints(int index) { return points.get(index); }
    public int getWins(int index) { return wins.get(index); }
    public int getRacesPlayed(int index) { return racesPlayed.get(index); }
    public int getSize() { return names.size(); }

    // Get badge based on wins
    public String getBadge(String name)
    {
        int index = names.indexOf(name);
        if (index == -1) return "";
        int w = wins.get(index);
        int r = racesPlayed.get(index);

        if (w >= 3) return "🏆 Speed Demon";
        if (r >= 5 && points.get(index) > 0) return "🔥 Iron Fingers";
        if (w >= 1) return "⭐ Winner";
        return "🎯 Competitor";
    }

    public void sortByPoints()
    {
        for (int i = 0; i < names.size() - 1; i++) {
            for (int j = i + 1; j < names.size(); j++) {
                if (points.get(j) > points.get(i)) {
                    Collections.swap(names, i, j);
                    Collections.swap(points, i, j);
                    Collections.swap(wins, i, j);
                    Collections.swap(racesPlayed, i, j);
                    Collections.swap(personalBests, i, j);
                }
            }
        }
    }
}
