package io.github.RangoUnchained.Model.Components;

public class BounceComponent implements Component {
    
    public int type;

    public final static int SUPER = 12;
    public final static int HIGH = 9;
    public final static int MEDIUM = 8;
    public final static int LOW = 6;
    public final static int NOBOUNCE = 5;

    public BounceComponent(int type){
        setType(type);
    }

    public void setType(int type){
        this.type = type;
        if (type<NOBOUNCE||type>SUPER){
            type = MEDIUM;
        }
    }

    public int getType(){
        return type;
    }
}
