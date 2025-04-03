package io.github.RangoUnchained.Model.Components;

public class BallComponent implements Component{
    
    public static final int ARMEDILLOTYPE = 0;
    public static final int TUMBLEWEEDTYPE = 1;
    public static final int CACTUSTYPE = 2;

    final int TYPE;
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
            default:
                return "Unknown";
        }
    }
}
