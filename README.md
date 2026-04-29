# Typing Race Simulator
Object Oriented Programming Project — ECS414U

## Overview
A typing race simulation where competitors race to type through a passage of text. 
The project includes a textual command-line simulation (Part 1) and a graphical 
user interface simulation (Part 2).

## Project Structure
```
TypingRaceSimulator/
├── Part1/    # Textual simulation (Java, command-line)
│   ├── Typist.java
│   ├── TypistTest.java
│   └── TypingRace.java
└── Part2/    # GUI simulation (Java Swing)
    ├── Typist.java
    ├── TypingRace.java
    ├── TypingRaceGUI.java
    ├── TypistSetupPanel.java
    ├── RacePanel.java
    ├── StatsPanel.java
    ├── Leaderboard.java
    ├── LeaderboardPanel.java
    └── SponsorSystem.java
```

## Part 1 — Textual Simulation

### How to compile
```bash
cd Part1
javac Typist.java TypingRace.java
```

### How to run
Add a main method to TypingRace.java:
```java
public static void main(String[] args) {
    TypingRace race = new TypingRace(40);
    race.addTypist(new Typist('①', "TURBOFINGERS", 0.85), 1);
    race.addTypist(new Typist('②', "QWERTY_QUEEN", 0.60), 2);
    race.addTypist(new Typist('③', "HUNT_N_PECK",  0.30), 3);
    race.startRace();
}
```
### How to run tests
```bash
javac Typist.java TypistTest.java
java TypistTest
```

Then run:
```bash
java TypingRace
```
## Part 2 — GUI Simulation

### How to compile
```bash
cd Part2
javac *.java
```

### How to run
```bash
java TypingRaceGUI
```

### Features
- Configure passage length (short, medium, long or custom)
- 2 to 6 customisable typists
- Typing styles: Touch Typist, Hunt & Peck, Phone Thumbs, Voice-to-Text
- Keyboard types: Mechanical, Membrane, Touchscreen, Stenography
- Accessories: Wrist Support, Energy Drink, Noise-Cancelling Headphones
- Difficulty modifiers: Autocorrect, Caffeine Mode, Night Shift
- Live race animation with progress bars
- Post-race statistics including WPM, accuracy and burnout count
- Leaderboard with points, wins and badges
- Sponsor system with earnings and bonus conditions
- Comparison view for typists

## Notes
- All code compiles and runs using standard command-line tools
- No IDE-specific configuration required
- The textual version is started by calling startRace() in Part1
- The graphical version is started by calling startRaceGUI() in Part2