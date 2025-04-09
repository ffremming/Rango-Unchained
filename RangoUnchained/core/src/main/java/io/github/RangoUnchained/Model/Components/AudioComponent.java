package io.github.RangoUnchained.Model.Components;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import java.util.List;


public class AudioComponent implements Component{

    public enum ActionType {
        SHOOT, MOVE, BOUNCE, POP
    }

    public enum EntityType {
        PLAYER, BALL, PROJECTILE
    }

    public HashMap<ActionType, List<Sound>> audioMap = new HashMap<>();
    public Queue<ActionType> audioQueue = new LinkedList<>();

    public AudioComponent(EntityType entity) {
        switch (entity) {
            case PLAYER:
                List<Sound> shootSounds = new LinkedList<>();
                shootSounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/Lick_1.mp3")));
                shootSounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/Lick_2.mp3")));
                audioMap.put(ActionType.SHOOT, shootSounds);

                List<Sound> moveSounds = new LinkedList<>();
                moveSounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/walking_1.wav")));
                moveSounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/walking_2.wav")));
                moveSounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/walking_3.wav")));
                moveSounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/walking_4.wav")));
                moveSounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/walking_5.wav")));
                audioMap.put(ActionType.MOVE, moveSounds);
                break;

            case BALL:
                List<Sound> bounceSounds = new LinkedList<>();
                bounceSounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/BalloonBounce.mp3")));
                audioMap.put(ActionType.BOUNCE, bounceSounds);

            case PROJECTILE:
                List<Sound> popSounds = new LinkedList<>();
                popSounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/PopBalloonSound_1.mp3")));
                popSounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/PopBalloonSound_2.mp3")));
                popSounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/PopBalloonSound_3.mp3")));
                audioMap.put(ActionType.POP, popSounds);
                break;
        }
    }
}
