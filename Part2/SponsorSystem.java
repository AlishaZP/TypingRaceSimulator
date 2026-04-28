import java.util.ArrayList;

public class SponsorSystem
{
    private ArrayList<String> typistNames;
    private ArrayList<Integer> earnings;
    private ArrayList<String> sponsors;
    private ArrayList<String> sponsorConditions;

    public SponsorSystem()
    {
        typistNames = new ArrayList<>();
        earnings = new ArrayList<>();
        sponsors = new ArrayList<>();
        sponsorConditions = new ArrayList<>();
    }

    // Register a typist with a sponsor
    public void registerTypist(String name)
    {
        if (!typistNames.contains(name))
        {
            typistNames.add(name);
            earnings.add(0);

            // Assign a random sponsor
            String[] sponsorList = {"KeyCorp", "TypeMaster", "SpeedKeys", "FingerFly"};
            String[] conditionList = {
                "No burnouts: +50 coins",
                "Finish in top half: +30 coins",
                "WPM over 50: +40 coins",
                "Win the race: +60 coins"
            };
            int random = (int)(Math.random() * sponsorList.length);
            sponsors.add(sponsorList[random]);
            sponsorConditions.add(conditionList[random]);
        }
    }

    // Award prize money based on position and WPM
    public void recordResult(String name, int position, double wpm, boolean burntOut)
    {
        int index = typistNames.indexOf(name);
        if (index == -1) return;

        // Base prize money
        int prize = 0;
        if (position == 1) prize = 100;
        else if (position == 2) prize = 60;
        else prize = 30;

        // Speed bonus
        if (wpm > 50) prize += 40;

        // Burnout penalty
        if (burntOut) prize -= 20;

        // Sponsor bonus
        String condition = sponsorConditions.get(index);
        if (condition.contains("No burnouts") && !burntOut) prize += 50;
        else if (condition.contains("Win the race") && position == 1) prize += 60;
        else if (condition.contains("WPM over 50") && wpm > 50) prize += 40;
        else if (condition.contains("top half") && position <= 2) prize += 30;

        earnings.set(index, earnings.get(index) + Math.max(0, prize));
    }

    public String getName(int index) { return typistNames.get(index); }
    public int getEarnings(int index) { return earnings.get(index); }
    public String getSponsor(int index) { return sponsors.get(index); }
    public String getSponsorCondition(int index) { return sponsorConditions.get(index); }
    public int getSize() { return typistNames.size(); }
}
