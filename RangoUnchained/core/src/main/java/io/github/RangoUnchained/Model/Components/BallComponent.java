package io.github.RangoUnchained.Model.Components;

/**
 * Component representing the type of a ball entity.
 */
public class BallComponent implements Component{

    public static final int ARMEDILLOTYPE = 0;
    public static final int TUMBLEWEEDTYPE = 1;
    public static final int CACTUSTYPE = 2;
    public static final int BOMB = 3;

    private final int TYPE;

    /**
     * Constructs a new {@link BallComponent} with the specified type.
     *
     * @param type the ball type
     */
    public BallComponent(int type){
        this.TYPE = type;
    }

    public String getTypeName(){
        switch (TYPE) {
            case ARMEDILLOTYPE:
                return "Armedillo";
            case TUMBLEWEEDTYPE:
                return "Tumbleweed";
            case CACTUSTYPE:
                return "Cactus";
            case BOMB:
                return "Bomb";
            default:
                return "Unknown";
        }
    }
}
