public class TypistTest
{
    public static void main(String[] args)
    {
        // Test 1 - progress cannot go below zero
        Typist t = new Typist('①', "TURBOFINGERS", 0.85);
        t.slideBack(10);
        System.out.println("Test 1 - slideBack below zero: " + t.getProgress()); // should print 0

        // Test 2 - burnout countdown
        t.burnOut(3);
        System.out.println("Test 2 - burnout turns remaining: " + t.getBurnoutTurnsRemaining()); // should print 3
        t.recoverFromBurnout();
        System.out.println("After 1 recovery: " + t.getBurnoutTurnsRemaining()); // should print 2
        t.recoverFromBurnout();
        System.out.println("After 2 recovery: " + t.getBurnoutTurnsRemaining()); // should print 1
        t.recoverFromBurnout();
        System.out.println("After 3 recovery: " + t.getBurnoutTurnsRemaining()); // should print 0
        System.out.println("Is burnt out: " + t.isBurntOut()); // should print false

        // Test 3 - resetToStart
        t.typeCharacter();
        t.typeCharacter();
        t.burnOut(2);
        t.resetToStart();
        System.out.println("Test 3 - progress after reset: " + t.getProgress()); // should print 0
        System.out.println("Burnt out after reset: " + t.isBurntOut()); // should print false

        // Test 4 - accuracy clamping
        t.setAccuracy(1.8);
        System.out.println("Test 4 - accuracy above 1.0: " + t.getAccuracy()); // should print 1.0
        t.setAccuracy(-0.5);
        System.out.println("Accuracy below 0.0: " + t.getAccuracy()); // should print 0.0

        // Test 5 - normal forward movement
        t.setAccuracy(0.85);
        t.typeCharacter();
        t.typeCharacter();
        t.typeCharacter();
        System.out.println("Test 5 - progress after 3 typeCharacter: " + t.getProgress()); // should print 3
    }
}