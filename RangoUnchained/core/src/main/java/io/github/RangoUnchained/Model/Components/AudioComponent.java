package io.github.RangoUnchained.Model.Components;
import java.util.LinkedList;
import java.util.Queue;


public class AudioComponent implements Component{

    public enum ActionType {
        SHOOT, MOVE, BOUNCE, POP
    }
    public Queue<ActionType> audioQueue = new LinkedList<>();

}
