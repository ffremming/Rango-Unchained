package io.github.RangoUnchained.Model.Components;

public class LifeTimeComponent implements Component {
    
    int lifeTime;

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
