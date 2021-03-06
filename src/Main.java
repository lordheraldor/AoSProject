import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        //Necessary information variables
        Scanner input = new Scanner(System.in);
        int attackerModels;
        int defendingModels;
        int attackerRanks;
        int defenderRanks;

        //Unit names
        String attackerUnitName;
        String defenderUnitName;

        //Attacking and Defending unit declarations
        Unit attackingUnit = null;
        Unit defendingUnit = null;


        //Select units for attacker and defender
        //Attacker
       while (attackingUnit == null){
           try {
               System.out.println("Please select the attacking unit:");
               showUnitSelection();
               System.out.print("Enter selection:");
               attackingUnit = selectUnit(input.nextInt());
           } catch (Exception e) {
               unitSelectionErrorMessage();
           }
        }

        //Collect attacker's name
        attackerUnitName = attackingUnit.getUnitName();

        //Unit size of attacker
        System.out.printf("How many models are there in the %s unit?\n", attackerUnitName);
        attackerModels = input.nextInt();

        //Check if unit one size is appropriate
        if (attackerModels < attackingUnit.getMinimumUnitSize()) {
            attackingUnit.setUnitSize(attackingUnit.getMinimumUnitSize());
            System.out.printf("Not enough models in unit. Unit size set to minimum: %s\n", attackingUnit.getMinimumUnitSize());
        }
        else {
            attackingUnit.setUnitSize(attackerModels);
        }

        //Assigning attacker formation depth
        System.out.println("How many ranks are there in the formation?\n");
        attackerRanks = input.nextInt();
        attackingUnit.setRanks(attackerRanks);


        //Defender
        // Collect unit
        while (defendingUnit == null) {
            try {
                System.out.println("Please select the defending unit:");
                showUnitSelection();
                System.out.print("Enter selection:");
                defendingUnit = selectUnit(input.nextInt());
            } catch (Exception e) {
                unitSelectionErrorMessage();
            }
        }

        //Collect defender's name
        defenderUnitName = defendingUnit.unitName;

        //Unit size of defender
        System.out.printf("How many models are there in the %s unit?\n", defenderUnitName);
        defendingModels = input.nextInt();

        //Check if unit two size is appropriate
        if (defendingModels < defendingUnit.getMinimumUnitSize()) {
            defendingUnit.setUnitSize(defendingUnit.getMinimumUnitSize());
            System.out.printf("Not enough models in unit. Unit size set to minimum: %s\n", defendingUnit.getMinimumUnitSize());
        }
        else {
            defendingUnit.setUnitSize(defendingModels);
        }

        //Assigning defender formation depth
        System.out.println("How many ranks are there in the formation?\n");
        defenderRanks = input.nextInt();
        defendingUnit.setRanks(defenderRanks);



        System.out.println("Combat begins!!\n\n");

        //Turn Timer, cannot be over 6 turns
        int turnTimer = 0;

        do {

            ++turnTimer;

            if (attackingUnit.getUnitSize() <= 0) {
                System.out.printf("%s win the combat!\n\n", defenderUnitName);
                break;
            }

            if (defendingUnit.getUnitSize() <= 0) {
                System.out.printf("%s win the combat!\n\n", attackerUnitName);
                break;
            }

            //Attacker attack loop
            System.out.printf("%s attack their enemy\n\n", attackerUnitName);
            int damageDoneToDefender;
            damageDoneToDefender = attackPhase(defendingUnit, attackingUnit);

            System.out.printf("%s take %s damage from Greatswords\n", defenderUnitName, damageDoneToDefender);
            int unitOneKilled = killedModels(damageDoneToDefender, defendingUnit);
            System.out.printf("%s %s died as a result of combat\n", unitOneKilled, defenderUnitName);

            defendingUnit.setUnitSize(defendingUnit.getUnitSize() - unitOneKilled);
            System.out.printf("Remaining %s: %s\n\n", defenderUnitName, defendingUnit.getUnitSize());


            //Counter attack loop
            System.out.printf("%s counter attack!!\n\n", defenderUnitName);

            damageDoneToDefender = attackPhase(attackingUnit, defendingUnit);
            int unitTwoKilled = killedModels(damageDoneToDefender, attackingUnit);

            System.out.printf("%s %s died as a result of combat\n", unitTwoKilled, attackerUnitName);
            attackingUnit.setUnitSize(attackingUnit.getUnitSize() - unitTwoKilled);
            System.out.printf("Remaining %s: %s\n\n", attackerUnitName, attackingUnit.getUnitSize());

            //Battleshock Test
            if (!defendingUnit.modelsSlain) {
                battleShock(defendingUnit, unitOneKilled);
            }

            if (!attackingUnit.modelsSlain) {
                battleShock(defendingUnit, unitTwoKilled);
            }

            //Turn timer read out
            System.out.printf("Turn %s\n\n __________________________________________\n\n", turnTimer);

        } while (attackingUnit.getUnitSize() > 0 && defendingUnit.getUnitSize() > 0 && turnTimer < 6);
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

    private static Unit selectUnit(int selection) {

        switch (selection) {
            case 1:
                return Unit.GREATSWORDS;
            case 2:
                return Unit.BLACKORCS;
            case 3:
                return Unit.BESTIGOR;
            case 4:
                return Unit.CHAOSCHOSEN;
            case 5:
                return Unit.MENATARMS;
            case 6:
                return Unit.EXECUTIONERS;
            case 7:
                return Unit.GRAVEGUARD;
            case 8:
                return Unit.RETRIBUTORS;
            case 9:
                return Unit.STORMVERMIN;
            case 10:
                return Unit.SWORDMASTERS;
            case 11:
                return Unit.TOMBGUARD;
            case 12:
                return Unit.WILDWOODRANGERS;
        }

        return null;
    }

    private static void showUnitSelection() {
        System.out.println("1 - Greatswords\n" +
                "2 - Black Orcs\n" +
                "3 - Bestigor\n" +
                "4 - Chosen\n" +
                "5 - Men at Arms\n" +
                "6 - Executioners\n" +
                "7 - Grave Guard\n" +
                "8 - Retributors\n" +
                "9 - Stormvermin\n" +
                "10 - Swordmasters\n" +
                "11 - Tomb Guard\n" +
                "12 - Wildwood Rangers\n");
    }

    private static void unitSelectionErrorMessage() {
        System.out.println("Please enter a valid option: (1-12 are valid options)");
    }
}
