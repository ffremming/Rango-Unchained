package io.github.RangoUnchained.Model.Components;

/**
 * Component representing how much an entity bounces.
 */
public class BounceComponent implements Component {

    private int type;

    public final static int SUPER = 12;
    public final static int HIGH = 9;
    public final static int MEDIUM = 8;
    public final static int LOW = 6;
    public final static int NOBOUNCE = 5;

    /**
     * Constructs a {@link BounceComponent} with the specified bounce type.
     * Invalid values default to {@code MEDIUM}.
     *
     * @param type the bounce type
     */
    public BounceComponent(int type) {
        setType(type);
    }

    public void setType(int type){
        if (type < NOBOUNCE || type > SUPER) {
            this.type = MEDIUM;
        } else {
            this.type = type;
        }
    }

    public int getType(){
        return type;
    }
}
