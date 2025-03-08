public class VelocityComponent {

    private int velocityY;

    private int velocityX;

    public VelocityComponent(int velocityY, int velocityX) {
        this.velocityY = velocityY;
        this.velocityX = velocityX;
    }

    public int getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }

    public int getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }
}
