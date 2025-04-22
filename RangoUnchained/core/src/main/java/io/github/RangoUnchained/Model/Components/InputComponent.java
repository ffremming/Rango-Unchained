package io.github.RangoUnchained.Model.Components;

/**
 * Component representing input state and input lock for an entity.
 */
public class InputComponent implements Component {
    private int inputLock = 0;
    private boolean left, right, shoot;

    /**
     * Sets a lock duration that prevents input handling.
     *
     * @param duration the lock duration
     */
    public void setTimer(int duration) {
        inputLock = duration;
    }

    public boolean isLocked(){
        return inputLock > 0;
    }

    public void decrementInputLock(){
        if (inputLock> 0 ) {
            inputLock--;
        }
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

        public boolean isLeft() {
            return left;
        }
}
