package io.github.RangoUnchained.Model.Components;

public class PhysicsComponent implements Component{
    
    int contactLock;

    public void setContactLock(int duration){
        contactLock = duration;
    }

    public void decrementContactLock(){
        if (contactLock>0){contactLock --;}
    }

    public boolean isContactLocked(){
        return contactLock>0;
    }

}
