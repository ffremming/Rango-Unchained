package io.github.RangoUnchained.Model.Components;

public class InputComponent implements Component {

    private boolean left;
    private boolean right;
    private boolean shoot;
    private int inputLock = 0;


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

    public void setTimer(int duration){
        inputLock = duration;
    }

    public boolean isLocked(){
        return inputLock>0;
    }

    public void decrementInputLock(){
        if (inputLock>0){
            inputLock--;
        }
    }
}
