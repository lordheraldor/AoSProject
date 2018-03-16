public enum  Unit {
    GreatSwords(6, 1, 5, 4, 1, 2, 4, 3, 1, 1),
    BlackOrcs(6, 2, 4, 4, 1, 2, 4, 3, 1, 1);

    protected int bravery;
    protected int wounds;

    protected int movementInInches;

    protected int save;
    protected int range;

    protected int attacks;
    protected int successfulHit;
    protected int successfulWound;
    protected int rend;
    protected int damageDealt;
    protected int unitSize;

    protected int ranks;

    protected int woundsTaken = 0;

    protected boolean modelsSlain = false;

    Unit(int bravery, int wounds, int movementInInches, int save, int range, int attacks, int successfulHit, int successfulWound, int rend, int damageDealt) {
        this.bravery = bravery;
        this.wounds = wounds;
        this.movementInInches = movementInInches;
        this.save = save;
        this.range = range;
        this.attacks = attacks;
        this.successfulHit = successfulHit;
        this.successfulWound = successfulWound;
        this.rend = rend;
        this.damageDealt = damageDealt;
    }

    public int attack() {
        int attempts = getAttacks() * ((getUnitSize() / getRanks()) * getRange());

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

    public int getAttacks() {
        return attacks;
    }

    public void setAttacks(int attacks) {
        this.attacks = attacks;
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
}
