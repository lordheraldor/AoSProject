public class Die {
    public int faceUp;

    public Die() {
        roll();
    }

    public void roll() {
        faceUp = (int) (Math.random() * 6 + 1);
    }
}
