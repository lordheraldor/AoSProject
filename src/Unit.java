public enum  Unit {
    GreatSwords(6, 1, 5, 4, 1, 2, 4, 3, 1, 1, 10),
    BlackOrcs(6, 2, 4, 4, 1, 2, 4, 3, 1, 1, 5),
    Bestigor(6, 1, 6, 4, 1, 2, 4, 3, 1, 1, 5),
    ChaosChosen(7, 2, 5, 4, 1, 3, 3, 3, 1, 1, 5),
    Executioners(7, 1, 6, 4, 1, 2, 3, 3, 0, 1, 5),
    GraveGuard(10, 1, 4, 5, 1, 2, 3, 3, 1, 1, 5),
    MenAtArms(4, 1, 5, 6, 2, 1, 5, 4, 0, 1, 10),
    Retributors(7, 3, 4, 4, 1, 2, 3, 3, 1, 2, 3),
    SwordMasters(7, 1, 6, 4, 1, 2, 3, 3, 1, 1, 5),
    TombGuard(10, 1, 4, 5, 1, 2, 4, 3, 1, 1, 5),
    WildWoodRangers(7, 1, 6, 5, 2, 2, 3, 3, 1, 1, 5),
    StormVermin(5, 1, 6, 5, 2, 2, 4, 3, 1, 1, 10);

    protected int bravery;
    protected int wounds;

    protected int movementInInches;

    protected int save;
    protected int range;

    protected int attacksPerModel;
    protected int successfulHit;
    protected int successfulWound;
    protected int rend;
    protected int damageDealt;
    protected int unitSize;

    protected int minimumUnitSize;

    protected int ranks;

    protected int woundsTaken = 0;

    protected boolean modelsSlain = false;

    Unit(int bravery, int wounds, int movementInInches, int save, int range, int attacksPerModel, int successfulHit, int successfulWound, int rend, int damageDealt, int minimumUnitSize) {
        this.bravery = bravery;
        this.wounds = wounds;
        this.movementInInches = movementInInches;
        this.save = save;
        this.range = range;
        this.attacksPerModel = attacksPerModel;
        this.successfulHit = successfulHit;
        this.successfulWound = successfulWound;
        this.rend = rend;
        this.damageDealt = damageDealt;
    }

    public int attack() {
        int attempts = getAttacksPerModel() * numberOfValidAttacks();

        int successes = 0;

        for (int i = 0; i < attempts; ++i) {
            Die die = new Die();
            die.roll();
            if (die.faceUp > getSuccessfulHit()) {
                successes++;
            }
        }

        System.out.printf("Unit lands: %s hits!\n", successes);

        return successes;
    }

    public int wounding(int successfulHits) {

        int successes = 0;

        for (int i = 0; i < successfulHits; ++i) {
            Die die = new Die();
            die.roll();
            if (die.faceUp > getSuccessfulWound()) {
                successes++;
            }
        }

        System.out.printf("Unit lands: %s wounds!\n", successes);

        return successes;
    }

    public int saves(int successfulWounds, Unit b) {

        int savesMissed = 0;

        for (int i = 0; i < successfulWounds; ++i) {
            Die die = new Die();
            die.roll();
            if ((die.faceUp - rend) < b.getWounds())
            {
                savesMissed++;
            }
        }

        System.out.printf("Unit takes: %s hit(s) from the opponent!\n", savesMissed);

        return savesMissed;
    }

    public int damageCalculation(int successfulWounds, Unit a) {
        int damageTaken = successfulWounds * a.getDamageDealt();

        if (successfulWounds == 0) {
            damageTaken = 0;
        }

        return damageTaken;
    }

    public int numberOfValidAttacks() {
        int validAttacks;

        if (getUnitSize() / getRanks() < getMinimumUnitSize()) {
            validAttacks = (getUnitSize() / getRanks()) * getRange();
        }
        else {
            validAttacks = getUnitSize();
        }

        return validAttacks;
    }

    public int getUnitSize() {
        return unitSize;
    }

    public void setUnitSize(int unitSize) {
        this.unitSize = unitSize;
    }

    public int getBravery() {
        return bravery;
    }

    public void setBravery(int bravery) {
        this.bravery = bravery;
    }

    public int getWounds() {
        return wounds;
    }

    public void setWounds(int wounds) {
        this.wounds = wounds;
    }

    public int getMovementInInches() {
        return movementInInches;
    }

    public void setMovementInInches(int movementInInches) {
        this.movementInInches = movementInInches;
    }

    public int getSave() {
        return save;
    }

    public void setSave(int save) {
        this.save = save;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getAttacksPerModel() {
        return attacksPerModel;
    }

    public void setAttacksPerModel(int attacksPerModel) {
        this.attacksPerModel = attacksPerModel;
    }

    public int getSuccessfulHit() {
        return successfulHit;
    }

    public void setSuccessfulHit(int successfulHit) {
        this.successfulHit = successfulHit;
    }

    public int getSuccessfulWound() {
        return successfulWound;
    }

    public void setSuccessfulWound(int successfulWound) {
        this.successfulWound = successfulWound;
    }

    public int getRend() {
        return rend;
    }

    public void setRend(int rend) {
        this.rend = rend;
    }

    public int getDamageDealt() {
        return damageDealt;
    }

    public void setDamageDealt(int damageDealt) {
        this.damageDealt = damageDealt;
    }

    public int getRanks() {
        return ranks;
    }

    public void setRanks(int ranks) {
        this.ranks = ranks;
    }

    public int getMinimumUnitSize() {
        return minimumUnitSize;
    }

    public void setMinimumUnitSize(int minimumUnitSize) {
        this.minimumUnitSize = minimumUnitSize;
    }
}
