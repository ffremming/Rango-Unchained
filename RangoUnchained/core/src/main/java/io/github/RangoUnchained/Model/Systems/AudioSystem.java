package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Timer;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import io.github.RangoUnchained.Model.Components.AudioComponent;
import io.github.RangoUnchained.Model.Components.InputComponent;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.FloorEntity;
import io.github.RangoUnchained.Model.Entities.ObstacleEntity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;
import io.github.RangoUnchained.Model.Entities.ProjectileEntity;
import io.github.RangoUnchained.Model.Factories.AudioLoader;

public class AudioSystem implements System {


    private static final long COLLISION_SOUND_COOLDOWN_MS = 150;
    private static final int MAX_CONCURRENT_SOUNDS = 5;
    private static final float SOUND_EXPIRE_TIME = 1.0f;

    private static final float VOLUME_BOUNCE = 0.5f;
    private static final float VOLUME_POP = 10f;
    private static final float VOLUME_MOVE = 1.5f;
    private static final float VOLUME_SHOOT = 0.7f;

    private static final float DELAY_WALKING = 0.5f;
    private static final float DELAY_SHOOTING = 1.2f;


    private final ComponentFilter filter = new ComponentFilter();
    private final Random random = new Random();
    private final AudioLoader audioLoader;
    private long lastCollisionSoundTime = 0;
    private final Queue<PlayingSound> activeSounds = new LinkedList<>();


    private static class PlayingSound {
        public final Sound sound;
        public final long soundId;
        public final AudioComponent.ActionType actionType;
        public PlayingSound(Sound sound, long soundId, AudioComponent.ActionType actionType) {
            this.sound = sound;
            this.soundId = soundId;
            this.actionType = actionType;
        }
    }

    private int getPriority(AudioComponent.ActionType actionType) {
        switch (actionType) {
            case SHOOT:
                return 10;
            case POP:
                return 8;
            case MOVE:
                return 5;
            case BOUNCE:
                return 2;
            default:
                return 1;
        }
    }

    public AudioSystem(ContactSystem centralContactListener){
        filter.require(AudioComponent.class);
        audioLoader = AudioLoader.getInstance();

        centralContactListener.subscribe(
            BallEntity.class, FloorEntity.class,
            collisionEvent -> playSound(AudioComponent.ActionType.BOUNCE, VOLUME_BOUNCE),
            null);
        centralContactListener.subscribe(
            BallEntity.class, ObstacleEntity.class,
            collisionEvent -> playSound(AudioComponent.ActionType.BOUNCE, VOLUME_BOUNCE),
            null);
        centralContactListener.subscribe(
            BallEntity.class, ProjectileEntity.class,
            collisionEvent -> playSound(AudioComponent.ActionType.POP, VOLUME_POP),
            null);
    }


    private void playSound(AudioComponent.ActionType soundKey, float volume) {

        // Handle bounce cooldown to prevent audio spam.
        if (soundKey == AudioComponent.ActionType.BOUNCE) {
            long currentTime = java.lang.System.currentTimeMillis();
            if (currentTime - lastCollisionSoundTime < COLLISION_SOUND_COOLDOWN_MS) {
                return;
            }
            lastCollisionSoundTime = currentTime;
        }

        List<Sound> sounds = audioLoader.getSounds(soundKey);

        // Handle no sound found.
        if (sounds == null || sounds.isEmpty()) {
            Gdx.app.error("AudioSystem", "No sound assets found for action: " + soundKey);
            return;
        }

        int newSoundPriority = getPriority(soundKey);

        //Handle active sounds: If full, determine if the sound can be inserted by exchanging a lower priority sound.
        if (activeSounds.size() >= MAX_CONCURRENT_SOUNDS) {
            PlayingSound lowestPrioritySound = null;
            int lowestPriority = Integer.MAX_VALUE;

            for (PlayingSound ps : activeSounds) {
                int psPriority = getPriority(ps.actionType);
                if (psPriority < lowestPriority) {
                    lowestPriority = psPriority;
                    lowestPrioritySound = ps;
                }
            }
            if (lowestPrioritySound != null && lowestPriority < newSoundPriority) {
                lowestPrioritySound.sound.stop(lowestPrioritySound.soundId);
                activeSounds.remove(lowestPrioritySound);
            } else {
                //No suitable slot available.
                return;
            }
        }

        //Adds the new sound to the active sounds.
        Sound sound = sounds.get(random.nextInt(sounds.size()));
        long soundId = sound.play(volume);
        Gdx.app.log("AudioSystem", "Playing sound: " + sound);

        PlayingSound playing = new PlayingSound(sound, soundId, soundKey);
        activeSounds.add(playing);

        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                activeSounds.remove(playing);
            }
        }, SOUND_EXPIRE_TIME);
    }

    private void delaySound (AtomicBoolean action, float delay){
        action.set(true);
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                action.set(false);
            }
        }, delay);
    }

    @Override
    public void updateEntity(Entity entity, float delta) {
        AudioComponent audio = (AudioComponent) entity.getComponent(AudioComponent.class);
        InputComponent input = (InputComponent) entity.getComponent(InputComponent.class);


        if( entity instanceof PlayerEntity){
            if((input.isRight() || input.isLeft()) && !audio.hasWalkingAudio.get()){
                playSound(AudioComponent.ActionType.MOVE, VOLUME_MOVE);
                delaySound(audio.hasWalkingAudio, DELAY_WALKING);
            }
            if (input.isShoot() && !audio.hasShootingAudio.get()){
                playSound(AudioComponent.ActionType.SHOOT, VOLUME_SHOOT);
                delaySound(audio.hasShootingAudio, DELAY_SHOOTING);
            }

        }

    }

    @Override
    public boolean filter(Entity entity) {
        return (filter.matches(entity));
    }

}
