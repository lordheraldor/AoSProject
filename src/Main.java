import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        int numberOfGS;
        int numberOfBO;
        int gSRanks;
        int bORanks;

        //Greatswords: Number of troops
        System.out.println("How many Greatswords are in the unit?\n");
        numberOfGS = input.nextInt();

        //Check if unit one size is appropriate
        if (numberOfGS < Unit.GreatSwords.getMinimumUnitSize()) {
            Unit.GreatSwords.setUnitSize(Unit.GreatSwords.getMinimumUnitSize());
            System.out.printf("Not enough models in unit. Unit size set to minimum: %s\n", Unit.GreatSwords.getMinimumUnitSize());
        }
        else {
            Unit.GreatSwords.unitSize = numberOfGS;
        }

        //Assigning unit one formation depth
        System.out.println("How many ranks are there in the formation?");
        gSRanks = input.nextInt();
        Unit.GreatSwords.ranks = gSRanks;

        //Black Orcs: Number of troops
        System.out.println("How many Black Orcs are there in the unit?");
        numberOfBO = input.nextInt();

        //Check if unit two size is appropriate
        if (numberOfBO < Unit.BlackOrcs.getMinimumUnitSize()) {
            Unit.BlackOrcs.setUnitSize(Unit.BlackOrcs.getMinimumUnitSize());
            System.out.printf("Not enough models in unit. Unit size set to minimum: %s\n", Unit.BlackOrcs.getMinimumUnitSize());
        }
        else {
            Unit.BlackOrcs.unitSize = numberOfBO;
        }

        //Assigning unit two formation depth
        System.out.println("How many ranks are there in the formation?");
        bORanks = input.nextInt();
        Unit.BlackOrcs.ranks = bORanks;

        System.out.println("Combat begins!!\n\n");

        //Turn Timer, cannot be over 6 turns
        int turnTimer = 0;

        do {

            ++turnTimer;

            if (Unit.GreatSwords.getUnitSize() <= 0) {
                System.out.print("Black Orcs win the combat!\n\n");
                break;
            }

            if (Unit.BlackOrcs.getUnitSize() <= 0) {
                System.out.print("Greatswords win the combat!\n\n");
                break;
            }

            //Attacker attack loop
            System.out.println("Greatswords attack their enemy");
            int damageDoneToDefender;
            damageDoneToDefender = attackPhase(Unit.BlackOrcs, Unit.GreatSwords);

            System.out.printf("Black Orcs take %s damage from Greatswords\n", damageDoneToDefender);
            int unitOneKilled = killedModels(damageDoneToDefender, Unit.BlackOrcs);
            System.out.printf("%s Black Orcs died as a result of combat\n", unitOneKilled);

            Unit.BlackOrcs.setUnitSize(Unit.BlackOrcs.getUnitSize() - unitOneKilled);
            System.out.printf("Remaining Black Orcs: %s\n\n", Unit.BlackOrcs.getUnitSize());


            //Counter attack loop
            System.out.println("Black Orcs counter attack!!\n");
            damageDoneToDefender = attackPhase(Unit.GreatSwords, Unit.BlackOrcs);
            int unitTwoKilled = killedModels(damageDoneToDefender, Unit.GreatSwords);

            System.out.printf("%s Greatswords died as a result of combat\n", unitTwoKilled);
            Unit.GreatSwords.setUnitSize(Unit.GreatSwords.getUnitSize() - unitTwoKilled);

            System.out.printf("Remaining Greatswords: %s\n\n", Unit.GreatSwords.getUnitSize());

            /*
            Possibly redundant code
            if (Unit.GreatSwords.unitSize < 0 || Unit.BlackOrcs.unitSize < 0) {
                break;
            }
            */

            //Battleshock Test
            if (!Unit.BlackOrcs.modelsSlain) {
                battleShock(Unit.BlackOrcs, unitOneKilled);
            }

            if (!Unit.GreatSwords.modelsSlain) {
                battleShock(Unit.GreatSwords, unitTwoKilled);
            }

            //Turn timer read out
            System.out.printf("Turn %s\n\n __________________________________________\n\n", turnTimer);

        } while (Unit.GreatSwords.unitSize > 0 && Unit.BlackOrcs.unitSize > 0 && turnTimer < 6);
    }

    //Attack calculations and damage
    private static int attackPhase(Unit defender, Unit attacker) {
        int attackResults,
                woundResults,
                saveResults,
                damageReceived;

        attackResults = attacker.attack();

        if (attackResults == 0) {
            return 0;
        }

        woundResults = attacker.wounding(attackResults);

        if (woundResults == 0) {
            return 0;
        }

        saveResults = attacker.saves(woundResults, defender);

        if (saveResults == 0) {
            return 0;
        }
        else {
            damageReceived = attacker.damageCalculation(saveResults, attacker);
        }

        return damageReceived;
    }

    //Killing off models from unit
    private static int killedModels(int damageDone, Unit defender) {
        int result;

        if (damageDone >= defender.wounds) {
            result = (damageDone + defender.woundsTaken) / defender.wounds;
            defender.modelsSlain = true;
            defender.woundsTaken = damageDone % defender.wounds;
            return result;
        }

        defender.woundsTaken = defender.woundsTaken + damageDone;
        if (defender.woundsTaken >= defender.wounds) {
            result = defender.woundsTaken / defender.wounds;
            defender.modelsSlain = true;
            defender.woundsTaken = defender.woundsTaken % defender.wounds;
            return result;
        }

        defender.modelsSlain = false;
        return 0;
    }

    //Test of courage
    private static void battleShock(Unit defender, int killed) {
        int sizeBonus = defender.unitSize / 10;
        int cowards = 0;

        Die die = new Die();
        int braveryRoll = die.faceUp + killed;

        if (braveryRoll > defender.bravery + sizeBonus) {
            cowards = braveryRoll - (defender.bravery + sizeBonus);
        }

        System.out.printf("%s cowards run from the battlefield! A shameful display!\n\n", cowards);
        defender.unitSize = defender.unitSize - cowards;
        defender.modelsSlain = false;
    }
}
