package core.src.main.java.io.github.RangoUnchained.Model.Components;

public class PositionComponent implements Component {

    private int posX;
    private int posY;
    private int rotation;

    public PositionComponent() {
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }
}


