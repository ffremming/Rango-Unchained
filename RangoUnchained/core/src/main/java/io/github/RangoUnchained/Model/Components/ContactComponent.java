package io.github.RangoUnchained.Model.Components;

/**
 * Component used to manage temporary contact lock for an entity.
 */
public class ContactComponent implements Component{

    private int contactLock;

    public void setContactLock(int duration) {
        contactLock = duration;
    }

    public void decrementContactLock(){
        if (contactLock > 0) {
            contactLock--;
        }
    }

    public boolean isContactLocked(){
        return contactLock > 0;
    }
}
