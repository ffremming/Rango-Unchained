package core.src.main.java.io.github.RangoUnchained.Model.Components;

public class VelocityComponent implements Component {

    private int velocity;

    public VelocityComponent(int velocity) {
        this.velocity = velocity;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }
}
