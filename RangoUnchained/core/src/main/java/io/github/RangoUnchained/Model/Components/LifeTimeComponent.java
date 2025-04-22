package io.github.RangoUnchained.Model.Components;

/**
 * Component representing the remaining lifetime of an entity.
 */
public class LifeTimeComponent implements Component {

    private int lifeTime;

    public LifeTimeComponent(int lifeTime) {
        this.lifeTime = lifeTime;
    }

    public void decrementLifeTime() {
        lifeTime--;
    }

    public int getLifeTime() {
        return lifeTime;
    }

}
