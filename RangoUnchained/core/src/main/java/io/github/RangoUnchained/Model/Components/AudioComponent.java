package io.github.RangoUnchained.Model.Components;
import java.util.concurrent.atomic.AtomicBoolean;


public class AudioComponent implements Component{
    public enum ActionType {
        SHOOT, MOVE, BOUNCE, POP
    }

    public final AtomicBoolean hasWalkingAudio = new AtomicBoolean(false);
    public final AtomicBoolean hasShootingAudio = new AtomicBoolean(false);

}
