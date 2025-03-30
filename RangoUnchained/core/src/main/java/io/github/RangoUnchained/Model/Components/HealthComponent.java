package io.github.RangoUnchained.Model.Components;

public class HealthComponent implements Component{
    int health = 1;

    public void decreaseHealth(){
        health--;
    }

    public int getHealth(){
        return health;
    }

    public boolean isAlive(){
        return health>0;
    }
}
