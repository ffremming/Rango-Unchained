package io.github.RangoUnchained.Model.Components;

public class InputComponent implements Component {

    private boolean left;
    private boolean right;
    private boolean shoot;

    public InputComponent() {
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isShoot() {return shoot;}

    public void setShoot(boolean shoot) {
        this.shoot = shoot;
    }
}
