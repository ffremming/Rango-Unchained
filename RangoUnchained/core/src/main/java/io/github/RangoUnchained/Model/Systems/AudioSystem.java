package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Timer;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.AudioComponent;
import io.github.RangoUnchained.Model.Components.InputComponent;
import io.github.RangoUnchained.Model.ContactStrategies.ContactStrategy;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;
import io.github.RangoUnchained.Model.Entities.ProjectileEntity;
import io.github.RangoUnchained.Model.Factories.AudioLoader;

public class AudioSystem implements Systems, ContactStrategy {


    private static final long COLLISION_SOUND_COOLDOWN_MS = 175;
    private static final int MAX_CONCURRENT_SOUNDS = 5;
    private static final float SOUND_EXPIRE_TIME = 1f;

    private static final float VOLUME_POP = 20f;
    private static final float VOLUME_MOVE = 3f;
    private static final float VOLUME_SHOOT = 2.4f;

    private static final float DELAY_WALKING = 1f;
    private static final float DELAY_SHOOTING = 1f;

    private static float globalVolumeMultiplier;


    private final ComponentFilter filter = new ComponentFilter();
    private final Random random = new Random();
    private final AudioLoader audioLoader;
    private long lastCollisionSoundTime = 0;
    private final Queue<PlayingSound> activeSounds = new LinkedList<>();

    @Override
    public void setContactStrategies() {
        ContactSystem centralContactListener = LevelController.getInstance().getSystem(ContactSystem.class);

        centralContactListener.subscribe(
            BallEntity.class, ProjectileEntity.class,
            collisionEvent -> playSound(AudioComponent.ActionType.POP, VOLUME_POP),
            null);
    }

    private static class PlayingSound {
        public final Sound sound;
        public final long soundId;
        public final float baseVolume;
        public final AudioComponent.ActionType actionType;
        public PlayingSound(Sound sound, long soundId, AudioComponent.ActionType actionType, float baseVolume) {
            this.sound = sound;
            this.soundId = soundId;
            this.actionType = actionType;
            this.baseVolume = baseVolume;
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

    public AudioSystem(float volume){
        filter.require(AudioComponent.class);
        audioLoader = AudioLoader.getInstance();
        globalVolumeMultiplier = volume;
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
        float adjustedVolume = volume * globalVolumeMultiplier;
        Sound sound = sounds.get(random.nextInt(sounds.size()));
        long soundId = sound.play(adjustedVolume);
        Gdx.app.log("AudioSystem", "Playing sound: " + sound);

        PlayingSound playing = new PlayingSound(sound, soundId, soundKey, volume);
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

    public void setVolume(float multiplier) {
        if (multiplier < 0f) {
            multiplier = 0f;
        }
        globalVolumeMultiplier = multiplier;
        // Update the volume of all currently active sounds.
        for (PlayingSound ps : activeSounds) {
            float adjustedVolume = ps.baseVolume * globalVolumeMultiplier;
            ps.sound.setVolume(ps.soundId, adjustedVolume);
        }
    }

    @Override
    public boolean filter(Entity entity) {
        return (filter.matches(entity));
    }

}
