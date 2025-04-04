package io.github.RangoUnchained.Model.Components;

public class InputComponent implements Component {
    private int inputLock = 0;
    private boolean left;
    private  boolean right;
    private boolean shoot;

   /* public InputState inputState = InputState.IDLE;*/

    public InputComponent() {
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

   /* public InputState getInputState() {
        return inputState;
    }*/

   /* public void setInputState(String state) {
        switch (state) {
            case "LEFT":
                inputState = InputState.LEFT;
                break;
            case "RIGHT":
                inputState = InputState.RIGHT;
                break;
            case "SHOOTING":
                inputState = InputState.SHOOTING;
                break;
            case "IDLE":
                inputState = InputState.IDLE;
                break;
        }
    }*/

  /*  public enum InputState {
        LEFT,
        RIGHT,
        SHOOTING,
        IDLE
    }*/
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

        public boolean isLeft() {
            return left;
        }

}
