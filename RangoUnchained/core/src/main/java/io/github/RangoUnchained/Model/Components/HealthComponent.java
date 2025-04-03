package io.github.RangoUnchained.Model.Components;

public class HealthComponent implements Component{
    private boolean shieldActive = false;
    private int health = 1;

    public HealthComponent(int health){
       setHealth(health);
    }

    public void setHealth(int health){
        this.health = health;
    }

    public void addHealth(int additionalHealth){
        health += additionalHealth;
    }

    public void decreaseHealth(){
        health--;
    }

    public int getHealth(){
        return health;
    }

    public boolean isAlive(){
        return health>0;
    }

    public void setShieldActive(boolean shieldActive) {
        this.shieldActive = shieldActive;
    }

    public boolean isShieldActive() {
        return shieldActive;
    }
}
